package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.tags.TagDTO;
import com.nastyastrel.springbootrest.model.tags.TodoItemsWithTags;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import com.nastyastrel.springbootrest.service.tags.TagService;
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

@Service
@CacheConfig(cacheNames = "todoItems")
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final TagService tagService;
    private final ChuckNorrisClient chuckNorrisClient;
    private final UserService userService;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepository repository, TagService tagService, ChuckNorrisClient chuckNorrisClient, UserService userService) {
        this.todoItemRepository = repository;
        this.tagService = tagService;
        this.chuckNorrisClient = chuckNorrisClient;
        this.userService = userService;
    }

    @Override
    @Cacheable(key = "#user.login")
    public List<TodoItemsWithTags> findAll(User user) {
        List<TodoItem> todoItemList = todoItemRepository.findAllByTodoItemOwnerEquals(user.getId());
        return fromTodoItemList(todoItemList, user);
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

    private Optional<TodoItem> deleteItem(Long itemId, User user) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        return todoItemOptional.filter(todoItem -> user.getId().equals(todoItem.getTodoItemOwner())).map(
                todoItem -> {
                    todoItemRepository.deleteById(itemId);
                    return todoItem;
                }
        );
    }

    @Override
    @Cacheable(key = "#user.login")
    public ResponseEntity<?> findAllOrFilter(String word, User user, String tagName) {
        if (word != null) {
            List<TodoItemsWithTags> filteredTodoItemsByWord = filterTodoItemsByWord(word, user);
            if (filteredTodoItemsByWord.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else
                return new ResponseEntity<>(filteredTodoItemsByWord, HttpStatus.OK);
        }
        if (tagName != null) {
            List<TodoItemsWithTags> filteredTodoItemsByTag = filterTodoItemsByTag(tagName, user);
            if (filteredTodoItemsByTag.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else return new ResponseEntity<>(filteredTodoItemsByTag, HttpStatus.OK);
        }
        List<TodoItemsWithTags> allTodoItems = findAll(user);
        if (!isEachTaskStateHasStateDone(allTodoItems)) {
            return new ResponseEntity<>(allTodoItems, HttpStatus.OK);
        } else return new ResponseEntity<>(getTodoItemWithNorrisJoke(allTodoItems), HttpStatus.OK);
    }

    private List<TodoItemsWithTags> filterTodoItemsByWord(String word, User user) {
        List<TodoItem> todoItemList = todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(word, user.getId());
        return fromTodoItemList(todoItemList, user);
    }

    private List<TodoItemsWithTags> filterTodoItemsByTag(String tagName, User user) {
        List<TodoItem> todoItemList = new ArrayList<>();
        tagService.findAllByUserIdEqualsAndTagNameEqualsIgnoreCase(user.getId(), tagName)
                .forEach(tag -> todoItemList.addAll(todoItemRepository.findAllByItemIdEquals(tag.getItemId())));
        return fromTodoItemList(todoItemList, user);
    }

    private List<TodoItemsWithTags> fromTodoItemList(List<TodoItem> todoItemList, User user) {
        return todoItemList.stream()
                .map(todoItem -> new TodoItemsWithTags(todoItem, tagService.findAllByUserIdEqualsAndItemIdEquals(user.getId(), todoItem.getItemId())
                        .stream().map(TagDTO::from).toList())).toList();
    }

    private boolean isEachTaskStateHasStateDone(List<TodoItemsWithTags> todoItemList) {
        return todoItemList.stream().allMatch(todoItemsWithTags -> todoItemsWithTags.todoItem().getState().equals(TaskState.DONE));
    }

    private TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke(List<TodoItemsWithTags> todoItemList) {
        return new TodoItemListWithNorrisJoke(todoItemList, chuckNorrisClient.getChuckNorrisJoke());
    }


    @Override
    @CacheEvict(key = "#itemId")
    public ResponseEntity<TodoItem> todoItemIsDone(Long itemId) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        optionalUser.map(user -> changeStateToDone(itemId, user));
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
        return todoItemOptional.filter(todoItem -> user.getId().equals(todoItem.getTodoItemOwner())).map(
                todoItem -> {
                    todoItem.setState(TaskState.DONE);
                    save(todoItem);
                    return todoItem;
                }
        );
    }
}
