package com.ec.response;

import java.util.List;

public class Response<T> {
    private String statusMsg;
    private List<T> results;

    public String getStatusMsg() {
	return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
	this.statusMsg = statusMsg;
    }

    public List<T> getResults() {
	return results;
    }

    public void setResults(List<T> results) {
	this.results = results;
    }

    @Override
    public String toString() {
	final int maxLen = 10;
	return "Response [statusMsg=" + statusMsg + ", results="
		+ (results != null ? results.subList(0, Math.min(results.size(), maxLen)) : null) + "]";
    }

}
