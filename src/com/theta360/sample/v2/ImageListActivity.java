package com.theta360.sample.v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.magic.demo_camera.R;
import com.magic.upload.util.FileUtils;
import com.theta360.sample.v2.model.ImageSize;
import com.theta360.sample.v2.network.DeviceInfo;
import com.theta360.sample.v2.network.HttpConnector;
import com.theta360.sample.v2.network.HttpEventListener;
import com.theta360.sample.v2.network.ImageInfo;
import com.theta360.sample.v2.network.StorageInfo;
import com.theta360.sample.v2.view.ImageListArrayAdapter;
import com.theta360.sample.v2.view.ImageRow;
import com.theta360.sample.v2.view.ImageSizeDialog;
import com.theta360.sample.v2.view.LogView;
import com.theta360.sample.v2.view.MJpegInputStream;
import com.theta360.sample.v2.view.MJpegView;


/**
 *  相机主活动 显示照片列表的活动  
 * Activity that displays the photo list
 */
@SuppressLint("NewApi") 
public class ImageListActivity extends Activity implements ImageSizeDialog.DialogBtnListener {
	private String cameraIpAddress;

	private Button btnShoot;
	private ImageSize currentImageSize;
	private MJpegView mMv;
	private boolean mConnectionSwitchEnabled = false;

	private LoadObjectListTask sampleTask = null;
	private ShowLiveViewTask livePreviewTask = null;
	private GetImageSizeTask getImageSizeTask = null;
	private String latestCapturedFileId;

