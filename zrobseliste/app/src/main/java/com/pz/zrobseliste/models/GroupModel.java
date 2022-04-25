package com.pz.zrobseliste.models;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupModel {

    private int groupID;
    private String name;
    private String group_code;

    public GroupModel() {

    }

    public GroupModel(int groupID, String name, String group_code) {
        this.groupID = groupID;
        this.name = name;
        this.group_code = group_code;
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

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String abb) {
        this.group_code = abb;
    }

    public JSONObject registrationDatatoJSON(){

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject()
                    .put("groupID", this.groupID)
                    .put("name", this.name)
                    .put("abb", this.group_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}