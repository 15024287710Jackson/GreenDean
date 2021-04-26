package com.example.greendean;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class privatechatActivity extends Activity {
    private ArrayList<User> mUserArrayList = new ArrayList<>();//定义一个存储信息的列表
    private Map<String, Integer> localhistory;
    private ArrayList<ArrayList<User>> storedhistory;
    private ArrayList<User> touxiangList = new ArrayList<>();
    private EditText mInputText;//输入框
    private Button mSend;//发送按钮
    private RecyclerView mRecyclerView;//滑动框
    private ChooseAdapter selectionAdapter;
    private UserAdapter mAdapter;//适配器
    private ChooseAdapter mAdapter2;
    private TextView mOnlinenum;
    private TextView mTargetId;
    private boolean backFlag = false;
    private WebSocket mSocket;
    private RecyclerView mSelectionView;
    private String TargetUserId;
    private UserList mUserList;
    private int setter1;
    private int SHUTDOWNFLAG = 0;
    //private ArrayList<User> Userlistarray123;



    private User mUser;;//全局User
    private User testuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privatechat);
        getActionBar().hide();
        initView();



        //申请userlist
        //mUserList = new UserList();
        //ArrayList<User> Userlistarray123 = new ArrayList<User>();
        localhistory = new ConcurrentHashMap<>();
        storedhistory = new ArrayList<ArrayList<User>>();

        System.out.println("初始化");
        TargetUserId = "";
        setter1=0;

        mOnlinenum = (TextView)findViewById(R.id.onlinenum);
        mTargetId = (TextView)findViewById(R.id.targetid);

        //getUserList();



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


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false);
        mSelectionView.setLayoutManager(layoutManager2);
        mAdapter2 = new ChooseAdapter(touxiangList);
        mAdapter2.setOnItemClickListener(new ChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, User user) {
                System.out.println(user);
                System.out.println(mUserArrayList);
                if(localhistory.get(TargetUserId)==null)//检查当前用户是否已保存
                    //如果没有保存，没有拥有setter
                {
                    localhistory.put(TargetUserId,setter1);//保存此用户的setter
                    //System.out.println(mUserArrayList);
                    storedhistory.add(setter1,mUserArrayList);//在setter对应位置上保存聊天记录
                    //System.out.println(storedhistory.get(setter1));
                    setter1++;
                    System.out.println("1");
                }
                else if(localhistory.get(TargetUserId)!=null)//如果已经保存过 拥有setter
                {
                    int setter2 = localhistory.get(TargetUserId);//取出对应的setter
                    storedhistory.set(setter2,mUserArrayList);//在setter对应位置上保存聊天记录
                    System.out.println("2");
                }
                //保存结束
                //开始加载
                TargetUserId = user.getUserId();
                mTargetId.setText("你正在与用户  "+user.getUserName()+"  私聊！");
                mUserArrayList.clear();
                if(localhistory.get(TargetUserId)!=null)//如果targetId已经保存过 拥有setter
                {
                    int setter2 = localhistory.get(TargetUserId);//取出对应的setter
                    System.out.println(storedhistory.get(setter2));
                    mUserArrayList = storedhistory.get(setter2);//取出聊天记录
                    //System.out.println(setter2);
                    //System.out.println(storedhistory.get(setter2));
                    //System.out.println("3");


                }
                System.out.println("4");
                System.out.println(setter1);
                updateRecyclerView();


            }
        });
        mSelectionView.setAdapter(mAdapter2);


        //开启连接
        start(mUser.getUserId());

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(true,content,false);
                    msg.setTargetUserID(TargetUserId);
                    msg.setPrivate(true);
                    User tempUser = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg);
                    tempUser.getUserMsg().setTargetUserID(TargetUserId);
                    tempUser.getUserMsg().setPrivate(true);
                    mSocket.send(tempUser.toString());
                    mUserArrayList.add(tempUser);
                    updateRecyclerView();//刷新RecyclerView
                    //清空输入栏
                    mInputText.setText("");
                }
            }
        });


    }



    /**
     * 刷新view
     * */
    private void updateRecyclerView(){
        //当有新消息时，刷新RecyclerView中的显示
        //mAdapter.notifyItemInserted(mUserArrayList.size() - 1);
        mAdapter.notifyDataSetChanged();
        //将RecyclerView定位到最后一行
        mRecyclerView.scrollToPosition(mUserArrayList.size() - 1);
    }

    private void updatetouxiangView(int i){
        //当有新消息时，刷新RecyclerView中的显示
        //mAdapter2.notifyItemInserted(i);
        mAdapter2.notifyDataSetChanged();
        //将RecyclerView定位到最后一行
        //mRecyclerView.scrollToPosition(mUserArrayList.size() - 1);
    }
    private void updatetouxiangViewdel(int i){
        //当有新消息时，刷新RecyclerView中的显示
        mAdapter2.notifyDataSetChanged();

        //将RecyclerView定位到最后一行
        //mRecyclerView.scrollToPosition(mUserArrayList.size() - 1);
    }


    //更新表面

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

    private void outputforuser(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                touxiangList.add(user);
                updatetouxiangView(touxiangList.indexOf(user));
                mOnlinenum.setText("当前有"+String.valueOf(touxiangList.size())+"人在线！");
            }
        });
    }

    private void outputdeluser(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //int i = touxiangList.indexOf(user);
                System.out.println(touxiangList.size());
                for(int j = 0 ; j < touxiangList.size();j++)
                {
                    System.out.println("remove");

                    if(touxiangList.get(j).getUserId().equals(user.getUserId()))
                    {
                        //System.out.println(j);
                        System.out.println("remove one!");
                        touxiangList.remove(j);
                        mAdapter2.notifyDataSetChanged();
                    }
                }
                mOnlinenum.setText("当前有"+String.valueOf(touxiangList.size())+"人在线！");

                //updatetouxiangViewdel(1);
            }
        });
    }

    /**
     * 初始化界面
     * */
    private void initView(){
        mInputText = findViewById(R.id.input_text_private);
        mSend = findViewById(R.id.send_private);
        mRecyclerView = findViewById(R.id.msg_recycler_view_private);
        mSelectionView = findViewById(R.id.privateselection);
        mOnlinenum = findViewById(R.id.onlinenum);
    }

    private void sendclosemessage()
    {
        Msg msg123456 = new Msg(true,"CloseTheSessionPLZ",false);
        msg123456.setConfi(true);
        User tempUser123456 = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg123456);
        tempUser123456.getUserMsg().setConfi(true);
        mSocket.send(tempUser123456.toString());
    }

    @Override
    protected void onDestroy() {
        //this.sendclosemessage();
        super.onDestroy();
    }

    /**
     * 静态方法返回一个能启动自己的intent
     * */
    public static Intent newIntent(Context context, String data){


        Intent intent = new Intent(context,privatechatActivity.class);
        intent.putExtra("data",data);
        return intent;
    }

    /**
     * 对返回键的处理
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK&&!backFlag){
            Toast.makeText(privatechatActivity.this,"再按一次退出私聊",Toast.LENGTH_SHORT).show();
            backFlag = true;
            return true;
        }else {
            sendclosemessage();
            Intent mintent = ChatActivity.newIntent(privatechatActivity.this,mUser.toString());
            startActivity(mintent);
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 使用正则表达式提取中括号中的内容
     * @param msg
     * @return
     */
    public static ArrayList<String> extractMessageByRegular(String msg){

        ArrayList<String> list=new ArrayList<String>();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(msg);
        while(m.find()){
            list.add(m.group().substring(1, m.group().length()-1));
        }
        return list;
    }

    /**
     * 提取{}括号中内容，忽略括号中的括号
     * @param msg
     * @return
     */
    public static ArrayList<String> extractMessage(String msg) {

        ArrayList<String> list = new ArrayList<String>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '{') {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (msg.charAt(i) == '}') {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(msg.substring(start , i+1));
                }
            }
        }
        return list;
    }



    /**
     * 内部类，监听web socket回调
     * */
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;    //实例化web socket

            Msg msg888 = new Msg(true,"ShEnQiNgLiEbIaO",false);
            msg888.setConfi(true);
            User tempUser888 = new User(mUser.getUserId(),mUser.getUserName(),R.drawable.boy,msg888);
            tempUser888.getUserMsg().setConfi(true);
            mSocket.send(tempUser888.toString());

            User user = new User();
            user.setUserMsg(new Msg(false,"连接成功",true));
            output(user);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            System.out.println(text);
            if(text.contains("gEiNiShEnQiNgLiEbIaO"))
            {

                System.out.println("11111");//mUserList = JSON.parseObject(text, UserList.class);
                ArrayList<User> Userlistarray123 = new ArrayList<User>();

                ArrayList<String> Userlistarray123string;
                ArrayList<String> Userlistarray345string;

                Userlistarray123string = extractMessageByRegular(text);
                Userlistarray345string = extractMessage(Userlistarray123string.get(0));


                System.out.println(Userlistarray345string);

                for(String stringpart : Userlistarray345string)
                {
                    //System.out.println('1');
                    //User tempuser = new User();
                    //String tempstring = Userlistarray123string.get(j);
                    //String tempimg = tempstring.substring(tempstring.indexOf("Img"),tempstring.indexOf("userMsg"));
                    //String tempimgnew = tempimg.substring(5,tempimg.length()-2);
                    //System.out.println(tempimgnew);
                    //String tempname = tempstring.substring(tempstring.indexOf("userName"),tempstring.indexOf("userId"));
                    //String tempnamenew = tempname.substring(11,tempname.length()-3);
                    //System.out.println(tempnamenew);
                    //tempuser.setUserImg(Integer.valueOf(tempimgnew));
                    //tempuser.setUserName(tempnamenew);
                    //Userlistarray123.add(tempuser);


                    //System.out.println(JSON.parseObject(stringpart, User.class));
                    //System.out.println(JSONArray.parseObject(stringpart, User.class));

                    Userlistarray123.add(JSON.parseObject(stringpart, User.class));
                    System.out.println(Userlistarray123);
                }


                //String msg = "PerformanceManager[第1个中括号]Product[第2个中括号]<[第3个中括号]79~";
                //List<String> list = extractMessageByRegular(msg);
                //for (int i = 0; i < list.size(); i++) {
                //    System.out.println(i+"-->"+list.get(i));
                //}
                //JSONArray jsonArray1= JSONArray.parseArray(text);
                //System.out.println(jsonArray1);
                //Userlistarray123 = JSONArray.parseArray(text,User.class);

                //Userlistarray123 = jsonArray1.toJavaList(User.class);

                //Userlistarray123.forEach(student -> System.out.println("stundet info: " + student));

                System.out.println("是列表");

                if(!Userlistarray123.isEmpty())
                {
                    System.out.println("非空，开始更新");
                    for(User user1:Userlistarray123)
                    {
                        //touxiangList.add(user1);
                        //mAdapter2.notifyItemInserted(touxiangList.size() - 1);
                        outputforuser(user1);
                    }
                }
                return;

            }


            User user = JSON.parseObject(text, User.class);
            System.out.println(user);

            if(user.getUserMsg().isConfi())
            {

                System.out.println("axiba2");


                //if(user.getUserMsg().getContent().equals("gEiNiShEnQiNgLiEbIaO"))
                //{
                //    System.out.println("axiba3");
                //    touxiangList.add(user);
                //    mAdapter2.notifyItemInserted(mUserArrayList.size() - 1);
                //}


                if(user.getUserMsg().getContent().equals("NEWUSERCOME"))
                {
                    System.out.println("更新User列表！");
                    outputforuser(user);
                }

                return;
            }

            System.out.println(user.getUserMsg().isConfi());

            if(user.getUserMsg().isSystem())
            {
                if(user.getUserMsg().getContent().equals("有用户断开聊天"))
                {
                    //Userlistarray123.remove(user);
                    if(user.getUserId().equals(TargetUserId))
                    {
                        System.out.println(TargetUserId);
                        System.out.println(user.getUserId());
                        //Toast.makeText(getApplicationContext(),"您私聊的用户已经下线",Toast.LENGTH_LONG).show();
                        //System.out.println("111111111111111111111111111111");
                        Intent mintent = ChatActivity.newIntent(privatechatActivity.this,mUser.toString());

                        startActivity(mintent);
                        SHUTDOWNFLAG =1;

                        finish();




                        return;


                    }
                    else {
                        System.out.println("delete user");
                        outputdeluser(user);

                        return;
                    }
                }


            }

            if(user.getUserMsg().isPrivate())
            {

                if(user.getUserMsg().getTargetUserID().equals(mUser.getUserId()))
                {
                    user.setUserName(user.getUserName()+"     私聊");
                    if(SHUTDOWNFLAG == 0) {
                        System.out.println("normal output");
                        output(user);
                        return;
                    }

                }
                else return;


            }




            if(SHUTDOWNFLAG == 0) {
                System.out.println("normal output");
                output(user);
            }
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
