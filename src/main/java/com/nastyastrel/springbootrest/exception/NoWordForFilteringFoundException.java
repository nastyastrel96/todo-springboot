package com.nastyastrel.springbootrest.exception;

public class NoWordForFilteringFoundException extends RuntimeException {
    public NoWordForFilteringFoundException(String word) {
        super(String.format(ExceptionMessage.NO_WORD_FOR_FILTERING, word));
    }
}
