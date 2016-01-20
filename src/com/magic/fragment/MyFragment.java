package com.magic.fragment;

import org.json.JSONObject;

import com.magic.demo_camera.LoginActivity;
import com.magic.demo_camera.MainActivity;
import com.magic.demo_camera.MyHouseActivity;
import com.magic.demo_camera.MyTourActivity;
import com.magic.demo_camera.R;
import com.magic.demo_camera.WaitUploadActivity;
import com.magic.utils.Config;
import com.magic.utils.Http;
import com.magic.utils.IsNetwork;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragment extends Fragment {
	EditText et_loadpasswd,et_newpasswd;
	Button btn_change,btn_back;
	ProgressDialog progressDialog;
	String telephone="";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		et_loadpasswd = (EditText) view.findViewById(R.id.edt_leftmenu_myhome_changpasswd_loadpasswd);
		et_newpasswd = (EditText) view.findViewById(R.id.edt_leftmenu_myhome_changpasswd_newpasswd);
		btn_change = (Button) view.findViewById(R.id.btn_leftmenu_myhome_changpasswd_change);
		btn_back = (Button) view.findViewById(R.id.btn_back);
		
		btn_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences userinfo = getActivity()
						.getSharedPreferences("userinfo",
								Context.MODE_PRIVATE);
				userinfo.edit().clear().commit(); 
				
				startActivity(new Intent(getActivity(),
						LoginActivity.class));
			}
			
		});
		
		btn_change.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_loadpasswd.getText().length() != 0) {
					if (et_newpasswd.getText().length() != 0) {
							progressDialog = new ProgressDialog(
									getActivity());
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
						} 
					else {
						Toast.makeText(getActivity(), "旧密码不能为空",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getActivity(), "新密码不能为空",
							Toast.LENGTH_LONG).show();
				}
			}
		
		});
	}
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i("login_requst", "修改密码读取数据开始");
			String string_loadpasswd = et_loadpasswd.getText().toString();
			String string_newpasswd = et_newpasswd.getText().toString();
			
//			获取用户的手机号
			SharedPreferences userinfo = getActivity().getSharedPreferences("userinfo",Context.MODE_PRIVATE);
			telephone=userinfo.getString("telephone", ""); 
			
			JSONObject jsonO = new JSONObject();
			try {
				jsonO.put("serviceName", "submitPwd");
				JSONObject jsonP = new JSONObject();
				jsonP.put("telephone",telephone);
				jsonP.put("password", string_newpasswd);
				jsonP.put("oldpassword", string_loadpasswd);
				jsonO.put("queryParameters",jsonP);
				
				
//				Log.e("userJson:",jsonO.toString());
			} catch (Exception e) {
				Toast.makeText(getActivity(),
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
				Log.i("login_requst", "我是修改密码返回状态。。。" + status);
				if (status == 1) {
					Log.i("login_requst", "我是修改密码返回状态1kaishi");
//					JSONObject j = getjson.getJSONObject("dataList");
//					Log.i("userid", "我是修改密码成功" + j.toString());
//					String telephone = j.getString("telephone");
//					int vid = j.getInt("vid");
//					SharedPreferences userinfo = getActivity()
//							.getSharedPreferences("userinfo",
//									Context.MODE_PRIVATE);
//					Editor editor = userinfo.edit();
//					editor.putString("telephone", telephone);
//					editor.putInt("vid", vid);
//					Log.i("userid", "我是修改密码ID...." + vid + telephone);
//					editor.commit();
					String comment = getjson.getString("comment");
					Toast.makeText(getActivity(), comment,
							Toast.LENGTH_LONG).show();

					getActivity().finish();

					SharedPreferences userinfo = getActivity()
							.getSharedPreferences("userinfo",
									Context.MODE_PRIVATE);
					userinfo.edit().clear().commit(); 
					startActivity(new Intent(getActivity(),
							LoginActivity.class));
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					intent.putExtra("phone", telephone);
					startActivity(intent);
				} else {
					String comment = getjson.getString("comment");
					Toast.makeText(getActivity(), comment,
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getActivity(),
						"获取数据失败:" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressDialog.dismiss();
		};
	};
}
