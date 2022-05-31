package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
    List<TodoItem> findAllByUserIdEquals(Long userId);

    List<TodoItem> findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEqualsAndTagsTagNameEquals(String word, Long userId, String tagName);

    List<TodoItem> findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEquals(String word, Long userId);

    List<TodoItem> findByUserIdEqualsAndTagsTagNameEquals(Long userId, String tagName);
}
