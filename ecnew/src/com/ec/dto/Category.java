package com.ec.dto;

public class Category {

    private String name;
    private String description;
    private String quantity;

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

    public String getQuantity() {
	return quantity;
    }

    public void setQuantity(String quantity) {
	this.quantity = quantity;
    }

    @Override
    public String toString() {
	return "Category [name=" + name + ", description=" + description + ", quantity=" + quantity + "]";
    }

}
