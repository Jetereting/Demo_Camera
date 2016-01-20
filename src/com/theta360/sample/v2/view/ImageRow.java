package com.theta360.sample.v2.view;

/**
 * ����Ƭ�б���ʾʱ���ж��� Line object for list when photo list is displayed
 */
public class ImageRow {

	private String fileId;
	private long fileSize;
	private boolean isPhoto;
	private byte[] thumbnail;
	private String fileName;
	private String captureDate;

	/**
	 * һ��������Ƭ����ı�ʶֵ��ȡ���� Identifier value acquisition method for photo object
	 * 
	 * @return Handle value for photo object
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * ��Ƭ����ı�ʶֵ���÷��� Identifier value setting method for photo object
	 * 
	 * @param fileId
	 *            identifier value for photo object
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * ��ȡ�ļ���С Acquire file size
	 * 
	 * @return File size
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * �����ļ���С Set file size
	 * 
	 * @param fileSize
	 *            File size
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * ��Ƭ��Ϣ�������ԣ���ֵ����ȡ���� Photo information feasibility value acquisition method
	 * 
	 * @return Photo information feasibility value
	 */
	public boolean isPhoto() {
		return isPhoto;
	}

	/**
	 * ��Ƭ��Ϣ�����Լ�ֵ�趨���� Photo information feasibility value setting method
	 * 
	 * @param isPhoto
	 *            Photo information feasibility value
	 */
	public void setIsPhoto(boolean isPhoto) {
		this.isPhoto = isPhoto;
	}

	/**
	 * ����ͼ��Ϣ��ȡ���� Thumbnail information acquisition method
	 * 
	 * @return Thumbnail information
	 */
	public byte[] getThumbnail() {
		return thumbnail;
	}

	/**
	 * ����ͼ��Ϣ���÷��� Thumbnail information setting method
	 * 
	 * @param thumbnail
	 *            Thumbnail information
	 */
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * �ļ�����ȡ���� File name acquisition method
	 * 
	 * @return File name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * �ļ������÷��� File name setting method
	 * 
	 * @param fileName
	 *            File name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * �������ں�ʱ�䲶�񷽷� Capture date and time acquisition method
	 * 
	 * @return Capture date and time
	 */
	public String getCaptureDate() {
		return captureDate;
	}

	/**
	 * �������ں�ʱ���趨���� Capture date and time setting method
	 * 
	 * @param captureDate
	 *            Capture date and time
	 */
	public void setCaptureDate(String captureDate) {
		this.captureDate = captureDate;
	}
}
