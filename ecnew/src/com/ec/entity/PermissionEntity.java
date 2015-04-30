package com.ec.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
public class PermissionEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private int id;

    @Column(name = "LINK")
    private String link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
	return "PermissionEntity [id=" + id + ", link=" + link + "]";
    }
    
    

}
