package com.nastyastrel.springbootrest.service.todo;


import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository repository;
    private final ChuckNorrisClient chuckNorrisClient;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepository repository, ChuckNorrisClient chuckNorrisClient) {
        this.repository = repository;
        this.chuckNorrisClient = chuckNorrisClient;
    }

    @Override
    public List<TodoItem> findAll(Long idItemOwner) {
        return new ArrayList<>(repository.findAllByTodoItemOwnerEquals(idItemOwner));
    }

    @Override
    public TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke(User user) {
        return new TodoItemListWithNorrisJoke(findAll(user.getId()), chuckNorrisClient.getChuckNorrisJoke());
    }

    @Override
    public boolean checkTasksStateToBeDone(User user) {
        List<TodoItem> todoItemList = findAll(user.getId());
        return todoItemList.stream().allMatch(todoItem -> todoItem.getState().equals(TaskState.DONE));
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
    public Optional<TodoItem> changeStateToDone(Long serialNumber, User user) {
        Optional<TodoItem> todoItemOptional = repository.findById(serialNumber);
        return todoItemOptional.filter(todoItem -> user.getId().equals(todoItem.getTodoItemOwner())).map(
                todoItem -> {
                    todoItem.setState(TaskState.DONE);
                    save(todoItem);
                    return todoItem;
                }
        );
    }


    @Override
    @Transactional
    public Optional<TodoItem> deleteItem(Long serialNumber, User user) {
        Optional<TodoItem> todoItemOptional = repository.findById(serialNumber);
        return todoItemOptional.filter(todoItem -> user.getId().equals(todoItem.getTodoItemOwner())).map(
                todoItem -> {
                    repository.deleteById(serialNumber);
                    return todoItem;
                }
        );
    }
}
