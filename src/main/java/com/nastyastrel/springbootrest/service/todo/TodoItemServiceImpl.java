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
import com.nastyastrel.springbootrest.tree.TodoItemResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.*;
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
            List<TodoItem> todoItemsWithHandledRepeatability = handleRepeatability(allTodoItems, userId);
            if (!isEachTaskStateHasStateDone(todoItemsWithHandledRepeatability)) {
                return new TodoItemListWithNorrisJoke(todoItemsWithHandledRepeatability, null);
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

    private List<TodoItem> handleRepeatability(List<TodoItem> todoItems, Long userId) {
        todoItems.stream()
                .filter(TodoItem::isRepeatable)
                .filter(todoItem -> todoItem.getState().equals(TaskState.DONE))
                .filter(todoItem -> (LocalTime.now().compareTo(todoItem.getUpdatingTime()) >= 0))
                .forEach(todoItem -> {
                    todoItemRepository.save(new TodoItem(todoItem.getDescription(), TaskState.UNDONE, true, todoItem.getUpdatingTime(), LocalDateTime.of(LocalDate.now(), todoItem.getUpdatingTime()), todoItem.getUserId()));
                    todoItem.setRepeatable(false);
                    todoItemRepository.save(todoItem);
                });
        return findAll(userId);
    }

    @Override
    public List<TodoItemResponse> getTree(Long userId) {
        List<TodoItem> todoItems = findAll(userId);
        Map<Long, TodoItemResponse> todoItemsWithSubtasks = new HashMap<>();
        List<TodoItemResponse> root = new ArrayList<>();
        todoItems.forEach(todoItem -> todoItemsWithSubtasks.put(todoItem.getItemId(), new TodoItemResponse(todoItem.getItemId(), todoItem.getDescription(), todoItem.getState(), new ArrayList<>())));
        todoItems.stream().filter(todoItem -> todoItem.getParentId() != null).forEach(todoItem -> todoItemsWithSubtasks.get(todoItem.getParentId()).subtasks().add(todoItemsWithSubtasks.get(todoItem.getItemId())));
        todoItems.stream().filter(todoItem -> todoItem.getParentId() == null).forEach(todoItem -> root.add(new TodoItemResponse(todoItem.getItemId(), todoItem.getDescription(), todoItem.getState(), new ArrayList<>())));
        todoItemsWithSubtasks.keySet().forEach(id -> root.stream().filter(todoItemResponse -> todoItemResponse.itemId().equals(id)).forEach(todoItemResponse -> todoItemResponse.subtasks().addAll(todoItemsWithSubtasks.get(id).subtasks())));
        return root;
    }
}
