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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.user = new UserModel();
    }




    public void onBtnLogClick(View view) {
        TextView infozwrotne = findViewById(R.id.infozwrotne);
        EditText polelogowania = findViewById(R.id.loginField);
        EditText polehasla = findViewById(R.id.polehasla);
        if(polelogowania.getText().toString().equals("przemek") && polehasla.getText().toString().equals("admin123") )
        {
            infozwrotne.setText("Logowanie udane");
        }
        else
        {
            infozwrotne.setText("Logowanie nieudane");
        }

        user.setEmail(polelogowania.getText().toString());
        user.setPassword(polehasla.getText().toString());
        System.out.println(user.toJSON().toString());

    }

    public void onBtnRejClick(View view)
    {
        startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));

    }
}
