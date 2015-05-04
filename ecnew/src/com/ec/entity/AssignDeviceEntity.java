package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deviceassignment")
public class AssignDeviceEntity {

    @Column(name = "DEVICEID")
    private int deviceID;

    @Column(name = "USERID")
    private int userID;

    @Column(name = "DATEASSIGNED")
    private Date assignmentDate;

    @Id
    @GeneratedValue
    @Column(name = "ASSIGNMENTID")
    private int id;

    public int getDeviceID() {
	return deviceID;
    }

    public void setDeviceID(int deviceID) {
	this.deviceID = deviceID;
    }

    public int getUserID() {
	return userID;
    }

    public void setUserID(int userID) {
	this.userID = userID;
    }

    public Date getAssignmentDate() {
	return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
	this.assignmentDate = assignmentDate;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    @Override
    public String toString() {
	return "deviceassignment [id=" + id + ", deviceID=" + deviceID + ", userID=" + userID + ", assignmentDate="
		+ assignmentDate + "]";
    }

}
