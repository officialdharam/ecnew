package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class CustomerEntity {
    
    @Id
    @Column(name = "CUSTOMERID")
    @GeneratedValue
    private int customerID;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "PHONE")
    private long phoneNumber;
    
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LASTUPDATED")
    private Date updatedAt;

    @Column(name = "PINCODE")
    private String pincode;

    public int getCustomerID() {
	return customerID;
    }

    public void setCustomerID(int customerID) {
	this.customerID = customerID;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public long getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Date getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
    }

    public String getPincode() {
	return pincode;
    }

    public void setPincode(String pincode) {
	this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
	return "CustomerEntity [customerID=" + customerID + ", firstName=" + firstName + ", lastName=" + lastName
		+ ", phoneNumber=" + phoneNumber + ", address=" + address + ", email=" + email + ", pincode=" + pincode + "]";
    }

    
}
