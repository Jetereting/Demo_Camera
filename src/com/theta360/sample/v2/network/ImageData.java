package com.theta360.sample.v2.network;

/**
 * ͼ��������
 * Image data class
 */
public class ImageData {
    private byte[] mRawData;
    private Double pitch = 0.0d;
    private Double roll = 0.0d;
    private Double yaw = 0.0d;

    /**
     * ��ȡͼ���ԭʼ����
     * Acquire raw data of image
     * @return Raw data of image
     */
    public byte[] getRawData() {
        return mRawData;
    }

    /**
     * ����ͼ���ԭʼ����
     * Set raw data of image
     * @param rawData Raw data of image
     */
    public void setRawData(byte[] rawData) {
        mRawData = rawData;
    }

    /**
     * ��ø�����
     * Acquire pitch angle
     * @return Pitch angle
     */
    public Double getPitch() {
        return pitch;
    }

    /**
     * ���ø�����
     * Set pitch angle
     * @param pitch Pitch angle (value must be between -90 and 90)
     */
    public void setPitch(Double pitch) {
        this.pitch = pitch;
    }

    /**
     * ��ù�ת��
     * Acquire roll angle
     * @return Roll angle
     */
    public Double getRoll() {
        return roll;
    }

    /**
     * ���ù���
     * Set roll angle
     * @param roll Roll angle (value must be between -180 and 180)
     */
    public void setRoll(Double roll) {
        this.roll = roll;
    }

    /**
     * ���ƫ����
     * Acquire yaw angle
     * @return Yaw angle
     */
    public Double getYaw() {
        return yaw;
    }

    /**
     * ����ƫ����
     * Set yaw angle
     * @param yaw Yaw angle (value must be between 0 and 360)
     */
    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }
}
