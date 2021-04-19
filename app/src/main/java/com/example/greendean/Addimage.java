package com.example.greendean;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Addimage extends AppCompatActivity implements View.OnClickListener, UploadUtil.OnUploadProcessListener {

    private static final String TAG = "uploadImage";

    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;
    /***
     * 这里的这个URL是我服务器的javaEE环境URL
     */
    private static String requestURL = "http://101.37.75.202:8081/upLoadFile/upLoadPic";
    private Button selectButton,uploadButton;
    private ImageView imageView;
    private TextView uploadImageResult;
    private ProgressBar progressBar;

    private String picPath = null;
    private ProgressDialog progressDialog;
    private static final int TAKE_PHOTO_ID = 0;
    private Button take_pic_button,upload_button;
    private ImageView image;
    private EditText editText;
    private Bitmap bitmap_whole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addimage);
        take_pic_button = (Button) findViewById(R.id.button_takepic);
        upload_button = (Button) findViewById(R.id.upload_button);
        image = (ImageView) findViewById(R.id.imageView_pic);
        editText=(EditText)findViewById(R.id.et_account) ;
        progressDialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        String userId = bundle.getString("userId");
        String userName =  bundle.getString("userName");
        take_pic_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_ID);
            }
        });
        upload_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                picPath=saveBitmap( bitmap_whole);
//                uploadImageResult.setText("正在上传中...");
//                progressDialog.setMessage("正在上传文件...");
//                progressDialog.show();
                String fileKey = "file";
//                UploadUtil uploadUtil = UploadUtil.getInstance();;
//                uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", "20210416214433675");
//                params.put("Message", editText.getText().toString());
//                uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("id", userId);
                params.put("Message", editText.getText().toString());
                new Thread(new Runnable(){
                    @Override
                    public void run() {
//                        String result = multipartRequest(requestURL, params, picPath, "file", "image/jpeg");
                        File file = new File(picPath);
                        Callback callback = null;
//                        String response = UploadImage.uploadFile(file, requestURL);
                        try {
                            JSONObject jsonObject=post_file(requestURL,  params, file,"file",callback);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        System.out.println(response);
                    }
                }).start();
                System.out.println("1");
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_ID) {
            if (data != null && data.hasExtra("data")) {
                Bitmap bitmap = data.getParcelableExtra("data");
                bitmap_whole=bitmap;
                image.setImageBitmap(bitmap);
                Toast.makeText(this, "A photo is taken.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onClick(View v) {

    }
    public JSONObject post_file(final String url, final Map<String, Object> param, File file, String name, okhttp3.Callback callback) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        Map<String, String> head = new HashMap<>();
        head.put("Content-Type", "multipart/form-data");
        if (file != null) {
                // MediaType.parse() 里面是上传的文件类型。
                RequestBody body = RequestBody.create(MediaType.parse("image/jpg"),  file);
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
        System.out.println(response.body().string());
        jsonObject= new JSONObject(response.body().string());;
        if(response.code()==200){
            Toast.makeText(this, "Upload Successfully.",
                    Toast.LENGTH_LONG).show();
        }
        return jsonObject;
        // readTimeout("请求超时时间" , 时间单位);
//       client.newBuilder().readTimeout(50000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(callback);
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        handler.sendMessage(msg);
    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {

    }
    private void toUploadFile()
    {
        uploadImageResult.setText("正在上传中...");
        progressDialog.setMessage("正在上传文件...");
        progressDialog.show();
        String fileKey = "pic";
        UploadUtil uploadUtil = UploadUtil.getInstance();;
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", "20210416214433675");
        params.put("orderId", "11111");
        uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;

                case UPLOAD_INIT_PROCESS:
                    progressBar.setMax(msg.arg1);
                    break;
                case UPLOAD_IN_PROCESS:
                    progressBar.setProgress(msg.arg1);
                    break;
                case UPLOAD_FILE_DONE:
                    String result = "响应码："+msg.arg1+"\n响应信息："+msg.obj+"\n耗时："+UploadUtil.getRequestTime()+"秒";
                    uploadImageResult.setText(result);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

//    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
//    private static final String IN_PATH = "/dskqxt/pic/";

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }
    /**
     * 保存bitmap到本地
     *
     * @param mBitmap
     * @return
     */
    public String saveBitmap( Bitmap mBitmap) {
        String savePath;
        File filePic;
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//            savePath = SD_PATH;
//        } else {
//            savePath = context.getApplicationContext().getFilesDir()
//                    .getAbsolutePath()
//                    + IN_PATH;
//        }
        try {
            String sdCard = this.getExternalFilesDir(null).getAbsolutePath();
            File directory = new File (sdCard + "/FilesExternal");
            directory.mkdirs();
            filePic= new File(directory , generateFileName() + ".jpg");
//            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }

    private static final String CHARSET = "utf-8"; // 设置编码
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private static final String BOUNDARY =  UUID.randomUUID().toString(); // 边界标识 随机生成
    public String multipartRequest(String urlTo, Map<String, String> parmas, String filepath, String filefield, String fileMimeType) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", CHARSET); // 设置编码
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + file.getName() + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: application/octet-stream" + lineEnd);
//            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            outputStream.writeBytes(lineEnd);
            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            System.out.println(connection);

            if (200 != connection.getResponseCode()) {
//                throw new CustomException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
                System.out.println("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }else{
                inputStream = connection.getInputStream();
                result = this.convertStreamToString(inputStream);
                fileInputStream.close();
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}