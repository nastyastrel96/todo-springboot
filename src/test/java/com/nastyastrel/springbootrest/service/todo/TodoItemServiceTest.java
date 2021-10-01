package com.nastyastrel.springbootrest.service.todo;

import com.nastyastrel.springbootrest.model.clientchucknorris.ChuckNorrisJoke;
import com.nastyastrel.springbootrest.model.todo.TaskState;
import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.repository.TodoItemRepository;
import com.nastyastrel.springbootrest.restclient.ChuckNorrisClient;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {
    @Mock
    private TodoItemRepository todoItemRepository;

    @Mock
    private ChuckNorrisClient chuckNorrisClient;

    @Mock
    private UserService userService;

    private TodoItemServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TodoItemServiceImpl(todoItemRepository, chuckNorrisClient, userService);
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
        when(todoItemRepository.findAllByTodoItemOwnerEquals(Mockito.eq(user.getId()))).thenReturn(todoItemList);

        //then
        assertTrue(underTest.findAll(user).stream().anyMatch(todoItem -> todoItem.getState().equals(TaskState.DONE)));
        assertEquals(todoItemList, underTest.findAll(user), "Return list of todoItems");
    }

    @Test
    void itShouldSaveTodoItem() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);

        //when
        underTest.save(todoItem);

        //then
        verify(todoItemRepository).save(ArgumentMatchers.eq(todoItem));
    }

    @Test
    void itShouldDeleteTodoItemIfUserIsCorrect() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(todoItemRepository.findById(todoItem.getSerialNumber())).thenReturn(Optional.of(todoItem));

        //when
        var answer = underTest.deleteTodoItem(todoItem.getSerialNumber());

        //then
        verify(userService).getAuthenticatedUser();
        verify(todoItemRepository).deleteById(todoItem.getSerialNumber());
        assertEquals(new ResponseEntity<TodoItem>(HttpStatus.NO_CONTENT), answer);
    }

    @Test
    void itShouldNotDeleteTodoItemIfUserIsIncorrect() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        //when
        var answer = underTest.deleteTodoItem(todoItem.getSerialNumber());

        //then
        assertEquals(new ResponseEntity<TodoItem>(HttpStatus.UNAUTHORIZED), answer);
    }

    @Test
    void itShouldSaveTodoItemIfUserIsCorrect() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(userService.getAuthenticatedUser()).thenReturn(Optional.of(user));
        when(todoItemRepository.findById(todoItem.getSerialNumber())).thenReturn(Optional.of(todoItem));

        //when
        var answer = underTest.todoItemIsDone(todoItem.getSerialNumber());

        //then
        verify(userService).getAuthenticatedUser();
        verify(todoItemRepository).findById(todoItem.getSerialNumber());
        assertEquals(new ResponseEntity<TodoItem>(HttpStatus.OK), answer);
    }

    @Test
    void itShouldNotSaveTodoItemIfUserIsIncorrect() {
        //given
        TodoItem todoItem = new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L);
        when(userService.getAuthenticatedUser()).thenReturn(Optional.empty());

        //when
        var answer = underTest.todoItemIsDone(todoItem.getSerialNumber());

        //then
        verify(userService).getAuthenticatedUser();
        assertEquals(new ResponseEntity<TodoItem>(HttpStatus.UNAUTHORIZED), answer);
    }

    @Test
    void itShouldFilterTodoItems() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        List<TodoItem> todoItemList =
                List.of(new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L));
        when(todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(word, user.getId())).thenReturn(todoItemList);

        //when
        var answer = underTest.findAllOrFilter(word, user);

        //then
        verify(todoItemRepository).findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(word, user.getId());
        assertEquals(new ResponseEntity<>(todoItemList, HttpStatus.OK), answer);

    }

    @Test
    void itShouldNotFilterTodoItemsBecauseThereIsNoNecessaryWord() {
        //given
        String word = "read";
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        when(todoItemRepository.findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(word, user.getId())).thenReturn(Collections.emptyList());

        //when
        var answer = underTest.findAllOrFilter(word, user);

        //then
        verify(todoItemRepository).findTodoItemByDescriptionIgnoreCaseContainsAndTodoItemOwnerEquals(word, user.getId());
        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), answer);
    }

    @Test
    void itShouldFindAllWithoutChuckNorrisJoke() {
        //given
        String word = null;
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        List<TodoItem> todoItemList =
                List.of(new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L),
                        new TodoItem(26L, "Read over the article", TaskState.UNDONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L));
        when(todoItemRepository.findAllByTodoItemOwnerEquals(user.getId())).thenReturn(todoItemList);


        //when
        var answer = underTest.findAllOrFilter(word, user);

        //then
        verify(todoItemRepository, Mockito.times(2)).findAllByTodoItemOwnerEquals(user.getId());
        assertEquals(new ResponseEntity<>(todoItemList, HttpStatus.OK), answer);
    }

    @Test
    void itShouldFindAllWithChuckNorrisJoke() {
        //given
        String word = null;
        User user = new User(3L, "Filipp", "Kirkorov", "filipp");
        List<TodoItem> todoItemList =
                List.of(new TodoItem(25L, "Buy tickets", TaskState.DONE, LocalDateTime.of(2021, 9, 20, 21, 48, 22), 3L),
                        new TodoItem(26L, "Read over the article", TaskState.DONE, LocalDateTime.of(2021, 9, 28, 13, 18, 28), 3L));
        ChuckNorrisJoke chuckNorrisJoke = new ChuckNorrisJoke();
        chuckNorrisJoke.setValue("Очень смешно");
        TodoItemListWithNorrisJoke todoItemListWithNorrisJoke = new TodoItemListWithNorrisJoke(todoItemList, chuckNorrisJoke);
        when(todoItemRepository.findAllByTodoItemOwnerEquals(user.getId())).thenReturn(todoItemList);
        when(chuckNorrisClient.getChuckNorrisJoke()).thenReturn(chuckNorrisJoke);


        //when
        var answer = underTest.findAllOrFilter(word, user);

        //then
        verify(todoItemRepository, Mockito.times(2)).findAllByTodoItemOwnerEquals(user.getId());
        verify(chuckNorrisClient).getChuckNorrisJoke();
        assertEquals(new ResponseEntity<>(todoItemListWithNorrisJoke, HttpStatus.OK), answer);
    }
}