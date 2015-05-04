package com.ec.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ordercontent")
public class OrderContentEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "ORDERID")
    private int orderID;

    @Column(name = "CATEGORYID")
    private int categoryID;

    @Column(name = "QUANTITY")
    private float quantity;
    
    @Column(name = "CREATEDATE")
    private Date createDate;

    @Column(name = "UPDATEDATE")
    private Date updateDate;

    public long getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public int getOrderID() {
	return orderID;
    }

    public void setOrderID(int orderID) {
	this.orderID = orderID;
    }

    public int getCategoryID() {
	return categoryID;
    }

    public void setCategoryID(int categoryID) {
	this.categoryID = categoryID;
    }

    public float getQuantity() {
	return quantity;
    }

    public void setQuantity(float quantity) {
	this.quantity = quantity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
	return "ordercontent [id=" + id + ", orderID=" + orderID + ", categoryID=" + categoryID + ", quantity=" + quantity
		+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
    }
}
