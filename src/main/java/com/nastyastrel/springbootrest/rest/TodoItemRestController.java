package com.nastyastrel.springbootrest.rest;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import com.nastyastrel.springbootrest.model.todo.TodoItemListWithNorrisJoke;
import com.nastyastrel.springbootrest.model.user.User;
import com.nastyastrel.springbootrest.service.todo.TodoItemService;
import com.nastyastrel.springbootrest.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;


@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TodoItemRestController {
    private final TodoItemService todoItemService;
    private final UserService userService;

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody TodoItem todoItem, Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        todoItem.setUserId(user.getUserId());
        todoItem.setCreationDate(LocalDateTime.now());
        todoItemService.save(todoItem);
    }

    @GetMapping("/todos")
    public ResponseEntity<TodoItemListWithNorrisJoke> findAllOrFilter(@RequestParam(value = "word", required = false) String word,
                                                                      @RequestParam(value = "tag", required = false) String tagName,
                                                                      Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        return new ResponseEntity<>(todoItemService.findAllOrFilter(word, user.getUserId(), tagName), HttpStatus.OK);
    }

    @PatchMapping("/todos/{number}")
    public ResponseEntity<TodoItem> changeToDone(@PathVariable Long number, Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        return new ResponseEntity<>(todoItemService.changeToDone(number, user.getUserId()), HttpStatus.OK);
    }

    @DeleteMapping("/todos/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long number, Principal principal) {
        User user = userService.getAuthenticatedUser(principal);
        todoItemService.deleteTodoItem(number, user.getUserId());
    }
}
