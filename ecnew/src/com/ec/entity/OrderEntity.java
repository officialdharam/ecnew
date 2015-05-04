package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "ORDERID")
    @GeneratedValue
    private int orderID;

    @Column(name = "CUSTOMERID")
    private int customerID;
    
    @Column(name = "CENTERID")
    private int centerID;

    @Column(name = "DEVICEID")
    private int deviceID;

    @Column(name = "COMMENTS")
    private String specialComments;

    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "UPDATEDBY")
    private String updatedBy;
    
    @Column(name = "BILLAMOUNT")
    private float amount;

    @Column(name = "ORDERDATE")
    private Date orderDate;

    @Column(name = "PICKUPDATE")
    private Date pickUpDate;

    @Column(name = "UPDATEDATE")
    private Date lastUpdated;

    @Column(name = "PICKUPTIME")
    private String pickupTime;

    public int getOrderID() {
	return orderID;
    }

    public String getSpecialComments() {
	return specialComments;
    }

    public void setSpecialComments(String specialComments) {
	this.specialComments = specialComments;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Date getOrderDate() {
	return orderDate;
    }

    public void setOrderDate(Date orderDate) {
	this.orderDate = orderDate;
    }

    public Date getLastUpdated() {
	return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
	this.lastUpdated = lastUpdated;
    }

    public Date getPickUpDate() {
	return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
	this.pickUpDate = pickUpDate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getCenterID() {
        return centerID;
    }

    public void setCenterID(int centerID) {
        this.centerID = centerID;
    }

    @Override
    public String toString() {
	return "orders [customerID=" + customerID + ", centerID=" + centerID + ", status=" + status + ", orderDate="
		+ orderDate + ", pickUpDate=" + pickUpDate + ", pickupTime=" + pickupTime + "]";
    }

}
