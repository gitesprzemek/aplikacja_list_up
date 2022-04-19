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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
