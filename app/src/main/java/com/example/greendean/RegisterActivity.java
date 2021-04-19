package com.example.greendean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {
    Button submitButton;
    EditText editName,editPwd,editEmail,editTele;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        submitButton = (Button)findViewById(R.id.submitButton);
        editName = (EditText) findViewById(R.id.editTextTextPersonName);
        editPwd = (EditText) findViewById(R.id.editTextTextPassword);
        editEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTele = (EditText) findViewById(R.id.editTextTeleNum);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //传到sql里
                String edit_name = editName.getText().toString();
                String edit_pwd = editPwd.getText().toString();
                String edit_email = editEmail.getText().toString();
                String edit_tele = editTele.getText().toString();
                //传到sql里
                if (!edit_name.equals("") && !edit_pwd.equals("") && !edit_email.equals("")&& !edit_tele.equals("")) {
                    new Thread() {
                        public void run() {
                            JSONObject jsonObject=insertSql(edit_name, edit_pwd, edit_email, edit_tele);
                            Looper.prepare();
                            try {
                                if(jsonObject.getString("result").equals("Y")){
                                    savePreferences(edit_name, edit_pwd);
                                    Toast.makeText(RegisterActivity.this,jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(RegisterActivity.this,jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }.start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }, 500);
                } else if (edit_name.equals("")) {
                    Toast.makeText(RegisterActivity.this, R.string.input_name, Toast.LENGTH_LONG).show();
                } else if (edit_pwd.equals("")) {
                    Toast.makeText(RegisterActivity.this, R.string.input_pwd, Toast.LENGTH_LONG).show();
                } else if (edit_email.equals("")) {
                    Toast.makeText(RegisterActivity.this, R.string.input_email, Toast.LENGTH_LONG).show();
                } else if (edit_tele.equals("")) {
                    Toast.makeText(RegisterActivity.this, R.string.input_tel, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected JSONObject insertSql(String edit_name,String edit_pwd,String edit_email,String edit_tele)  {
        JSONObject jsonObject = null;
        int code;
        try{
            String path = "http://101.37.75.202:8081/resgisterLogin/resgisterUser";
            URL url = new URL(path);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            JSONObject json = new JSONObject();
            json.put("userName", edit_name);
            json.put("passWord", edit_pwd);
            json.put("emailAddress", edit_email);
            json.put("telephoneNumber", edit_tele);
            String jsonString = json.toString();
            OutputStream os = conn.getOutputStream();
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);
            System.out.println(conn.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            if(conn.getResponseCode()==200){
                String str = response.toString();
                jsonObject= new JSONObject(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void savePreferences(String account, String password) {
        SharedPreferences pref = getSharedPreferences("GreenDeanAccountInfo", MODE_PRIVATE);
        pref.edit().putString("account", account).apply();
        pref.edit().putString("password", password).apply();
    }

}