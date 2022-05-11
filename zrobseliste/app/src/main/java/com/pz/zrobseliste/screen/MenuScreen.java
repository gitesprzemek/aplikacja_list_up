package com.pz.zrobseliste.screen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Menu_Screen_Adapter_Rec;
import com.pz.zrobseliste.interfaces.MenuHandlerInterface;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.models.MenuModel;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import android.support.v4.app.Fragment;

public class MenuScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MenuHandlerInterface {

    JSONObject jsonObject;
    RecyclerView recylerView;
    Menu_Screen_Adapter_Rec recyclerAdapter;
    private ArrayList<MenuModel> options = new ArrayList<>();
    BottomNavigationView bottom_nav;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView text_view_user;

    public static final String cookie = "cookie";
    public static final String checkboxstate = "checkboxstate";
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String useremail= "useremail";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        //===========================options====================================
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text_view_user = findViewById(R.id.text_view_user);
        text_view_user.setText(sharedPreferences.getString(useremail,""));
        text_view_user.setTextColor(this.getResources().getColor(R.color.teal_200));
        //==========================================================================
        options.add(new MenuModel(R.drawable.ic_action_user,1,getResources().getString(R.string.about_creators)));
        options.add(new MenuModel(R.drawable.settings_icon,2,getResources().getString(R.string.settings)));
        options.add(new MenuModel(R.drawable.icon_delete_account,3,getResources().getString(R.string.delete_account)));
        options.add(new MenuModel(R.drawable.logout_icon,4,getResources().getString(R.string.logout)));


        recylerView = findViewById(R.id.rec_view_menu);
        recyclerAdapter = new Menu_Screen_Adapter_Rec(options,this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(recyclerAdapter);

        //===========================bottommenu================================
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_menu);

    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_account_text).
                setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount(false);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_tasks:
                finish();
                startActivity(new Intent(MenuScreen.this, AllTasksScreen.class));
                break;
            case R.id.nav_main_screen:
                finish();
                startActivity(new Intent(MenuScreen.this, MainScreen.class));
                break;
            case R.id.nav_groups:
                finish();
                startActivity(new Intent(MenuScreen.this, GroupsScreen.class));
                break;
            case R.id.nav_menu:
                break;

        }
        return true;
    }


    @Override
    public void onMainMenuItemClick(int position) {
        MenuModel temp;
        temp = options.get(position);
        if(temp.getId()== 1)
        {
            startActivity(new Intent(MenuScreen.this, AboutCreatorsScreen.class));
        }
        if(temp.getId()==2)
        {
            startActivity(new Intent(MenuScreen.this, SettingsScreen.class));
        }
        if(temp.getId()==3)
        {
            buildDialog();
        }
        if(temp.getId()==4)
        {
            logout(false);

        }

    }

    public void deleteAccount(Boolean repeat)
    {

        OkHttpClient client = CustomHttpBuilder.SSL().build();

        String url = "https://weaweg.mywire.org:8080/api/users/self";
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String sesja = sharedPreferences.getString(cookie,"");
        Log.d("sesja",sesja);


        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)deleteAccount(true);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("statuscode", String.valueOf(response.code()));
                if(response.code()>=200 & response.code()<300)
                {

                    Log.d("responselog",response.headers().toString());
                    sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(cookie,"");
                    editor.putBoolean(checkboxstate,false);
                    editor.commit();

                    Intent intent = new Intent(MenuScreen.this,LoginScreen.class);
                    finish();
                    finish();
                    startActivity(intent);
                }
                else if(response.code()==401)
                {
                    MenuScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MenuScreen.this, R.string.delete_account_info_failed,Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
    }

    public void logout(Boolean repeat)
    {
        OkHttpClient client = CustomHttpBuilder.SSL().build();
        jsonObject = new JSONObject();
        try {
            jsonObject.put("zadanie","wyloguj");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://weaweg.mywire.org:8080/api/users/logout";
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String sesja = sharedPreferences.getString(cookie,"");
        Log.d("sesja",sesja);

        RequestBody body = RequestBody.create(String.valueOf(jsonObject),JSON);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if(!repeat)logout(true);
                if(repeat)e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("statuscode", String.valueOf(response.code()));
                Log.d("responselog",response.headers().toString());
                if(response.code()>=200 && response.code()<300) {
                    sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString(cookie,"");
                    //editor.putString(useremail,"");
                    editor.putBoolean(checkboxstate,false);
                    editor.commit();

                    Intent intent = new Intent(MenuScreen.this,LoginScreen.class);
                    finish();
                    finish();
                    startActivity(intent);
                }

            }
        });
    }
}