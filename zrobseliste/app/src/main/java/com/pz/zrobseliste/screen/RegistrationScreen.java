package com.pz.zrobseliste.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.UserModel;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationScreen extends AppCompatActivity {

    private UserModel user;
    JSONObject jsonObject;
    private Button registerButton;
    private EditText emailField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText passwordFieldRepeat;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailField = findViewById(R.id.emailField);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        passwordFieldRepeat = findViewById(R.id.passwordFieldRepeat);
        //=================rejestracja=========================================
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnZarClick(v);
            }
        });

    }
    public void onBtnZarClick(View view)
    {
        if(passwordField.getText().toString().equals(passwordFieldRepeat.getText().toString())) {
            this.user = new UserModel(emailField.getText().toString(), usernameField.getText().toString(), passwordField.getText().toString());
            jsonObject = user.registrationDatatoJSON();
            registration(false,jsonObject);
        }
        else
        {
            Toast.makeText(RegistrationScreen.this, R.string.sprawdzanie_hasel,Toast.LENGTH_SHORT).show();
        }

    }

    public void registration(Boolean repeat,JSONObject jsonObject)
    {


            //==================================connection===========================================

            OkHttpClient client = CustomHttpBuilder.SSL().build();

            String url = "https://weaweg.mywire.org:8080/api/users/register";

            RequestBody body = RequestBody.create(String.valueOf(jsonObject),JSON);

            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if(!repeat)registration(false,jsonObject);
                    if(repeat)e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    RegistrationScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(response.code()>=200 && response.code()<=300)
                            {
                                Toast.makeText(RegistrationScreen.this, R.string.registration_successful,Toast.LENGTH_SHORT).show();
                                finish();
                                //informationView.setText(response.headers().toString());
                            }// 403 status jesli istnieje ktos o takim mailu
                            //400 bad request przy problemie z wprowadzanymi danymi
                            else if(response.code()==403)
                            {
                                Toast.makeText(RegistrationScreen.this, R.string.mail_already_existed,Toast.LENGTH_SHORT).show();
                                //informationView.setText("nie udalo sie zarejestrowac");
                            }
                            else if(response.code()==400)
                            {
                                Toast.makeText(RegistrationScreen.this, R.string.wrong_data,Toast.LENGTH_SHORT).show();
                                //informationView.setText("nie udalo sie zarejestrowac");
                            }
                            else
                            {
                                Toast.makeText(RegistrationScreen.this, R.string.registration_failed,Toast.LENGTH_SHORT).show();
                            }

                        }

                    });
                }
            });
        }


    public void onBtnRetClick(View view)
    {
        finish();
    }
}
