package com.theta360.sample.v2.network;

/**
 * HTTPͨ���¼��������� HTTP communication event listener class
 */
public interface HttpEventListener {
	/**
	 * ֪ͨ����豸״̬����� Notifies you of the device status check results
	 * 
	 * @param newStatus
	 *            true:Update available, false;No update available
	 */
	void onCheckStatus(boolean newStatus);

	/**
	 * �����ļ�ʱ֪ͨ�� Notifies you when the file is saved
	 * 
	 * @param latestCapturedFileId
	 *            ID of saved file
	 */
	void onObjectChanged(String latestCapturedFileId);

	/**
	 * �¼����֪ͨ Notify on completion of event
	 */
	void onCompleted();

	/**
	 * ��������ʱ֪ͨ Notify in the event of an error
	 */
	void onError(String errorMessage);
}
