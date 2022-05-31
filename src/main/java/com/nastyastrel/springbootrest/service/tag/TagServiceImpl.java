package com.nastyastrel.springbootrest.service.tag;

import com.nastyastrel.springbootrest.exception.NoSuchIdItemException;
import com.nastyastrel.springbootrest.exception.NoSuchIdTagException;
import com.nastyastrel.springbootrest.exception.NoSuchTagException;
import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.repository.TagRepository;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TodoItemRepository todoItemRepository;

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public TodoItem attachTag(Long userId, Long itemId, Long tagId) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.filter(tag -> tag.getUserId().equals(userId)).isPresent()) {
            if (todoItemOptional.filter(todoItem -> todoItem.getUserId().equals(userId)).isPresent()) {
                TodoItem todoItem = todoItemOptional.get();
                Tag tag = tagOptional.get();
                todoItem.addTag(tag);
                todoItemRepository.save(todoItem);
                return todoItem;
            } else throw new NoSuchIdItemException(itemId.toString());
        } else throw new NoSuchIdTagException(tagId.toString());
    }

    @Override
    public void detachTag(Long userId, Long itemId, Long tagId) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.filter(tag -> tag.getUserId().equals(userId)).isPresent()) {
            if (todoItemOptional.filter(todoItem -> todoItem.getUserId().equals(userId)).isPresent()) {
                TodoItem todoItem = todoItemOptional.get();
                todoItem.deleteTag(tagOptional.get());
                todoItemRepository.save(todoItem);
            } else throw new NoSuchIdItemException(itemId.toString());
        } else throw new NoSuchIdTagException(tagId.toString());
    }
}
