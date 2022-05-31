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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return Objects.equals(TagName, tagDTO.TagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TagName);
    }
}
