package com.nastyastrel.springbootrest.service.tag;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.user.User;
import org.springframework.http.ResponseEntity;

public interface TagService {
    void save(Tag tag);

    ResponseEntity<Tag> assignTag(Long userId, Long itemId, Long tagId);

    ResponseEntity<Tag> deleteTag(Long userId, Long itemId, Long tagId);
}
