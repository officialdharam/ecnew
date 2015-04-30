package com.ec.dto;

public class DeviceAssignment {
    private String device;
    private String name;
    private int fe;
    private int deviceID;
    private int assignmentID;

    public String getDevice() {
	return device;
    }

    public void setDevice(String device) {
	this.device = device;
    }

    public int getFe() {
	return fe;
    }

    public void setFe(int fe) {
	this.fe = fe;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getDeviceID() {
	return deviceID;
    }

    public void setDeviceID(int deviceID) {
	this.deviceID = deviceID;
    }

    public int getAssignmentID() {
	return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
	this.assignmentID = assignmentID;
    }

    @Override
    public String toString() {
	return "DeviceAssignment [device=" + device + ", name=" + name + ", fe=" + fe + ", deviceID=" + deviceID
		+ ", assignmentID=" + assignmentID + "]";
    }
}
