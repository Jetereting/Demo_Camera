package com.theta360.sample.v2.network;

/**
 * �豸��Ϣ�� Device information class
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
	 * ��ȡģ������ Acquire model name
	 * 
	 * @return Model name
	 */
	public String getModel() {
		return mModel;
	}

	/**
	 * ����ģ������ Set model name
	 * 
	 * @param model
	 *            Model name
	 */
	public void setModel(String model) {
		mModel = model;
	}

	/**
	 * ������к� Acquire serial number
	 * 
	 * @return Serial number
	 */
	public String getSerialNumber() {
		return mSerialNumber;
	}

	/**
	 * �������к� Set serial number
	 * 
	 * @param serialNumber
	 *            Serial number
	 */
	public void setSerialNumber(String serialNumber) {
		mSerialNumber = serialNumber;
	}

	/**
	 * ��ù̼��汾 Acquire firmware version
	 * 
	 * @return Firmware version
	 */
	public String getDeviceVersion() {
		return mDeviceVersion;
	}

	/**
	 * ���ù̼��汾 Set firmware version
	 * 
	 * @param version
	 *            Firmware version
	 */
	public void setDeviceVersion(String version) {
		mDeviceVersion = version;
	}
}
