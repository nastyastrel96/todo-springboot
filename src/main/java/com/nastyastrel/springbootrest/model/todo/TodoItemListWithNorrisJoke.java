package com.nastyastrel.springbootrest.model.todo;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;

import java.util.List;

public record TodoItemListWithNorrisJoke(List<TodoItem> todoItemList, ChuckNorrisJoke chuckNorrisJoke) {
}
