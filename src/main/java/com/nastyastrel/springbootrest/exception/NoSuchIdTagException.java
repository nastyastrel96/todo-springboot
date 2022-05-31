package com.nastyastrel.springbootrest.exception;

public class NoSuchIdTagException extends RuntimeException{
    public NoSuchIdTagException(String tagId) {
        super(String.format(ExceptionMessage.NO_TAG_ID, tagId));
    }
}
