package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.tags.TagDTO;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TagRepository;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "todoItems")
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final ChuckNorrisClient chuckNorrisClient;
    private final UserService userService;
    private final TagRepository tagRepository;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepository repository, ChuckNorrisClient chuckNorrisClient, UserService userService, TagRepository tagRepository) {
        this.todoItemRepository = repository;
        this.chuckNorrisClient = chuckNorrisClient;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }

    @Override
    @Cacheable(key = "#user.login")
    public List<TodoItem> findAll(User user) {
        return todoItemRepository.findAllByUserIdEquals(user.getUserId());
    }

    @Override
    public void save(TodoItem item) {
        todoItemRepository.save(item);
    }

    @Override
    @Transactional
    @CacheEvict(key = "#itemId")
    public ResponseEntity<TodoItem> deleteTodoItem(Long itemId) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        if (optionalUser.isPresent()) {
            Optional<TodoItem> optionalTodoItem = deleteItem(itemId, optionalUser.get());
            if (optionalTodoItem.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private Optional<TodoItem> deleteItem(Long serialNumber, User user) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(serialNumber);
        return todoItemOptional.filter(todoItem -> user.getUserId().equals(todoItem.getUserId())).map(
                todoItem -> {
                    todoItemRepository.deleteById(serialNumber);
                    return todoItem;
                }
        );
    }

    @Override
    @Cacheable(key = "#user.login")
    public ResponseEntity<?> findAllOrFilter(String word, User user, String tagName) {
        if (word != null) {
            List<TodoItem> filteredTodoItems = filterTodoItemsByWord(word, user);
            if (filteredTodoItems.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else
                return new ResponseEntity<>(filteredTodoItems, HttpStatus.OK);
        }
        List<TodoItem> allTodoItems = findAll(user);
        if (!isEachTaskStateHasStateDone(allTodoItems)) {
            return new ResponseEntity<>(allTodoItems, HttpStatus.OK);
        }
        if (tagName != null) {
            return new ResponseEntity<>(filterTodoItemsByTag(allTodoItems, tagName), HttpStatus.OK);
        } else return new ResponseEntity<>(getTodoItemWithNorrisJoke(allTodoItems), HttpStatus.OK);
    }

    private List<TodoItem> filterTodoItemsByWord(String word, User user) {
        return todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEquals(word, user.getUserId());
    }

    private boolean isEachTaskStateHasStateDone(List<TodoItem> todoItemList) {
        return todoItemList.stream().allMatch(todoItem -> todoItem.getState().equals(TaskState.DONE));
    }

    private TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke(List<TodoItem> todoItemList) {
        return new TodoItemListWithNorrisJoke(todoItemList, chuckNorrisClient.getChuckNorrisJoke());
    }

    private List<TodoItem> filterTodoItemsByTag(List<TodoItem> list, String tagName) {
        List<TodoItem> todoItemListToBeFiltered = new ArrayList<>();
        for (TodoItem todoItem : list) {
            todoItem.getTags().stream().filter(tagDTO -> tagDTO.getTagName().equals(tagName)).findAny().ifPresent(tagDTO -> todoItemListToBeFiltered.add(todoItem));
        }
        return todoItemListToBeFiltered;
    }

    @Override
    @CacheEvict(key = "#itemId")
    public ResponseEntity<TodoItem> todoItemIsDone(Long itemId) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        if (optionalUser.isPresent()) {
            Optional<TodoItem> optionalTodoItem = changeStateToDone(itemId, optionalUser.get());
            if (optionalTodoItem.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private Optional<TodoItem> changeStateToDone(Long itemId, User user) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        return todoItemOptional.filter(todoItem -> user.getUserId().equals(todoItem.getUserId())).map(
                todoItem -> {
                    todoItem.setState(TaskState.DONE);
                    save(todoItem);
                    return todoItem;
                }
        );
    }
}
