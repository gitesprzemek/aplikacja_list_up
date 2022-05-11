package com.pz.zrobseliste.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Group_Management_Adapter;
import com.pz.zrobseliste.adapter.Tasks_Assignment_Adapter;
import com.pz.zrobseliste.interfaces.GroupManagementInterface;
import com.pz.zrobseliste.interfaces.TaskAssignmentInterface;
import com.pz.zrobseliste.models.GroupUserModel;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import android.support.v4.app.Fragment;

public class TasksAsignmentScreen extends AppCompatActivity implements TaskAssignmentInterface {

    RecyclerView recylerView;
    Tasks_Assignment_Adapter recyclerAdapter;
    private ArrayList<GroupUserModel> users;
    private Request request;
    private URL url;
    private RequestBody body;
    private OkHttpClient client;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String cookie = "cookie";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_assignment);


        //=======================recycler_view_users==========================
        users = new ArrayList<>();
        recylerView = findViewById(R.id.rec_view_assignment_management);
        recyclerAdapter = new Tasks_Assignment_Adapter(this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setusers(users);

        //===========================get groups========================
        getUsers(false);

        //======================tolbar========================================
        Toolbar toolbar = findViewById(R.id.toolbar_assignment);
        setSupportActionBar(toolbar);

    }


    @Override
    public void onAssignmentCardClick(int position) {

        assignUser(false,position);

    }

    public void assignUser(Boolean repeat,int position)
    {
        GroupUserModel user = users.get(position);
        client = CustomHttpBuilder.SSL().build();
        String task_id = "" + getIntent().getIntExtra("task_id",0);
        String user_id = "" + user.getId();

        url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/tasks/" + task_id)
                .addQueryParameter("userId",user_id)
                .build().url();

        Log.d("user assigned url", String.valueOf(url));

        body = RequestBody.create("", null);

        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("odpowiedz z serwera","nie udalo sie polaczyc z serwerem");
                if(!repeat)assignUser(true,position);
                if(repeat)e.printStackTrace();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("response code assign user",String.valueOf(response.code()));
                if (response.code() >= 200 && response.code() < 300) {
                    Log.d("response body assing user",response.body().string());
                    TasksAsignmentScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            finish();
                            startActivity(new Intent(TasksAsignmentScreen.this,MainScreen.class));
                        }
                    });
                }
            }
        });

    }

    public void getUsers(Boolean repeat)
    {
        int groupid = getIntent().getIntExtra("group_id",0);
        client = CustomHttpBuilder.SSL().build();

        url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/groups/" + groupid + "/users")
                .build().url();

        Log.d("group_id", String.valueOf(groupid));

        Log.d("url", url.toString());

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)getUsers(true);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                //Log.d("responebody",response.body().string());
                Log.d("statuscode", String.valueOf(response.code()));

                if (response.code() >= 200 && response.code() < 300) {

                    try {
                        users = new ArrayList<>();
                        JSONObject json;
                        final JSONArray data = new JSONArray(response.body().string());
                        for (int i = 0; i < data.length(); i++) {
                            json = data.getJSONObject(i);
                            int id = json.getInt("user_id");
                            String name = json.getString("name");

                            users.add(new GroupUserModel(id, name));
                            Log.d("person", name + " " + id );
                        }
                        TasksAsignmentScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("lista userow", users.toString());
                                recyclerAdapter.setusers(users);
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
