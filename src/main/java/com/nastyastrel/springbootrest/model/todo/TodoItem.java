package com.nastyastrel.springbootrest.model.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.nastyastrel.springbootrest.model.tags.Tag;
import com.nastyastrel.springbootrest.model.tags.TagDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Entity
@Table(name = "items")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long itemId;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TaskState state;

    @Column(name = "repeatable")
    private boolean repeatable;

    @Column(name = "updating_time")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime updatingTime;

    @Column(name = "creation_date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationDate;

    @Column(name = "users_id")
    private Long userId;

    @Column(name = "parent_item_id")
    private Long parentId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "item_tag",
            joinColumns = @JoinColumn(name = "items_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private Set<Tag> tags = new HashSet<>();

    public TodoItem() {
    }

    public TodoItem(Long itemId, String description, TaskState state, LocalDateTime creationDate, Long userId) {
        this.itemId = itemId;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public TodoItem(String description, TaskState state, boolean repeatable, LocalTime updatingTime, LocalDateTime creationDate, Long userId) {
        this.description = description;
        this.state = state;
        this.repeatable = repeatable;
        this.updatingTime = updatingTime;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public TodoItem(String description, TaskState state, LocalDateTime creationDate, Long userId) {
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void deleteTag(Tag tag) {
        tags.remove(tag);
    }

    public Long getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public TaskState getState() {
        return state;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public List<TagDTO> getTags() {
        List<TagDTO> dtoList = new ArrayList<>();
        for (Tag tag : tags) {
            dtoList.add(TagDTO.fromTag(tag));
        }
        return dtoList;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public LocalTime getUpdatingTime() {
        return updatingTime;
    }

    public void setUpdatingTime(LocalTime updatingTime) {
        this.updatingTime = updatingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(itemId, todoItem.itemId);
    }

    @Override
    public int hashCode() {
        return itemId != null ? itemId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "itemId=" + itemId +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", repeatable=" + repeatable +
                ", updatingTime=" + updatingTime +
                ", creationDate=" + creationDate +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", tags=" + tags +
                '}';
    }
}
