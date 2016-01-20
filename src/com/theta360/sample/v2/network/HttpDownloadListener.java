package com.theta360.sample.v2.network;

/**
 * HTTP通信下载者类 HTTP communication download listener class
 */
public interface HttpDownloadListener {
	/**
	 * 总字节数 Total byte count
	 */
	void onTotalSize(long totalSize);

	/**
	 * 接收字节计数 Received byte count
	 */
	void onDataReceived(int size);
}
