package com.nastyastrel.springbootrest.exception;

public class CircularTasksException extends RuntimeException {
    public CircularTasksException() {
        super(ExceptionMessage.CIRCULAR_TASKS);
    }
}
