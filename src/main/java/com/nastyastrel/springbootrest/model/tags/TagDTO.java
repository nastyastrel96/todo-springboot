package com.nastyastrel.springbootrest.model.tags;

public record TagDTO(String tag) {

    public static TagDTO from(Tag tag) {
        return new TagDTO(tag.getTagName());
    }
}
