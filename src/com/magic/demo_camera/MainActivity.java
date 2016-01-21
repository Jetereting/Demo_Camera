package com.magic.demo_camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.magic.fragment.AlbumFragment;
import com.magic.fragment.CameraFragment;
import com.magic.fragment.EditFragment;
import com.magic.fragment.MyFragment;
import com.theta360.sample.v2.ImageListActivity;

/**
 * 主活动
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	// tab按钮对象数组
	private Button[] tabs;
	// fragment对象数组
	private Fragment[] fragments;
	// fragment界面
	private Fragment fragment_edit, fragment_album, fragment_camera,
			fragment_my;
	// 当前tab下标
	private int currentTabIndex;

	private TextView tev_title_content;
	TextView tv_user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initTab();
		switch(getIntent().getIntExtra("page",0)){
		case 1:tabSelect(1);break;
		case 2:tabSelect(2);break;
		case 3:tabSelect(3);break;
		}
	}

	/**
	 * 控件初始化
	 */
	private void initView() {
		tev_title_content = (TextView) findViewById(R.id.tev_app_title);
		tabs = new Button[5];
		tabs[0] = (Button) findViewById(R.id.btn_main_tab_edit);
		tabs[1] = (Button) findViewById(R.id.btn_main_tab_album);
		tabs[2] = (Button) findViewById(R.id.btn_main_tab_camera);
		tabs[3] = (Button) findViewById(R.id.btn_main_tab_my);
		
		SharedPreferences userinfo = MainActivity.this
				.getSharedPreferences("userinfo",
						Context.MODE_PRIVATE);
		tv_user=(TextView)findViewById(R.id.tv_user);
		tv_user.setText(userinfo.getString("telephone", "")+"\t");
		
		// 把第一个tab设为选中状态
		tabs[0].setSelected(true);
		tabs[0].setOnClickListener(this);
		tabs[1].setOnClickListener(this);
		tabs[2].setOnClickListener(this);
		tabs[3].setOnClickListener(this);
	}

	/**
	 * 初始化fragments数据
	 */
	private void initTab() {
		fragment_edit = new EditFragment();
		fragment_album = new AlbumFragment();
		fragment_camera = new CameraFragment();
		fragment_my = new MyFragment();
		fragments = new Fragment[] { fragment_edit, fragment_album,
				fragment_camera, fragment_my };
		// 添加显示第一个fragment
		getFragmentManager().beginTransaction()
				.add(R.id.fragment_container, fragment_edit).commit();
	}

	/**
	 * 切换fragment
	 * 
	 * @param index
	 */
	public void tabSelect(int index) {
		if (currentTabIndex != index) {
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				transaction.add(R.id.fragment_container, fragments[index]);
			}
			transaction.show(fragments[index]).commit();
		}
		tabs[currentTabIndex].setSelected(false);
		tabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_main_tab_edit:
			tev_title_content.setText("首页");
			tabSelect(0);
			break;
		case R.id.btn_main_tab_album:
			tev_title_content.setText("点击图片上传");
			tabSelect(1);
			break;
		case R.id.btn_main_tab_camera:
			tev_title_content.setText("相机");
			tabSelect(2);
			break;
		case R.id.btn_main_tab_my:
			tev_title_content.setText("我的");
			tabSelect(3);
			break;
		}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//		 if (keyCode == KeyEvent.KEYCODE_BACK) {
//	            new AlertDialog.Builder(this).setTitle("确认退出吗？")
//	                    .setIcon(android.R.drawable.ic_dialog_info)
//	                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//
//	                        @Override
//	                        public void onClick(DialogInterface dialog, int which) {
//	                            // 点击“确认”后的操作
	                            Intent home = new Intent(Intent.ACTION_MAIN);
	                            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                            home.addCategory(Intent.CATEGORY_HOME);
	                            startActivity(home);
//	                        }
//	                    })
//	                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
//	                        @Override
//	                        public void onClick(DialogInterface dialog, int which) {
//	                            // 点击“返回”后的操作,这里不设置没有任何操作
//	                        }
//	                    }).show();
//	        }
	        return true;
	}

}
