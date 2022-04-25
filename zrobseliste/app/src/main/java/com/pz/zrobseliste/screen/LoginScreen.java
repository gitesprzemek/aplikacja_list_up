package com.pz.zrobseliste.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pz.zrobseliste.R;
import com.pz.zrobseliste.models.UserModel;
import com.pz.zrobseliste.utils.CustomHttpBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginScreen extends AppCompatActivity {


    private UserModel user;
    JSONObject jsonObject;
    SharedPreferences.Editor editor;
    private CheckBox checkBox;
    boolean login_in;

    SharedPreferences sharedPreferences;
    private EditText loginField;
    private EditText passwordField;
    private Button loginbutton;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String cookie = "cookie";
    public static final String useremail= "useremail";




    public static final String checkboxstate = "checkboxstate";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        setLocale(sharedPreferences.getString("language",""));
        setContentView(R.layout.activity_login);
        loginField = findViewById(R.id.loginField);
        passwordField = findViewById(R.id.passwordField);
        checkBox = findViewById(R.id.check_box_login);
        //=============================automatic login =============================================

        sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        login_in = sharedPreferences.getBoolean(checkboxstate,false);

        checkBox.setChecked(login_in);

        //===================================checking session==============================================

        automaticlogin();


        //=========================logowanie============================================
        loginbutton = findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnLogClick(v);
            }
        });

    }

    public void onBtnLogClick(View view) {
        String login = loginField.getText().toString();
        this.user = new UserModel(login,passwordField.getText().toString());
        jsonObject = user.loginDatatoJSON();


            //===================================user login==============================================

        OkHttpClient client = CustomHttpBuilder.SSL().build();

            String url = "https://weaweg.mywire.org:8080/api/users/login";

            RequestBody body = RequestBody.create(String.valueOf(jsonObject),JSON);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        LoginScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(response.code()>=200 && response.code()<300) {
                                    Log.d("logininfo",response.headers().toString());
                                    sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString(cookie,response.headers().get("Set-Cookie"));
                                    editor.putString(useremail,login);
                                    editor.commit();

                                    Log.d("loginciasteczkoinfo",sharedPreferences.getString(cookie,""));
                                    Toast.makeText(LoginScreen.this, R.string.login_successful,Toast.LENGTH_SHORT).show();
                                }
                                if(response.code()==418)
                                {
                                    Toast.makeText(LoginScreen.this, R.string.login_failed,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    if(response.code()>=200 && response.code()<300) {
                        startActivity(new Intent(LoginScreen.this, MainScreen.class));
                    }
                    }

                });
        //=====================================user data ===================================================
        /*
        client = new OkHttpClient().newBuilder()
                .build();

        url = "http://weaweg.mywire.org:8080/api/users/self";
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String sesja = sharedPreferences.getString(cookie,"");
        Log.d("sesja",sesja);


        request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String status = "" + response.code();
                Log.d("status polecenia",status);
                if(response.code()>=200 & response.code()<300)
                {
                    Log.d("infookliencie",response.body().string());
                    Log.d("moze tu cos bedzxie", response.headers().toString());
                    Log.d("chuj","chuj");


                }
                else if(response.code()==401)
                {
                    LoginScreen.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginScreen.this, R.string.session_time,Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
            */




    }

    public void onBtnRejClick(View view)
    {
        startActivity(new Intent(LoginScreen.this, RegistrationScreen.class));

    }

    private void setLocale(String lang)
    {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
        getBaseContext().getResources().getDisplayMetrics());
    }

    public void automaticlogin()
    {
        if(login_in) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();

            String url = "https://weaweg.mywire.org:8080/api/users/self";
            sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
            String sesja = sharedPreferences.getString(cookie,"");
            Log.d("sesja",sesja);


            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Cookie",sharedPreferences.getString(cookie,""))
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String status = "" + response.code();
                    Log.d("status polecenia",status);
                    if(response.code()>=200 & response.code()<300)
                    {
                        startActivity(new Intent(LoginScreen.this, MainScreen.class));
                    }
                    else if(response.code()==401)
                    {
                        LoginScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginScreen.this, R.string.session_time,Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }
            });


        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(checkboxstate,true);
                    editor.commit();
                }
                else
                {
                    sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(checkboxstate,false);
                    editor.commit();
                }
            }
        });
    }


}
