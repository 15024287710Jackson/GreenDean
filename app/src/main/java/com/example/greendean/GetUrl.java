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
import java.net.URL;

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



public class GetUrl {


    public Uri ReturnUri(int pos){
        if(pos == 1){
            return Uri.parse("android.resource://com.example.greendean/" + R.raw.v1);
        }
        else if(pos == 2){
            return Uri.parse("android.resource://com.example.greendean/" + R.raw.v2);
        }
        else if(pos == 3){
            return Uri.parse("android.resource://com.example.greendean/" + R.raw.v3);
        }


        return Uri.parse("android.resource://com.example.greendean/" + R.raw.v1);
    }
}
