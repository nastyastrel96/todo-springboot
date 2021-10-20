package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
    List<TodoItem> findAllByTodoItemOwnerEquals(Long todoItemOwner);

    List<TodoItem> findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(String word, Long todoItemOwner);

    List<TodoItem> findAllByItemIdEquals(Long itemId);
}