	/**
	 * onCreate Method
	 * @param savedInstanceState onCreate Status value
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ricoh_activity_object_list);

		cameraIpAddress = getResources().getString(R.string.theta_ip_address);
		getActionBar().setTitle(cameraIpAddress);

		btnShoot = (Button) findViewById(R.id.btn_shoot);
		btnShoot.setText("点击右上角切换至ON打开相机");
		btnShoot.setEnabled(false);
		btnShoot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("拍照".equals(btnShoot.getText().toString())){
					btnShoot.setEnabled(false);
					//拍摄任务
					new ShootTask().execute();
				}else if("上传".equals(btnShoot.getText().toString())){
					new GetThumbnailTask(latestCapturedFileId).execute();
				}
			}
		});

		mMv = (MJpegView) findViewById(R.id.live_view);

		forceConnectToWifi();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMv.stopPlay();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMv.play();

		if (livePreviewTask != null) {
			livePreviewTask.cancel(true);
			livePreviewTask = new ShowLiveViewTask();
			livePreviewTask.execute(cameraIpAddress);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == GLPhotoActivity.REQUEST_REFRESH_LIST) {
			if (sampleTask != null) {
				sampleTask.cancel(true);
			}
			sampleTask = new LoadObjectListTask();
			sampleTask.execute();
		}
	}

	@Override
	protected void onDestroy() {
		if (sampleTask != null) {
			sampleTask.cancel(true);
		}

		if (livePreviewTask != null) {
			livePreviewTask.cancel(true);
		}

		if (getImageSizeTask != null) {
			getImageSizeTask.cancel(true);
		}

		super.onDestroy();
	}

	/**
	 * onCreateOptionsMenu Method
	 * @param menu Menu initialization object
	 * @return Menu display feasibility status value
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.connection, menu);

		Switch connectionSwitch = (Switch) menu.findItem(R.id.connection).getActionView().findViewById(R.id.connection_switch);
		connectionSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					btnShoot.setText("拍照");
					btnShoot.setEnabled(true);

					if (sampleTask == null) {
						sampleTask = new LoadObjectListTask();
						sampleTask.execute();
					}

					if (livePreviewTask == null) {
						livePreviewTask = new ShowLiveViewTask();
						livePreviewTask.execute(cameraIpAddress);
					}

					if (getImageSizeTask == null) {
						getImageSizeTask = new GetImageSizeTask();
						getImageSizeTask.execute();
					}
				} else {
					btnShoot.setText("点击右上角切换至ON打开相机");
					btnShoot.setEnabled(false);

					if (sampleTask != null) {
						sampleTask.cancel(true);
						sampleTask = null;
					}

					if (livePreviewTask != null) {
						livePreviewTask.cancel(true);
						livePreviewTask = null;
					}

					if (getImageSizeTask != null) {
						getImageSizeTask.cancel(true);
						getImageSizeTask = null;
					}

					new DisConnectTask().execute();
					mMv.stopPlay();
				}
			}
		});
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Switch connectionSwitch = (Switch) menu.findItem(R.id.connection).getActionView().findViewById(R.id.connection_switch);
		if (!mConnectionSwitchEnabled) {
			connectionSwitch.setChecked(false);
		}
		connectionSwitch.setEnabled(mConnectionSwitchEnabled);
		return true;
	}

	private void changeCameraStatus(final int resid) {
		runOnUiThread(new Runnable() {
			public void run() {
			}
		});
	}

	private void appendLogView(final String log) {
		runOnUiThread(new Runnable() {
			public void run() {
			}
		});
	}

	@Override
	public void onDialogCommitClick(ImageSize imageSize) {
		currentImageSize = imageSize;
		new ChangeImageSizeTask().execute(currentImageSize);
	}

	private class ChangeImageSizeTask extends AsyncTask<ImageSize, String, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(ImageSize... size) {
			publishProgress("set image size to " + size[0].name());
			HttpConnector camera = new HttpConnector(cameraIpAddress);
			camera.setImageSize(size[0]);

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}

		@Override
		protected void onPostExecute(Void aVoid) {
		}
	}

	private class GetImageSizeTask extends AsyncTask<Void, String, ImageSize> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ImageSize doInBackground(Void... params) {
			publishProgress("get current image size");
			HttpConnector camera = new HttpConnector(cameraIpAddress);
			ImageSize imageSize = camera.getImageSize();

			return imageSize;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}

		@Override
		protected void onPostExecute(ImageSize imageSize) {
			if (imageSize != null) {
				currentImageSize = imageSize;
			} else {
			}
		}
	}


	private class ShowLiveViewTask extends AsyncTask<String, String, MJpegInputStream> {
		@Override
		protected MJpegInputStream doInBackground(String... ipAddress) {
			MJpegInputStream mjis = null;
			final int MAX_RETRY_COUNT = 20;

			for (int retryCount = 0; retryCount < MAX_RETRY_COUNT; retryCount++) {
				try {
					publishProgress("start Live view");
					HttpConnector camera = new HttpConnector(ipAddress[0]);
					InputStream is = camera.getLivePreview();
					mjis = new MJpegInputStream(is);
					retryCount = MAX_RETRY_COUNT;
				} catch (IOException e) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} catch (JSONException e) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}

			return mjis;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}

		@Override
		protected void onPostExecute(MJpegInputStream mJpegInputStream) {
			if (mJpegInputStream != null) {
				mMv.setSource(mJpegInputStream);
			} else {
			}
		}
	}

	private class LoadObjectListTask extends AsyncTask<Void, String, List<ImageRow>> {


		public LoadObjectListTask() {
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<ImageRow> doInBackground(Void... params) {
			try {
				publishProgress("------");
				publishProgress("connecting to " + cameraIpAddress + "...");
				HttpConnector camera = new HttpConnector(cameraIpAddress);
				changeCameraStatus(R.string.text_camera_standby);

				DeviceInfo deviceInfo = camera.getDeviceInfo();
				publishProgress("connected.");
				publishProgress(deviceInfo.getClass().getSimpleName() + ":<" + deviceInfo.getModel() + ", " + deviceInfo.getDeviceVersion() + ", " + deviceInfo.getSerialNumber() + ">");

				List<ImageRow> imageRows = new ArrayList<ImageRow>();

				StorageInfo storage = camera.getStorageInfo();
				ImageRow storageCapacity = new ImageRow();
				int freeSpaceInImages = storage.getFreeSpaceInImages();
				int megaByte = 1024 * 1024;
				long freeSpace = storage.getFreeSpaceInBytes() / megaByte;
				long maxSpace = storage.getMaxCapacity() / megaByte;
				storageCapacity.setFileName("Free space: " + freeSpaceInImages + "[shots] (" + freeSpace + "/" + maxSpace + "[MB])");
				imageRows.add(storageCapacity);

				ArrayList<ImageInfo> objects = camera.getList();
				int objectSize = objects.size();

				for (int i = 0; i < objectSize; i++) {
					ImageRow imageRow = new ImageRow();
					ImageInfo object = objects.get(i);
					imageRow.setFileId(object.getFileId());
					imageRow.setFileSize(object.getFileSize());
					imageRow.setFileName(object.getFileName());
					imageRow.setCaptureDate(object.getCaptureDate());
					publishProgress("<ImageInfo: File ID=" + object.getFileId() + ", filename=" + object.getFileName() + ", capture_date=" + object.getCaptureDate()
							+ ", image_pix_width=" + object.getWidth() + ", image_pix_height=" + object.getHeight() + ", object_format=" + object.getFileFormat()
							+ ">");

					if (object.getFileFormat().equals(ImageInfo.FILE_FORMAT_CODE_EXIF_JPEG)) {
						imageRow.setIsPhoto(true);
						Bitmap thumbnail = camera.getThumb(object.getFileId());
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
						final byte[] thumbnailImage = baos.toByteArray();
						imageRow.setThumbnail(thumbnailImage);
					} else {
						imageRow.setIsPhoto(false);
					}
					imageRows.add(imageRow);
					publishProgress("getList: " + (i + 1) + "/" + objectSize);
				}
				return imageRows;

			} catch (Throwable throwable) {
				String errorLog = Log.getStackTraceString(throwable);
				publishProgress(errorLog);
				return null;
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}

		@Override
		protected void onPostExecute(List<ImageRow> imageRows) {
			if (imageRows != null) {
				String info = imageRows.get(0).getFileName();
				imageRows.remove(0);

				ImageListArrayAdapter imageListArrayAdapter = new ImageListArrayAdapter(ImageListActivity.this, R.layout.ricoh_listlayout_object, imageRows);

			} else {
			}

		}

		@Override
		protected void onCancelled() {
		}

	}

	private class DeleteObjectTask extends AsyncTask<String, String, Void> {

		@Override
		protected Void doInBackground(String... fileId) {
			publishProgress("start delete file");
			DeleteEventListener deleteListener = new DeleteEventListener();
			HttpConnector camera = new HttpConnector(getResources().getString(R.string.theta_ip_address));
			camera.deleteFile(fileId[0], deleteListener);

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}

		@Override
		protected void onPostExecute(Void aVoid) {
		}

		private class DeleteEventListener implements HttpEventListener {
			@Override
			public void onCheckStatus(boolean newStatus) {
				if (newStatus) {
					appendLogView("deleteFile:FINISHED");
				} else {
					appendLogView("deleteFile:IN PROGRESS");
				}
			}

			@Override
			public void onObjectChanged(String latestCapturedFileId) {
				appendLogView("delete " + latestCapturedFileId);
			}

			@Override
			public void onCompleted() {
				appendLogView("deleted.");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new LoadObjectListTask().execute();
					}
				});
			}

			@Override
			public void onError(String errorMessage) {
				appendLogView("delete error " + errorMessage);
			}
		}
	}

	private class DisConnectTask extends AsyncTask<Void, String, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				publishProgress("disconnected.");
				return true;

			} catch (Throwable throwable) {
				String errorLog = Log.getStackTraceString(throwable);
				publishProgress(errorLog);
				return false;
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}
	}

	private class ShootTask extends AsyncTask<Void, Void, HttpConnector.ShootResult> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected HttpConnector.ShootResult doInBackground(Void... params) {
			CaptureListener postviewListener = new CaptureListener();
			HttpConnector camera = new HttpConnector(getResources().getString(R.string.theta_ip_address));
			HttpConnector.ShootResult result = camera.takePicture(postviewListener);

			return result;
		}

		@Override
		protected void onPostExecute(HttpConnector.ShootResult result) {
			if (result == HttpConnector.ShootResult.FAIL_CAMERA_DISCONNECTED) {
			} else if (result == HttpConnector.ShootResult.FAIL_STORE_FULL) {
			} else if (result == HttpConnector.ShootResult.FAIL_DEVICE_BUSY) {
			} else if (result == HttpConnector.ShootResult.SUCCESS) {
				Toast.makeText(getApplicationContext(), "拍照成功！", Toast.LENGTH_SHORT).show();
			}
		}

		private class CaptureListener implements HttpEventListener {
			
			private boolean ImageAdd = false;

			@Override
			public void onCheckStatus(boolean newStatus) {
				if(newStatus) {
					appendLogView("takePicture:FINISHED");
				} else {
					appendLogView("takePicture:IN PROGRESS");
				}
			}

			@Override
			public void onObjectChanged(String latestCapturedFileId) {
				this.ImageAdd = true;
				latestCapturedFileId = latestCapturedFileId;
				appendLogView("ImageAdd:FileId " + latestCapturedFileId);
			}

			@Override
			public void onCompleted() {
//				appendLogView("CaptureComplete");
//				if (ImageAdd) {
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							btnShoot.setEnabled(true);
//							new GetThumbnailTask(latestCapturedFileId).execute();
//						}
//					});
//				}
				Toast.makeText(getApplicationContext(), "拍照完成！", Toast.LENGTH_SHORT).show();
				btnShoot.setText("上传");
				btnShoot.setEnabled(true);
			}

			@Override
			public void onError(String errorMessage) {
				appendLogView("CaptureError " + errorMessage);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btnShoot.setEnabled(true);
					}
				});
			}
		}

	}

	/**
	 * ````````````````/////////////////////////////图像处理
	 * @author Administrator
	 *
	 */
	private class GetThumbnailTask extends AsyncTask<Void, String, Void> {

