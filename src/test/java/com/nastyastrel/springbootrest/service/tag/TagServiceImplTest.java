package com.nastyastrel.springbootrest.service.tag;

import com.nastyastrel.springbootrest.exception.NoSuchIdItemException;
import com.nastyastrel.springbootrest.exception.NoSuchIdTagException;
import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.tags.TagDTO;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TagRepository;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private TodoItemRepository todoItemRepository;

    private TagServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TagServiceImpl(tagRepository, todoItemRepository);
    }

    @Test
    void itShouldSaveTag() {
        //given
        Tag tag = new Tag(1L, "something", 1L);

        //when
        underTest.save(tag);

        //then
        verify(tagRepository).save(tag);
    }

    @Test
    void itShouldAttachTagToTodoItem() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> set = new HashSet<>();
        set.add(tag);
        TodoItem todoItemBefore = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        TodoItem todoItemAfter = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItemAfter.setTags(set);
        when(todoItemRepository.findById(eq(todoItemBefore.getItemId()))).thenReturn(Optional.of(todoItemBefore));
        when(tagRepository.findById(eq(tag.getTagId()))).thenReturn(Optional.of(tag));

        //when
        var result = underTest.attachTag(3L, 26L, 1L);

        //then
        verify(todoItemRepository).save(argThat(todoItem -> List.of(TagDTO.fromTag(tag)).equals(todoItem.getTags())));
    }

    @Test
    void itShouldThrowExceptionIfTodoItemIdIsNotCorrectWhileAttaching() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        when(todoItemRepository.findById(eq(todoItem.getItemId()))).thenReturn(Optional.empty());
        when(tagRepository.findById(eq(tag.getTagId()))).thenReturn(Optional.of(tag));

        //then
        Throwable exception = assertThrows(NoSuchIdItemException.class, () -> underTest.attachTag(3L, 26L, 1L));
        assertEquals("Item with id = 26 is not found", exception.getMessage());
    }

    @Test
    void itShouldThrowExceptionIfTodoTagIdIsNotCorrectWhileAttaching() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        when(todoItemRepository.findById(eq(todoItem.getItemId()))).thenReturn(Optional.of(todoItem));
        when(tagRepository.findById(eq(tag.getTagId()))).thenReturn(Optional.empty());

        //then
        Throwable exception = assertThrows(NoSuchIdTagException.class, () -> underTest.attachTag(3L, 26L, 1L));
        assertEquals("Tag with id = 1 is not found", exception.getMessage());
    }

    @Test
    void itShouldDetachTagFromTodoItem() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> set = new HashSet<>();
        set.add(tag);
        TodoItem todoItemBefore = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        TodoItem todoItemAfter = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItemBefore.setTags(set);
        todoItemAfter.setTags(new HashSet<>());
        when(todoItemRepository.findById(eq(todoItemBefore.getItemId()))).thenReturn(Optional.of(todoItemBefore));
        when(tagRepository.findById(eq(tag.getTagId()))).thenReturn(Optional.of(tag));

        //when
        underTest.detachTag(3L, 26L, 1L);

        //then
        verify(todoItemRepository).save(argThat(todoItem -> todoItem.getTags().equals(Collections.emptyList())));
    }

    @Test
    void itShouldThrowExceptionIfTodoItemIdIsNotCorrectWhileDetaching() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        when(todoItemRepository.findById(eq(todoItem.getItemId()))).thenReturn(Optional.empty());
        when(tagRepository.findById(eq(tag.getTagId()))).thenReturn(Optional.of(tag));

        //then
        Throwable exception = assertThrows(NoSuchIdItemException.class, () -> underTest.detachTag(3L, 26L, 1L));
        assertEquals("Item with id = 26 is not found", exception.getMessage());
    }

    @Test
    void itShouldThrowExceptionIfTodoTagIdIsNotCorrectWhileDetaching() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        when(todoItemRepository.findById(eq(todoItem.getItemId()))).thenReturn(Optional.of(todoItem));
        when(tagRepository.findById(eq(tag.getTagId()))).thenReturn(Optional.empty());

        //then
        Throwable exception = assertThrows(NoSuchIdTagException.class, () -> underTest.detachTag(3L, 26L, 1L));
        assertEquals("Tag with id = 1 is not found", exception.getMessage());
    }
}