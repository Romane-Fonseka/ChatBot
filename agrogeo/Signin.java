package com.example.agrogeo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView name =(TextView) findViewById(R.id.Wname);
        TextView mobile =(TextView) findViewById(R.id.Wno);
        TextView password =(TextView) findViewById(R.id.psd);
        TextView repsd =(TextView) findViewById(R.id.repsd);
        Button signinBtn = (Button) findViewById(R.id.SigninBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name_w = name.getText().toString();
                String mobile_w = mobile.getText().toString();
                String password_w = password.getText().toString();
                String repsd_w = repsd.getText().toString();

            }
        });

    }
}