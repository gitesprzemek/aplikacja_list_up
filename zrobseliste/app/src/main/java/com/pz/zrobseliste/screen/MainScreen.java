package com.pz.zrobseliste.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.utils.SwipeListener;

public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener,GestureDetector.OnGestureListener {

    private SwipeListener swipeListener;
    private GestureDetectorCompat detector;

    String[] items = {"lista1","lista2","lista3"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottom_nav;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        swipeListener = new SwipeListener();

        detector = new GestureDetectorCompat(this, this);

        //---------------------menu------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_main_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //------------------ drop down list----------------------------
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
        }

    public void onBtnAddClick(View view)
    {
        Toast.makeText(this, "dodano liste", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_home:
                Toast.makeText(this,"glowna",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_settings:
                Toast.makeText(this,"ustawienia",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_logout:
                finish();
                break;
            case R.id.nav_tasks:
                Toast.makeText(this,"zadania",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_main_screen:
                Toast.makeText(this,"glowny",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_groups:
                Toast.makeText(this,"grupy",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
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