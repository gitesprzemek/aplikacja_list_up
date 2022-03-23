package com.pz.zrobseliste.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Menu_Screen_Adapter_Rec;
import com.pz.zrobseliste.interfaces.MenuHandlerInterface;
import com.pz.zrobseliste.models.MenuModel;

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

public class MenuScreen extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MenuHandlerInterface {

    RecyclerView recylerView;
    Menu_Screen_Adapter_Rec recyclerAdapter;
    private ArrayList<MenuModel> options = new ArrayList<>();
    BottomNavigationView bottom_nav;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);
        //===========================options====================================
        options.add(new MenuModel(R.drawable.ic_action_user,1,getResources().getString(R.string.about_creators)));
        options.add(new MenuModel(R.drawable.settings_icon,2,getResources().getString(R.string.settings)));
        options.add(new MenuModel(R.drawable.logout_icon,3,getResources().getString(R.string.logout)));


        recylerView = findViewById(R.id.rec_view_menu);
        recyclerAdapter = new Menu_Screen_Adapter_Rec(options,this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(recyclerAdapter);

        //===========================bottommenu================================
        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(this);
        bottom_nav.setSelectedItemId(R.id.nav_menu);

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
            Toast.makeText(this,temp.getText(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MenuScreen.this, AboutCreatorsScreen.class));
        }
        if(temp.getId()==2)
        {
            Toast.makeText(this,temp.getText(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MenuScreen.this, SettingsScreen.class));
        }
        if(temp.getId()==3)
        {
            Intent intent = new Intent(MenuScreen.this,LoginScreen.class);
            finish();
            finish();
            startActivity(intent);
        }

    }
}