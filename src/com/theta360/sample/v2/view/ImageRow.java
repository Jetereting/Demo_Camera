package com.theta360.sample.v2.view;

/**
 * 当照片列表显示时，行对象 Line object for list when photo list is displayed
 */
public class ImageRow {

	private String fileId;
	private long fileSize;
	private boolean isPhoto;
	private byte[] thumbnail;
	private String fileName;
	private String captureDate;

	/**
	 * 一种用于照片对象的标识值获取方法 Identifier value acquisition method for photo object
	 * 
	 * @return Handle value for photo object
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * 照片对象的标识值设置方法 Identifier value setting method for photo object
	 * 
	 * @param fileId
	 *            identifier value for photo object
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 获取文件大小 Acquire file size
	 * 
	 * @return File size
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * 设置文件大小 Set file size
	 * 
	 * @param fileSize
	 *            File size
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 照片信息，可行性，价值，获取方法 Photo information feasibility value acquisition method
	 * 
	 * @return Photo information feasibility value
	 */
	public boolean isPhoto() {
		return isPhoto;
	}

	/**
	 * 照片信息可行性价值设定方法 Photo information feasibility value setting method
	 * 
	 * @param isPhoto
	 *            Photo information feasibility value
	 */
	public void setIsPhoto(boolean isPhoto) {
		this.isPhoto = isPhoto;
	}

	/**
	 * 缩略图信息获取方法 Thumbnail information acquisition method
	 * 
	 * @return Thumbnail information
	 */
	public byte[] getThumbnail() {
		return thumbnail;
	}

	/**
	 * 缩略图信息设置方法 Thumbnail information setting method
	 * 
	 * @param thumbnail
	 *            Thumbnail information
	 */
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * 文件名获取方法 File name acquisition method
	 * 
	 * @return File name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 文件名设置方法 File name setting method
	 * 
	 * @param fileName
	 *            File name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 捕获日期和时间捕获方法 Capture date and time acquisition method
	 * 
	 * @return Capture date and time
	 */
	public String getCaptureDate() {
		return captureDate;
	}

	/**
	 * 捕获日期和时间设定方法 Capture date and time setting method
	 * 
	 * @param captureDate
	 *            Capture date and time
	 */
	public void setCaptureDate(String captureDate) {
		this.captureDate = captureDate;
	}
}
