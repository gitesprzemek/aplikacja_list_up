package com.pz.zrobseliste.models;

import android.graphics.drawable.Drawable;

public class MenuModel {
    int image;
    int id;
    String text;

    public MenuModel(int image, int id, String text) {
        this.image = image;
        this.id = id;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
