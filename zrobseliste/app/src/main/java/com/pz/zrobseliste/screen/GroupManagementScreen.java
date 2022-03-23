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
import com.pz.zrobseliste.interfaces.GroupManagementInterface;
import com.pz.zrobseliste.models.GroupUserModel;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

//import android.support.v4.app.Fragment;

public class GroupManagementScreen extends AppCompatActivity implements GroupManagementInterface {

    RecyclerView recylerView;
    Group_Management_Adapter recyclerAdapter;
    private ArrayList<GroupUserModel> users;
    private EditText member;
    private ImageButton addMember;

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
    }


    @Override
    public void onDeleteUserClick(int position) {
        users.remove(position);
        recyclerAdapter.setusers(users);
        recylerView.setAdapter(recyclerAdapter);
    }
}
