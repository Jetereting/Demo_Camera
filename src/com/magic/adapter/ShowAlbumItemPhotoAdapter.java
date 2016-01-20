package com.magic.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.magic.demo_camera.R;
import com.magic.upload.util.ImageItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ShowAlbumItemPhotoAdapter extends BaseAdapter {

	List<ImageItem> list;
	Context context;
	BitmapUtils bitmapUtils;

	public ShowAlbumItemPhotoAdapter(List<ImageItem> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ImageItem getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.showalbumitemphoto_item, null);
		}
		initView(view, getItem(arg0));
		return view;
	}

	private void initView(View view, ImageItem imageItem) {
		ImageView imageView = (ImageView) view
				.findViewById(R.id.img_showalbumitemphoto_item);
		bitmapUtils.display(imageView, imageItem.getImagePath());
	}

}
