package com.nastyastrel.springbootrest.exception;

public class NoSuchItemAndTagException extends RuntimeException{
    public NoSuchItemAndTagException() {
        super(ExceptionMessage.NO_ITEM_AND_TAG);
    }
}
