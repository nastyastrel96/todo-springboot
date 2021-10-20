package com.nastyastrel.springbootrest.model.todo;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;
import com.nastyastrel.springbootrest.model.tags.TodoItemsWithTags;

import java.util.List;

public record TodoItemListWithNorrisJoke(List<TodoItemsWithTags> todoItemList, ChuckNorrisJoke chuckNorrisJoke) {
}
