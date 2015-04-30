package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pincode")
public class PincodeEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "CENTERID")
    private int centerID;

    @Column(name = "PINCODE")
    private String pincode;
    
    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getCenterID() {
	return centerID;
    }

    public void setCenterID(int centerID) {
	this.centerID = centerID;
    }

    public String getPincode() {
	return pincode;
    }

    public void setPincode(String pincode) {
	this.pincode = pincode;
    }

    @Override
    public String toString() {
	return "PincodeEntity [id=" + id + ", centerID=" + centerID + ", pincode=" + pincode + "]";
    }

}
