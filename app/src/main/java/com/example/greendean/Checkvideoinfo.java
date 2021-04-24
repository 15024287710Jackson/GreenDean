package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Checkvideoinfo extends AppCompatActivity {
    VideoView videoView;
    TextView textView_videoUser;
    Button next_video_button,del_video_button;
    private int jsonArrayLength;
    private int  count=0;
    private JSONArray jsonArray;
    private String num;
    private String video_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkvideoinfo);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        textView_videoUser=(TextView)findViewById(R.id.textView_videoUser);
        videoView=(VideoView)findViewById(R.id.videoView_checkvideo);
        next_video_button=(Button)findViewById(R.id.next_video_button);
        del_video_button=(Button)findViewById(R.id.del_video_button);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    jsonArray= loadVideo(userId);
//                    System.out.println(jsonArray);
//                    System.out.println(jsonArray.length());
                    jsonArrayLength = jsonArray.length();
                    if(jsonArrayLength==0){
                        Looper.prepare();
                        Toast.makeText(Checkvideoinfo.this, "No Video.", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }else{
                        JSONObject jsonObject= (JSONObject) jsonArray.get(0);
                        video_url= jsonObject.getString("videoUrl");
                        num= jsonObject.getString("number");
//                        System.out.println(url);
//                        bitmap=downloadImage(url);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // imageView_check.setImageBitmap(bitmap);
//                              textView.setText(jsonObject.getString("message"));
                                Uri uri = Uri.parse(video_url);
                                videoView.setVideoURI(uri);
                                videoView.start();
                                try {
                                    textView_videoUser.setText(jsonObject.getString("message"));
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
        next_video_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(jsonArrayLength>0){
                    count++;
                    int numCount = count%jsonArrayLength;
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = (JSONObject) jsonArray.get(numCount);
                        String url= jsonObject.getString("videoUrl");
                        Uri uri = Uri.parse(url);
                        System.out.println(uri);
                        videoView.setVideoURI(uri);
                        videoView.start();
//                        imageView_check.setImageBitmap(bitmap);
                        textView_videoUser.setText(jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Checkvideoinfo.this, "No More Picture.", Toast.LENGTH_LONG).show();
                }
            }
        });
        del_video_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                System.out.println(2);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            String str =delVideoSql(num);
                            JSONObject jsonArray = new JSONObject(str);
                            System.out.println(jsonArray);
                            String result = jsonArray.getString("result");
                            if(result.equals("1")){
                                Looper.prepare();
                                Toast.makeText(Checkvideoinfo.this, "Successfully", Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }else{
                                Looper.prepare();
                                Toast.makeText(Checkvideoinfo.this, "Fail", Toast.LENGTH_LONG).show();
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


    private JSONArray loadVideo(String id) throws JSONException {
        String str = checkVideoSql(id);
        StringEscapeUtils.unescapeJava(str);
        JSONArray jsonArray = new JSONArray(str);
        return jsonArray;
    }

    protected String checkVideoSql(String id){
        JSONArray jsonObject = null;
        int code;
        String resposeStr=null;
        try{
            String path = "http://101.37.75.202:8081/upLoadFile/selectUserVideoInfos";
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

    protected String delVideoSql(String num){
        JSONArray jsonObject = null;
        int code;
        String resposeStr=null;
        try{
            String path = "http://101.37.75.202:8081/upLoadFile/deleteUserVideoInfos";
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