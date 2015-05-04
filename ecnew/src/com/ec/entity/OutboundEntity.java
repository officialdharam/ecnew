package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "outbound")
public class OutboundEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Column(name = "CENTER_ID")
    private int centerId;

    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    @Column(name = "QUANTITY")
    private float quantity;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getCenterId() {
	return centerId;
    }

    public void setCenterId(int centerId) {
	this.centerId = centerId;
    }

    public Date getLastUpdated() {
	return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
	this.lastUpdated = lastUpdated;
    }

    public float getQuantity() {
	return quantity;
    }

    public void setQuantity(float quantity) {
	this.quantity = quantity;
    }

    @Override
    public String toString() {
	return "outbound [id=" + id + ", categoryName=" + categoryName + ", centerId=" + centerId + ", lastUpdated="
		+ lastUpdated + ", quantity=" + quantity + "]";
    }

    public String getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

}
