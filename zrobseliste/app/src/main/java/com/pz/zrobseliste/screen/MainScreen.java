package com.pz.zrobseliste.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.utils.SwipeListener;

public class MainScreen extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private SwipeListener swipeListener;
    private GestureDetectorCompat detector;

    String[] items = {"lista1","lista2","lista3"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        swipeListener = new SwipeListener();

        detector = new GestureDetectorCompat(this, this);

        //autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        //adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        //autoCompleteTxt.setAdapter(adapterItems);
        //autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           // @Override
           // public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //     String item = parent.getItemAtPosition(position).toString();
          //      Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
          //  }

       // });
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {

        detector.onTouchEvent(touchEvent);
        return super.onTouchEvent(touchEvent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x1 = e1.getX();
        float y1 = e1.getY();
        float x2 = e2.getX();
        float y2 = e2.getY();

        swipeListener.setSwipeType(x1, y1, x2, y2, velocityX, velocityY);

        switch (swipeListener.getSwipeType()){
            case LEFT:
                startActivity(new Intent(MainScreen.this, AllTasksScreen.class));
                return true;
            case RIGHT:
                startActivity(new Intent(MainScreen.this, GroupsScreen.class));
                return true;
        }

        return false;
    }
}