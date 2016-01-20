package com.magic.demo_camera;

import com.magic.upload.activity.UploadMainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MyTourActivity extends Activity implements OnClickListener {

	Button btn_title_back;
	TextView tev_title_content;
	Button btn_upload, btn_lookshare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mytour);
		initView();
	}

	private void initView() {
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		btn_upload = (Button) findViewById(R.id.btn_mytour_waitupload);
		btn_lookshare = (Button) findViewById(R.id.btn_mytour_lookshare);
		tev_title_content.setText("Œ“µƒ¬√”Œ");
		btn_title_back.setOnClickListener(this);
		btn_upload.setOnClickListener(this);
		btn_lookshare.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_title_back:
			finish();
			break;
		case R.id.btn_mytour_waitupload:
			startActivity(new Intent(MyTourActivity.this,
					UploadMainActivity.class));
			break;
		case R.id.btn_mytour_lookshare:
			Intent intent = new Intent(MyTourActivity.this,
					LookShareActivity.class);
			intent.putExtra("type", "MyTourActivity");
			startActivity(intent);
			break;
		}
	}

}
