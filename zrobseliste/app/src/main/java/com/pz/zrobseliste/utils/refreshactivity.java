package com.pz.zrobseliste.utils;


import androidx.appcompat.app.AppCompatActivity;

public class refreshactivity {
    public static void finishApp(AppCompatActivity activity)
    {
        activity.finish();
    }
    public static void refreshApp(AppCompatActivity activity)
    {
        activity.recreate();
    }

}
