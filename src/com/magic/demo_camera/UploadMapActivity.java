package com.magic.demo_camera;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

import com.magic.adapter.AlbumAdapter;
import com.magic.upload.util.AlbumHelper;
import com.magic.upload.util.ImageBucket;
import com.magic.widget.FileBrowser;
import com.magic.widget.OnFileBrowserListener;

public class UploadMapActivity extends Activity implements
OnFileBrowserListener {
	private AlbumAdapter folderAdapter;
	GridView gridView;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	ProgressDialog progressDialog;
	String uploadUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		FileBrowser fileBrowser = (FileBrowser)findViewById(R.id.filebrowser);
		findViewById(R.id.tev_app_title).setVisibility(View.VISIBLE);
		fileBrowser.setOnFileBrowserListener(this);
	}

	public void onDirItemClick(String path){}

	public void onFileItemClick(final String filename)
	{
		String para=getIntent().getStringExtra("para");
		uploadUrl = "http://120.24.249.33/uploadfile/FileUploadServlet2?path="+para;
		Log.e("ͼƬ�ϴ�","����ͼ�ϴ�"+para);
		final String end = "\r\n";
		final String twoHyphens = "--";		// �������ַ�
		final String boundary = "******";		// �ֽ�����ַ���
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
						// ����Http����ͷ
						httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
						httpURLConnection.setRequestProperty("Charset", "UTF-8");
						//  ������Content-Type ����ͷ��ָ���ֽ���е������ַ���
						httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

						//��������д������׼���ϴ��ļ�
						DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
						dos.writeBytes(twoHyphens + boundary + end);
						//�������ϴ��ļ���ص���Ϣ
						dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
								+ filename.substring(filename.lastIndexOf("/") + 1)
								+ "\"" + end);
						dos.writeBytes(end);

						FileInputStream fis = new FileInputStream(filename);
						byte[] buffer = new byte[8192]; // 8k
						int count = 0;
						// ��ȡ�ļ������ݣ���д��OutputStream����
						while ((count = fis.read(buffer)) != -1)
						{
							dos.write(buffer, 0, count);
						}
						fis.close();
						SharedPreferences userinfo = UploadMapActivity.this
								.getSharedPreferences("userinfo",
										Context.MODE_PRIVATE);

						dos.writeBytes(end);
						dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
						//						dos.writeBytes("telephone="+userinfo.getString("telephone", ""));
						dos.flush();
						// ��ʼ��ȡ�ӷ���������������Ϣ
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
					Toast.makeText(UploadMapActivity.this, UploadMapActivity.this.getString(R.string.progress_ok), Toast.LENGTH_LONG).show();
					UploadMapActivity.this.startActivity(new Intent(UploadMapActivity.this,ShowMapActivity.class));
					Looper.loop();
				}
			});
			progressDialog = new ProgressDialog(
					UploadMapActivity.this);
			// ���ý�������񣬷��ΪԲ�Σ���ת��
			progressDialog
			.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// ����ProgressDialog ����
			// progressDialog.setTitle("���ȶԻ���");
			// ����ProgressDialog ��ʾ��Ϣ
			progressDialog.setMessage("�����ϴ�ͼƬ...");
			// ����ProgressDialog ����ͼ��
			// progressDialog.setIcon(android.R.drawable.ic_dialog_map);

			// ����ProgressDialog �Ľ������Ƿ���ȷ
			progressDialog.setIndeterminate(false);
			// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
			progressDialog.setCancelable(true);
			// ��ʾ
			progressDialog.show();
			t.start();
		}
		catch (Exception e)
		{
			//			setTitle(e.getMessage());
		}

	}

}
