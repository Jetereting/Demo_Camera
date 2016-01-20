package com.magic.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.magic.adapter.WaitUploadAdapter.ViewHolder;
import com.magic.demo_camera.R;
import com.magic.model.LookShareModel;
import com.magic.model.WaitUploadModel;

public class LookShareAdpter extends BaseAdapter{

	Context context;
	List<WaitUploadModel> list;
	// 用来控制Button的选中状况
	private static HashMap<Integer, Boolean> isSelected;
	
	public LookShareAdpter(Context context, List<WaitUploadModel> list) {
		super();
		this.context = context;
		this.list = list;
	}



	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public WaitUploadModel getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder = null;
		if (view==null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			view=LayoutInflater.from(context).inflate(R.layout.lookshare_listview_item, null);
			holder.imageView=(ImageView)view.findViewById(R.id.imageView);
			holder.button=(Button)view.findViewById(R.id.button);
			// 为view设置标签
			view.setTag(holder);
		}else{
			// 取出holder
			holder = (ViewHolder)view.getTag();
		}
		holder.imageView.setImageBitmap(getItem(position).getBitmap());
		return view;
	}
	public static class ViewHolder {
		public ImageView imageView;
		public Button button;
	}
}

