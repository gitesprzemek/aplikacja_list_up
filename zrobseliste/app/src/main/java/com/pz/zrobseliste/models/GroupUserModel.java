package com.pz.zrobseliste.models;

public class GroupUserModel {

    int id;
    String username;
    String email;

    public GroupUserModel() {
    }

    public GroupUserModel(String username) {
        this.username = username;
    }

    public GroupUserModel(int id, String username) {
        this.id=id;
        this.username=username;
    }

    public GroupUserModel(int id, String username,String email) {
        this.id=id;
        this.username=username;
        this.email = email;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
