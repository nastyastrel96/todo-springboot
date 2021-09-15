package com.nastyastrel.springbootrest.service.todo;


import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
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
    public TodoItemServiceImpl(TodoItemRepository repository, UserService userService, ChuckNorrisClient chuckNorrisClient) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public List<TodoItem> findAll(Long todoItemOwner) {
        return new ArrayList<>(repository.findAllByTodoItemOwnerEquals(todoItemOwner));
    }

    @Override
    public TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke() {
        return new TodoItemListWithNorrisJoke(findAll(userService.definePrincipal().getId()), new ChuckNorrisClient().getChuckNorrisJoke());
    }

    @Override
    public Optional<List<TodoItem>> checkTasksState() {
        int count = 0;
        List<TodoItem> todoItems = findAll(userService.definePrincipal().getId());
        for (TodoItem todoItem : todoItems) {
            if (todoItem.getState().equals(TaskState.DONE)) {
                count++;
            }
        }
        if (count == todoItems.size()) {
            return Optional.empty();
        } else return Optional.of(todoItems);
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
