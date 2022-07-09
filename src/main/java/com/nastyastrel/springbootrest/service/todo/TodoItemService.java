package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.tree.TodoItemResponse;

import java.util.List;

public interface TodoItemService {
    List<TodoItem> findAll(Long userId);

    void save(TodoItem item);

    void deleteTodoItem(Long itemId, Long userId);

    TodoItemListWithNorrisJoke findAllOrFilter(String word, Long userId, String tagName);

    TodoItem changeToDone(Long itemId, Long userId);

    List<TodoItemResponse> getTree(Long userId);
}
