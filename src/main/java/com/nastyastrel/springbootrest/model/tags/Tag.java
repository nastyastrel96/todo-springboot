package com.nastyastrel.springbootrest.model.tags;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "todo_tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag")
    private String tagName;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "todo")
    private Long itemId;


    protected Tag() {
    }

    public Tag(Long tagId, String tag) {
        this.tagId = tagId;
        this.tagName = tag;
    }

    public Long getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag1 = (Tag) o;
        return Objects.equals(tagId, tag1.tagId) && Objects.equals(tagName, tag1.tagName) && Objects.equals(userId, tag1.userId) && Objects.equals(itemId, tag1.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tagName, userId, itemId);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tag='" + tagName + '\'' +
                ", userId=" + userId +
                ", itemId=" + itemId +
                '}';
    }
}
