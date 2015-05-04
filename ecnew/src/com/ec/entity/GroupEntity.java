package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    @Column(name = "GROUPID")
    @GeneratedValue
    private int id;

    @Column(name = "GROUPNAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATEDAT")
    private Date createdAt;

    @Column(name = "UPDATEDAT")
    private Date updatedAt;

    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "PRIORITY")
    private int priority;
    
    @Column(name = "LANDING")
    private String landingPage;
    
    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
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

    public String getActive() {
	return active;
    }

    public void setActive(String active) {
	this.active = active;
    }

    public int getPriority() {
	return priority;
    }

    public void setPriority(int priority) {
	this.priority = priority;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    @Override
    public String toString() {
	return "groups [id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt
		+ ", updatedAt=" + updatedAt + ", active=" + active + ", priority=" + priority + ", landingPage=" + landingPage
		+ "]";
    }

}
