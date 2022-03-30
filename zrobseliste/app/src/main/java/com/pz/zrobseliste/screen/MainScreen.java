package com.pz.zrobseliste.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.MainScreenItemTouch;
import com.pz.zrobseliste.adapter.Main_Screen_Adapter_Rec;
import com.pz.zrobseliste.interfaces.DialogCloseListener;
import com.pz.zrobseliste.interfaces.MainScreenInterface;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.utils.AddNewTask;
import com.pz.zrobseliste.utils.SwipeListener;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, DialogCloseListener, MainScreenInterface {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private SwipeListener swipeListener;
    private GestureDetectorCompat detector;
    Main_Screen_Adapter_Rec tasksAdapter;
    private List<ToDoModel> taskList;
    private ImageButton addTaskButton;
    private Button group_code_button;
    private ImageButton deleteAllSelectedButton;
    private ImageButton addListButton;
    private ImageButton deleteListButton;

    RecyclerView tasks_rec_view;

    String[] items = {"lista1","lista2","lista3"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    BottomNavigationView bottom_nav;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String group_code = "group_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //======================etykieta grupy=================================
        group_code_button = findViewById(R.id.group_code_button);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        group_code_button.setText(sharedPreferences.getString(group_code,"BD1"));
        group_code_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //=====================add list=================================
        addListButton = findViewById(R.id.addListButton);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialogAddList();
            }
        });
        //=====================delete list=================================
        deleteListButton = findViewById(R.id.deleteListButton);
        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialogDeleteList();
            }
        });
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
        tasksAdapter = new Main_Screen_Adapter_Rec(this,this);
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

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_main_screen);

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MainScreenItemTouch(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasks_rec_view);
        //=============================deleteAllSelectedButton=====================
        deleteAllSelectedButton = findViewById(R.id.deleteAllSelectedButton);
        deleteAllSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialog();
            }
        });

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

    public void onBtnAddClick(View view)
    {
        Toast.makeText(this, "dodano liste", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
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
            case R.id.nav_menu:
                finish();
                startActivity(new Intent(MainScreen.this, MenuScreen.class));
                break;
        }
        return true;
    }

    private void buildDialogAddList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_new_list_dialog,null);

        EditText name = view.findViewById(R.id.edit_text_list_name);

        builder.setView(view);
        builder.setTitle(R.string.enter_group_name).
                setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();

    }

    private void buildDialogDeleteList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.do_you_want_delete_list).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();

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
    public void onAssignmentButtonClick(int position) {
        startActivity(new Intent(MainScreen.this, TasksAsignmentScreen.class));
    }
}