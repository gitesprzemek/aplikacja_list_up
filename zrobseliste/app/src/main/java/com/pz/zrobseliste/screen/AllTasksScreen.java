package com.pz.zrobseliste.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.All_Tast_Screen_Adapter_Rec;
import com.pz.zrobseliste.models.ToDoModel1;

import java.util.ArrayList;
import java.util.List;

public class AllTasksScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{


    RelativeLayout relativeLayout;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    All_Tast_Screen_Adapter_Rec tasksAdapter;
    private List<ToDoModel1> taskList;

    RecyclerView tasks_rec_view;

    ArrayAdapter<String> adapterItems;
    BottomNavigationView bottom_nav;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_screen);

        //--------------------list----------------------------------------------
        taskList = new ArrayList<>();

        tasks_rec_view = findViewById(R.id.task_rec_view);
        tasks_rec_view.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new All_Tast_Screen_Adapter_Rec(this);
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
        ToDoModel1 task = new ToDoModel1();
        task.setTask("ZadanieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        task.setStatus(0);
        task.setId(7);
        task.setGroup("GR" + 7);
        taskList.add(task);

        tasksAdapter.setTasks(taskList);
        //---------------------menu------------------------------------------
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_tasks);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(null);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_tasks:
                break;
            case R.id.nav_main_screen:
                finish();
                startActivity(new Intent(AllTasksScreen.this, MainScreen.class));
                break;
            case R.id.nav_groups:
                finish();
                startActivity(new Intent(AllTasksScreen.this, GroupsScreen.class));
                break;
            case R.id.nav_menu:
                finish();
                startActivity(new Intent(AllTasksScreen.this, MenuScreen.class));
                break;
        }
        return true;
    }


}