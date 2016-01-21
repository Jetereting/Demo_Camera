package com.magic.demo_camera;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magic.adapter.LookShareAdpter;
import com.magic.adapter.LookShareAdpter.Callback;
import com.magic.fragment.AlbumFragment;
import com.magic.model.WaitUploadModel;
import com.magic.utils.CombineBitmap;
import com.magic.utils.Config;
import com.magic.utils.GetBitmapFromUrl;
import com.magic.utils.Http;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class LookShareActivity extends Activity implements Callback {

	Button btn_title_back;
	TextView tev_title_content;
	ListView listView;
	LookShareAdpter mAdapter;
	List<WaitUploadModel> list = new ArrayList<WaitUploadModel>();

	String type;
	String path;
	String selectPath="";
	String telephone="";
	int photoID=0;
	String[] url;
	String urlList[];
	
	ProgressDialog progressDialog;

	// �Ի��� �ı�
	TextView tev_share, tev_look, tev_cancel;
	
	//���� url ������Ȧ
	private static final String APPID="wx682c11d1193ecf81";
	private IWXAPI iwxapi;
    
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lookshare);
		
//		start ���NetworkOnMainThreadException
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads().detectDiskWrites().detectNetwork()  
        .penaltyLog().build());  
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()  
        .penaltyLog().penaltyDeath().build());  
//		end ���NetworkOnMainThreadException
		
		iwxapi=WXAPIFactory.createWXAPI(this, APPID);
		initView();
		
		type = getIntent().getStringExtra("type");
		if (type.equals("trip_share")) {
		    mAdapter = new LookShareAdpter(this, list,1,this);
		    listView.setAdapter(mAdapter);
//			�б�ѡ��
			tev_title_content.setText("���η���");
		} else {
		    mAdapter = new LookShareAdpter(this, list,2,this);
		    listView.setAdapter(mAdapter);
//			�б�ѡ��
			tev_title_content.setText("��������");
		}
		progressDialog = new ProgressDialog(
				LookShareActivity.this);
		// ���ý�������񣬷��ΪԲ�Σ���ת��
		progressDialog
				.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// ����ProgressDialog ����
		// progressDialog.setTitle("���ȶԻ���");
		// ����ProgressDialog ��ʾ��Ϣ
		progressDialog.setMessage("���ڻ�ȡ����...");
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
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.listview_lookshare);
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		tev_title_content.setText("�鿴����");
		btn_title_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}


	public void showDialog(Context context, final Object object) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_custom, null);
		final Dialog dialog = new Dialog(this, R.style.myDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		dialog.show();

		tev_share = (TextView) view.findViewById(R.id.tev_dialog_share);
		tev_look = (TextView) view.findViewById(R.id.tev_dialog_look);
		tev_cancel = (TextView) view.findViewById(R.id.tev_dialog_cancel);

		tev_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				//����΢������Ȧ
				shareToWX("http://www.baidu.com");
			}
		});

		tev_look.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// �鿴
				dialog.dismiss();
				System.out.println("-------------------------look");

			}
		});

		tev_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
	}
	
	private void shareToWX(String url){
		//���ڷ�װҪ���͵�url
		WXWebpageObject wxWebpageObject=new WXWebpageObject();
		wxWebpageObject.webpageUrl=url;
		//����һ���������� ����android����΢�ŷ�����Ϣ
		WXMediaMessage wxMediaMessage=new WXMediaMessage(wxWebpageObject);
		wxMediaMessage.title="title";
		wxMediaMessage.description="desc";
		//��������ͼ
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		wxMediaMessage.thumbData=bitmap.getNinePatchChunk();
		//�������Ͷ���
		SendMessageToWX.Req req=new SendMessageToWX.Req();
		//���ñ�ʶ
		req.transaction=(System.currentTimeMillis()+"webpage");
		req.message=wxMediaMessage;
		req.scene=SendMessageToWX.Req.WXSceneTimeline;
	    iwxapi.sendReq(req);
	}
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i("login_requst", "��ȡ���ݿ�ʼ");
			JSONObject jsonO = new JSONObject();
			try {
				SharedPreferences userinfo = LookShareActivity.this
						.getSharedPreferences("userinfo",
								Context.MODE_PRIVATE);
				telephone=userinfo.getString("telephone","");
				jsonO.put("serviceName", "findMyGoodsOrder2");
				JSONObject jsonP = new JSONObject();
				jsonP.put("telphone", telephone);
				if (type.equals("trip_share")) {
					jsonP.put("type", 1);
				} else {
					jsonP.put("type", 2);
				}
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
				Log.i("requst","requst+"+e.toString());
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
					Log.i("userid", "���ǻ�ȡͼƬchenggong" + j);
					path = j.getString("path");
					String url11 = j.getString("url");
					urlList=url11.split(";");
					url = path.split(";");
					for(int i=0;i<url.length;i++){
						String photoPath[]=url[i].split(",");
						Bitmap bitmap = null,bitmap1 = null,bitmapWrite=null;
						bitmapWrite =((BitmapDrawable)getResources().getDrawable(R.drawable.write)).getBitmap();
						for(int j1=0;j1<photoPath.length;j1++){
							bitmap1=GetBitmapFromUrl.getBitmap(photoPath[j1]);
							if(photoPath.length>1&&j1<=photoPath.length-2){
								bitmap1=CombineBitmap.combine(bitmapWrite, bitmap1, 0, 50);
								bitmap=CombineBitmap.combine(bitmap1,GetBitmapFromUrl.getBitmap(photoPath[j1+1]),440,50);
							}else if(photoPath.length==1){
								bitmap=bitmap1;
							}
						}
						list.add(new WaitUploadModel(url[i]+"",bitmap));
						mAdapter.notifyDataSetChanged();
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
			progressDialog.dismiss();
		};
	};


	@Override
	public void click(View v) {
		switch(v.getId()){
		case R.id.button:
			Intent intent=new Intent();
			intent.putExtra("url", urlList[(Integer) v.getTag()]);
			intent.setClass(getApplicationContext(), ShowShareActivity.class);
			startActivity(intent);
			break;
		case R.id.button1:
			Intent intent1=new Intent();
			intent1.putExtra("page",1);
			intent1.setClass(getApplicationContext(), MainActivity.class);
			startActivity(intent1);
		}
		
	}
	

}
