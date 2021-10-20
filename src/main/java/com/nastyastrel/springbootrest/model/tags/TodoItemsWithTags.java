package com.nastyastrel.springbootrest.model.tags;

import com.nastyastrel.springbootrest.model.todo.TodoItem;

import java.util.List;

public record TodoItemsWithTags(TodoItem todoItem, List<TagDTO> tagList) {
}
