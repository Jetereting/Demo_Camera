package com.magic.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.magic.demo_camera.R;
import com.magic.model.WaitUploadModel;
public class WaitUploadAdapter extends BaseAdapter{

	Context context;
	List<WaitUploadModel> list;
	// 用来控制CheckBox的选中状况
	private static HashMap<Integer, Boolean> isSelected;
	
	public WaitUploadAdapter(Context context, List<WaitUploadModel> list) {
		super();
		this.context = context;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		// 初始化数据
		initDate();
	}
	// 初始化isSelected的数据
	private void initDate() {
		for (int i = 0; i <77; i++) {
			getIsSelected().put(i, false);
		}
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
			view=LayoutInflater.from(context).inflate(R.layout.waitupload_listview_item, null);
			holder.textView=(TextView)view.findViewById(R.id.tev_waitupload_item);
			holder.imageView=(ImageView)view.findViewById(R.id.imageView);
			holder.checkBox=(CheckBox)view.findViewById(R.id.checkBox);
			// 为view设置标签
			view.setTag(holder);
		}else{
			// 取出holder
			holder = (ViewHolder)view.getTag();
		}
		holder.textView.setText(getItem(position).getWaitupload_name());
		holder.imageView.setImageBitmap(getItem(position).getBitmap());
		holder.checkBox.setChecked(getIsSelected().get(position));
		return view;
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		WaitUploadAdapter.isSelected = isSelected;
	}
	public static class ViewHolder {
		public TextView textView;
		public ImageView imageView;
		public CheckBox checkBox;
	}
}
