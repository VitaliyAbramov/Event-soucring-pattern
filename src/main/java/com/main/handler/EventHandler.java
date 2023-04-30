package com.main.handler;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Common event handler.
 *
 * @param <C> type of specific command
 */
public interface EventHandler<C> {

    /**
     * Handles the commands that update the object state.
     *
     * @param command the specific command for processing
     * @throws JsonProcessingException possible exception
     */
    void handle(C command) throws JsonProcessingException;
}
