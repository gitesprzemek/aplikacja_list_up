package com.pz.zrobseliste.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.ToDoAdapter;
import com.pz.zrobseliste.adapter.ToDoAdapter1;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.models.ToDoModel1;
import com.pz.zrobseliste.utils.SwipeListener;
import com.pz.zrobseliste.utils.SwipeType;

import java.util.ArrayList;
import java.util.List;

public class AllTasksScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationView.OnNavigationItemSelectedListener{

    SwipeListener swipeListener;
    RelativeLayout relativeLayout;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private GestureDetectorCompat detector;
    ToDoAdapter1 tasksAdapter;
    private List<ToDoModel1> taskList;

    RecyclerView tasks_rec_view;

    ArrayAdapter<String> adapterItems;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottom_nav;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_screen);

        swipeListener = new SwipeListener();

        //--------------------list----------------------------------------------
        taskList = new ArrayList<>();

        tasks_rec_view = findViewById(R.id.task_rec_view);
        tasks_rec_view.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter1(this);
        tasks_rec_view.setAdapter(tasksAdapter);


        for(int i=1;i<=6;i++)
        {
            ToDoModel1 task = new ToDoModel1();
            task.setTask("Zadanie : " + i);
            task.setStatus(0);
            task.setId(i);
            task.setGroup("GR" + i);
            taskList.add(task);
        }
        tasksAdapter.setTasks(taskList);
        //---------------------menu------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_tasks);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
                finish();
                startActivity(new Intent(AllTasksScreen.this, MainScreen.class));
                break;
            case R.id.nav_groups:
                Toast.makeText(this,"grupy",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    public boolean onTouchEvent(MotionEvent touchEvent) {

        if(swipeListener.getSwipeType() == SwipeType.LEFT){
            startActivity(new Intent(AllTasksScreen.this, MainScreen.class));
            return true;}


        return true;
    }

}