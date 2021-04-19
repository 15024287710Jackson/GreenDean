package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    Button reg_button, login_button;
    EditText user_name, user_pwd;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    int login_result = 0;
    String user_id;
    String account = "";
    String password = "";

    public static Intent newIntent(Context context, String data){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra("data",data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name = (EditText) findViewById(R.id.et_account);
        user_pwd = (EditText) findViewById(R.id.et_pwd);
        login_button = (Button) findViewById(R.id.btn_log_login);
        reg_button = (Button) findViewById(R.id.btn_log_reg);
        loadPreferences();
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String pwd;
                name = user_name.getText().toString();
                pwd = user_pwd.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.input_name, Toast.LENGTH_LONG).show();
                } else if (pwd.equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.input_pwd, Toast.LENGTH_LONG).show();
                } else {//sql查询
                    System.out.println(2);
                    new Thread() {
                        public void run() {
                            JSONObject jsonObject = loginSql(name, pwd);
                            Looper.prepare();
                            try {
                                if (jsonObject.getString("result").equals("Y")) {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                    login_result = 1;
                                    user_id = jsonObject.getString("id");
                                    String userName = name;
                                    String userId = user_id;
                                    savePreferences(name, pwd);
                                    Intent intent = new Intent(getApplicationContext(),Controllercentre.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId", userId);
                                    bundle.putString("userName", userName);
//                                    bundle.putString("weight", weight);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
//                                    User user = new User(userId, userName);
//                                    Intent intent = ChatActivity.newIntent(LoginActivity.this, user.toString());
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }.start();
//                    new Thread(){
//                        public void run(){
//                            int code ;
//                            System.out.println(2);
//                            try{
//                                String path = "http://109.166.36.56:54288/hello/getWholeUserInfo";
//                                URL url = new URL(path);
//                                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
//                                conn.setRequestMethod("GET");
//                                conn.setConnectTimeout(5000);
//                                code = conn.getResponseCode();
//                                if(code==200){
//                                    InputStream is = conn.getInputStream();
//                                    String result = HttpUtils.readMyInputStream(is);
//                                    Message msg = new Message();
//                                    msg.obj = result;
//                                    msg.what = SUCCESS;
//                                    handler.sendMessage(msg);
//                                }
//                            }catch (Exception e){
//                                    e.printStackTrace();
//                                Message msg = new Message();
//                                msg.what = FAILURE;
//                                handler.sendMessage(msg);
//                            }
//                        }
//                    }.start();
                }
            }
        });
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    /**
                     * 获取信息成功后，对该信息进行JSON解析，得到所需要的信息，然后在textView上展示出来。
                     */
                    JSONAnalysis(msg.obj.toString());
                    Toast.makeText(LoginActivity.this, "获取数据成功", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case FAILURE:
                    Toast.makeText(LoginActivity.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(LoginActivity.this, "获取的CODE码不为200！",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    protected void JSONAnalysis(String string) {
        JSONObject object = null;
        try {
            object = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * 在你获取的string这个JSON对象中，提取你所需要的信息。
         */
        System.out.println(object);
    }

    protected JSONObject loginSql(String edit_name, String edit_pwd) {
        JSONObject jsonObject = null;
        int code;
        try {
            String path = "http://101.37.75.202:8081/resgisterLogin/loginUser";
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            JSONObject json = new JSONObject();
            json.put("userName", edit_name);
            json.put("passWord", edit_pwd);
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
            if (conn.getResponseCode() == 200) {
                String str = response.toString();
                jsonObject = new JSONObject(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void savePreferences(String account, String password) {
        SharedPreferences pref = getSharedPreferences("GreenDeanAccountInfo", MODE_PRIVATE);
        pref.edit().putString("account", account).apply();
        pref.edit().putString("password", password).apply();
    }

    public void loadPreferences() {
        SharedPreferences pref = getSharedPreferences("GreenDeanAccountInfo", MODE_PRIVATE);
        account = pref.getString("account", "");
        password = pref.getString("password", "");
        if (!account.equals("") && account != null) {
            user_name.setText(account);
        }
        if (!password.equals("") && password != null) {
            user_pwd.setText(password);
        }
    }
}
