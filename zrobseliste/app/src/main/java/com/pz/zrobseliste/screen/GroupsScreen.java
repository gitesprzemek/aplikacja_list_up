package com.pz.zrobseliste.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.utils.SwipeListener;
import com.pz.zrobseliste.utils.SwipeType;

public class GroupsScreen extends AppCompatActivity {

    SwipeListener swipeListener;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_screen);

        swipeListener = new SwipeListener();
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_UP:
                if (swipeListener.getSwipeType() == SwipeType.RIGHT) {
                    startActivity(new Intent(GroupsScreen.this, MainScreen.class));
                }
                break;
        }

        return true;
    }

}