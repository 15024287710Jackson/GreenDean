package com.example.greendean;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView_video;
    Button button_video,button_picfun,button_addfunc;
    TextView textView99;
    String message=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        button_video=(Button)findViewById(R.id.button_video);
        button_picfun = (Button) findViewById(R.id.button_picfun);
        button_addfunc = (Button)findViewById(R.id.button_addfunc);
        videoView_video = (VideoView)findViewById(R.id.videoView_video);
        textView99= (TextView)findViewById(R.id.textView99);
        loadVideo();
        button_video.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadVideo();
            }
        });
        button_picfun.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent reg_intent = new Intent(getApplicationContext(),ImageVideoInfo.class);
//                startActivity(reg_intent);
                Intent intent = new Intent(getApplicationContext(),ImageVideoInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
                VideoActivity.this.finish();
            }
        });
        button_addfunc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent reg_intent = new Intent(getApplicationContext(),Addvideo.class);
//                startActivity(reg_intent);
                Intent intent = new Intent(getApplicationContext(),Addvideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
                VideoActivity.this.finish();
            }
        });
    }

    private void loadVideo(){
        String path="http://101.37.75.202:8081/upLoadFile/randomSelectVideo";
        final Bitmap[] bitmap_ret = new Bitmap[1];
        new Thread() {
            public void run() {
                try {
                    JSONObject json = new JSONObject();
//                    json=httpPostMethod.downLoadData(path);
                    json=httpPost( path);
                    JSONObject finalJson = json;
                    String id = finalJson.getString("id");
                    message = finalJson.getString("message");
                    String videoUrl= finalJson.getString("videoUrl");
                    String number = finalJson.getString("number");
//                    bitmap=downloadImage(pictureUrl);
                    videoView_video.post(new Runnable() {
                        @Override
                        public void run() {
                            videoView_video.setVideoURI(Uri.parse(videoUrl));
                            videoView_video.start();
                            textView99.setText(message);
                        }
                    });
//
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public JSONObject httpPost(String path)  {
        JSONObject jsonObject = null;
        int code;
        try{
//            String path = "http://109.166.36.56:54288/resgisterLogin/loginUser";
            URL url = new URL(path);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
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

}