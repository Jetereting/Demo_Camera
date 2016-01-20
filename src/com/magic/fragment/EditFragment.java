package com.magic.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.magic.demo_camera.LookShareActivity;
import com.magic.demo_camera.MyHouseActivity;
import com.magic.demo_camera.MyTourActivity;
import com.magic.demo_camera.R;
import com.magic.demo_camera.WaitUploadActivity;

public class EditFragment extends Fragment implements OnClickListener {

	Button trip_share, trip_upload, house_share,house_upload;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		trip_share = (Button) view.findViewById(R.id.trip_share);
		trip_upload = (Button) view.findViewById(R.id.trip_upload);
		house_share = (Button) view.findViewById(R.id.house_share);
		house_upload = (Button) view.findViewById(R.id.house_upload);
		trip_share.setOnClickListener(this);
		trip_upload.setOnClickListener(this);
		house_share.setOnClickListener(this);
		house_upload.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.trip_share:
			Intent intent=new Intent(getActivity(),LookShareActivity.class);
			intent.putExtra("type", "trip_share");
			startActivity(intent);
			break;
		case R.id.trip_upload:
			Intent intent_myhouse=new Intent(getActivity(),WaitUploadActivity.class);
			intent_myhouse.putExtra("type", "trip_upload");
			startActivity(intent_myhouse);
			break;
		case R.id.house_share:
			Intent intent_waitupload=new Intent(getActivity(),LookShareActivity.class);
			intent_waitupload.putExtra("type", "house_share");
			startActivity(intent_waitupload);
			break;
		case R.id.house_upload:
			Intent intent_waitupload1=new Intent(getActivity(),WaitUploadActivity.class);
			intent_waitupload1.putExtra("type", "house_upload");
			startActivity(intent_waitupload1);
			break;
		}
	}

}
