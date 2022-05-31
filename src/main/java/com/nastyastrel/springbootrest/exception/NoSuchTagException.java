package com.nastyastrel.springbootrest.exception;

public class NoSuchTagException extends RuntimeException{
    public NoSuchTagException(String tagName) {
        super(String.format(ExceptionMessage.NO_TAG, tagName));
    }
}
