package com.ec.dto;

import java.util.List;

public class User {

    private int id;
    private String fName;
    private String lName;
    private String userName;
    private String groupName;
    private String landingPage;
    private String centerID;
    private String password;
    private String email;
    private List<String> centerIds;
    
    public String getfName() {
        return fName;
    }
    public void setfName(String fName) {
        this.fName = fName;
    }
    public String getlName() {
        return lName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLandingPage() {
	return landingPage;
    }

    public void setLandingPage(String landingPage) {
	this.landingPage = landingPage;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<String> getCenterIds() {
        return centerIds;
    }
    public void setCenterIds(List<String> centerIds) {
        this.centerIds = centerIds;
    }
    public String getCenterID() {
        return centerID;
    }
    public void setCenterID(String centerID) {
        this.centerID = centerID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
	final int maxLen = 10;
	return "User [id=" + id + ", fName=" + fName + ", lName=" + lName + ", userName=" + userName + ", groupName=" + groupName
		+ ", landingPage=" + landingPage + ", centerID=" + centerID + ", password=" + password + ", email=" + email
		+ ", centerIds=" + (centerIds != null ? centerIds.subList(0, Math.min(centerIds.size(), maxLen)) : null) + "]";
    }
    
}
