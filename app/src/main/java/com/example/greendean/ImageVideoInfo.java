package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageVideoInfo extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Button next_button,video_button,add_image;
    Bitmap bitmap;
    String message=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_video_info);
        getSupportActionBar().hide();
        next_button=(Button)findViewById(R.id.next_button);
        video_button = (Button) findViewById(R.id.video_button);
        add_image = (Button)findViewById(R.id.add_image);
        imageView=(ImageView)findViewById(R.id.UserImageView);
        textView = (TextView) findViewById(R.id.textView5);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        loadImage();
//        imageView.setImageBitmap(bitmap);
//        textView.setText(message);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
                textView.setText(message);
                imageView.setImageBitmap(bitmap);
            }
        });
        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
                ImageVideoInfo.this.finish();
            }
        });
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Addimage.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
                ImageVideoInfo.this.finish();
            }
        });
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
    private void loadImage(){
        String path="http://101.37.75.202:8081/upLoadFile/randomSelectImage";
        final Bitmap[] bitmap_ret = new Bitmap[1];
        new Thread() {
            public void run() {
                try {
                    JSONObject  json = new JSONObject();
//                    json=httpPostMethod.downLoadData(path);
                    json=httpPost( path);
                    JSONObject finalJson = json;
                    String id = finalJson.getString("id");
                    message = finalJson.getString("message");
                    String pictureUrl= finalJson.getString("pictureUrl");
                    String number = finalJson.getString("number");
                    bitmap=downloadImage(pictureUrl);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                            textView.setText(message);
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