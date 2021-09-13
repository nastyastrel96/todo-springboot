package com.nastyastrel.springbootrest.service.todo;


import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository repository;
    private final UserService userService;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public List<TodoItem> findAll(Long todoItemOwner) {
        List<TodoItem> todoItems = new ArrayList<>();
        repository.findAllByTodoItemOwnerEquals(todoItemOwner).forEach(todoItems::add);
        return todoItems;
    }

    @Override
    public void save(TodoItem item) {
        repository.save(item);
    }

    @Override
    public List<TodoItem> findSpecificItem(String wordToBeFound, Long idItemOwner) {
        return new ArrayList<>(repository.findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(wordToBeFound, idItemOwner));
    }

    @Override
    public Optional<TodoItem> changeStateToDone(int serialNumber) {
        Optional<TodoItem> todoItemOptional = repository.findById(serialNumber);
        if (todoItemOptional.isPresent()) {
            TodoItem item = todoItemOptional.get();
            if (Objects.equals(item.getTodoItemOwner(), userService.definePrincipal().getId())) {
                TodoItem todoItem = todoItemOptional.get();
                todoItem.setState(TaskState.DONE);
                save(todoItem);
                return todoItemOptional;
            } else return Optional.empty();
        }
        return todoItemOptional;
    }


    @Override
    @Transactional
    public Optional<TodoItem> deleteItem(int serialNumber) {
        Optional<TodoItem> todoItemOptional = repository.findById(serialNumber);
        if (todoItemOptional.isPresent()) {
            TodoItem item = todoItemOptional.get();
            if (Objects.equals(item.getTodoItemOwner(), userService.definePrincipal().getId())) {
                repository.deleteTodoItemBySerialNumber(serialNumber);
                return todoItemOptional;
            } else return Optional.empty();
        }
        return todoItemOptional;
    }
}
