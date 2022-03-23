package com.pz.zrobseliste.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.AllTaskScreenItemTouch;
import com.pz.zrobseliste.adapter.All_Task_Screen_Adapter_Rec;
import com.pz.zrobseliste.adapter.MainScreenItemTouch;
import com.pz.zrobseliste.interfaces.DialogCloseListener;
import com.pz.zrobseliste.models.ToDoModel1;

import java.util.ArrayList;
import java.util.List;

public class AllTasksScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, DialogCloseListener {


    RelativeLayout relativeLayout;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    All_Task_Screen_Adapter_Rec tasksAdapter;
    private List<ToDoModel1> taskList;
    private ImageButton deleteAllSelectedButton;

    RecyclerView tasks_rec_view;

    ArrayAdapter<String> adapterItems;
    BottomNavigationView bottom_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_screen);

        //--------------------list----------------------------------------------
        taskList = new ArrayList<>();

        tasks_rec_view = findViewById(R.id.task_rec_view);
        tasks_rec_view.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new All_Task_Screen_Adapter_Rec(this);
        tasks_rec_view.setAdapter(tasksAdapter);


        for(int i=1;i<=6;i++)
        {
            ToDoModel1 task = new ToDoModel1();
            task.setTask("Zadanie : " + i);
            task.setStatus(1);
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
        //=============================deleteAllSelectedButton=====================
        deleteAllSelectedButton = findViewById(R.id.deleteAllSelectedButton);
        deleteAllSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });
        //============================================================================
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new AllTaskScreenItemTouch(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasks_rec_view);
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

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.do_you_really_wanna_delete_all_selected_tasks).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskList.removeIf(x -> x.getStatus() == 1);
                        tasksAdapter.setTasks(taskList);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();

    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
       /*
        for(int i=1;i<=6;i++)
        {
            ToDoModel1 task = new ToDoModel1();
            task.setTask("Zadanie : " + i);
            task.setStatus(1);
            task.setId(i);
            task.setGroup("GR" + i);
            taskList.add(task);
        }
        tasksAdapter.setTasks(taskList);
        */

    }
}