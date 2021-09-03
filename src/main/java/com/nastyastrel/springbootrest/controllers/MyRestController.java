package com.nastyastrel.springbootrest.controllers;

import com.nastyastrel.springbootrest.entity.todo.TodoItem;
import com.nastyastrel.springbootrest.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class MyRestController {
    private final TodoItemService todoItemService;

    @Autowired
    public MyRestController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody TodoItem todoItem) {
        todoItemService.save(todoItem);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<TodoItem>> findAllOrFilter(@RequestParam(value = "q", required = false) String word) {
        Optional<String> isFilterToNeed = Optional.ofNullable(word);
        if (isFilterToNeed.isPresent()) {
            if (todoItemService.findSpecificItem(isFilterToNeed.get()).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else return new ResponseEntity<>(todoItemService.findSpecificItem(isFilterToNeed.get()), HttpStatus.OK);
        } else return new ResponseEntity<>(todoItemService.findAll(), HttpStatus.OK);
    }


    @PatchMapping("/todos/{number}")
    public ResponseEntity<TodoItem> changeStateToDone(@PathVariable int number) {
        for (TodoItem todoItem : todoItemService.findAll()) {
            if (todoItem.getSerialNumber() == number) {
                todoItemService.changeStateToDone(number);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{number}")
    public ResponseEntity<TodoItem> deleteItem(@PathVariable int number) {
        for (TodoItem todoItem : todoItemService.findAll()) {
            if (todoItem.getSerialNumber() == number) {
                todoItemService.deleteItem(number);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
