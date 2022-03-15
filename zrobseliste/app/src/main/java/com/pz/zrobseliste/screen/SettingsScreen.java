package com.pz.zrobseliste.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.pz.zrobseliste.R;

//import android.support.v4.app.Fragment;

public class SettingsScreen extends AppCompatActivity{

    ImageButton button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);


        button = findViewById(R.id.image_back_button_settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}