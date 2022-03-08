package com.pz.zrobseliste.screen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Groups_Screen_Adapter;
import com.pz.zrobseliste.interfaces.Groups_onClick_Interface;
import com.pz.zrobseliste.models.GroupModel;
import com.pz.zrobseliste.utils.SwipeListener;

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

public class MenuScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{



    BottomNavigationView bottom_nav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        //===========================bottommenu================================
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_groups);

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

}