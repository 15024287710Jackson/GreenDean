package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Addvideo extends AppCompatActivity {
    private MediaRecorder mMediaRecorder;
    VideoView videoView_addvideo;
    Button take_video_button,upload_button;
    EditText et_account;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    String video_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvideo);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        videoView_addvideo = (VideoView)findViewById(R.id.videoView_addvideo);
        take_video_button = (Button)findViewById(R.id.take_video_button);
        upload_button = (Button)findViewById(R.id.upload_button);
        et_account = (EditText)findViewById(R.id.et_account);
        take_video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
        });
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="http://101.37.75.202:8081/upLoadFile/upLoadVideo";
                File file=new File(video_url);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("id", userId);
                params.put("Message", et_account.getText().toString());
//                JSONObject jsonObject = new JSONObject();
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject=post_file(url, params, file, "file");
                            Looper.prepare();
                            if(jsonObject.getString("result").equals("Y")){
                                Intent intent = new Intent(getApplicationContext(),Controllercentre.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", userId);
                                bundle.putString("userName", userName);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                Addvideo.this.finish();
                            }
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            ContentResolver contentResolver = getContentResolver();
            video_url=getRealVideoPathFromURI(contentResolver, videoUri);
            videoView_addvideo.setVideoURI(videoUri);
            videoView_addvideo.start();
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//        }
    }
    public JSONObject post_file(final String url, final Map<String, Object> param, File file, String name) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "multipart/form-data");
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("video/mp4"),  file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart(name,  file.getName(), body);
        }

        if (param != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : param.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        final Request.Builder builder = new Request.Builder().url(url);
        if (head != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : head.entrySet()) {
                builder.addHeader(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        Request request = builder.url(url).post(requestBody.build()).build();
        Response response = client.newCall(request).execute();
        String str=response.body().string();
        System.out.println(str);
        jsonObject= new JSONObject(str);;
        if(response.code()==200){
            Looper.prepare();
            try {
                Toast.makeText(this, "Upload Successfully.", Toast.LENGTH_LONG).show();
            }catch (Exception e) {
                e.printStackTrace();
            }
            Looper.loop();
        }
        return jsonObject;
        // readTimeout("请求超时时间" , 时间单位);
//       client.newBuilder().readTimeout(50000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);
    }

    public static String getRealVideoPathFromURI(ContentResolver contentResolver, Uri contentURI) {
        Cursor cursor = contentResolver.query(contentURI, null, null, null, null);
        if (cursor == null)
            return contentURI.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            try {
                return cursor.getString(idx);
            } catch (Exception exception) {
                return null;
            }
        }
    }

}