package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogintestActivity extends AppCompatActivity {

    private EditText mEditTextName,mEditTextId;
    private Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logintest);
        init();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mEditTextName.getText().toString();
                String userId = mEditTextId.getText().toString();
                User user = new User(userId,userName);
                Intent intent = ChatActivity.newIntent(LogintestActivity.this,user.toString());
                startActivity(intent);
                LogintestActivity.this.finish();
            }
        });
    }

    private void init(){
        mEditTextName = findViewById(R.id.nickname_editText);
        mEditTextId = findViewById(R.id.id_editText);
        mEditTextName.setCompoundDrawables(initDrawable(R.drawable.nickname),null,null,null);
        mEditTextId.setCompoundDrawables(initDrawable(R.drawable.id),null,null,null);
        mButtonLogin = findViewById(R.id.login);
    }

    /**
     * 设置EditText左边图片的大小
     * */
    private Drawable initDrawable(int res){
        Drawable drawable = getResources().getDrawable(res);
        //距离左边距离，距离上边距离，长，宽
        drawable.setBounds(0,0,100,100);
        return drawable;
    }

}