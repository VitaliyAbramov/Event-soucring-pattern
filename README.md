# Event-soucring-pattern

This is a simple example of the microservice pattern **Event Sourcing** using Java.
> **NOTICE**: This example uses the clean Event Sourcing pattern without the CQRS pattern components. 
As these both patterns are closely connected, CQRS uses Event Sourcing architecture as part of its own and adds additional components to expand the logic.

The main domain here is related to orders and customers.
Here are using the next key components of the event sourcing pattern:
1. **Events** - describe the fact of a change in the state of the system. Events are immutable and contain all the necessary information to restore the state.
2. **Event Store** - specialized storage designed for storing and retrieving events. The event store ensures that events are stored in chronological order.
3. **Commands** - describe requests to change the state of an aggregate. The commands are checked for business rules and, if successful, result in the generation of events.
4. **Command handlers** - responsible for processing commands and calling the appropriate aggregate methods that fire events.
5. **Aggregates** - they represent the domain entities of the system and restore their state based on the application of events. Aggregates also fire new events when a state change occurs in the system.
6. **Subscribers/Listeners** - components that respond to events and perform additional actions such as sending notifications or updating other aggregates.
7. **Snapshots** (optional component, not used in this project) - in the case of a large number of events for the aggregate, restoring the state can take a long time. Snapshots are saved states of an implement at a point in time and can be used to speed up the state recovery process.

Here is shown the main architecture flow of the **Event Sourcing** pattern in this project example:
[Project architerture](architecture/EventSourcing.svg)

The project contains two main aggregates such as **OrderAggregates** and **CustomerAggregates**.<br>
These two entities deal with different processes that change the states of entities and at the same time entities apply those processes to themself. 
The main endpoints that exists are shown below:
```
1. POST /api/orders - creates order for the customer (Changes order status from **NEW -> CREATED**)
2. POST /api/orders/{orderId}/confirm?customerId={customerId} - checks customer balance and approves the order 
(Changes order status from **CREATED -> APPROVED** or rejects in case of low customer balance).
3. GET /api/orders/{orderId} - restores the order state from event history.
```

The order of the workflow in the project is the next:
1. **Controller Layer**: We receive the request from the specific endpoint.<br>
Creates specific relative commands depending on the request and sends it to a specific command handler. Service Layer here is used.
2. **CommandHandler Layer**: Creates specific event and use aggregates if needed. After that sends the generated event to the event store (Kafka).
3. **EventConsumer Layer**: Pulling specific events from Kafka, using the aggregates, and saving data as a state to the database.
4. **Service Layer**: Independent layer that uses here only in the controller layer to restore the object state from the event history.<br>
Service pulls all events that are relevant to specific orders and restores the order state in the right order.<br>
The service layer potentially can be used from other layers, for this example, it's no such need, only in case the new extensive logic will be added.
