package com.ec.dto;

public class Group {

    private String groupName;
    private int groupValue;

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public int getGroupValue() {
	return groupValue;
    }

    public void setGroupValue(int groupValue) {
	this.groupValue = groupValue;
    }

    @Override
    public String toString() {
	return "Group [groupName=" + groupName + ", groupValue=" + groupValue + "]";
    }

}
