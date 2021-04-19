package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.Toast;
import com.example.greendean.Uploadvideo;
import com.loopj.android.http.HttpGet;

import java.io.IOException;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


public class VideoPlayer extends AppCompatActivity {

    private static final int TAKE_PHOTO_ID = 0;
    private Button button1,button2,button3;
    private String strurlbase = "http://101.37.75.202:8081/";
    private String strurl = "";

    int cnt = 0;


    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        bindViews();
        button1 = (Button) findViewById(R.id.take_photo);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_ID);
            }

        });

        button2 = (Button) findViewById(R.id.previous);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(cnt <= 0){
                    Toast.makeText(VideoPlayer.this,"This is the first video!",Toast.LENGTH_SHORT).show();
                }
                else{
                    cnt--;
                }
                strurl = strurlbase + Integer.toString(cnt) +".mp4";
                bindViews();
            }

        });


        button3 = (Button) findViewById(R.id.next);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(cnt >= 100){
                    Toast.makeText(VideoPlayer.this,"This is the last video!",Toast.LENGTH_SHORT).show();
                }
                else{
                    cnt++;
                }
                strurl = strurlbase + Integer.toString(cnt) +".mp4";
                bindViews();
            }

        });





    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_ID) {
            if (data != null && data.hasExtra("data")) {
                Bitmap bitmap = data.getParcelableExtra("data");
                Uploadvideo k = new Uploadvideo();
                k.sendImage(bitmap);
                Toast.makeText(getApplicationContext(), "Picture has been taken", Toast.LENGTH_LONG).show();
            }
        }
        bindViews();
    }







    void bindViews() {
        videoView = findViewById(R.id.videoview);


        videoView.setVideoURI(Uri.parse(strurl));

        /**
         * 为 VideoView 视图设置媒体控制器，设置了之后就会自动由进度条、前进、后退等操作
         */
        videoView.setMediaController(new MediaController(this));

        /**视频准备完成时回调
         * */
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("tag", "--------------视频准备完毕,可以进行播放.......");
            }
        });
        /**
         * 视频播放完成时回调
         */
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("tag", "------------------视频播放完毕..........");
                /**播放完成时，再次循环播放*/
                videoView.start();
            }
        });

        /**
         * 视频播放发送错误时回调
         */
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("tag", "---------------------视频播放失败...........");
                return false;
            }
        });

        /**开始播放视频
         * */
        videoView.start();
    }

}