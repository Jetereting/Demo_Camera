package com.theta360.sample.v2.network;

/**
 * HTTP通信事件监听器类 HTTP communication event listener class
 */
public interface HttpEventListener {
	/**
	 * 通知你的设备状态检查结果 Notifies you of the device status check results
	 * 
	 * @param newStatus
	 *            true:Update available, false;No update available
	 */
	void onCheckStatus(boolean newStatus);

	/**
	 * 保存文件时通知你 Notifies you when the file is saved
	 * 
	 * @param latestCapturedFileId
	 *            ID of saved file
	 */
	void onObjectChanged(String latestCapturedFileId);

	/**
	 * 事件完成通知 Notify on completion of event
	 */
	void onCompleted();

	/**
	 * 发生错误时通知 Notify in the event of an error
	 */
	void onError(String errorMessage);
}
