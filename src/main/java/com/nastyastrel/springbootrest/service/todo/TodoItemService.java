package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.todo.TodoItem;

import java.util.List;
import java.util.Optional;

public interface TodoItemService {
    List<TodoItem> findAll(Long todoItemOwner);

    void save(TodoItem item);

    List<TodoItem> findSpecificItem(String wordToBeFound, Long idItemOwner);

    Optional<TodoItem> changeStateToDone(int serialNumber);

    Optional<TodoItem> deleteItem(int serialNumber);
}
