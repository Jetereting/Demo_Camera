package com.magic.demo_camera;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.magic.adapter.ShowAlbumItemPhotoAdapter;
import com.magic.model.PhotoListModel;
import com.magic.upload.util.ImageItem;

public class ShowAlbumItemPhoto extends Activity implements OnItemClickListener{

	Button btn_title_back;
	TextView tev_title_content;
	GridView gridView;

	ArrayList<ImageItem> list = new ArrayList<ImageItem>();
	ShowAlbumItemPhotoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_showalbumitemphoto);
		initView();
		Intent intent = getIntent();

		tev_title_content.setText(intent.getStringExtra("folderName"));
		list = ((PhotoListModel) intent.getExtras().get("photoListModel"))
				.getPhotoList();
		adapter = new ShowAlbumItemPhotoAdapter(list, this);
		gridView.setAdapter(adapter);
	}

	private void initView() {
		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		btn_title_back = (Button) findViewById(R.id.btn_title_back);

		btn_title_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		gridView = (GridView) findViewById(R.id.grv_showalbumitemphoto);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// π”√Intent
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri mUri = Uri.parse("file://" +adapter.getItem(arg2).getImagePath());
		intent.setDataAndType(mUri, "image/*");
		startActivity(intent);
	}

}
