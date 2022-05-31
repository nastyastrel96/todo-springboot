package com.nastyastrel.springbootrest.exception;

public class NoSuchIdItemException extends RuntimeException{
    public NoSuchIdItemException(String itemId) {
        super(String.format(ExceptionMessage.NO_ITEM_ID, itemId));
    }
}
