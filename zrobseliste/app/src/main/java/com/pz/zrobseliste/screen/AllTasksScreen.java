package com.pz.zrobseliste.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.utils.SwipeListener;

public class AllTasksScreen extends AppCompatActivity {

    SwipeListener swipeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_screen);
    }
}