package com.example.greendean;

import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpPostMethod {
    public JSONObject httpPost(String path,JSONObject json)  {
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
//            JSONObject json = new JSONObject();
//            json.put("userName", edit_name);
//            json.put("passWord", edit_pwd);
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
                jsonObject= new JSONObject(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
