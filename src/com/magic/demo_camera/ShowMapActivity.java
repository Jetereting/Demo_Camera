package com.magic.demo_camera;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ShowMapActivity extends Activity {

    WebView webView;
    RadioGroup radioGroup;
    RadioButton rb_1, rb_2;
    int num,x0,y0,x1,y1;
    String outUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        initView();

        String url=getIntent().getStringExtra("url");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        
        num=Integer.parseInt(getIntent().getStringExtra("num"));
        if (num == 1) {
            findViewById(R.id.rb_2).setVisibility(View.GONE);
        }
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
            	if(rb_1.isChecked()){
                    x0=(int)event.getX();y0=(int)event.getY();
                    rb_1.setText("房间1  x 坐标:"+x0+"\ty坐标："+y0);
                }else if(rb_2.isChecked()){
                    x1=(int)event.getX();y1=(int)event.getY();
                    rb_2.setText("房间2  x 坐标:"+x1+"\ty坐标："+y1);
                }else {
                    Toast.makeText(getApplicationContext(),"请先选择房间号",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
//      处理点击
      findViewById(R.id.bt_result).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
        	  if(num==1) {
                  outUrl = "http://120.24.249.33/uploadfile/Dom4j?path=" + getIntent().getStringExtra("para") + "&num=" + num+"&x0="+x0+"&y0="+y0;
                  if(x0==0){
                      Toast.makeText(getApplicationContext(),"请先获取房间1的坐标后 再点击处理",Toast.LENGTH_SHORT).show();
                      return;
                  }
              }else if(num==2){
                  if(x0==0){
                      Toast.makeText(getApplicationContext(),"请先获取房间1的坐标后 再点击处理",Toast.LENGTH_SHORT).show();
                      return;
                  }else if(x1==0){
                      Toast.makeText(getApplicationContext(),"请先获取房间2的坐标后 再点击处理",Toast.LENGTH_SHORT).show();
                      return;
                  }
                  outUrl = "http://120.24.249.33/uploadfile/Dom4j?path=" + getIntent().getStringExtra("para") + "&num=" + num+"&x0="+x0+"&y0="+y0+"&x1="+x1+"&y1="+y1;
              }
              try {
            	  Log.e("outUrl",outUrl);
                  URL url = new URL(outUrl);
                  HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                  httpURLConnection.setDoInput(true);
                  httpURLConnection.setDoOutput(true);
                  httpURLConnection.setUseCaches(false);
                  httpURLConnection.setRequestMethod("POST");
                  // 设置Http请求头
                  httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                  httpURLConnection.setRequestProperty("Charset", "UTF-8");
                  httpURLConnection.connect();
              }catch (Exception e){
                  Log.e("处理异常",e.toString());
              }
          }
      });

    }
	private void initView() {
		// TODO Auto-generated method stub
		webView=(WebView)findViewById(R.id.webView);
		radioGroup = (RadioGroup)this.findViewById(R.id.rg);
		rb_1 = (RadioButton) findViewById(R.id.rb_1);
        rb_2 = (RadioButton) findViewById(R.id.rb_2);
	}
}
