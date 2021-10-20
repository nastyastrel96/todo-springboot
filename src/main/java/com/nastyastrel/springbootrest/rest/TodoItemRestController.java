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
    public ResponseEntity<?> findAllOrFilter(@RequestParam(value = "q", required = false) String word,
                                             @RequestParam(value = "t", required = false) String tag) {
        User user = userService.getAuthenticatedUser().orElseThrow();
        return todoItemService.findAllOrFilter(word, user, tag);
    }

    @PatchMapping("/todos/{number}")
    public ResponseEntity<TodoItem> todoIsDone(@PathVariable Long number) {
        return todoItemService.todoItemIsDone(number);
    }

    @DeleteMapping("/todos/{number}")
    public ResponseEntity<TodoItem> deleteItem(@PathVariable Long number) {
        return todoItemService.deleteTodoItem(number);
    }
}
