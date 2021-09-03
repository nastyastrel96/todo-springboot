package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.entity.todo.TodoItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface TodoItemRepository extends CrudRepository<TodoItem, Integer> {
    Iterable<TodoItem> findTodoItemByDescriptionContainsOrderBySerialNumberAsc(String wordToBeFound);

    Iterable<TodoItem> findAllByOrderBySerialNumberAsc();
}
