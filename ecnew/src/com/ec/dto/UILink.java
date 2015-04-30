package com.ec.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class UILink implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String displayName;
    private String href;
    private String dbName;
    private Set<UILink> children;

    public UILink(String displayName, String href, String dbName) {
	super();
	this.displayName = displayName;
	this.href = href;
	this.dbName = dbName;
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

    public String getDbName() {
	return dbName;
    }

    public void setDbName(String dbName) {
	this.dbName = dbName;
    }

    public Set<UILink> getChildren() {
	return children;
    }

    public void setChildren(Set<UILink> children) {
	this.children = children;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((dbName == null) ? 0 : dbName.hashCode());
	result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
	result = prime * result + ((href == null) ? 0 : href.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	UILink other = (UILink) obj;
	if (dbName == null) {
	    if (other.dbName != null)
		return false;
	} else if (!dbName.equals(other.dbName))
	    return false;
	if (displayName == null) {
	    if (other.displayName != null)
		return false;
	} else if (!displayName.equals(other.displayName))
	    return false;
	if (href == null) {
	    if (other.href != null)
		return false;
	} else if (!href.equals(other.href))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	final int maxLen = 10;
	return "UILink [displayName=" + displayName + ", href=" + href + ", dbName=" + dbName + ", children="
		+ (children != null ? toString(children, maxLen) : null) + "]";
    }

    private String toString(Collection<?> collection, int maxLen) {
	StringBuilder builder = new StringBuilder();
	builder.append("[");
	int i = 0;
	for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
	    if (i > 0)
		builder.append(", ");
	    builder.append(iterator.next());
	}
	builder.append("]");
	return builder.toString();
    }

}
