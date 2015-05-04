package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permissionmap")
public class PermissionMapEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "PERMISSIONID")
    private int permissionID;

    @Column(name = "GROUPID")
    private int groupID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    @Override
    public String toString() {
	return "permissionmap [id=" + id + ", permissionID=" + permissionID + ", groupID=" + groupID + "]";
    }

    
}
