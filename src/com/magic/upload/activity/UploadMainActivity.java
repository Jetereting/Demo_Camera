package com.magic.upload.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.magic.demo_camera.R;
import com.magic.upload.util.Bimp;
import com.magic.upload.util.FileUtils;
import com.magic.upload.util.ImageItem;
import com.magic.upload.util.PublicWay;
import com.magic.upload.util.Res;

@SuppressLint("ShowToast")
public class UploadMainActivity extends Activity {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;

	// 照片上传
	Button btn_upload_photo;
	// title
	Button btn_title_back;
	TextView tev_title_content;

	ProgressDialog pd;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.imgupload_icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(
				R.layout.imgupload_activity_selectimg, null);
		setContentView(parentView);
		Init();

		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		btn_title_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		tev_title_content = (TextView) findViewById(R.id.tev_title_content);
		tev_title_content.setText("上传图片");

		btn_upload_photo = (Button) findViewById(R.id.btn_upload_img);
		btn_upload_photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				uploadImage(Bimp.tempSelectBitmap);
			}
		});

	}

	@SuppressWarnings("deprecation")
	public void Init() {

		pop = new PopupWindow(UploadMainActivity.this);

		View view = getLayoutInflater().inflate(
				R.layout.imgupload_item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UploadMainActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(
						R.anim.imgupload_activity_translate_in,
						R.anim.imgupload_activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		for (ImageItem iterable_element : Bimp.tempSelectBitmap) {
			System.out.println(iterable_element.getImagePath());
		}

		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							UploadMainActivity.this,
							R.anim.imgupload_activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(UploadMainActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.imgupload_item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(),
						R.drawable.imgupload_icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

	/**
	 * 上传 图片
	 * 
	 * @param list
	 */
	private void uploadImage(final ArrayList<ImageItem> list) {

		pd = ProgressDialog.show(UploadMainActivity.this, null, "正在查询......");
		
		Thread t =  new Thread(new Runnable(){

			@Override
			public void run() {
				
				DefaultHttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求 
				HttpPost post = new HttpPost("http://120.24.249.33/uploadfile/FileUploadServlet");//创建 HTTP POST 请求  
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//						builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
//				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
				int count=0;
				File [] files = null;
				for (int i = 0; i < list.size(); i++) {
					files[i]=new File(list.get(i).getImagePath());
				}
				for (File file:files) {
//							FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
//							builder.addPart("file"+count, fileBody);
					builder.addBinaryBody("file"+count, file);
					count++;
				}		
				//builder.addTextBody("method", params.get("method"));//设置请求参数
				//builder.addTextBody("fileTypes", params.get("fileTypes"));//设置请求参数
				HttpEntity entity = builder.build();// 生成 HTTP POST 实体  	
				post.setEntity(entity);//设置请求参数
				HttpResponse response = null;
				try {
					response = client.execute(post);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 发起请求 并返回请求的响应
				if (response.getStatusLine().getStatusCode()==200) {
					//return true;
				}
				//return false;					
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
//				HttpUtils httpUtils = new HttpUtils();
//				RequestParams params = new RequestParams();
//				params.setHeader("enctype","multipart/form-data");
////				params.setContentType("multipart/form-data;boundary=" + java.util.UUID.randomUUID().toString());
//				params.addQueryStringParameter("method", "upload");
//				for (int i = 0; i < list.size(); i++) {
//					params.addQueryStringParameter("file" + i, list.get(i)
//							.getImagePath());
//					System.out.println(list.get(i).getImagePath()
//							+ "--------------------------");
//				}
////				String url = "http://192.168.1.101:8080/Demo_imageForAndroid/servlet/MyServlet";
////				String url = "http://120.24.249.33/fileUpload/file_upload";
//				String url = "http://120.24.249.33/uploadfile/FileUploadServlet";
//				httpUtils.send(HttpMethod.POST, url, params,
//						
//						new RequestCallBack<String>() {
//
//							@Override
//							public void onFailure(HttpException arg0, String arg1) {
//								pd.dismiss();
//								Toast.makeText(UploadMainActivity.this, "上传失败", 1)
//										.show();
//							}
//
//							@Override
//							public void onSuccess(ResponseInfo<String> arg0) {
//								System.out.println(arg0.result);
//								pd.dismiss();
//								Toast.makeText(UploadMainActivity.this, "上传成功", 1)
//										.show();
//							}
//						});
			}
			
		});
		
		t.start();
		
		
	}

}
