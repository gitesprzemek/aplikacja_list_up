package com.pz.zrobseliste.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {

    private String email;
    private String username;
    private String password;

    public UserModel() {
    }

    public UserModel(String name, String password) {
        this.email = name;
        this.password = password;
    }
    public UserModel(String email,String username,String password) {
        this.email = email;
        this.username = username;
        this.password = password;

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

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public JSONObject loginDatatoJSON(){

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
    public JSONObject registrationDatatoJSON(){

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject()
                    .put("email", this.getEmail())
                    .put("name",this.getUsername())
                    .put("password", this.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
