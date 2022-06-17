package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.cache.RedisConfig;
import com.nastyastrel.springbootrest.exception.NoSuchIdItemException;
import com.nastyastrel.springbootrest.exception.NoSuchTagException;
import com.nastyastrel.springbootrest.exception.NoSuchItemAndTagException;
import com.nastyastrel.springbootrest.exception.NoWordForFilteringFoundException;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final ChuckNorrisClient chuckNorrisClient;

    @Override
    public List<TodoItem> findAll(Long userId) {
        return todoItemRepository.findAllByUserIdEquals(userId);
    }

    @CacheEvict(cacheNames = RedisConfig.TODO_ITEMS, key = "#item.userId")
    @Override
    public void save(TodoItem item) {
        todoItemRepository.save(item);
    }

    @CacheEvict(cacheNames = RedisConfig.TODO_ITEMS, key = "#userId")
    @Override
    public void deleteTodoItem(Long itemId, Long userId) {
        Optional<TodoItem> optionalTodoItem = deleteItem(itemId, userId);
        if (optionalTodoItem.isEmpty()) {
            throw new NoSuchIdItemException(itemId.toString());
        }
    }

    private Optional<TodoItem> deleteItem(Long itemId, Long userId) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        return todoItemOptional.filter(todoItem -> userId.equals(todoItem.getUserId())).map(
                todoItem -> {
                    todoItemRepository.deleteById(itemId);
                    return todoItem;
                }
        );
    }

    @Cacheable(cacheNames = RedisConfig.TODO_ITEMS, key = "#userId", condition = "#word==null && #tagName==null")
    @Override
    public TodoItemListWithNorrisJoke findAllOrFilter(String word, Long userId, String tagName) {
        if (word != null && tagName != null) {
            List<TodoItem> filteredTodoItemsByWordAndTag = filterTodoItemsByWordAndTag(word, userId, tagName);
            if (filteredTodoItemsByWordAndTag.isEmpty()) {
                throw new NoSuchItemAndTagException();
            }
            return new TodoItemListWithNorrisJoke(filteredTodoItemsByWordAndTag, null);
        } else if (word == null && tagName == null) {
            List<TodoItem> allTodoItems = findAll(userId);
            if (!isEachTaskStateHasStateDone(allTodoItems)) {
                return new TodoItemListWithNorrisJoke(allTodoItems, null);
            } else return getTodoItemWithNorrisJoke(findAll(userId));
        } else if (word != null) {
            List<TodoItem> filteredTodoItems = filterTodoItemsByWord(word, userId);
            if (filteredTodoItems.isEmpty()) {
                throw new NoWordForFilteringFoundException(word);
            } else
                return new TodoItemListWithNorrisJoke(filteredTodoItems, null);
        } else {
            List<TodoItem> filteredTodoItemsByTag = filterTodoItemsByTag(userId, tagName);
            if (filteredTodoItemsByTag.isEmpty()) {
                throw new NoSuchTagException(tagName);
            }
            return new TodoItemListWithNorrisJoke(filteredTodoItemsByTag, null);
        }
    }

    private List<TodoItem> filterTodoItemsByWordAndTag(String word, Long userId, String tagName) {
        return todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEqualsAndTagsTagNameEquals(word, userId, tagName);
    }

    private List<TodoItem> filterTodoItemsByWord(String word, Long userId) {
        return todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEquals(word, userId);
    }

    private List<TodoItem> filterTodoItemsByTag(Long userId, String tagName) {
        return todoItemRepository.findByUserIdEqualsAndTagsTagNameEquals(userId, tagName);
    }

    private boolean isEachTaskStateHasStateDone(List<TodoItem> todoItemList) {
        return todoItemList.stream().allMatch(todoItem -> todoItem.getState().equals(TaskState.DONE));
    }

    private TodoItemListWithNorrisJoke getTodoItemWithNorrisJoke(List<TodoItem> todoItemList) {
        return new TodoItemListWithNorrisJoke(todoItemList, chuckNorrisClient.getChuckNorrisJoke());
    }

    @CacheEvict(cacheNames = RedisConfig.TODO_ITEMS, key = "#userId")
    @Override
    public TodoItem changeToDone(Long itemId, Long userId) {
        Optional<TodoItem> optionalTodoItem = changeStateToDone(itemId, userId);
        return optionalTodoItem.orElseThrow(() -> new NoSuchIdItemException(itemId.toString()));
    }

    private Optional<TodoItem> changeStateToDone(Long itemId, Long userId) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        return todoItemOptional.filter(todoItem -> userId.equals(todoItem.getUserId())).map(
                todoItem -> {
                    todoItem.setState(TaskState.DONE);
                    save(todoItem);
                    return todoItem;
                }
        );
    }
}
