package com.nastyastrel.springbootrest.service.tag;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.todo.TodoItem;


public interface TagService {
    void save(Tag tag);

    TodoItem attachTag(Long userId, Long itemId, Long tagId);

    void detachTag(Long userId, Long itemId, Long tagId);
}
