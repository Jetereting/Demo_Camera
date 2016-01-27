package com.magic.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowser extends ListView implements
android.widget.AdapterView.OnItemClickListener
{
	private final String namespace = "http://mobile.blogjava.net";
	private String sdcardDirectory;
	private List<File> fileList = new ArrayList<File>();
	private Stack<String> dirStack = new Stack<String>();
	private FileListAdapter fileListAdapter;
	private OnFileBrowserListener onFileBrowserListener;
	private int folderImageResId;
	private int otherFileImageResId;
	private Map<String, Integer> fileImageResIdMap = new HashMap<String, Integer>();
	private boolean onlyFolder = false;

	public FileBrowser(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		sdcardDirectory = android.os.Environment.getExternalStorageDirectory()
				.toString()+ "/Photo_LJ/";
		setOnItemClickListener(this);
		setBackgroundColor(android.graphics.Color.WHITE);

		folderImageResId = attrs.getAttributeResourceValue(namespace,
				"folderImage", 0);
		otherFileImageResId = attrs.getAttributeResourceValue(namespace,
				"otherFileImage", 0);
		onlyFolder = attrs.getAttributeBooleanValue(namespace, "onlyFolder",
				false);
		int index = 1;
		while (true)
		{
			String extName = attrs.getAttributeValue(namespace, "extName"
					+ index);
			int fileImageResId = attrs.getAttributeResourceValue(namespace,
					"fileImage" + index, 0);

			if ("".equals(extName) || extName == null || fileImageResId == 0)
			{
				break;
			}
			fileImageResIdMap.put(extName, fileImageResId);
			index++;
		}

		dirStack.push(sdcardDirectory);
		addFiles();

		fileListAdapter = new FileListAdapter(getContext());
		setAdapter(fileListAdapter);

	}

	private void addFiles()
	{
		fileList.clear();
		String currentPath = getCurrentPath();
		File[] files = new File(currentPath).listFiles();
		if (dirStack.size() > 1)
			fileList.add(null);
		for (File file : files)
		{
			if (onlyFolder)
			{
				if (file.isDirectory())
					fileList.add(file);
			}
			else
			{
				fileList.add(file);
			}
		}
	}

	private String getCurrentPath()
	{
		String path = "";
		for (String dir : dirStack)
		{
			path += dir + "/";
		}
		path = path.substring(0, path.length() - 1);
		return path;
	}

	private String getExtName(String filename)
	{

		int position = filename.lastIndexOf(".");
		if (position >= 0)
			return filename.substring(position + 1);
		else
			return "";
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		if (fileList.get(position) == null)
		{
			dirStack.pop();
			addFiles();
			fileListAdapter.notifyDataSetChanged();
			if (onFileBrowserListener != null)
			{
				onFileBrowserListener.onDirItemClick(getCurrentPath());
			}
		}
		else if (fileList.get(position).isDirectory())
		{
			dirStack.push(fileList.get(position).getName());
			addFiles();
			fileListAdapter.notifyDataSetChanged();
			if (onFileBrowserListener != null)
			{
				onFileBrowserListener.onDirItemClick(getCurrentPath());
			}
		}
		else
		{
			if (onFileBrowserListener != null)
			{
				String filename = getCurrentPath() + "/"
						+ fileList.get(position).getName();
				onFileBrowserListener.onFileItemClick(filename);
			}
		}

	}

	private class FileListAdapter extends BaseAdapter
	{
		private Context context;

		public FileListAdapter(Context context)
		{
			this.context = context;
		}

		@Override
		public int getCount()
		{
			return fileList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return fileList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout fileLayout = new LinearLayout(context);
			fileLayout.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			fileLayout.setOrientation(LinearLayout.HORIZONTAL);
			fileLayout.setPadding(5, 10, 0, 10);
			ImageView ivFile = new ImageView(context);
			ivFile.setLayoutParams(new LayoutParams(222, 110));
			TextView tvFile = new TextView(context);
			tvFile.setTextColor(android.graphics.Color.WHITE);
			tvFile.setTextAppearance(context,
					android.R.style.TextAppearance_Large);

			tvFile.setPadding(5, 5, 0, 0);
			if (fileList.get(position) == null)
			{

				if (folderImageResId > 0)
					ivFile.setImageResource(folderImageResId);
				tvFile.setText(". .");
			}
			else if (fileList.get(position).isDirectory())
			{
				if (folderImageResId > 0){
					//					ivFile.setImageResource(folderImageResId);
					ivFile.setImageBitmap(getImageThumbnail(sdcardDirectory+fileList.get(position).getName(),222,110));
				}
				tvFile.setText(fileList.get(position).getName());
			}
			else
			{
				tvFile.setText(fileList.get(position).getName());
				Integer resId = fileImageResIdMap.get(getExtName(fileList.get(
						position).getName()));
				int fileImageResId = 0;
				if (resId != null)
				{
					if (resId > 0)
					{
						fileImageResId = resId;
					}

				}
				if (fileImageResId > 0){
//					ivFile.setImageResource(fileImageResId);
					ivFile.setImageBitmap(getImageThumbnail(sdcardDirectory+fileList.get(position).getName(),222,110));
				}
				else if (otherFileImageResId > 0){
//					ivFile.setImageResource(otherFileImageResId);
					ivFile.setImageBitmap(getImageThumbnail(sdcardDirectory+fileList.get(position).getName(),222,110));
				}
			}

			tvFile.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			fileLayout.addView(ivFile);
			fileLayout.addView(tvFile);
			return fileLayout;
		}
	}

	public void setOnFileBrowserListener(OnFileBrowserListener listener)
	{
		this.onFileBrowserListener = listener;
	}

	private Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}