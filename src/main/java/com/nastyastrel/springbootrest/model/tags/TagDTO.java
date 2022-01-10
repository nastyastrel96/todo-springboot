package com.nastyastrel.springbootrest.model.tags;

import java.util.Objects;

public class TagDTO {
    private final String TagName;

    private TagDTO(String tagName) {
        TagName = tagName;
    }

    public static TagDTO fromTag(Tag tag) {
        return new TagDTO(tag.getTagName());
    }

    public String getTagName() {
        return TagName;
    }
}
