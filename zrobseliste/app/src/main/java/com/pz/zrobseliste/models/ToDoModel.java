package com.pz.zrobseliste.models;

public class ToDoModel {
    private int id;
    boolean status;
    private String task;
    private String assigment;

    public ToDoModel(int id, boolean status, String task) {
        this.id = id;
        this.status = status;
        this.task = task;
    }

    public ToDoModel(int id, boolean status, String task, String assigment) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.assigment = assigment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getAssigment() {
        return assigment;
    }

    public void setAssigment(String assigment) {
        this.assigment = assigment;
    }

    @Override
    public String toString() {
        return "ToDoModel{" +
                "id=" + id +
                ", status=" + status +
                ", task='" + task + '\'' +
                ", assigment='" + assigment + '\'' +
                '}';
    }
}
