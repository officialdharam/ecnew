package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "center")
public class CenterEntity {
    @Id
    @Column(name = "CENTERID")
    @GeneratedValue
    private int centerID;

    @Column(name = "CENTERNAME")
    private String centerName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PINCODE")
    private String pincode;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ACTIVE")
    private String active;

    public int getCenterID() {
	return centerID;
    }

    public void setCenterID(int centerID) {
	this.centerID = centerID;
    }

    public String getCenterName() {
	return centerName;
    }

    public void setCenterName(String centerName) {
	this.centerName = centerName;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getPincode() {
	return pincode;
    }

    public void setPincode(String pincode) {
	this.pincode = pincode;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public String getActive() {
	return active;
    }

    public void setActive(String active) {
	this.active = active;
    }

    @Override
    public String toString() {
	return "center [centerID=" + centerID + ", centerName=" + centerName + ", address=" + address + ", city=" + city
		+ ", pincode=" + pincode + ", state=" + state + ", active=" + active + "]";
    }

}