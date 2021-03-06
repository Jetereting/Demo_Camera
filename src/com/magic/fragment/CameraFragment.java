package com.magic.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.magic.demo_camera.R;
import com.theta360.sample.v2.ImageListActivity;

public class CameraFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_camera, container, false);
		view.findViewById(R.id.btn_camera_opencamera).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ImageListActivity.class);
				getActivity().startActivity(intent);
			}
		});
		return view;
	}
}
