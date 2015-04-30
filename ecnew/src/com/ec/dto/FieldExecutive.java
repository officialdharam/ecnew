package com.ec.dto;

public class FieldExecutive {

    private String name;
    private int centerID;
    private String centerName;
    private int id;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

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

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public FieldExecutive(String name, int centerID, String centerName, int id) {
	super();
	this.name = name;
	this.centerID = centerID;
	this.centerName = centerName;
	this.id = id;
    }

    @Override
    public String toString() {
	return "FieldExecutive [name=" + name + ", centerID=" + centerID + ", centerName=" + centerName + ", id=" + id + "]";
    }

}
