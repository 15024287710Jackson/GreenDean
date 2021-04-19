package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button login_btn, reg_btn,skip_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_btn=(Button)findViewById(R.id.btn_main_login);
        reg_btn = (Button) findViewById(R.id.btn_main_reg);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login_intent);
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(reg_intent);
            }
        });
    }
}