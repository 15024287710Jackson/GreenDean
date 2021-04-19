package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.example.greendean.GetUrl.*;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;



public class VideoPlayer extends Activity {


    private static final int TAKE_PHOTO_ID = 0;
    private Button button1,button2,button3;
    GetUrl getUrl = new GetUrl();
    private Uri uri = getUrl.ReturnUri(1);;
    int cnt = 1;





    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private static boolean verifyPermissions(Activity activity) {
        // Check if we have write permission
        int read_permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (read_permission != PackageManager.PERMISSION_GRANTED && write_permission !=
                PackageManager.PERMISSION_GRANTED ) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }





    private VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyPermissions(this);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.videoview);
        videoView.setVideoURI(uri);
        videoView.start();


        button1 = (Button) findViewById(R.id.take_photo);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_ID);
            }

        });

        button2 = (Button) findViewById(R.id.previous);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(cnt <= 1){
                    Toast.makeText(VideoPlayer.this,"This is the first video!",Toast.LENGTH_SHORT).show();
                }
                else{

                    cnt--;
                }

                uri = getUrl.ReturnUri(cnt);
                videoView.setVideoURI(uri);
                videoView.start();
            }

        });




        button3 = (Button) findViewById(R.id.next);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(cnt >= 3){
                    Toast.makeText(VideoPlayer.this,"This is the last video!",Toast.LENGTH_SHORT).show();
                }
                else{

                    cnt++;
                }
                uri = getUrl.ReturnUri(cnt);
                videoView.setVideoURI(uri);
                videoView.start();
            }

        });





    }





    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
//                strurl = uri.toString();
////                Uri path=Uri.fromFile(new File(strurl));
////                strurl = path.toString();
//                Log.i("tag", strurl);
                videoView.setVideoURI(uri);
                videoView.start();
                Toast.makeText(getApplicationContext(), "Video has been taken", Toast.LENGTH_LONG).show();
            }
        }

    }







    void bindViews() {
        videoView = findViewById(R.id.videoview);


        videoView.setVideoURI(uri);


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