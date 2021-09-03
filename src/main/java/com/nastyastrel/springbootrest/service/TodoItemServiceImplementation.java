package com.nastyastrel.springbootrest.service;


import com.nastyastrel.springbootrest.entity.todo.TaskState;
import com.nastyastrel.springbootrest.entity.todo.TodoItem;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoItemServiceImplementation implements TodoItemService {
    private final TodoItemRepository repository;

    @Autowired
    public TodoItemServiceImplementation(TodoItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TodoItem> findAll() {
        List<TodoItem> todoItems = new ArrayList<>();
        repository.findAllByOrderBySerialNumberAsc().forEach(todoItems::add);
        return todoItems;
    }

    @Override
    public void save(TodoItem item) {
        repository.save(item);
    }

    @Override
    public List<TodoItem> findSpecificItem(String wordToBeFound) {
        List<TodoItem> todoItems = new ArrayList<>();
        repository.findTodoItemByDescriptionContainsOrderBySerialNumberAsc(wordToBeFound).forEach(todoItems::add);
        return todoItems;
    }

    @Override
    public void changeStateToDone(int serialNumber) {
        Optional<TodoItem> todoItemOptional = repository.findById(serialNumber);
        if (todoItemOptional.isPresent()) {
            TodoItem todoItem = todoItemOptional.get();
            todoItem.setState(TaskState.DONE);
            save(todoItem);
        }
    }

    @Override
    public void deleteItem(int serialNumber) {
        repository.deleteById(serialNumber);
    }
}
