package com.ec.dto;

public class Device {

    private int deviceID;

    private String uniqueCode;

    private String centerName;

    public Device(int deviceID, String uniqueCode, String centerName) {
	super();
	this.deviceID = deviceID;
	this.uniqueCode = uniqueCode;
	this.centerName = centerName;
    }

    public int getDeviceID() {
	return deviceID;
    }

    public void setDeviceID(int deviceID) {
	this.deviceID = deviceID;
    }

    public String getUniqueCode() {
	return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
	this.uniqueCode = uniqueCode;
    }

    public String getCenterName() {
	return centerName;
    }

    public void setCenterName(String centerName) {
	this.centerName = centerName;
    }

    @Override
    public String toString() {
	return "Device [deviceID=" + deviceID + ", uniqueCode=" + uniqueCode + ", centerName=" + centerName + "]";
    }

}
