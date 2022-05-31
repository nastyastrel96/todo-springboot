package com.nastyastrel.springbootrest.model.tags;

import com.nastyastrel.springbootrest.model.todo.TodoItem;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tags")
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long tagId;

    @Column(name = "tag")
    private String tagName;

    @Column(name = "users_id")
    private Long userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "item_tag",
            joinColumns = @JoinColumn(name = "tags_id"),
            inverseJoinColumns = @JoinColumn(name = "items_id"))
    private List<TodoItem> todoItems = new ArrayList<>();

    public Tag(Long tagId, String tagName, Long userId) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.userId = userId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId) && Objects.equals(tagName, tag.tagName) && Objects.equals(userId, tag.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tagName, userId);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
