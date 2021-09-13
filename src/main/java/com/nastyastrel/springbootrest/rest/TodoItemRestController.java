package com.nastyastrel.springbootrest.rest;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.service.todo.TodoItemService;
import com.nastyastrel.springbootrest.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<List<TodoItem>> findAllOrFilter(@RequestParam(value = "q", required = false) String word) {
        User authenticatedUser = userService.definePrincipal();
        if (word != null) {
            if (todoItemService.findSpecificItem(word, authenticatedUser.getId()).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else
                return new ResponseEntity<>(todoItemService.findSpecificItem(word, authenticatedUser.getId()), HttpStatus.OK);
        } else return new ResponseEntity<>(todoItemService.findAll(authenticatedUser.getId()), HttpStatus.OK);
    }

    @PatchMapping("/todos/{number}")
    public ResponseEntity<TodoItem> changeStateToDone(@PathVariable int number) {
        if (todoItemService.changeStateToDone(number).isPresent()) {
            todoItemService.changeStateToDone(number);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{number}")
    public ResponseEntity<TodoItem> deleteItem(@PathVariable int number) {
        if (todoItemService.deleteItem(number).isPresent()) {
            todoItemService.deleteItem(number);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
