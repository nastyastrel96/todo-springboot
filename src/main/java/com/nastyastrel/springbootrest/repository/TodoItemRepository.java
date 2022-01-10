package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
    List<TodoItem> findAllByUserIdEquals(Long todoItemOwner);

    List<TodoItem> findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEquals(String word, Long todoItemOwner);

    List<TodoItem> findAllByItemIdEquals(Long itemId);
}
