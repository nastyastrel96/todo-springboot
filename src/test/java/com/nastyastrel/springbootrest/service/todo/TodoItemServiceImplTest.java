package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.exception.NoSuchIdItemException;
import com.nastyastrel.springbootrest.exception.NoSuchTagException;
import com.nastyastrel.springbootrest.exception.NoSuchItemAndTagException;
import com.nastyastrel.springbootrest.exception.NoWordForFilteringFoundException;
import com.nastyastrel.springbootrest.model.chucknorris.ChuckNorrisJoke;
import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceImplTest {
    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private ChuckNorrisClient chuckNorrisClient;

    private TodoItemServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TodoItemServiceImpl(todoItemRepository, chuckNorrisClient);
    }

    @Test
    @DisplayName("Find all todos")
    void itShouldFindAllTodoItems() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        List<TodoItem> todoItemList =
                List.of(new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L),
                        new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L));
        //when
        when(todoItemRepository.findAllByUserIdEquals(eq(user.getUserId()))).thenReturn(todoItemList);

        //then
        assertEquals(todoItemList, underTest.findAll(3L), "Return list of todoItems");
    }

    @Test
    void itShouldSaveTodoItem() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);

        //when
        underTest.save(todoItem);

        //then
        verify(todoItemRepository).save(eq(todoItem));
    }

    @Test
    void itShouldDeleteTodoItemIfUserIsCorrect() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(todoItemRepository.findById(eq(todoItem.getItemId()))).thenReturn(Optional.of(todoItem));

        //when
        underTest.deleteTodoItem(todoItem.getItemId(), user.getUserId());

        //then
        verify(todoItemRepository).deleteById(eq(todoItem.getItemId()));
    }

    @Test
    void itShouldThrowExceptionIfItemIdIsIncorrect() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(todoItemRepository.findById(eq(todoItem.getItemId()))).thenReturn(Optional.empty());

        //then
        Throwable exception = assertThrows(NoSuchIdItemException.class, () -> underTest.deleteTodoItem(todoItem.getItemId(), user.getUserId()));
        assertEquals("Item with id = 25 is not found", exception.getMessage());
    }


    @Test
    void itShouldChangeStateToDone() {
        //given
        TodoItem todoItemBefore = new TodoItem(25L, "Buy tickets", TaskState.UNDONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        TodoItem todoItemAfter = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(todoItemRepository.findById(eq(todoItemBefore.getItemId()))).thenReturn(Optional.of(todoItemBefore));

        //when
        var answer = underTest.changeToDone(todoItemBefore.getItemId(), user.getUserId());

        //then
        assertEquals(todoItemAfter, answer);
    }

    @Test
    void itShouldFindTodoItemsFilterByWordAndTag() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        List<TodoItem> todoItemList = List.of(todoItem);
        when(todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEqualsAndTagsTagNameEquals(eq(word), eq(user.getUserId()), eq(tag.getTagName()))).thenReturn(todoItemList);

        //when
        var answer = underTest.findAllOrFilter(word, user.getUserId(), tag.getTagName());

        //then
        verify(todoItemRepository).findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEqualsAndTagsTagNameEquals(eq(word), eq(user.getUserId()), eq(tag.getTagName()));
        assertEquals(new TodoItemListWithNorrisJoke(todoItemList, null), answer);

    }

    @Test
    void itShouldThrowExceptionIfThereIsNoSuchTodoItemWithTheseWordAndTag() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        when(todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEqualsAndTagsTagNameEquals(eq(word), eq(user.getUserId()), eq(tag.getTagName()))).thenReturn(Collections.emptyList());

        //then
        Throwable exception = assertThrows(NoSuchItemAndTagException.class, () -> underTest.findAllOrFilter("read", user.getUserId(), "reading"));
        assertEquals("There is no corresponding item according to your request", exception.getMessage());
    }

    @Test
    void itShouldFindAllWithoutChuckNorrisJoke() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        List<TodoItem> todoItemList = List.of(todoItem);
        when(todoItemRepository.findAllByUserIdEquals(eq(user.getUserId()))).thenReturn(todoItemList);

        //when
        var answer = underTest.findAllOrFilter(null, user.getUserId(), null);

        //then
        assertEquals(new TodoItemListWithNorrisJoke(todoItemList, null), answer);
    }

    @Test
    void itShouldFindAllWithChuckNorrisJoke() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.DONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        List<TodoItem> todoItemList = List.of(todoItem);
        ChuckNorrisJoke chuckNorrisJoke = new ChuckNorrisJoke("Очень смешно");
        TodoItemListWithNorrisJoke todoItemListWithNorrisJoke = new TodoItemListWithNorrisJoke(todoItemList, chuckNorrisJoke);
        when(todoItemRepository.findAllByUserIdEquals(eq(user.getUserId()))).thenReturn(todoItemList);
        when(chuckNorrisClient.getChuckNorrisJoke()).thenReturn(chuckNorrisJoke);

        //when
        var answer = underTest.findAllOrFilter(null, user.getUserId(), null);

        //then
        assertEquals(todoItemListWithNorrisJoke, answer);
    }

    @Test
    void itShouldFindTodoItemsFilterByWord() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        List<TodoItem> todoItemList = List.of(todoItem);
        when(todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEquals(eq(word), eq(user.getUserId()))).thenReturn(todoItemList);

        //when
        var answer = underTest.findAllOrFilter(word, user.getUserId(), null);

        //then
        assertEquals(new TodoItemListWithNorrisJoke(todoItemList, null), answer);
    }

    @Test
    void itShouldThrowExceptionIfThereIsNoSuchTodoItemWithThisWord() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        when(todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndUserIdEquals(eq(word), eq(user.getUserId()))).thenReturn(Collections.emptyList());

        //then
        Throwable exception = assertThrows(NoWordForFilteringFoundException.class, () -> underTest.findAllOrFilter(word, user.getUserId(), null));
        assertEquals("There is no corresponding item with the word you filter by : read", exception.getMessage());
    }

    @Test
    void itShouldFindTodoItemsFilterByTag() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        List<TodoItem> todoItemList = List.of(todoItem);
        when(todoItemRepository.findByUserIdEqualsAndTagsTagNameEquals(eq(user.getUserId()), eq(tag.getTagName()))).thenReturn(todoItemList);

        //when
        var answer = underTest.findAllOrFilter(null, user.getUserId(), "reading");

        //then
        assertEquals(new TodoItemListWithNorrisJoke(todoItemList, null), answer);
    }

    @Test
    void itShouldThrowExceptionIfThereIsNoSuchTodoItemWithThisTag() {
        //given
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        Tag tag = new Tag(1L, "reading", 3L);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        TodoItem todoItem = new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L);
        todoItem.setTags(tagSet);
        when(todoItemRepository.findByUserIdEqualsAndTagsTagNameEquals(eq(user.getUserId()), eq(tag.getTagName()))).thenReturn(Collections.emptyList());

        //then
        Throwable exception = assertThrows(NoSuchTagException.class, () -> underTest.findAllOrFilter(null, user.getUserId(), "reading"));
        assertEquals("Item with tag = reading is not found", exception.getMessage());

    }
}


