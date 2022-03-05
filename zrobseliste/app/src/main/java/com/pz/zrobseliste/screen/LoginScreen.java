package com.pz.zrobseliste.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.UserModel;

public class LoginScreen extends AppCompatActivity {

    private UserModel user;
    TextView informationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        this.user = new UserModel();
    }

    public void onBtnLogClick(View view) {
        informationView = findViewById(R.id.informationView);
        EditText loginField = findViewById(R.id.loginField);
        EditText passwordField = findViewById(R.id.passwordField);
        if(loginField.getText().toString().equals("") && passwordField.getText().toString().equals("") )
        {
            informationView.setText("Logowanie udane");
            startActivity(new Intent(LoginScreen.this, MainScreen.class));
            informationView.setText("");
        }
        else
        {
            informationView.setText("Logowanie nieudane");
        }

        user.setEmail(loginField.getText().toString());
        user.setPassword(passwordField.getText().toString());
        System.out.println(user.loginDatatoJSON().toString());

    }

    public void onBtnRejClick(View view)
    {

        startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));

    }
}
