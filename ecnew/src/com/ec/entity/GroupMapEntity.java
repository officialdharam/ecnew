package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groupmap")
public class GroupMapEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "GROUPID")
    private int groupID;

    @Column(name = "USERID")
    private int userID;

    @Column(name = "UPDATEDAT")
    private Date updatedAt;

    @Column(name = "CREATEDAT")
    private Date createdAt;

    @Column(name = "ACTIVE")
    private String active;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getGroupID() {
	return groupID;
    }

    public void setGroupID(int groupID) {
	this.groupID = groupID;
    }

    public int getUserID() {
	return userID;
    }

    public void setUserID(int userID) {
	this.userID = userID;
    }

    public Date getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
	this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
    }

    public String getActive() {
	return active;
    }

    public void setActive(String active) {
	this.active = active;
    }

    @Override
    public String toString() {
	return "groupmap [id=" + id + ", groupID=" + groupID + ", userID=" + userID + ", updatedAt=" + updatedAt
		+ ", createdAt=" + createdAt + ", active=" + active + "]";
    }

}
