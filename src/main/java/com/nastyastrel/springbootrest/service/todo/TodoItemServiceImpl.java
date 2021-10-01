package com.nastyastrel.springbootrest.service.todo;


import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final ChuckNorrisClient chuckNorrisClient;
    private final UserService userService;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepository repository, ChuckNorrisClient chuckNorrisClient, UserService userService) {
        this.todoItemRepository = repository;
        this.chuckNorrisClient = chuckNorrisClient;
        this.userService = userService;
    }

    @Override
    public List<TodoItem> findAll(User user) {
        return todoItemRepository.findAllByTodoItemOwnerEquals(user.getId());
    }

    @Override
    public void save(TodoItem item) {
        todoItemRepository.save(item);
    }

    @Override
    @Transactional
    public ResponseEntity<TodoItem> deleteTodoItem(Long number) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        if (optionalUser.isPresent()) {
            Optional<TodoItem> optionalTodoItem = deleteItem(number, optionalUser.get());
            if (optionalTodoItem.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private Optional<TodoItem> deleteItem(Long serialNumber, User user) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(serialNumber);
        return todoItemOptional.filter(todoItem -> user.getId().equals(todoItem.getTodoItemOwner())).map(
                todoItem -> {
                    todoItemRepository.deleteById(serialNumber);
                    return todoItem;
                }
        );
    }

    @Override
    public ResponseEntity<?> findAllOrFilter(String word, User user) {
        if (word != null) {
            List<TodoItem> todoItemList = findSpecificItem(word, user);
            if (todoItemList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else
                return new ResponseEntity<>(todoItemList, HttpStatus.OK);
        } else if (!isEachTaskStateHasStateDone(user)) {
            return new ResponseEntity<>(findAll(user), HttpStatus.OK);
        } else return new ResponseEntity<>(getTodoItemWithNorrisJoke(user), HttpStatus.OK);
    }

    private List<TodoItem> findSpecificItem(String wordToBeFound, User user) {
        return todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(wordToBeFound, user.getId());
    }

    private boolean isEachTaskStateHasStateDone(User user) {
        List<TodoItem> todoItemList = findAll(user);
        return todoItemList.stream().allMatch(todoItem -> todoItem.getState().equals(TaskState.DONE));
    }

    private TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke(User user) {
        return new TodoItemListWithNorrisJoke(findAll(user), chuckNorrisClient.getChuckNorrisJoke());
    }


    @Override
    public ResponseEntity<TodoItem> todoItemIsDone(Long number) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        if (optionalUser.isPresent()) {
            Optional<TodoItem> optionalTodoItem = changeStateToDone(number, optionalUser.get());
            if (optionalTodoItem.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private Optional<TodoItem> changeStateToDone(Long serialNumber, User user) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(serialNumber);
        return todoItemOptional.filter(todoItem -> user.getId().equals(todoItem.getTodoItemOwner())).map(
                todoItem -> {
                    todoItem.setState(TaskState.DONE);
                    save(todoItem);
                    return todoItem;
                }
        );
    }
}
