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
		
//		start 解决NetworkOnMainThreadException
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads().detectDiskWrites().detectNetwork()  
        .penaltyLog().build());  
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  
        .penaltyLog().penaltyDeath().build());  
//		end 解决NetworkOnMainThreadException
		
		initView();
		new Thread(runnable).start();
		adapter=new WaitUploadAdapter(this, list);
		listView.setAdapter(adapter);
//		来自哪里
		type = getIntent().getStringExtra("type");
		if (type.equals("trip_upload")) {
			tev_title_content.setText("旅游上传");
		} else {
			tev_title_content.setText("房产上传");
		}
//		上传点击
		findViewById(R.id.bt_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	progressDialog = new ProgressDialog(
    					WaitUploadActivity.this);
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
            	new Thread(runnableOfUpload).start();
            	startActivity(new Intent(getApplicationContext(),MainActivity.class));
            	Toast.makeText(getApplicationContext(),
						"上传完成！" , Toast.LENGTH_SHORT).show();
            }
        });
//		列表选择
		listView.setOnItemClickListener(this);
	}

	private void initView() {
		
		list=new ArrayList<WaitUploadModel>();
		
		listView = (ListView) findViewById(R.id.listview_waitupload);
        listView.setOnItemClickListener(this);
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		tev_title_content.setText("我的旅游上传");
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
			Log.i("login_requst", "读取数据开始");
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
						"获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
			}

			String input = jsonO.toString();
			Log.i("login_requst", "请求的数据" + input);
			Http http = new Http();
			String requst = http.Result(Config.login_url, input);
			JSONObject json = null;
			try {
				json = new JSONObject(requst);
				Log.i("login_requst", "我是获取图片返回数据" + json.toString());
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
				Log.i("login_requst", "我是获取图片返回状态。。。" + status);
				if (status == 1) {
					JSONObject j = getjson.getJSONObject("dataList");
					Log.i("userid", "我是获取图片chenggong" + j.toString());
					String url = j.getString("url");
					path = j.getString("path");
					Log.i("userid", "我是获取图片...." + url + path);
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
						"获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
				Log.e("我错了",e.toString());
			}
		};
	};
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
		ViewHolder holder = (ViewHolder) arg1.getTag();
		// 改变CheckBox的状态
		holder.checkBox.toggle();
		// 将CheckBox的选中状况记录下来
		WaitUploadAdapter.getIsSelected().put(position, holder.checkBox.isChecked());
	}
//	上传按钮点击
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
						"获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
			}

			String input = jsonO.toString();
			Log.i("login_requst", "请求的数据" + input);
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
				Log.i("login_requst", "我是登陆返回状态。。。" + status);
				if (status == 1) {
					Log.i("login_requst", "我是登陆返回状态1kaishi");
					JSONObject j = getjson.getJSONObject("dataList");
					Log.i("userid", "我是登陆chenggong" + j.toString());
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
						"获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
//			Looper.prepare();
//			Toast.makeText(WaitUploadActivity.this, WaitUploadActivity.this.getString(R.string.progress_ok), Toast.LENGTH_LONG).show();
//			Looper.loop();
		};
	};

}
