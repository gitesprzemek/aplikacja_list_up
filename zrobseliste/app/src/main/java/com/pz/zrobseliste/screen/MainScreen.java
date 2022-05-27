package com.pz.zrobseliste.screen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Drop_Down_Menu_Adapter;
import com.pz.zrobseliste.adapter.MainScreenItemTouch;
import com.pz.zrobseliste.adapter.Main_Screen_Adapter_Rec;
import com.pz.zrobseliste.interfaces.AddNewTaskInterface;
import com.pz.zrobseliste.interfaces.DialogCloseListener;
import com.pz.zrobseliste.interfaces.MainScreenInterface;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.models.ListModel;
import com.pz.zrobseliste.models.ToDoModel;
import com.pz.zrobseliste.models.ToDoModel1;
import com.pz.zrobseliste.utils.AddNewTask;
import com.pz.zrobseliste.utils.CustomHttpBuilder;
import com.pz.zrobseliste.utils.SwipeListener;

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
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, DialogCloseListener, MainScreenInterface, AddNewTaskInterface {

    Main_Screen_Adapter_Rec tasksAdapter;
    private List<ToDoModel> taskList;
    private ImageButton addTaskButton;
    private Button group_code_button;
    private ImageButton deleteAllSelectedButton;
    private ImageButton addListButton;
    private ImageButton deleteListButton;
    private Drop_Down_Menu_Adapter drop_down_menu_adapter;
    private ListModel selectedlist;

    private OkHttpClient client;
    private Request request;

    RecyclerView tasks_rec_view;

    ArrayList<ListModel> items;
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<ListModel> adapterItems;
    BottomNavigationView bottom_nav;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SwipeRefreshLayout swipeRefreshLayout;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String group_code = "group_code";
    public static final String group_id = "group_id";
    public static final String cookie = "cookie";
    public static final String listid = "listid";
    public static final String listname = "listname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //======================etykieta grupy=================================
        group_code_button = findViewById(R.id.group_code_button);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        if(sharedPreferences.getInt(group_id,0)!=0) {
            group_code_button.setText(sharedPreferences.getString(group_code, ""));
        }
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
        //------------------ drop down list----------------------------
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String g_id = "" + sharedPreferences.getInt(group_id,0);
        if(!g_id.equals("0")) {
            getLists(false,g_id);
        }

        items = new ArrayList<>();
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        drop_down_menu_adapter = new Drop_Down_Menu_Adapter(this,R.layout.activity_main_screen,R.id.list_item,items);
        autoCompleteTxt.setAdapter(drop_down_menu_adapter);
        Log.d("ssssprzedhttp",items.toString());


        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedlist = (ListModel) parent.getItemAtPosition(position);
                sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(listname,selectedlist.getName());
                editor.putInt(listid,selectedlist.getId());
                editor.commit();
                getTasks(false);
            }

        });

        //--------------------list----------------------------------------------
        taskList = new ArrayList<>();

        tasks_rec_view = findViewById(R.id.task_rec_view);
        tasks_rec_view.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new Main_Screen_Adapter_Rec(this,taskList,this);
        tasks_rec_view.setAdapter(tasksAdapter);


        //---------------------menu------------------------------------------

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_main_screen);


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
        //========================refresh===================================
        swipeRefreshLayout = findViewById(R.id.swiperefreshmain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                getTasks(false);
                tasksAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    @Override
    public void handleDialogClose(DialogInterface dialog)
    {
        new CountDownTimer(300, 300) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                getTasks(false);
            }

        }.start();

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
        builder.setTitle(R.string.enter_list_name).
                setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addList(false,name.getText().toString());
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
                        deleteList(false);
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
                        deleteAllSelected(false);
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
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Intent intent = new Intent(MainScreen.this,TasksAsignmentScreen.class);
        ToDoModel task = taskList.get(position);
        //intent.putExtra("group_name",sharedPreferences.getString());
        intent.putExtra("group_id",sharedPreferences.getInt(group_id,0));
        intent.putExtra("group_code",sharedPreferences.getString(group_code,""));
        intent.putExtra("task_id",task.getId());
        if(sharedPreferences.getInt(group_id,0)!=0) {
            startActivity(intent);
        }
    }

    @Override
    public void deleteTask(int position) {
        deleteTaskHelper(false,position);
    }

    public void deleteTaskHelper(Boolean repeat, int position)
    {
        ToDoModel item = taskList.get(position);
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
                if(!repeat)deleteTaskHelper(true,position);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("status code delete item",String.valueOf(response.code()));
                Log.d("response body delete item",response.body().string());
                if(response.code()>=200 && response.code()<300)
                {


                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            taskList.remove(item);
                            tasksAdapter.setTasks(taskList);
                        }
                    });

                }
                if(response.code()==404)
                {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainScreen.this,R.string.cannot_delete_task,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void deleteList(Boolean repeat)
    {
        client = CustomHttpBuilder.SSL().build();
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        int listId = sharedPreferences.getInt(listid,0);
        String l_id = "" + listId;

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/lists")
                .addQueryParameter("listId",l_id)
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
                if(!repeat)deleteList(true);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("response code", String.valueOf(response.code()));
                if (response.code() >= 200 && response.code() < 300) {
                    sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(listname,"");
                    editor.putInt(listid,0);
                    editor.commit();

                    finish();
                    startActivity(new Intent(MainScreen.this, MainScreen.class));

                }
                if(response.code()==404)
                {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainScreen.this, R.string.cannot_delete_list_refresh,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public void getTasks(Boolean repeat)
    {
        taskList = new ArrayList<>();

        client = CustomHttpBuilder.SSL().build();
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String l_id = "" + sharedPreferences.getInt(listid,0);
        if(sharedPreferences.getInt(listid,0)!=0) {
            Log.d("list id : ",l_id);
            URL url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("weaweg.mywire.org")
                    .port(8080)
                    .addPathSegments("api/lists/" + l_id +"/tasks")
                    .build().url();

            Log.d("url_tasks", url.toString());


            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("pobieranie zadan","niepowodzenie!!!!!!!!!!!!!!!!!!!!!!!");
                    if(!repeat)getTasks(true);
                    if(repeat)e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.d("statuscode get tasks", String.valueOf(response.code()));
                    //Log.d("responsebody tasks",response.body().string());
                    ///*
                    if (response.code() >= 200 && response.code() < 300) {
                        JSONObject json;

                        try {
                            final JSONArray data = new JSONArray(response.body().string());
                            Log.d("zadania otrzymane z serwera gettasks",data.toString());
                            for (int i = 0; i < data.length(); i++) {
                                json = data.getJSONObject(i);
                                int id = json.getInt("task_id");
                                String name = json.getString("description");
                                boolean status = json.getBoolean("status");
                                String assigment = json.getString("user");
                                if(!assigment.equals("null"))
                                {
                                    JSONObject person = json.getJSONObject("user");
                                    assigment = person.getString("name");
                                }
                                else
                                {
                                    assigment = getResources().getString(R.string.assign);
                                }

                                taskList.add(new ToDoModel(id,status,name,assigment));
                            }
                                MainScreen.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("listy",items.toString());
                                        if(!items.isEmpty()) {
                                            Log.d("zadania do ustawienia na adapter",taskList.toString());
                                            tasksAdapter.setTasks(taskList);
                                            Log.d("zadania po ustawieniu na adapter",taskList.toString());

                                        }
                                        }
                                });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    //*/
                }
            });


        }
    }

   public void getLists(Boolean repeat, String g_id)
   {

       Log.d("group_id przy odpaleniu ",g_id);

       client = CustomHttpBuilder.SSL().build();

       URL url = new HttpUrl.Builder()
               .scheme("https")
               .host("weaweg.mywire.org")
               .port(8080)
               .addPathSegments("api/groups/" + g_id + "/lists")
               .build().url();

       Log.d("url", url.toString());

       sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
       Request request = new Request.Builder()
               .url(url)
               .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
               .get()
               .build();

       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(@NonNull Call call, @NonNull IOException e) {
               Log.d("pobieranie list","niepowodzenie!!!!!!!!!!!!!!!!!!!!!!!");
               if(!repeat)getLists(true,g_id);
               if(repeat)e.printStackTrace();
           }

           @Override
           public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
               Log.d("statuscode", String.valueOf(response.code()));
               if (response.code() >= 200 && response.code() < 300) {
                   //Log.d("lists",response.body().string());
                   JSONObject json;
                   final JSONArray data;
                   try {
                       items = new ArrayList<>();
                       data = new JSONArray(response.body().string());
                       Log.d("listy z serwera ", data.toString());
                       for (int i = 0; i < data.length(); i++) {
                           json = data.getJSONObject(i);
                           int id = json.getInt("list_id");
                           String name = json.getString("name");
                           items.add(new ListModel(id, name));
                       }
                           MainScreen.this.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   drop_down_menu_adapter = new Drop_Down_Menu_Adapter(MainScreen.this, R.layout.activity_main_screen, R.id.list_item, items);
                                   autoCompleteTxt.setAdapter(drop_down_menu_adapter);
                                   if (!sharedPreferences.getString(listname, "").equals("")) {
                                       Log.d("shared prefs lista",sharedPreferences.getString(listname, ""));
                                       Log.d("shared prefs lista id", String.valueOf(sharedPreferences.getInt(listid, 0)));
                                       //Log.d("autotxt", sharedPreferences.getString(listname, ""));
                                       autoCompleteTxt.setText(sharedPreferences.getString(listname, ""), false);
                                       getTasks(false);
                                   }
                                   else {
                                       if (!items.isEmpty()) {
                                           //Log.d("autotxt", items.get(0).getName());
                                           autoCompleteTxt.setText(items.get(0).getName(), false);
                                           editor.putString(listname,items.get(0).getName());
                                           editor.putInt(listid,items.get(0).getId());
                                           editor.commit();
                                           getTasks(false);
                                       }

                                   }


                               }
                           });


                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                    Log.d("no ciekawe","ile razy");

               }
           }
       });

   }


    public void deleteAllSelected(Boolean repeat)
    {
        client = CustomHttpBuilder.SSL().build();
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        int listId = sharedPreferences.getInt(listid,0);
        String l_id = "" + listId;

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/lists/"+l_id+"/doneTasks")
                .build().url();

        Log.d("url delete doneTasks",url.toString());

        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)deleteAllSelected(true);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("response code", String.valueOf(response.code()));
                Log.d("donetasks deleted response body",response.body().string());
                if (response.code() >= 200 && response.code() < 300) {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            taskList.removeIf(ToDoModel::getStatus);
                            tasksAdapter.setTasks(taskList);
                        }
                    });

                }
            }
        });
    }

    public void addList(Boolean repeat, String name)
    {
        client = CustomHttpBuilder.SSL().build();
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        int GroupID = sharedPreferences.getInt(group_id,0);
        String g_id = "" + GroupID;

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/lists")
                .addQueryParameter("groupId",g_id)
                .addQueryParameter("name",name)
                .build().url();

        Log.d("url",url.toString());

        RequestBody body = RequestBody.create("",null);


        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)addList(true,name);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("statuscode add list", String.valueOf(response.code()));
                //Log.d("cialo odpowiedzi",response.body().string());
                if (response.code() >= 200 && response.code() < 300) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        int id = json.getInt("list_id");
                        String name = json.getString("name");
                        items.add(new ListModel(id,name));
                        if(items.size()==1 && sharedPreferences.getInt(listid,0)==0)
                        {
                            MainScreen.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    autoCompleteTxt.setText(items.get(0).getName(), false);
                                    editor.putString(listname,items.get(0).getName());
                                    editor.putInt(listid,items.get(0).getId());
                                    editor.commit();
                                }
                            });
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
                if(response.code() == 400)
                {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainScreen.this, R.string.invalid_list_name,Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                if(response.code() == 404)
                {
                    MainScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainScreen.this, R.string.cannot_add_list_without_group,Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });
    }

    @Override
    public void addNewTask(String text) {


    }
}