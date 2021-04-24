package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Callback;

public class Checkimageinfo extends AppCompatActivity {
    private Button next_picture_button,del_picture_button;
    private ImageView imageView_check;
    private TextView textView;
    private int jsonArrayLength;
    private Bitmap bitmap;
    private int  count=0;
    private JSONArray jsonArray;
    private String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkimageinfo);
        next_picture_button= (Button)findViewById(R.id.next_picture_button);
        del_picture_button= (Button)findViewById(R.id.del_picture_button);
        imageView_check = (ImageView)findViewById(R.id.imageView_check);
        textView = (TextView)findViewById(R.id.textView6);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    jsonArray= loadPic(userId);
//                    System.out.println(jsonArray);
//                    System.out.println(jsonArray.length());
                    jsonArrayLength = jsonArray.length();
                    if(jsonArrayLength==0){
                        Looper.prepare();
                        Toast.makeText(Checkimageinfo.this, "No Picture.", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }else{
                        JSONObject jsonObject= (JSONObject) jsonArray.get(0);
                        String url= jsonObject.getString("pictureUrl");
                        num= jsonObject.getString("number");
//                        System.out.println(url);
                        bitmap=downloadImage(url);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    imageView_check.setImageBitmap(bitmap);
                                    textView.setText(jsonObject.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        next_picture_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(jsonArrayLength>0){
                    count++;
                    int numCount = count%jsonArrayLength;
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = (JSONObject) jsonArray.get(numCount);
                        String url= jsonObject.getString("pictureUrl");
                        num= jsonObject.getString("number");
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                bitmap=downloadImage(url);
                            }
                        }).start();
                        imageView_check.setImageBitmap(bitmap);
                        textView.setText(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Checkimageinfo.this, "No More Picture.", Toast.LENGTH_LONG).show();
                }
            }
        });
        del_picture_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println(2);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            String str =delImageSql(num);
                            JSONObject jsonArray = new JSONObject(str);
                            String result = jsonArray.getString("result");
                            if(result.equals("1")){
                                Looper.prepare();
                                Toast.makeText(Checkimageinfo.this, "Successfully", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }else{
                                Looper.prepare();
                                Toast.makeText(Checkimageinfo.this, "Fail", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private JSONArray loadPic(String id) throws JSONException {
        String str = checkImageSql(id);
        StringEscapeUtils.unescapeJava(str);
        JSONArray jsonArray = new JSONArray(str);
        return jsonArray;
    }
    protected String checkImageSql(String id){
        JSONArray jsonObject = null;
        int code;
        String resposeStr=null;
        try{
            String path = "http://101.37.75.202:8081/upLoadFile/selectUserPicInfos";
            URL url = new URL(path);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            JSONObject json = new JSONObject();
            json.put("id", id);
//            json.put("passWord", edit_pwd);
//            json.put("emailAddress", edit_email);
//            json.put("telephoneNumber", edit_tele);
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
                resposeStr=response.toString();
                jsonObject= new JSONArray(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resposeStr;
    }

    public Bitmap downloadImage(String path){
        Bitmap bitmap = null;
        InputStream inStream;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(2500);
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);
            urlConn.connect();
            inStream=urlConn.getInputStream();
            bitmap= BitmapFactory.decodeStream(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected String delImageSql(String num){
        JSONArray jsonObject = null;
        String resposeStr=null;
        try{
            String path = "http://101.37.75.202:8081/upLoadFile/deleteUserPicInfos";
            URL url = new URL(path);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            JSONObject json = new JSONObject();
            json.put("num", num);
//            json.put("passWord", edit_pwd);
//            json.put("emailAddress", edit_email);
//            json.put("telephoneNumber", edit_tele);
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
                resposeStr=response.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resposeStr;
    }

}