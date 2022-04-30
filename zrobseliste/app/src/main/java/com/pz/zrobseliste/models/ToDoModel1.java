package com.pz.zrobseliste.models;

public class ToDoModel1 {
    private int id;
    private boolean status;
    private String task;
    private String group;

    public ToDoModel1() {
    }

    public ToDoModel1(int id, boolean status, String task, String group) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.group = group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
