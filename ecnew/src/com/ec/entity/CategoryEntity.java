package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @Column(name = "CATEGORYID")
    @GeneratedValue
    private int categoryID;

    @Column(name = "CATEGORYNAME")
    private String categoryName;

    @Column(name = "DESCRIPTION")
    private String categoryDescription;
    
    @Column(name = "UNIT")
    private String unit;

    public int getCategoryID() {
	return categoryID;
    }

    public void setCategoryID(int categoryID) {
	this.categoryID = categoryID;
    }

    public String getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
	return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
	this.categoryDescription = categoryDescription;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
	return "CategoryEntity [categoryID=" + categoryID + ", categoryName=" + categoryName + ", categoryDescription="
		+ categoryDescription + ", unit=" + unit + "]";
    }

}
