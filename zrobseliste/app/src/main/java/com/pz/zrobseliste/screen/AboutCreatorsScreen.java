package com.pz.zrobseliste.screen;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pz.zrobseliste.R;
import com.pz.zrobseliste.adapter.Menu_Screen_Adapter_Rec;
import com.pz.zrobseliste.models.MenuModel;

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

public class AboutCreatorsScreen extends AppCompatActivity{


    ImageButton image_back_button_creators;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_creators_screen);

        Toolbar toolbar = findViewById(R.id.toolbar_creators);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.about_creators);

    }

}