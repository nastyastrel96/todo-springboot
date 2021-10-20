package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.tags.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {
    List<Tag> findAllByUserIdEqualsAndItemIdEquals(Long userId, Long itemId);

    List<Tag> findAllByUserIdEqualsAndTagNameEqualsIgnoreCase(Long userId, String tagName);
}
