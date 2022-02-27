package com.pz.zrobseliste.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupModel {

    private int groupID;
    private String name;
    private String abb;
    private ArrayList<Integer> usersId;
    private ArrayList<ToDoModel1> taskList;

    public GroupModel(int groupID, String name, String abb, ArrayList<Integer> usersId, ArrayList<ToDoModel1> taskList) {
        this.groupID = groupID;
        this.name = name;
        this.abb = abb;
        this.usersId = usersId;
        this.taskList = taskList;
    }

    public GroupModel(int groupID, String name) {
        this.groupID = groupID;
        this.name = name;
        this.usersId = null;
        this.taskList = null;
        String abb = "";

        String[] splitedString = name.split(" ");

        for (String s :splitedString) {
            abb += s.charAt(0);
        }
        this.abb = abb.toUpperCase();
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public ArrayList<Integer> getUsersId() {
        return usersId;
    }

    public void setUsersId(ArrayList<Integer> usersId) {
        this.usersId = usersId;
    }

    public ArrayList<ToDoModel1> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<ToDoModel1> taskList) {
        this.taskList = taskList;
    }

    public JSONObject registrationDatatoJSON(){

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject()
                    .put("groupID", this.groupID)
                    .put("name", this.name)
                    .put("abb", this.abb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}