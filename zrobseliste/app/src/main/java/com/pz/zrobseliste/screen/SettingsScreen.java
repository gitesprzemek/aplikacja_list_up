package com.pz.zrobseliste.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pz.zrobseliste.R;

import java.util.Locale;

//import android.support.v4.app.Fragment;

public class SettingsScreen extends AppCompatActivity{

    ImageButton button;
    RadioGroup radioGroup;
    RadioButton radioButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String language = "language";
    public static final String id = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);
        //====================toolbar===================================
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings);
        //===========================language==================================
        radioGroup = findViewById(R.id.radio_group);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        radioGroup.check(sharedPreferences.getInt(id,R.id.radio_button_polish));
        //======================radio===========================

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_button_polish:
                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString(language,"");
                        editor.putInt(id, R.id.radio_button_polish);
                        editor.commit();
                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        break;
                    case R.id.radio_button_english:
                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString(language,"en");
                        editor.putInt(id, R.id.radio_button_english);
                        editor.commit();
                        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        break;
                }
                setLocale(sharedPreferences.getString(language,""));
            }
        });
    }

    private void setLocale(String lang)
    {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.finish();
        Intent refresh = new Intent(this, SettingsScreen.class);
        startActivity(refresh);
    }

}