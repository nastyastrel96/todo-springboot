package com.nastyastrel.springbootrest.service.tags;


import com.nastyastrel.springbootrest.model.tags.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAllByUserIdEqualsAndItemIdEquals(Long userId, Long itemId);

    List<Tag> findAllByUserIdEqualsAndTagNameEqualsIgnoreCase(Long userId, String tagName);
}