		private String fileId;

		public GetThumbnailTask(String fileId) {
			this.fileId = fileId;
		}

		@Override  
		protected Void doInBackground(Void... params) {
			btnShoot.setEnabled(false);
			HttpConnector camera = new HttpConnector(getResources().getString(R.string.theta_ip_address));
			Bitmap thumbnail = camera.getThumb(fileId);
			if (thumbnail != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] thumbnailImage = baos.toByteArray();
				GLPhotoActivity.startActivityForResult(ImageListActivity.this, cameraIpAddress, fileId, thumbnailImage, true);
				//将照片存储到 sd上面去
				FileUtils.saveBitmap(thumbnail, System.currentTimeMillis()+"");
				Toast.makeText(ImageListActivity.this, "上传完成！", Toast.LENGTH_SHORT).show();
				btnShoot.setText("拍照");
				btnShoot.setEnabled(true);
			} else {
				publishProgress("failed to get file data.");
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			for (String log : values) {
			}
		}
	}

	/**
	 * Force this applicatioin to connect to Wi-Fi
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void forceConnectToWifi() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			if ((info != null) && info.isAvailable()) {
				NetworkRequest.Builder builder = new NetworkRequest.Builder();
				builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
				NetworkRequest requestedNetwork = builder.build();

				ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
					@Override
					public void onAvailable(Network network) {
						super.onAvailable(network);

						ConnectivityManager.setProcessDefaultNetwork(network);
						mConnectionSwitchEnabled = true;

						invalidateOptionsMenu();
						appendLogView("connect to Wi-Fi AP");
					}

					@Override
					public void onLost(Network network) {
						super.onLost(network);

						mConnectionSwitchEnabled = false;
						invalidateOptionsMenu();
						appendLogView("lost connection to Wi-Fi AP");
					}
				};
				cm.requestNetwork(requestedNetwork, callback);
			}
		} else {
			mConnectionSwitchEnabled = true;

			invalidateOptionsMenu();
		}
	}
}
