package com.pz.zrobseliste.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Groups_Screen_Adapter_Rec;
import com.pz.zrobseliste.interfaces.GroupsonClickInterface;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import android.support.v4.app.Fragment;

public class GroupsScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, GroupsonClickInterface{

    private static final String TAG = "GroupsScreen";
    private static final int NUM_COLUMNS=2;
    private ArrayList<GroupModel> groups;
    private RecyclerView recyclerView;
    private Groups_Screen_Adapter_Rec groups_screen_adapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    JSONObject jsonObject;

    BottomNavigationView bottom_nav;
    Button add_group_button;
    private OkHttpClient client;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String group_code = "group_code";
    public static final String group_id = "group_id";
    public static final String cookie = "cookie";
    public static final String useremail= "useremail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_screen);

        //======================grupy http================================

        client = CustomHttpBuilder.SSL().build();

        URL url = new HttpUrl.Builder()
                .scheme("https")
                .host("weaweg.mywire.org")
                .port(8080)
                .addPathSegments("api/users/groups")
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

                //Log.d("responebody",response.body().string());
                Log.d("statuscode", String.valueOf(response.code()));
                if (response.code() >= 200 && response.code() < 300) {

                    try {
                        JSONObject json;
                        final JSONArray data = new JSONArray(response.body().string());
                        for (int i = 0; i < data.length(); i++) {
                            json = data.getJSONObject(i);
                            int id = json.getInt("group_id");
                            String name = json.getString("name");
                            String group_id = returngroupcode(json.getString("name"));
                            groups.add(new GroupModel(id, name, group_id));

                        }
                        GroupsScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                groups_screen_adapter.setGroups(groups);
                            }
                        });
                        Log.d("grupypoodpowiedzi",groups.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        //==========================grupy adapter===============================================
        groups = new ArrayList<>();
        Log.d("grupy",groups.toString());
        recyclerView = findViewById(R.id.groups_rec_view);
        groups_screen_adapter = new Groups_Screen_Adapter_Rec(this,groups,this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(groups_screen_adapter);

        add_group_button = findViewById(R.id.add_group_button);
        add_group_button.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                buildDialog();
           }
       });

        //===========================bottommenu================================
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_groups);


    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_new_group_dialog,null);

        EditText name = view.findViewById(R.id.edit_text_group_name);

        builder.setView(view);
        builder.setTitle(R.string.enter_group_name).
                setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        client = CustomHttpBuilder.SSL().build();

                        URL url = new HttpUrl.Builder()
                                .scheme("https")
                                .host("weaweg.mywire.org")
                                .port(8080)
                                .addPathSegments("api/groups")
                                .addQueryParameter("name", name.getText().toString())
                                .build().url();

                        Log.d("url",url.toString());

                        RequestBody body = RequestBody.create("",null);

                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        Request request = new Request.Builder()
                                .url(url)
                                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                                .put(body)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Log.d("statuscode",String.valueOf(response.code()));
                                if(response.code()>=200 && response.code()<300)
                                {
                                    JSONObject json = null;
                                    try {
                                        json = new JSONObject(response.body().string());
                                        int id = json.getInt("group_id");
                                        String name = json.getString("name");
                                        String group_code = returngroupcode(json.getString("name"));
                                        groups.add(new GroupModel(id, name, group_code));
                                        GroupsScreen.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                groups_screen_adapter.setGroups(groups);
                                            }
                                        });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                if(response.code()==400)
                                {
                                    GroupsScreen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GroupsScreen.this, R.string.creating_group_failed,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                    }

                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();

    }

    private String returngroupcode(String g_code) {
        String new_g_code="";
        String []arr = g_code.split(" ");
        if(arr.length==1){
            String []array = arr[0].split("");
            new_g_code=new_g_code + array[0];
            new_g_code=new_g_code + array[1];
            new_g_code=new_g_code + array[2];
            System.out.println(Arrays.toString(array));
        }
        if(arr.length==2){
            String []array = arr[0].split("");
            new_g_code=new_g_code + array[0];
            new_g_code=new_g_code + array[1];
            array = arr[1].split("");
            new_g_code=new_g_code + array[0];
        }
        if(arr.length>2){
            String []array = arr[0].split("");
            new_g_code=new_g_code + array[0];
            array = arr[1].split("");
            new_g_code=new_g_code + array[0];
            array = arr[2].split("");
            new_g_code=new_g_code + array[0];
        }

        return new_g_code;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_tasks:
                finish();
                startActivity(new Intent(GroupsScreen.this, AllTasksScreen.class));
                break;
            case R.id.nav_main_screen:
                finish();
                startActivity(new Intent(GroupsScreen.this, MainScreen.class));
                break;
            case R.id.nav_groups:
                break;
            case R.id.nav_menu:
                finish();
                startActivity(new Intent(GroupsScreen.this, MenuScreen.class));
                break;

        }
        return true;
    }

    private void buildDialogDelete(GroupModel group) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.do_you_want_leave_group).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        String email = sharedPreferences.getString(useremail,"");
                        int id = group.getGroupID();
                        client = CustomHttpBuilder.SSL().build();

                        URL url = new HttpUrl.Builder()
                                .scheme("https")
                                .host("weaweg.mywire.org")
                                .port(8080)
                                .addPathSegments("api/groups/" + id)
                                .addQueryParameter("email",email)
                                .build().url();

                        Log.d("url", url.toString());

                        //body = RequestBody.create("", null);

                        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        Request request = new Request.Builder()
                                .url(url)
                                .addHeader("Cookie", sharedPreferences.getString(cookie, ""))
                                .delete()
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Log.d("statuscode",String.valueOf(response.code()));

                                if(response.code()>=200 && response.code()<300)
                                {
                                    GroupsScreen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            groups.remove(group);
                                            groups_screen_adapter.setGroups(groups);
                                        }
                                    });

                                }
                                if(response.code()==409)
                                {
                                    GroupsScreen.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GroupsScreen.this, R.string.owner_leave_group_failed,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });


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
    public void onGroupButtonClick(int position) {
        GroupModel group = groups.get(position);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(group_code,group.getGroup_code());
        editor.putInt(group_id,group.getGroupID());
        editor.commit();
        Intent intent = new Intent(GroupsScreen.this,MainScreen.class);

        finish();
        startActivity(intent);
    }

    @Override
    public void onManagmentButtonClick(int position, View v) {
        GroupModel group = groups.get(position);
        PopupMenu popup = new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.manage:
                        Intent intent = new Intent(GroupsScreen.this,GroupManagementScreen.class);
                        intent.putExtra("group_name",group.getName());
                        intent.putExtra("group_id",group.getGroupID());
                        Log.d("group_id", String.valueOf(group.getGroupID()));
                        startActivity(intent);
                        break;
                    case R.id.leave_group:
                        buildDialogDelete(group);
                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        if(group.getGroup_code()==sharedPreferences.getString(group_code,"")&& group.getGroupID()==sharedPreferences.getInt(group_id,0)) {
                            editor = sharedPreferences.edit();
                            editor.putString(group_code, "");
                            editor.putInt(group_id,0);
                            editor.commit();
                        }
                        break;
                }

                return true;
            }
        });
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }



}