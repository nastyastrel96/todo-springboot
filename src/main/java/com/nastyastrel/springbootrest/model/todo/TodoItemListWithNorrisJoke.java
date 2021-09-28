package com.nastyastrel.springbootrest.model.todo;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;

import java.util.List;

public class TodoItemListWithNorrisJoke {
    private List<TodoItem> todoItemList;
    private ChuckNorrisJoke chuckNorrisJoke;

    public TodoItemListWithNorrisJoke() {
    }

    public TodoItemListWithNorrisJoke(List<TodoItem> todoItemList, ChuckNorrisJoke chuckNorrisJoke) {
        this.todoItemList = todoItemList;
        this.chuckNorrisJoke = chuckNorrisJoke;
    }

    public List<TodoItem> getTodoItemList() {
        return todoItemList;
    }

    public void setTodoItemList(List<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
    }

    public ChuckNorrisJoke getChuckNorrisJoke() {
        return chuckNorrisJoke;
    }

    public void setChuckNorrisJoke(ChuckNorrisJoke chuckNorrisJoke) {
        this.chuckNorrisJoke = chuckNorrisJoke;
    }

    @Override
    public String toString() {
        return "TodoItemListWithNorrisJoke{" +
                "todoItemList=" + todoItemList +
                ", chuckNorrisJoke=" + chuckNorrisJoke +
                '}';
    }
}
