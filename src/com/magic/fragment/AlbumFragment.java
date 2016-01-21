package com.magic.fragment;
import java.util.List;
import com.magic.widget.FileBrowser;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.magic.adapter.AlbumAdapter;
import com.magic.demo_camera.LoginActivity;
import com.magic.demo_camera.R;
import com.magic.demo_camera.ShowMapActivity;
import com.magic.demo_camera.WaitUploadActivity;
import com.magic.upload.util.AlbumHelper;
import com.magic.upload.util.ImageBucket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.magic.widget.OnFileBrowserListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AlbumFragment extends Fragment implements OnFileBrowserListener{

	private AlbumAdapter folderAdapter;
	GridView gridView;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main, container, false);
		
		FileBrowser fileBrowser = (FileBrowser) view.findViewById(R.id.filebrowser);
		fileBrowser.setOnFileBrowserListener(this);
		
		return view;
	}
	
	public void onDirItemClick(String path){}

	public void onFileItemClick(final String filename)
	{
//		Toast.makeText(getActivity(), filename.substring(filename.lastIndexOf("/") + 1)+"正在上传。。", Toast.LENGTH_LONG).show();
		final String uploadUrl = "http://120.24.249.33/uploadfile/FileUploadServlet";
		final String end = "\r\n";
		final String twoHyphens = "--";		// 两个连字符
		final String boundary = "******";		// 分界符的字符串
		try
		{
			Thread t=new Thread(new Runnable(){

				@Override
				public void run() {
					
					try{
						URL url = new URL(uploadUrl);
						HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
						httpURLConnection.setDoInput(true);
						httpURLConnection.setDoOutput(true);
						httpURLConnection.setUseCaches(false);
						httpURLConnection.setRequestMethod("POST");
						// 设置Http请求头
						httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
						httpURLConnection.setRequestProperty("Charset", "UTF-8");
						//  必须在Content-Type 请求头中指定分界符中的任意字符串
						httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
						
						//定义数据写入流，准备上传文件
						DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
						dos.writeBytes(twoHyphens + boundary + end);
						//设置与上传文件相关的信息
						dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
										+ filename.substring(filename.lastIndexOf("/") + 1)
										+ "\"" + end);
						dos.writeBytes(end);

						FileInputStream fis = new FileInputStream(filename);
						byte[] buffer = new byte[8192]; // 8k
						int count = 0;
						// 读取文件夹内容，并写入OutputStream对象
						while ((count = fis.read(buffer)) != -1)
						{
							dos.write(buffer, 0, count);
						}
						fis.close();
						SharedPreferences userinfo = getActivity()
								.getSharedPreferences("userinfo",
										Context.MODE_PRIVATE);
						
						dos.writeBytes(end);
						dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
//						dos.writeBytes("telephone="+userinfo.getString("telephone", ""));
						dos.flush();
						// 开始读取从服务器传过来的信息
						InputStream is = httpURLConnection.getInputStream();
						InputStreamReader isr = new InputStreamReader(is, "utf-8");
						BufferedReader br = new BufferedReader(isr);
						String result = br.readLine();
						dos.close();
						is.close();
					}catch(Exception e){
					}
					progressDialog.dismiss();
					Looper.prepare();
					Toast.makeText(getActivity(), getActivity().getString(R.string.progress_ok), Toast.LENGTH_LONG).show();
					getActivity().startActivity(new Intent(getActivity(),ShowMapActivity.class));
					Looper.loop();
				}
			});
			progressDialog = new ProgressDialog(
					getActivity());
			// 设置进度条风格，风格为圆形，旋转的
			progressDialog
					.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置ProgressDialog 标题
			// progressDialog.setTitle("进度对话框");
			// 设置ProgressDialog 提示信息
			progressDialog.setMessage("正在上传图片...");
			// 设置ProgressDialog 标题图标
			// progressDialog.setIcon(android.R.drawable.ic_dialog_map);

			// 设置ProgressDialog 的进度条是否不明确
			progressDialog.setIndeterminate(false);
			// 设置ProgressDialog 是否可以按退回按键取消
			progressDialog.setCancelable(true);
			// 显示
			progressDialog.show();
			t.start();
		}
		catch (Exception e)
		{
//			setTitle(e.getMessage());
		}

	}

}
