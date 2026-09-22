package com.example.todo;

// Model class representing a Task entity in the application
public class Task {
    // Status constants
    public static final int STATUS_TODO = 0;
    public static final int STATUS_DONE = 1;

    // Member variables
    private int id;
    private String name;
    private String description;
    private int status;
    private String dueDate;

    // Default empty constructor
    public Task() {
    }

    // Constructor with ID (used when reading existing tasks from database)
    public Task(int id, String name, String description, int status, String dueDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Constructor without ID (used when creating a new task before DB insertion)
    public Task(String name, String description, int status, String dueDate) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Getters and Setters
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

    // Helper method to check if the task is completed
    public boolean isDone() {
        return status == STATUS_DONE;
    }
}
