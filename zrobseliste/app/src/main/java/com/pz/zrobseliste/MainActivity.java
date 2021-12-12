package com.pz.zrobseliste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pz.zrobseliste.screen.LoginScreen;

public class MainActivity extends AppCompatActivity {

    private static int WELCOME_SCREEN_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onWelcomeTestBtnClick(View view) {
        startActivity(new Intent(MainActivity.this, LoginScreen.class));
        setContentView(R.layout.activity_login);
    }
}