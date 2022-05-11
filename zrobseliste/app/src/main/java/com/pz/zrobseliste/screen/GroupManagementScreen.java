package com.pz.zrobseliste.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Group_Management_Adapter;
import com.pz.zrobseliste.interfaces.GroupManagementInterface;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.models.GroupUserModel;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import android.support.v4.app.Fragment;

public class GroupManagementScreen extends AppCompatActivity implements GroupManagementInterface {

    RecyclerView recylerView;
    Group_Management_Adapter recyclerAdapter;
    private ArrayList<GroupUserModel> users;
    private EditText member;
    private ImageButton addMember;
    private ImageButton deleteGroup;
    private Request request;
    private URL url;
    private RequestBody body;


    private OkHttpClient client;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String group_code = "group_code";
    public static final String cookie = "cookie";
    public static final String group_id = "group_id";



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);
        //===========================recycler_view_users========================

        users = new ArrayList<>();
        //users.add(new GroupUserModel("Uzytkownik pierwszy"));
        //users.add(new GroupUserModel("Uzytkownik drugi"));
        //users.add(new GroupUserModel("Uzytkownik trzeci"));
        //users.add(new GroupUserModel("Uzytkownik czwarty"));

        //=================================end getting users===============================
        recylerView = findViewById(R.id.rec_view_group_management);
        recyclerAdapter = new Group_Management_Adapter(this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setusers(users);
        //=================================getting users==================================
        getUsers(false);

        //=========================add_member==============================
        member = findViewById(R.id.edit_text_new_member);
        addMember = findViewById(R.id.button_add_member);

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = member.getText().toString();
                //=========================connection==================================
                adduser(false,email);
            }
        });


        //======================tolbar========================================
        Toolbar toolbar = findViewById(R.id.toolbar_management);
        setSupportActionBar(toolbar);
        String name = getIntent().getStringExtra("group_name");
        getSupportActionBar().setTitle(name);
        //===========================delete group=============================
        deleteGroup = findViewById(R.id.button_delete_group);
        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialogDelete();
            }
        });
    }

    private void buildDialogDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.do_you_want_delete_group).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGroup(false);

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
    public void onDeleteUserClick(int position) {
        deleteUser(false,position);


    }

    public void deleteUser(Boolean repeat, int position)
    {
        GroupUserModel user = users.get(position);
        Log.d("email",user.getEmail());

        String email = user.getEmail();
        int groupid = getIntent().getIntExtra("group_id", 0);

        client = CustomHttpBuilder.SSL().build();

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/groups/" + groupid)
                .addQueryParameter("email", email)
                .build().url();

        Log.d("url", url.toString());

        //body = RequestBody.create("", null);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)deleteUser(true,position);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("statuscode",String.valueOf(response.code()));
                Log.d("responsebody",response.body().string());

                if(response.code()>=200 && response.code()<300)
                {
                    GroupManagementScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            users.remove(position);
                            recyclerAdapter.setusers(users);
                        }
                    });

                }

                if(response.code()==409)
                {
                    GroupManagementScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupManagementScreen.this, R.string.delete_user_failed,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public void getUsers(Boolean repeat)
    {
        int groupid = getIntent().getIntExtra("group_id", 0);
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
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                //Log.d("responebody",response.body().string());
                Log.d("statuscode", String.valueOf(response.code()));

                if (response.code() >= 200 && response.code() < 300) {

                    try {
                        JSONObject json;
                        final JSONArray data = new JSONArray(response.body().string());
                        for (int i = 0; i < data.length(); i++) {
                            json = data.getJSONObject(i);
                            int id = json.getInt("user_id");
                            String name = json.getString("name");
                            String email = json.getString("email");
                            users.add(new GroupUserModel(id, name,email));
                        }
                        GroupManagementScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
    public void adduser(Boolean repeat, String email)
    {
        int groupid = getIntent().getIntExtra("group_id", 0);
        client = CustomHttpBuilder.SSL().build();

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/groups/" + groupid)
                .addQueryParameter("email", email)
                .build().url();

        Log.d("url", url.toString());


        //String urld = "https://weaweg.mywire.org:8080/api/groups/"+groupid+"?email="+email;
        // Log.d("email",urld);
        body = RequestBody.create("", null);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)adduser(true,email);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("statuscode", String.valueOf(response.code()));
                //Log.d("body",response.body().string());

                if (response.code() >= 200 && response.code() < 300) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response.body().string());
                        int id = json.getInt("user_id");
                        String name = json.getString("name");
                        String email = json.getString("email");
                        users.add(new GroupUserModel(id, name,email));
                        GroupManagementScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerAdapter.setusers(users);
                                member.setText("");
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if (response.code() == 400) {
                    GroupManagementScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupManagementScreen.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void deleteGroup(Boolean repeat)
    {
        int groupid = getIntent().getIntExtra("group_id", 0);
        String GroupID = String.valueOf(groupid);
        client = CustomHttpBuilder.SSL().build();

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/groups")
                .addQueryParameter("groupId",GroupID)
                .build().url();

        Log.d("url", url.toString());

        //body = RequestBody.create("", null);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)deleteGroup(true);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("statuscode",String.valueOf(response.code()));
                GroupManagementScreen.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.code()>=200 && response.code()<300)
                        {
                            sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                            if(groupid==sharedPreferences.getInt(group_id,0))
                            {
                                editor = sharedPreferences.edit();
                                editor.putString(group_code, "");
                                editor.putInt(group_id,0);
                                editor.commit();

                            }

                            finish();
                            finish();
                            startActivity(new Intent(GroupManagementScreen.this, GroupsScreen.class));

                        }

                        if(response.code()==403)
                        {
                            Toast.makeText(GroupManagementScreen.this, R.string.delete_group_failed,Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
    }

}
