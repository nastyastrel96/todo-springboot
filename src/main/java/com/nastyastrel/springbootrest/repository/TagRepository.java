package com.nastyastrel.springbootrest.repository;

import com.nastyastrel.springbootrest.model.tags.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {
    //   List<Tag> findAllByUserIdEqualsAndTagNameEquals(Long userId, String tagName); Сделано без БД
}
