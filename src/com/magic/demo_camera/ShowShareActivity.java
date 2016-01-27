package com.magic.demo_camera;

import java.io.File;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

public class ShowShareActivity extends Activity {

	WebView webView;
	ImageView iv_share;
	EditText et_url;
	String url;
	//分享 url 到朋友圈
	private static final String APPID="wx682c11d1193ecf81";
	private IWXAPI iwxapi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("ShowShareActivity","onCreate");
		setContentView(R.layout.activity_show_share);

		webView=(WebView)findViewById(R.id.webView);
		Intent intent=getIntent();
		url=intent.getStringExtra("url");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(url);

		iv_share=(ImageView)findViewById(R.id.iv_share);
		iv_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				shareText(url);
			}
		});
		iv_share.getBackground().setAlpha(222);
		
		et_url=(EditText)findViewById(R.id.et_url);
		et_url.setText(url);
	}
	//分享文字
    public void shareText(String s) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, s);
        shareIntent.setType("text/plain");
 
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
}
