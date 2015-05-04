package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class MenuEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISPLAYNAME")
    private String displayName;

    @Column(name = "HREF")
    private String href;

    @Column(name = "PARENT")
    private int parent;

    @Column(name = "DESCRIPTION")
    private String description;

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

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getHref() {
	return href;
    }

    public void setHref(String href) {
	this.href = href;
    }

    public int getParent() {
	return parent;
    }

    public void setParent(int parent) {
	this.parent = parent;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public String toString() {
	return "menu [id=" + id + ", name=" + name + ", displayName=" + displayName + ", href=" + href + ", parent="
		+ parent + ", description=" + description + "]";
    }

}
