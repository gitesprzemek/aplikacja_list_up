package com.pz.zrobseliste;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.pz.zrobseliste.screen.LoginScreen;

//https://77.91.17.32:3232/user

public class MainActivity extends AppCompatActivity {

    private static int WELCOME_SCREEN_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, LoginScreen.class);
                startActivity(homeIntent);
                finish();
            }
        }, WELCOME_SCREEN_TIME);
    }



//    public void onWelcomeTestBtnClick(View view) {
//        startActivity(new Intent(MainActivity.this, LoginScreen.class));
//        setContentView(R.layout.activity_login);
//    }
}