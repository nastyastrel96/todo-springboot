package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.tags.TodoItemsWithTags;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.user.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TodoItemService {
    List<TodoItemsWithTags> findAll(User user);

    void save(TodoItem item);

    ResponseEntity<TodoItem> deleteTodoItem(Long itemId);

    ResponseEntity<?> findAllOrFilter(String word, User user, String tagName);

    ResponseEntity<TodoItem> todoItemIsDone(Long itemId);
}
