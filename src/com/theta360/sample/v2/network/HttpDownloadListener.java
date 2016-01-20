package com.theta360.sample.v2.network;

/**
 * HTTPͨ���������� HTTP communication download listener class
 */
public interface HttpDownloadListener {
	/**
	 * ���ֽ��� Total byte count
	 */
	void onTotalSize(long totalSize);

	/**
	 * �����ֽڼ��� Received byte count
	 */
	void onDataReceived(int size);
}
