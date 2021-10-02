package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.user.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TodoItemService {
    List<TodoItem> findAll(User user);

    void save(TodoItem item);

    ResponseEntity<TodoItem> deleteTodoItem(Long number);

    ResponseEntity<?> findAllOrFilter(String word, User user);

    ResponseEntity<TodoItem> todoItemIsDone(Long number);
}
