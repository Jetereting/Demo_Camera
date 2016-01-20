package com.magic.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.magic.demo_camera.R;
import com.magic.demo_camera.ShowAlbumItemPhoto;
import com.magic.model.PhotoListModel;
import com.magic.upload.util.BitmapCache;
import com.magic.upload.util.BitmapCache.ImageCallback;
import com.magic.upload.util.ImageBucket;
import com.magic.upload.util.ImageItem;
import com.magic.upload.util.Res;

public class AlbumAdapter extends BaseAdapter {

	private Context mContext;
	// private Intent mIntent;
	private DisplayMetrics dm;
	BitmapCache cache;

	List<ImageBucket> list;

	public AlbumAdapter(Context c, List<ImageBucket> list) {
		cache = new BitmapCache();
		init(c);
		this.list = list;
	}

	public void init(Context c) {
		mContext = c;
		// mIntent = ((Activity) mContext).getIntent();
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				}
			}
		}
	};

	private class ViewHolder {
		public ImageView imageView;
		public ImageView choose_back;
		public TextView folderName;
		public TextView fileNum;
	}

	ViewHolder holder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.imgupload_plugin_camera_select_folder, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.file_image);
			holder.choose_back = (ImageView) convertView
					.findViewById(R.id.choose_back);
			holder.folderName = (TextView) convertView.findViewById(R.id.name);
			holder.fileNum = (TextView) convertView.findViewById(R.id.filenum);
			holder.imageView.setAdjustViewBounds(true);
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String path;
		if (list.get(position).imageList != null) {

			path = list.get(position).imageList.get(0).imagePath;
			holder.folderName.setText(list.get(position).bucketName);
			holder.fileNum.setText("" + list.get(position).count);
		} else
			path = "android_hybrid_camera_default";
		if (path.contains("android_hybrid_camera_default"))
			holder.imageView.setImageResource(Res
					.getDrawableID("plugin_camera_no_pictures"));
		else {
			final ImageItem item = list.get(position).imageList.get(0);
			holder.imageView.setTag(item.imagePath);
			cache.displayBmp(holder.imageView, item.thumbnailPath,
					item.imagePath, callback);
		}
		holder.imageView.setOnClickListener(new ImageViewClickListener(
				position, holder.choose_back));
		return convertView;
	}

	private class ImageViewClickListener implements OnClickListener {
		private int position;

		// private Intent intent;
		// private ImageView choose_back;
		public ImageViewClickListener(int position, ImageView choose_back) {
			this.position = position;
			// this.intent = intent;
			// this.choose_back = choose_back;
		}

		public void onClick(View v) {

			ArrayList<ImageItem> photoList = (ArrayList<ImageItem>) list
					.get(position).imageList;
			PhotoListModel photoListModel = new PhotoListModel(photoList);
			String folderName = list.get(position).bucketName;
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("photoListModel", photoListModel);
            intent.putExtras(bundle);
			intent.putExtra("folderName", folderName);
			intent.setClass(mContext, ShowAlbumItemPhoto.class);
			mContext.startActivity(intent);
		}
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

}
