package com.magic.demo_camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.magic.adapter.WaitUploadAdapter;
import com.magic.adapter.WaitUploadAdapter.ViewHolder;
import com.magic.model.WaitUploadModel;
import com.magic.utils.Config;
import com.magic.utils.GetBitmapFromUrl;
import com.magic.utils.Http;

public class WaitUploadActivity extends Activity implements OnItemClickListener{

	Button btn_title_back;
	TextView tev_title_content;
	ListView listView;
	
	WaitUploadAdapter adapter;
	List<WaitUploadModel> list;
	String path;
	String selectPath="";
	String telephone="";
	String type;
	
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_waitupload);
		
//		start ���NetworkOnMainThreadException
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads().detectDiskWrites().detectNetwork()  
        .penaltyLog().build());  
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  
        .penaltyLog().penaltyDeath().build());  
//		end ���NetworkOnMainThreadException
		
		initView();
		new Thread(runnable).start();
		adapter=new WaitUploadAdapter(this, list);
		listView.setAdapter(adapter);
//		��������
		type = getIntent().getStringExtra("type");
		if (type.equals("trip_upload")) {
			tev_title_content.setText("�����ϴ�");
		} else {
			tev_title_content.setText("�����ϴ�");
		}
//		�ϴ����
		findViewById(R.id.bt_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	progressDialog = new ProgressDialog(
    					WaitUploadActivity.this);
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
            	new Thread(runnableOfUpload).start();
            	startActivity(new Intent(getApplicationContext(),MainActivity.class));
            	Toast.makeText(getApplicationContext(),
						"�ϴ���ɣ�" , Toast.LENGTH_SHORT).show();
            }
        });
//		�б�ѡ��
		listView.setOnItemClickListener(this);
	}

	private void initView() {
		
		list=new ArrayList<WaitUploadModel>();
		
		listView = (ListView) findViewById(R.id.listview_waitupload);
        listView.setOnItemClickListener(this);
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		tev_title_content.setText("�ҵ������ϴ�");
		btn_title_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
                finish();
			}
		});
	}
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i("login_requst", "��ȡ���ݿ�ʼ");
			JSONObject jsonO = new JSONObject();
			try {
				SharedPreferences userinfo = WaitUploadActivity.this
						.getSharedPreferences("userinfo",
								Context.MODE_PRIVATE);
				telephone=userinfo.getString("telephone","");
				jsonO.put("serviceName", "findMyGoodsOrder");
				JSONObject jsonP = new JSONObject();
				jsonP.put("telphone", telephone);
				jsonO.put("queryParameters", jsonP);
				
//				Log.e("userJson:",jsonO.toString());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"��ȡ����ʧ��:" + e.toString(), Toast.LENGTH_SHORT).show();
			}

			String input = jsonO.toString();
			Log.i("login_requst", "���������" + input);
			Http http = new Http();
			String requst = http.Result(Config.login_url, input);
			JSONObject json = null;
			try {
				json = new JSONObject(requst);
				Log.i("login_requst", "���ǻ�ȡͼƬ��������" + json.toString());
			} catch (Exception e) {
			}
			Message msg = new Message();
			msg.obj = json;
			handler.sendMessage(msg);
		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				JSONObject getjson = (JSONObject) msg.obj;
				int status = getjson.getInt("status");
				Log.i("login_requst", "���ǻ�ȡͼƬ����״̬������" + status);
				if (status == 1) {
					JSONObject j = getjson.getJSONObject("dataList");
					Log.i("userid", "���ǻ�ȡͼƬchenggong" + j.toString());
					String url = j.getString("url");
					path = j.getString("path");
					Log.i("userid", "���ǻ�ȡͼƬ...." + url + path);
					String[] urls = url.split(",");
					String[] paths = path.split(",");
					for(int i=0;i<urls.length;i++){
						Bitmap bitmap=GetBitmapFromUrl.getBitmap(urls[i]);
						list.add(new WaitUploadModel(paths[i]+"",bitmap));
						adapter.notifyDataSetChanged();
					}
					
				} else {
					String comment = getjson.getString("comment");
					Toast.makeText(getApplicationContext(), comment,
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"��ȡ����ʧ��:" + e.toString(), Toast.LENGTH_SHORT).show();
				Log.e("�Ҵ���",e.toString());
			}
		};
	};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
		ViewHolder holder = (ViewHolder) arg1.getTag();
		// �ı�CheckBox��״̬
		holder.checkBox.toggle();
		// ��CheckBox��ѡ��״����¼����
		WaitUploadAdapter.getIsSelected().put(position, holder.checkBox.isChecked());
	}
//	�ϴ���ť���
	Runnable runnableOfUpload = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject jsonO = new JSONObject();
//			start get selectPath
			HashMap<Integer, Boolean> map = WaitUploadAdapter.getIsSelected();
			for (int i = 0; i < map.size(); i++) {
				if (map.get(i)) {
					selectPath += list.get(i).getWaitupload_name()+",";
				}
			}
			WaitUploadAdapter.getIsSelected().get("");
//			end get path			
			try {
				jsonO.put("serviceName", "onloadProcess");
				JSONObject jsonP = new JSONObject();
				jsonP.put("telephone", telephone);
				jsonP.put("photo", selectPath);
				if (type.equals("trip_upload")) {
					jsonP.put("type", 1);
				} else {
					jsonP.put("type", 2);
				}
				jsonO.put("queryParameters", jsonP);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"��ȡ����ʧ��:" + e.toString(), Toast.LENGTH_SHORT).show();
			}

			String input = jsonO.toString();
			Log.i("login_requst", "���������" + input);
			Http http = new Http();
			String requst = http.Result(Config.login_url, input);
			JSONObject json = null;
			try {
				json = new JSONObject(requst);
			} catch (Exception e) {
			}
			Message msg = new Message();
			msg.obj = json;
			handler1.sendMessage(msg);
		}
	};
	private Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				JSONObject getjson = (JSONObject) msg.obj;
				int status = getjson.getInt("status");
				Log.i("login_requst", "���ǵ�½����״̬������" + status);
				if (status == 1) {
					Log.i("login_requst", "���ǵ�½����״̬1kaishi");
					JSONObject j = getjson.getJSONObject("dataList");
					Log.i("userid", "���ǵ�½chenggong" + j.toString());
					telephone = j.getString("telephone");
					WaitUploadActivity.this.finish();

					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					intent.putExtra("phone", telephone);
					startActivity(intent);
				} else {
					String comment = getjson.getString("comment");
					Toast.makeText(getApplicationContext(), comment,
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"��ȡ����ʧ��:" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
//			Looper.prepare();
//			Toast.makeText(WaitUploadActivity.this, WaitUploadActivity.this.getString(R.string.progress_ok), Toast.LENGTH_LONG).show();
//			Looper.loop();
		};
	};

}
