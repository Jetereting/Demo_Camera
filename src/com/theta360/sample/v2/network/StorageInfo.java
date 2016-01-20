package com.theta360.sample.v2.network;

/**
 * 设备存储信息类 Information class of device storage
 */
public class StorageInfo {
	int mRemainingPictures = 0;
	long mRemainingSpace = 0;
	long mTotalSpace = 0;

	/**
	 * 获得可以拍摄的图像的剩余数量 Acquire remaining number of images that can be shot
	 * 
	 * @return Remaining number of images that can be shot
	 */
	public int getFreeSpaceInImages() {
		return mRemainingPictures;
	}

	/**
	 * 设置可以拍摄的图像的剩余数量 Set remaining number of images that can be shot
	 * 
	 * @param remainingPictures
	 *            Remaining number of images that can be shot
	 */
	public void setFreeSpaceInImages(int remainingPictures) {
		mRemainingPictures = remainingPictures;
	}

	/**
	 * 获得剩余容量 Acquire remaining capacity
	 * 
	 * @return Remaining capacity (unit: bytes)
	 */
	public long getFreeSpaceInBytes() {
		return mRemainingSpace;
	}

	/**
	 * 设置剩余容量 Set remaining capacity
	 * 
	 * @param remainingSpace
	 *            Remaining capacity (unit: bytes)
	 */
	public void setFreeSpaceInBytes(long remainingSpace) {
		mRemainingSpace = remainingSpace;
	}

	/**
	 * 获得设备总容量 Acquire total capacity of device
	 * 
	 * @return Total capacity of device (unit: bytes)
	 */
	public long getMaxCapacity() {
		return mTotalSpace;
	}

	/**
	 * 装置总容量 Set total capacity of device
	 * 
	 * @param totalSpace
	 *            Total capacity of device (unit: bytes)
	 */
	public void setMaxCapacity(long totalSpace) {
		mTotalSpace = totalSpace;
	}
}
