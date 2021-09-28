package com.nastyastrel.springbootrest.model.todo;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Entity
@Table(name = "todo_items")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serial_number")
    private Long serialNumber;

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
        this.serialNumber = serialNumber;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
        this.todoItemOwner = todoItemOwner;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getTodoItemOwner() {
        return todoItemOwner;
    }

    public void setTodoItemOwner(Long todoItemOwner) {
        this.todoItemOwner = todoItemOwner;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "serialNumber=" + serialNumber +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", creationDate=" + creationDate +
                ", todoItemOwner=" + todoItemOwner +
                '}';
    }
}
