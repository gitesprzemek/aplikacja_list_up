package test.organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    }

    public void onBtnRejClick(View view)
    {
        startActivity(new Intent(Login.this, Registration.class));

    }

}
