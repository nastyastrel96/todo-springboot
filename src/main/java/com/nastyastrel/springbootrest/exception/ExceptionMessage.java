package com.nastyastrel.springbootrest.exception;

public class ExceptionMessage {
    public static final String NO_WORD_FOR_FILTERING = "There is no corresponding item with the word you filter by : %s";
    public static final String NO_ITEM_ID = "Item with id = %s is not found";
    public static final String NO_TAG = "Item with tag = %s is not found";
    public static final String NO_TAG_ID = "Tag with id = %s is not found";
    public static final String NO_ITEM_AND_TAG = "There is no corresponding item according to your request";

    public static final String CIRCULAR_TASKS = "TodoItem id and parent's todoItem id should be different";
}
