package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    Button submitButton;
    EditText editName,editPwd,editEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        submitButton = (Button)findViewById(R.id.submitButton);
        editName = (EditText) findViewById(R.id.editTextTextPersonName);
        editPwd = (EditText) findViewById(R.id.editTextTextPassword);
        editEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传到sql里
                String edit_name=editName.getText().toString();
                String edit_pwd = editPwd.getText().toString();
                String edit_email = editEmail.getText().toString();
                //传到sql里
                if(!edit_name.equals("") && !edit_pwd.equals("") && !edit_email.equals("")){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if(edit_name.equals("")){
                    Toast.makeText(RegisterActivity.this, R.string.input_name, Toast.LENGTH_LONG).show();
                } else if(edit_pwd.equals("")){
                    Toast.makeText(RegisterActivity.this, R.string.input_pwd, Toast.LENGTH_LONG).show();
                } else if(edit_email.equals("")){
                    Toast.makeText(RegisterActivity.this, R.string.input_email, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}