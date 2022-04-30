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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.pz.zrobseliste.interfaces.AllTaskScreenInterface;
import com.pz.zrobseliste.interfaces.DialogCloseListener;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.models.ToDoModel1;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllTasksScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, DialogCloseListener, AllTaskScreenInterface {


    RelativeLayout relativeLayout;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    All_Task_Screen_Adapter_Rec tasksAdapter;
    private List<ToDoModel1> taskList;
    private ImageButton deleteAllSelectedButton;
    private OkHttpClient client;
    private Request request;

    RecyclerView tasks_rec_view;


    ArrayAdapter<String> adapterItems;
    BottomNavigationView bottom_nav;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String cookie = "cookie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks_screen);

        //--------------------list----------------------------------------------
        taskList = new ArrayList<>();

        tasks_rec_view = findViewById(R.id.task_rec_view);
        tasks_rec_view.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new All_Task_Screen_Adapter_Rec(this,this);
        tasks_rec_view.setAdapter(tasksAdapter);

        /*
        for(int i=1;i<=6;i++)
        {
            ToDoModel1 task = new ToDoModel1();
            task.setTask("Zadanie : " + i);
            task.setStatus(true);
            task.setId(i);
            task.setGroup("GR" + i);
            taskList.add(task);
        }
        ToDoModel1 task = new ToDoModel1();
        task.setTask("ZadanieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        task.setStatus(false);
        task.setId(7);
        task.setGroup("GR" + 7);
        taskList.add(task);
           */
        tasksAdapter.setTasks(taskList);
        getTasks();
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
                        taskList.removeIf(ToDoModel1::getStatus);
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
        getTasks();

    }

    @Override
    public void deleteTask(int position) {

        ToDoModel1 item = taskList.get(position);
        String id = "" + item.getId();
        client = CustomHttpBuilder.SSL().build();
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/tasks")
                .addQueryParameter("taskId",id)
                .build().url();

        Log.d("url",url.toString());

        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("status code delete item",String.valueOf(response.code()));
                Log.d("response body delete item",response.body().string());
                if(response.code()>=200 && response.code()<300)
                {

                    taskList.remove(item);
                    AllTasksScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tasksAdapter.setTasks(taskList);
                        }
                    });

                }
            }
        });
    }
    public void getTasks()
    {
        taskList = new ArrayList<>();
        client = CustomHttpBuilder.SSL().build();
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

            URL url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("weaweg.mywire.org")
                    .port(8080)
                    .addPathSegments("api/users/tasks")
                    .build().url();

            Log.d("url_all_tasks", url.toString());


            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("statuscode get tasks", String.valueOf(response.code()));
                    //Log.d("responsebody tasks",response.body().string());

                    if(response.code()>= 200 && response.code()<300)
                    {
                        JSONObject json;
                        try {
                            final JSONArray data = new JSONArray(response.body().string());
                            for (int i = 0; i < data.length(); i++) {
                                json = data.getJSONObject(i);
                                int id = json.getInt("task_id");
                                String name = json.getString("description");
                                boolean status = json.getBoolean("status");
                                JSONObject list = json.getJSONObject("list");
                                JSONObject grupa = list.getJSONObject("group");

                                String group_name = grupa.getString("name");
                                taskList.add(new ToDoModel1(id,status,name,group_name));
                            }
                            AllTasksScreen.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        tasksAdapter.setTasks(taskList);

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });



    }
}