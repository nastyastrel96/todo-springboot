package com.nastyastrel.springbootrest.service.tag;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.repository.TagRepository;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TodoItemRepository todoItemRepository) {
        this.tagRepository = tagRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public ResponseEntity<Tag> assignTag(Long userId, Long itemId, Long tagId) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.filter(tag -> tag.getUserId().equals(userId)).isPresent()) {
            if (todoItemOptional.filter(todoItem -> todoItem.getUserId().equals(userId)).isPresent()) {
                TodoItem todoItem = todoItemOptional.get();
                todoItem.addTag(tagOptional.get());
                todoItemRepository.save(todoItem);
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<Tag> deleteTag(Long userId, Long itemId, Long tagId) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(itemId);
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        if (tagOptional.filter(tag -> tag.getUserId().equals(userId)).isPresent()) {
            if (todoItemOptional.filter(todoItem -> todoItem.getUserId().equals(userId)).isPresent()) {
                TodoItem todoItem = todoItemOptional.get();
                todoItem.deleteTag(tagOptional.get());
                todoItemRepository.save(todoItem);
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
