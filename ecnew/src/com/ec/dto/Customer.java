package com.ec.dto;

public class Customer {

    private long phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String pincode;

    public long getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
	this.phoneNumber = phoneNumber;
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

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getPincode() {
	return pincode;
    }

    public void setPincode(String pincode) {
	this.pincode = pincode;
    }

    @Override
    public String toString() {
	return "Customer [phoneNumber=" + phoneNumber + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
		+ email + ", address=" + address + ", pincode=" + pincode + "]";
    }

}
