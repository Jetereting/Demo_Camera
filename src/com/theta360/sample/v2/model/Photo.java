package com.theta360.sample.v2.model;

import android.graphics.Bitmap;

/**
 * 照片对象存储类
 * Photo object storage class
 */
public class Photo {

    private Double mOrientationAngle;

    private Double mElevationAngle;
    private Double mHorizontalAngle;

    private Bitmap mPhoto;

    private Photo() {
    }

    /**
     * Constructor
     * @param photo Photo object
     */
    public Photo(Bitmap photo) {
        this(photo, null, null, null);
    }

    /**
     * Constructor
     * @param photo Photo object
     * @param orientationAngle Orientation angle
     * @param elevationAngle Elevation angle
     * @param horizontalAngle Horizontal angle
     */
    public Photo(Bitmap photo, Double orientationAngle, Double elevationAngle, Double horizontalAngle) {
        this();

        mOrientationAngle = orientationAngle;
        mElevationAngle = elevationAngle;
        mHorizontalAngle = horizontalAngle;

        mPhoto = photo;
    }

    /**
     * 获得方位角
     * Acquires the orientation angle
     * @return Orientation angle
     */
    public Double getOrientationAngle() {
        return mOrientationAngle;
    }

    /**
     * 获得仰角
     * Acquires the elevation angle
     * @return Elevation angle
     */
    public Double getElevetionAngle() {
        return mElevationAngle;
    }

    /**
     * 获得水平角
     * Acquires the horizontal angle
     * @return Horizontal angle
     */
    public Double getHorizontalAngle() {
        return mHorizontalAngle;
    }

    /**
     * 获取照片对象
     * Acquires the photo object
     * @return Photo object
     */
    public Bitmap getPhoto() {
        return mPhoto;
    }

    /**
     * 更新照片对象
     * Updates the photo object
     * @param drawable Photo object
     */
    public void updatePhoto(Bitmap drawable) {
        mPhoto = drawable;
    }

}