package com.example.todo;

public class Task {
    public static final int STATUS_TODO = 0;
    public static final int STATUS_DONE = 1;

    private int id;
    private String name;
    private String description;
    private int status;
    private String dueDate;

    public Task() {
    }

    public Task(int id, String name, String description, int status, String dueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public Task(String name, String description, int status, String dueDate) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDone() {
        return status == STATUS_DONE;
    }
}
