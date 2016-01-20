package com.magic.demo_camera;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.magic.utils.Config;
import com.magic.utils.Http;

@SuppressLint("ShowToast")
public class RegisterActiviyt extends Activity {

	Button btn_title_back;
	TextView tev_title_content;
	EditText edt_username, edt_passwd, edt_passwd2;
	Button btn_register;
	HttpUtils httpUtils;
	
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		initView();
		httpUtils = new HttpUtils();
	}

	private void initView() {
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		edt_username = (EditText) findViewById(R.id.edt_register_username);
		edt_passwd = (EditText) findViewById(R.id.edt_register_passwd);
		edt_passwd2 = (EditText) findViewById(R.id.edt_register_passwd2);
		btn_register = (Button) findViewById(R.id.btn_register);
		tev_title_content.setText("ע��");

		btn_title_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				register(edt_username.getText().toString(), edt_passwd
						.getText().toString(), edt_passwd2.getText().toString());
			}
		});
	}

	private void register(String username, String passwd, String passwd2) {
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwd)
				|| TextUtils.isEmpty(passwd2)) {
			Toast.makeText(this, "����д����", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (passwd.equals(passwd2)) {
				progressDialog = new ProgressDialog(
						RegisterActiviyt.this);
				// ���ý�������񣬷��ΪԲ�Σ���ת��
				progressDialog
						.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// ����ProgressDialog ����
				// progressDialog.setTitle("���ȶԻ���");
				// ����ProgressDialog ��ʾ��Ϣ
				progressDialog.setMessage("�����ύ����...");
				// ����ProgressDialog ����ͼ��
				// progressDialog.setIcon(android.R.drawable.ic_dialog_map);

				// ����ProgressDialog �Ľ������Ƿ���ȷ
				progressDialog.setIndeterminate(false);
				// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
				progressDialog.setCancelable(true);
				// ��ʾ
				progressDialog.show();
				// progressDialog.dismiss();
				// �����߳��д��������ݡ�
				new Thread(runnable).start();
			} else {
				Toast.makeText(this, "�������벻һ��", Toast.LENGTH_SHORT).show();
			}
		}
	}
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			JSONObject jsonO = new JSONObject();
			try {
				jsonO.put("serviceName", "commonMemberRegister");
				JSONObject jsonP = new JSONObject();
				jsonP.put("telephone", edt_username.getText().toString());
				jsonP.put("password", edt_passwd2.getText().toString());
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
				Log.i("login_requst", "���ǵ�½��������" + json.toString());
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
				Log.i("login_requst", "���ǵ�½����״̬������" + status);
				if (status == 1) {
					Log.i("login_requst", "���ǵ�½����״̬1kaishi");
//					JSONObject j = getjson.getJSONObject("dataList");
//					Log.i("userid", "���ǵ�½chenggong" + j.toString());
					String telephone = getjson.getString("telephone");
//					int vid = j.getInt("vid");
					SharedPreferences userinfo = RegisterActiviyt.this
							.getSharedPreferences("userinfo",
									Context.MODE_PRIVATE);
					Editor editor = userinfo.edit();
					editor.putString("telephone", telephone);
//					editor.putInt("vid", vid);
//					Log.i("userid", "���ǵ�½ID...." + vid + telephone);
					editor.commit();
					RegisterActiviyt.this.finish();

					Intent intent = new Intent(RegisterActiviyt.this,
							MainActivity.class);
					intent.putExtra("phone", telephone);
					startActivity(intent);
				} else {
					String comment = getjson.getString("comment");
					Toast.makeText(RegisterActiviyt.this, comment,
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"��ȡ����ʧ��:" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
		};
	};
}
