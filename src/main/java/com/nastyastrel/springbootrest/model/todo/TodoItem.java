package com.nastyastrel.springbootrest.model.todo;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "todo_items")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serial_number")
    private Long itemId;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TaskState state;

    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "item_owner")
    private Long todoItemOwner;


    public TodoItem() {
    }

    public TodoItem(Long serialNumber, String description, TaskState state, LocalDateTime creationDate, Long todoItemOwner) {
        this.itemId = serialNumber;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
        this.todoItemOwner = todoItemOwner;
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

    public Long getTodoItemOwner() {
        return todoItemOwner;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(itemId, todoItem.itemId) && Objects.equals(description, todoItem.description) && state == todoItem.state && Objects.equals(creationDate, todoItem.creationDate) && Objects.equals(todoItemOwner, todoItem.todoItemOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, description, state, creationDate, todoItemOwner);
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "itemId=" + itemId +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", creationDate=" + creationDate +
                ", todoItemOwner=" + todoItemOwner +
                '}';
    }
}
