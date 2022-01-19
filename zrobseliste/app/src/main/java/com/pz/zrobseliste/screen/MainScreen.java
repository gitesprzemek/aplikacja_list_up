package com.pz.zrobseliste.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.utils.SwipeListener;
import com.pz.zrobseliste.utils.SwipeType;

public class MainScreen extends AppCompatActivity {

    SwipeListener swipeListener;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        relativeLayout = findViewById(R.id.relative_layout);
        swipeListener = new SwipeListener(relativeLayout);
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {

        if(swipeListener.getSwipeType() == SwipeType.RIGHT){
            startActivity(new Intent(MainScreen.this, GroupsScreen.class));
            return true;
        } else if(swipeListener.getSwipeType() == SwipeType.LEFT){
            startActivity(new Intent(MainScreen.this, AllTasksScreen.class));
            return true;
        }


        return true;
    }
}