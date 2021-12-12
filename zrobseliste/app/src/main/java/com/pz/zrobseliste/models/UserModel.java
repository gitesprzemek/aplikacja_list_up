package com.pz.zrobseliste.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {

    private String email;
    private String password;

    public UserModel(String name, String password) {
        this.email = name;
        this.password = password;
    }

    public UserModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject()
                    .put("email", this.getEmail())
                    .put("password", this.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
