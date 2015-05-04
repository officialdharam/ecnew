package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fieldexecutive")
public class FieldExecutiveEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "CENTERID")
    private int centerID;

    @Column(name = "NAME")
    private String name;
    
    @Column(name = "ADDRESS")
    private String address;
    
    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "ASSIGNED")
    private String assigned;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCenterID() {
        return centerID;
    }

    public void setCenterID(int centerID) {
        this.centerID = centerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    @Override
    public String toString() {
	return "fieldexecutive [id=" + id + ", centerID=" + centerID + ", name=" + name + ", address=" + address
		+ ", active=" + active + ", assigned=" + assigned + "]";
    }
}

