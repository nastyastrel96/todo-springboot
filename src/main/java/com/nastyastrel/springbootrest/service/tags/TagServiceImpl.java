package com.nastyastrel.springbootrest.service.tags;

import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAllByUserIdEqualsAndItemIdEquals(Long userId, Long itemId) {
        return tagRepository.findAllByUserIdEqualsAndItemIdEquals(userId, itemId);
    }

    @Override
    public List<Tag> findAllByUserIdEqualsAndTagNameEqualsIgnoreCase(Long userId, String tagName) {
        return tagRepository.findAllByUserIdEqualsAndTagNameEqualsIgnoreCase(userId, tagName);
    }
}
