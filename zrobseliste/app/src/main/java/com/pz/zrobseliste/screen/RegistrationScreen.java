package com.pz.zrobseliste.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.UserModel;

import org.json.JSONObject;

public class RegistrationScreen extends AppCompatActivity {

    private UserModel user;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


    }
    public void onBtnZarClick(View view)
    {
        TextView informationView = findViewById(R.id.informationView);
        EditText emailField = findViewById(R.id.emailField);
        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);
        EditText passwordFieldRepeat = findViewById(R.id.passwordFieldRepeat);

        if(passwordField.getText().toString().equals(passwordFieldRepeat.getText().toString()))
        {
            this.user = new UserModel(emailField.getText().toString(),usernameField.getText().toString(),passwordField.getText().toString());
            jsonObject = user.registrationDatatoJSON();

            HttpClient client
            informationView.setText("");
            finish();
        }
        else
        {
            informationView.setText("Podane hasla roznia sie");
        }


    }

    public void onBtnRetClick(View view)
    {
        finish();
    }
}
