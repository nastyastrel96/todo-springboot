package com.nastyastrel.springbootrest.tree;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.todo.TaskState;

import java.util.List;


public record TodoItemResponse(
        Long itemId,
        String description,
        TaskState state,
       // List<Tag> tags,
        List<TodoItemResponse> subtasks
) {
}
