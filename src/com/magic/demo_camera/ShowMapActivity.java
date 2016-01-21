package com.magic.demo_camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class ShowMapActivity extends Activity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
//        鍔犺浇缃戠粶鍥剧墖
        webView=(WebView)findViewById(R.id.webView);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        url="http://120.24.249.33/uploadfile/ImagesUploaded/17096241774_3/20160120202413129432/vtour/vr-go/1.jpg";
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getApplicationContext(),"x:"+event.getX()+"\n"+"y:"+event.getY(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
