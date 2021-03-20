package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button reg_button,login_button;
    EditText user_name,user_pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name =(EditText)findViewById(R.id.et_account);
        user_pwd =(EditText)findViewById(R.id.et_pwd);
        login_button = (Button)findViewById(R.id.btn_log_login);
        reg_button = (Button)findViewById(R.id.btn_log_reg);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = user_name.getText().toString();
                String pwd = user_pwd.getText().toString();
                if(name.equals("")){
                    Toast.makeText(LoginActivity.this, R.string.input_name, Toast.LENGTH_LONG).show();
                }else if(pwd.equals("")){
                    Toast.makeText(LoginActivity.this, R.string.input_pwd, Toast.LENGTH_LONG).show();
                }
                else{//sql查询

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
            }
        });
    }
}