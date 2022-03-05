package com.pz.zrobseliste.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.RecyclerItemTouch;
import com.pz.zrobseliste.adapter.ToDoAdapter;
import com.pz.zrobseliste.interfaces.DialogCloseListener;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.utils.AddNewTask;
import com.pz.zrobseliste.utils.SwipeListener;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        ,NavigationView.OnNavigationItemSelectedListener, DialogCloseListener {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private SwipeListener swipeListener;
    private GestureDetectorCompat detector;
    ToDoAdapter tasksAdapter;
    private List<ToDoModel> taskList;
    private ImageButton addTaskButton;

    RecyclerView tasks_rec_view;

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

        //=============================addTask==================================
        addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }

        });

        //--------------------list----------------------------------------------
        taskList = new ArrayList<>();

        tasks_rec_view = findViewById(R.id.task_rec_view);
        tasks_rec_view.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this);
        tasks_rec_view.setAdapter(tasksAdapter);


        for(int i=0;i<=6;i++)
        {
            ToDoModel task = new ToDoModel();
            task.setTask("Zadanie : " + i);
            task.setStatus(0);
            task.setId(i);
            taskList.add(task);
        }
        tasksAdapter.setTasks(taskList);

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
                if(item.equals("lista1"))

                {
                    taskList.clear();
                    for(int i=0;i<=6;i++)
                    {

                        ToDoModel task = new ToDoModel();
                        task.setTask("Zadanie : " + i);
                        task.setStatus(0);
                        task.setId(i);
                        taskList.add(task);
                    }
                    tasksAdapter.setTasks(taskList);
                }
                if(item.equals("lista2"))

                {
                    taskList.clear();
                    for(int i=7;i<=12;i++)
                    {
                        ToDoModel task = new ToDoModel();
                        task.setTask("Zadanie : " + i);
                        task.setStatus(1);
                        task.setId(i);
                        taskList.add(task);
                    }
                    tasksAdapter.setTasks(taskList);
                }
                if(item.equals("lista3"))

                {
                    taskList.clear();
                    for(int i=13;i<=18;i++)
                    {
                        ToDoModel task = new ToDoModel();
                        task.setTask("Zadanie : " + i);
                        task.setStatus(0);
                        task.setId(i);
                        taskList.add(task);
                    }
                    tasksAdapter.setTasks(taskList);
                }

            }

        });
        //=============================edit_delete_task=========================
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouch(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasks_rec_view);


    }
    @Override
    public void handleDialogClose(DialogInterface dialog)
    {
     /*   for(int i=0;i<=6;i++)
        {

            ToDoModel task = new ToDoModel();
            task.setTask("Zadanie : " + i);
            task.setStatus(0);
            task.setId(i);
            taskList.add(task);
        }
        tasksAdapter.setTasks(taskList);
    */
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
                finish();
                startActivity(new Intent(MainScreen.this, AllTasksScreen.class));
                break;
            case R.id.nav_main_screen:
                break;
            case R.id.nav_groups:
                finish();
                startActivity(new Intent(MainScreen.this, GroupsScreen.class));
                break;
        }
        return true;
    }



    @Override
    public boolean onTouchEvent(MotionEvent touchEvent) {

        detector.onTouchEvent(touchEvent);
        return super.onTouchEvent(touchEvent);
    }




}