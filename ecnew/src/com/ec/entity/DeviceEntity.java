package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class DeviceEntity {

    @Id
    @Column(name = "DEVICEID")
    @GeneratedValue
    private int deviceID;

    @Column(name = "CENTERID")
    private int centerID;

    @Column(name = "STATUS")
    private String active;
    
    @Column(name = "ASSIGNED")
    private String assigned;
    
    @Column(name = "UNIQUECODE")
    private String code;
    
    @Column(name = "CREATEDAT")
    private Date createdAt;
    
    @Column(name = "UPDATEDAT")
    private Date updatedAt;

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getCenterID() {
        return centerID;
    }

    public void setCenterID(int centerID) {
        this.centerID = centerID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
	return "DeviceEntity [deviceID=" + deviceID + ", centerID=" + centerID + ", active=" + active + ", assigned=" + assigned
		+ ", code=" + code + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
