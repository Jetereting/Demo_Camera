package com.theta360.sample.v2.network;

/**
 * 设备信息类 Device information class
 */
public class DeviceInfo {
	private String mModel = "";
	private String mDeviceVersion = "";
	private String mSerialNumber = "";

	/**
	 * Constructor
	 */
	public DeviceInfo() {
	}

	/**
	 * 获取模型名称 Acquire model name
	 * 
	 * @return Model name
	 */
	public String getModel() {
		return mModel;
	}

	/**
	 * 设置模型名称 Set model name
	 * 
	 * @param model
	 *            Model name
	 */
	public void setModel(String model) {
		mModel = model;
	}

	/**
	 * 获得序列号 Acquire serial number
	 * 
	 * @return Serial number
	 */
	public String getSerialNumber() {
		return mSerialNumber;
	}

	/**
	 * 设置序列号 Set serial number
	 * 
	 * @param serialNumber
	 *            Serial number
	 */
	public void setSerialNumber(String serialNumber) {
		mSerialNumber = serialNumber;
	}

	/**
	 * 获得固件版本 Acquire firmware version
	 * 
	 * @return Firmware version
	 */
	public String getDeviceVersion() {
		return mDeviceVersion;
	}

	/**
	 * 设置固件版本 Set firmware version
	 * 
	 * @param version
	 *            Firmware version
	 */
	public void setDeviceVersion(String version) {
		mDeviceVersion = version;
	}
}
