package com.nastyastrel.springbootrest.rest;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.service.todo.TodoItemService;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoItemRestController {
    private final TodoItemService todoItemService;
    private final UserService userService;

    @Autowired
    public TodoItemRestController(TodoItemService todoItemService, UserService userService) {
        this.todoItemService = todoItemService;
        this.userService = userService;
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody TodoItem todoItem) {
        todoItemService.save(todoItem);
    }

    @GetMapping("/todos")
    public ResponseEntity<?> findAllOrFilter(@RequestParam(value = "q", required = false) String word, @AuthenticationPrincipal User user) {
        user = userService.getAuthenticatedUser().orElseThrow();
        if (word != null) {
            if (todoItemService.findSpecificItem(word, user.getId()).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else
                return new ResponseEntity<>(todoItemService.findSpecificItem(word, user.getId()), HttpStatus.OK);
        } else if (!todoItemService.checkTasksStateToBeDone(user)) {
            return new ResponseEntity<>(todoItemService.findAll(user.getId()), HttpStatus.OK);
        } else return new ResponseEntity<>(todoItemService.getTodoItemWithNorrisJoke(user), HttpStatus.OK);
    }

    @PatchMapping("/todos/{number}")
    public ResponseEntity<TodoItem> changeStateToDone(@PathVariable Long number) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        if (optionalUser.isPresent()) {
            Optional<TodoItem> optionalTodoItem = todoItemService.changeStateToDone(number, optionalUser.get());
            if (optionalTodoItem.isPresent()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{number}")
    public ResponseEntity<TodoItem> deleteItem(@PathVariable Long number) {
        Optional<User> optionalUser = userService.getAuthenticatedUser();
        if (optionalUser.isPresent()) {
            Optional<TodoItem> optionalTodoItem = todoItemService.deleteItem(number, optionalUser.get());
            if (optionalTodoItem.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
