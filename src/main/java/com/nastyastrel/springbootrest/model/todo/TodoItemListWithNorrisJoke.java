package com.nastyastrel.springbootrest.model.todo;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;

import java.util.List;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItemListWithNorrisJoke that = (TodoItemListWithNorrisJoke) o;
        return Objects.equals(todoItemList, that.todoItemList) && Objects.equals(chuckNorrisJoke, that.chuckNorrisJoke);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoItemList, chuckNorrisJoke);
    }

    @Override
    public String toString() {
        return "TodoItemListWithNorrisJoke{" +
                "todoItemList=" + todoItemList +
                ", chuckNorrisJoke=" + chuckNorrisJoke +
                '}';
    }
}
