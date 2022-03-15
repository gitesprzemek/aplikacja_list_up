package com.pz.zrobseliste.screen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
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

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

public class GroupsScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, GroupsonClickInterface {

    private static final String TAG = "GroupsScreen";
    private static final int NUM_COLUMNS=2;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> group_codes = new ArrayList<>();

    private TableLayout table;
    private MainScreen mainScreen;
    private ArrayList<GroupModel> groupModels;
    BottomNavigationView bottom_nav;
    Button add_group_button;
    RelativeLayout layout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_screen);

        //======================grupy============================================
        group_codes.add("BD1");
        group_codes.add("Dom");
        group_codes.add("Imp");
        names.add("Bazy danych 1");
        names.add("Domownicy");
        names.add("Impreza");
        RecyclerView recyclerView = findViewById(R.id.groups_rec_view);
        Groups_Screen_Adapter_Rec groups_screen_adapter = new Groups_Screen_Adapter_Rec(this,names,group_codes,this);
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
        builder.setTitle("Podaj nazwe grupy").
                setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            group_codes.add(returngroupcode(name.getText().toString()));
                            names.add(name.getText().toString());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
            new_g_code=new_g_code + array[1];
            new_g_code=new_g_code + array[2];
            new_g_code=new_g_code + array[3];
        }
        if(arr.length==2){
            String []array = arr[0].split("");
            new_g_code=new_g_code + array[1];
            new_g_code=new_g_code + array[2];
            array = arr[1].split("");
            new_g_code=new_g_code + array[1];
        }
        if(arr.length>2){
            String []array = arr[0].split("");
            new_g_code=new_g_code + array[1];
            array = arr[1].split("");
            new_g_code=new_g_code + array[1];
            array = arr[2].split("");
            new_g_code=new_g_code + array[1];
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


    @Override
    public void onGroupButtonClick(int position) {
        Toast.makeText(this,names.get(position),Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(GroupsScreen.this, MainScreen.class));


    }

    @Override
    public void onManagmentButtonClick(int position) {

    }
}