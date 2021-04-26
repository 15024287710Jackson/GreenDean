package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Controllercentre extends AppCompatActivity {
    Button button_toPic,button_toVideo,button_toChat,button_checkimage,button_checkvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controllercentre);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        button_toPic = (Button)findViewById(R.id.button_toPic);
        button_toVideo = (Button)findViewById(R.id.button_toVideo);
        button_toChat = (Button)findViewById(R.id.button_toChat);
        button_checkimage=(Button)findViewById(R.id.button_checkimage);
        button_checkvideo=(Button)findViewById(R.id.button_checkvideo);
        button_toPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ImageVideoInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        button_toVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        button_toChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(userId, userName);
                Intent intent = ChatActivity.newIntent(Controllercentre.this, user.toString());
                startActivity(intent);
            }
        });
        button_checkimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Checkimageinfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        button_checkvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Checkvideoinfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("userName", userName);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    public static Intent newIntent(Context context, String data){
        Intent intent = new Intent(context,Controllercentre.class);
        intent.putExtra("data",data);
        return intent;
    }

}