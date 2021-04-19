package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends Activity {
    private ArrayList<User> mUserArrayList = new ArrayList<>();//定义一个存储信息的列表
    private EditText mInputText;//输入框
    private Button mSend;//发送按钮
    private RecyclerView mRecyclerView;//滑动框
    private UserAdapter mAdapter;//适配器
    private boolean backFlag = false;
    private WebSocket mSocket;
    private Button privatesend;


    private User mUser;;//全局User

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();

        

        String data = getIntent().getStringExtra("data");
        if (!data.equals("")){
            mUser = JSON.parseObject(data,User.class);
        }else {
            mUser = new User("0001","测试名字",R.drawable.boy);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserAdapter(mUserArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //开启连接
        start(mUser.getUserId());




        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(true,content,false);
                    User tempUser = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg);
                    mSocket.send(tempUser.toString());
                    mUserArrayList.add(tempUser);
                    updateRecyclerView();//刷新RecyclerView
                    //清空输入栏
                    mInputText.setText("");
                }
            }
        });

        privatesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSocket.close();
                sendclosemessage(1);//打开私聊
                //此函数向服务端发送关闭申请，服务端收到后返回允许关闭
                //收到允许关闭后关闭并打开私聊

            }
        });
    }

    private void openprivatechat()
    {
        Intent mintent = privatechatActivity.newIntent(ChatActivity.this,mUser.toString());
        startActivity(mintent);
        finish();
    }


    private void sendclosemessage(int i)
    {
        if(i==1)//打开私聊
        {
            Msg msg123456 = new Msg(true,"CloseTheSessionPLZandOPENprivate",false);
            msg123456.setConfi(true);
            User tempUser123456 = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg123456);
            tempUser123456.getUserMsg().setConfi(true);
            mSocket.send(tempUser123456.toString());
        }
        if(i==2)
        {
            Msg msg123456 = new Msg(true,"CloseTheSessionPLZ",false);
            msg123456.setConfi(true);
            User tempUser123456 = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg123456);
            tempUser123456.getUserMsg().setConfi(true);
            mSocket.send(tempUser123456.toString());
        }
    }


    @Override
    protected void onDestroy() {
        //this.sendclosemessage(2);
        super.onDestroy();
    }

    /**
     * 刷新view
     * */
    private void updateRecyclerView(){
        //当有新消息时，刷新RecyclerView中的显示
        mAdapter.notifyItemInserted(mUserArrayList.size() - 1);
        //将RecyclerView定位到最后一行
        mRecyclerView.scrollToPosition(mUserArrayList.size() - 1);
    }

    /**
     * 开启web socket连接
     * */
    private void start(String userId) {

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(300, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(300, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(300, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        //定义request
        Request request = new Request.Builder().url("ws://101.37.75.202:8081/test/"+userId).build();
        //绑定回调接口
        mOkHttpClient.newWebSocket(request, new EchoWebSocketListener());
        mOkHttpClient.dispatcher().executorService().shutdown();


    }

    /**
     * 显示内容
     * */
    private void output(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUserArrayList.add(user);
                updateRecyclerView();
            }
        });
    }

    /**
     * 初始化界面
     * */
    private void initView(){
        mInputText = findViewById(R.id.input_text);
        mSend = findViewById(R.id.send);
        privatesend = findViewById(R.id.privatechat);
        mRecyclerView = findViewById(R.id.msg_recycler_view);
    }

    /**
     * 静态方法返回一个能启动自己的intent
     * */
    public static Intent newIntent(Context context, String data){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra("data",data);
        return intent;
    }

    /**
     * 对返回键的处理
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&&!backFlag){
            Toast.makeText(ChatActivity.this,"再按一次退出聊天窗口",Toast.LENGTH_SHORT).show();
            backFlag = true;
            return true;
        }else {
            Intent mintent = LoginActivity.newIntent(ChatActivity.this,mUser.toString());
            startActivity(mintent);
            //mSocket.close();
            sendclosemessage(2);
            //此函数单纯为了确保sockets已经关闭
            //服务端不会发回任何消息
            finish();
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 内部类，监听web socket回调
     * */
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;    //实例化web socket
            //传送本用户头像及名字
            Msg msg777 = new Msg(true,"PaSsThEtOuXiAnGhEmInGzI",false);
            User tempUser777 = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg777);
            tempUser777.getUserMsg().setConfi(true);
            mSocket.send(tempUser777.toString());
            //
            User user = new User();
            user.setUserMsg(new Msg(false,"连接成功",true));
            output(user);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            System.out.println(text);
            User user = JSON.parseObject(text, User.class);
            if(user.getUserMsg().isConfi())
            {
                if(user.getUserMsg().getContent().equals("okyoucanclose"))
                {
                    openprivatechat();
                    return;
                }
            }
            output(user);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            User user = new User();
            user.setUserMsg(new Msg(false,"关闭连接",true));
            output(user);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            User user = new User();
            user.setUserMsg(new Msg(false,"连接失败:"+t.getMessage(),true));
            output(user);
        }
    }

}
