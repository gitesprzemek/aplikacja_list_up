package com.pz.zrobseliste.screen;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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
import okhttp3.Response;

//import android.support.v4.app.Fragment;

public class GroupManagementScreen extends AppCompatActivity implements GroupManagementInterface {

    RecyclerView recylerView;
    Group_Management_Adapter recyclerAdapter;
    private ArrayList<GroupUserModel> users;
    private EditText member;
    private ImageButton addMember;
    private ImageButton deleteGroup;

    private OkHttpClient client;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String group_code = "group_code";
    public static final String cookie = "cookie";



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);
        //===========================recycler_view_users========================

        users = new ArrayList<>();
        users.add(new GroupUserModel("Uzytkownik pierwszy"));
        users.add(new GroupUserModel("Uzytkownik drugi"));
        users.add(new GroupUserModel("Uzytkownik trzeci"));
        users.add(new GroupUserModel("Uzytkownik czwarty"));
        //=================================getting users==================================
        String groupid = getIntent().getStringExtra("group_id");
        client = new OkHttpClient().newBuilder()
                .build();
        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/groups/"+groupid+"users")
                .build().url();

        Log.d("url",url.toString());

        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                Log.d("responebody",response.body().string());
                Log.d("statuscode", String.valueOf(response.code()));
                /*
                if (response.code() >= 200 && response.code() < 300) {

                    try {
                        JSONObject json;
                        final JSONArray data = new JSONArray(response.body().string());
                        for (int i = 0; i < data.length(); i++) {
                            json = data.getJSONObject(i);
                            int id = json.getInt("group_id");
                            String name = json.getString("name");


                        }
                        GroupManagementScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }*/
            }
        });

        //=================================end getting users===============================
        recylerView = findViewById(R.id.rec_view_group_management);
        recyclerAdapter = new Group_Management_Adapter(this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setusers(users);
        //=========================add_member==============================
        member = findViewById(R.id.edit_text_new_member);
        addMember = findViewById(R.id.button_add_member);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwa = member.getText().toString();
                if(nazwa.isEmpty())
                {

                }
                else
                {
                    users.add(new GroupUserModel(nazwa));
                    recyclerAdapter.setusers(users);
                    recylerView.setAdapter(recyclerAdapter);
                }
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
        users.remove(position);
        recyclerAdapter.setusers(users);
        recylerView.setAdapter(recyclerAdapter);
    }

}
