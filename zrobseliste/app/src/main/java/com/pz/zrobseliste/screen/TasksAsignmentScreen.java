package com.pz.zrobseliste.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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

import java.util.ArrayList;

//import android.support.v4.app.Fragment;

public class TasksAsignmentScreen extends AppCompatActivity implements TaskAssignmentInterface {

    RecyclerView recylerView;
    Tasks_Assignment_Adapter recyclerAdapter;
    private ArrayList<GroupUserModel> users;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_assignment);
        //===========================recycler_view_users========================

        users = new ArrayList<>();
        users.add(new GroupUserModel("Uzytkownik pierwszy"));
        users.add(new GroupUserModel("Uzytkownik drugi"));
        users.add(new GroupUserModel("Uzytkownik trzeci"));
        users.add(new GroupUserModel("Uzytkownik czwarty"));

        recylerView = findViewById(R.id.rec_view_assignment_management);
        recyclerAdapter = new Tasks_Assignment_Adapter(this);
        recylerView.setLayoutManager(new LinearLayoutManager(this));
        recylerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setusers(users);

        //======================tolbar========================================
        Toolbar toolbar = findViewById(R.id.toolbar_assignment);
        setSupportActionBar(toolbar);

    }


    @Override
    public void onAssignmentCardClick(int position) {
        finish();
    }
}
