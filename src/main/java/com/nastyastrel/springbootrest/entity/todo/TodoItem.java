package com.nastyastrel.springbootrest.entity.todo;

import com.nastyastrel.springbootrest.entity.todo.TaskState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo")
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serial_number")
    private int serialNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private TaskState state;

    @Column(name = "creation_date")
    @CreationTimestamp
    private LocalDateTime creationDate;

    public TodoItem() {
    }

    public TodoItem(int serialNumber, String description, TaskState state, LocalDateTime creationDate) {
        this.serialNumber = serialNumber;
        this.description = description;
        this.state = state;
        this.creationDate = creationDate;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
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
}
