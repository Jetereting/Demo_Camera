package com.magic.demo_camera;

import java.io.File;
import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.magic.utils.Config;
import com.magic.utils.Http;
import com.magic.utils.IsNetwork;
import com.magic.utils.TimeCount;

@SuppressLint("HandlerLeak") 
public class LoginActivity extends Activity{

	EditText edt_username, edt_passwd;
	Button btn_login,bt_check_code,bt_weibo,bt_weixin;
	TextView tev_register;

	ProgressDialog progressDialog;
	String telephone="";
	TimeCount time;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mkMyDris();
		haveUser();
		initView();
		event();
	}


	private void event() {
		bt_check_code.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				time.start();
			}
		});
	}

	private void mkMyDris() {
		File f=new File(android.os.Environment.getExternalStorageDirectory()
				+ "/Photo_LJ/");
		if(!f.exists()){
			f.mkdirs();
		}

	}

	private void haveUser() {
		SharedPreferences userinfo = LoginActivity.this
				.getSharedPreferences("userinfo",
						Context.MODE_PRIVATE);

		if(userinfo.getString("telephone", "")!=""){
			startActivity(new Intent(LoginActivity.this,
					MainActivity.class));
		}
	}

	private void initView() {
		edt_username = (EditText) findViewById(R.id.edt_login_username);
		edt_passwd = (EditText) findViewById(R.id.edt_login_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		tev_register = (TextView) findViewById(R.id.tev_register);
		bt_check_code=(Button)findViewById(R.id.bt_check_code);
		bt_weibo=(Button)findViewById(R.id.bt_weibo);
		bt_weixin=(Button)findViewById(R.id.bt_weixin);
		time=new TimeCount(30000,1000,bt_check_code);
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (edt_username.getText().length() != 0) {
					if (edt_passwd.getText().length() != 0) {
						if (IsNetwork.isNetwork(LoginActivity.this)) {
							//						if(1==1){
							progressDialog = new ProgressDialog(
									LoginActivity.this);
							// 设置进度条风格，风格为圆形，旋转的
							progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							// 设置ProgressDialog 标题
							// progressDialog.setTitle("进度对话框");
							// 设置ProgressDialog 提示信息
							progressDialog.setMessage("正在提交数据...");
							// 设置ProgressDialog 标题图标
							// progressDialog.setIcon(android.R.drawable.ic_dialog_map);

							// 设置ProgressDialog 的进度条是否不明确
							progressDialog.setIndeterminate(false);
							// 设置ProgressDialog 是否可以按退回按键取消
							progressDialog.setCancelable(true);
							// 显示
							progressDialog.show();
							// progressDialog.dismiss();
							// 在新线程中打开网络数据。
							new Thread(runnable).start();
						} else {
							Toast.makeText(LoginActivity.this, "您的网络不给力",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(LoginActivity.this, "密码不能为空",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(LoginActivity.this, "用户名不能为空",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		tev_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this,
						RegisterActiviyt.class));
			}
		});
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i("login_requst", "读取数据开始");
			String u_name = edt_username.getText().toString();
			String u_pass = edt_passwd.getText().toString();
			JSONObject jsonO = new JSONObject();
			try {
				jsonO.put("serviceName", "login");
				JSONObject jsonP = new JSONObject();
				jsonP.put("loginName", u_name);
				jsonP.put("password", u_pass);
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
				Log.i("login_requst", "我是登陆返回数据" + json.toString());
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
				Log.i("login_requst", "我是登陆返回状态。。。" + status);
				if (status == 1) {
					Log.i("login_requst", "我是登陆返回状态1kaishi");
					JSONObject j = getjson.getJSONObject("dataList");
					Log.i("userid", "我是登陆chenggong" + j.toString());
					telephone = j.getString("telephone");
					int vid = j.getInt("vid");
					SharedPreferences userinfo = LoginActivity.this
							.getSharedPreferences("userinfo",
									Context.MODE_PRIVATE);
					Editor editor = userinfo.edit();
					editor.putString("telephone", telephone);
					editor.putInt("vid", vid);
					Log.i("userid", "我是登陆ID...." + vid + telephone);
					editor.commit();
					LoginActivity.this.finish();

					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.putExtra("phone", telephone);
					startActivity(intent);
				} else {
					String comment = getjson.getString("comment");
					Toast.makeText(LoginActivity.this, comment,
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
		};
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
		}
		return true;
	}
}