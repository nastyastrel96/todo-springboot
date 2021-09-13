package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TodoItemRepository extends CrudRepository<TodoItem, Integer> {
    List<TodoItem> findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(String description, Long todoItemOwner);

    List<TodoItem> findAllByTodoItemOwnerEquals(Long todoItemOwner);

    Optional<TodoItem> deleteTodoItemBySerialNumber(int serialNumber);
}
