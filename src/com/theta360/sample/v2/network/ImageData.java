package com.theta360.sample.v2.network;

/**
 * 图像数据类
 * Image data class
 */
public class ImageData {
    private byte[] mRawData;
    private Double pitch = 0.0d;
    private Double roll = 0.0d;
    private Double yaw = 0.0d;

    /**
     * 获取图像的原始数据
     * Acquire raw data of image
     * @return Raw data of image
     */
    public byte[] getRawData() {
        return mRawData;
    }

    /**
     * 设置图像的原始数据
     * Set raw data of image
     * @param rawData Raw data of image
     */
    public void setRawData(byte[] rawData) {
        mRawData = rawData;
    }

    /**
     * 获得俯仰角
     * Acquire pitch angle
     * @return Pitch angle
     */
    public Double getPitch() {
        return pitch;
    }

    /**
     * 设置俯仰角
     * Set pitch angle
     * @param pitch Pitch angle (value must be between -90 and 90)
     */
    public void setPitch(Double pitch) {
        this.pitch = pitch;
    }

    /**
     * 获得滚转角
     * Acquire roll angle
     * @return Roll angle
     */
    public Double getRoll() {
        return roll;
    }

    /**
     * 设置辊角
     * Set roll angle
     * @param roll Roll angle (value must be between -180 and 180)
     */
    public void setRoll(Double roll) {
        this.roll = roll;
    }

    /**
     * 获得偏航角
     * Acquire yaw angle
     * @return Yaw angle
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * 设置偏航角
     * Set yaw angle
     * @param yaw Yaw angle (value must be between 0 and 360)
     */
    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }
}
