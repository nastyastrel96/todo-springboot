package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;

import java.util.List;
import java.util.Optional;

public interface TodoItemService {
    List<TodoItem> findAll(Long idItemOwner);

    void save(TodoItem item);

    List<TodoItem> findSpecificItem(String wordToBeFound, Long idItemOwner);

    Optional<TodoItem> changeStateToDone(Long serialNumber, User user);

    Optional<TodoItem> deleteItem(Long serialNumber, User user);

    TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke(User user);

    boolean checkTasksStateToBeDone(User user);
}
