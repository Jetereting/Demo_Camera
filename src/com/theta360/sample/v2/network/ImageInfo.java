package com.theta360.sample.v2.network;

/**
 * 媒体文件信息类
 * Information class of media file
 */
public class ImageInfo {
    public static String FILE_FORMAT_CODE_EXIF_JPEG = "JPEG";
    public static String FILE_FORMAT_CODE_EXIF_MPEG = "MPEG";

    private String mFileName;
    private String mFileId;
    private long mFileSize;
    private String mCaptureDate;
    private String mFileFormat;
    private int mWidth;
    private int mHeight;

    /**
     * 获取文件名
     * Acquire file name
     * @return File name
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * 设置文件名
     * Set file name
     * @param fileName File name
     */
    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    /**
     * 获取文件标识
     * Acquire File ID
     * @return File ID
     */
    public String getFileId() {
        return mFileId;
    }

    /**
     * 设置文件标识
     * Set File ID
     * @param fileId File ID
     */
    public void setFileId(String fileId) {
        mFileId = fileId;
    }

    /**
     * 获取文件大小
     * Acquire file size
     * @return File size (unit: bytes)
     */
    public long getFileSize() {
        return mFileSize;
    }

    /**
     * 设置文件大小
     * Set file size
     * @param fileSize File size (unit: bytes)
     */
    public void setFileSize(long fileSize) {
        mFileSize = fileSize;
    }

    /**
     * 获得拍摄时间
     * Acquire shooting time
     * @return Shooting time
     */
    public String getCaptureDate() {
        return mCaptureDate;
    }

    /**
     * 设置拍摄时间
     * Set shooting time
     * @param captureDate Shooting time
     */
    public void setCaptureDate(String captureDate) {
        mCaptureDate = captureDate;
    }

    /**
     * 获取媒体格式
     * Acquire media format
     * @return Media format
     */
    public String getFileFormat() {
        return mFileFormat;
    }

    /**
     * 设置媒体格式
     * Set media format<p>
     * Set {@link ImageInfo#FILE_FORMAT_CODE_EXIF_JPEG} or {@link ImageInfo#FILE_FORMAT_CODE_EXIF_MPEG}.
     * @param fileFormat Media format
     */
    public void setFileFormat(String fileFormat) {
        mFileFormat = fileFormat;
    }

    /**
     * 获取图像宽度
     * Acquire image width
     * @return Image width
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * 设置图像宽度
     * Set image width
     * @param width Image width
     */
    public void setWidth(int width) {
        mWidth = width;
    }

    /**
     * 获取图像高度
     * Acquire image height
     * @return Image height
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * 设置图像高度
     * Set image height
     * @param height Image height
     */
    public void setHeight(int height) {
        mHeight = height;
    }
}
