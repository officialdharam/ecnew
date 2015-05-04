package com.ec.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityFilter implements Filter {

    private List<String> globalAccess;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	HttpServletRequest httpRequest = (HttpServletRequest) request;
	HttpServletResponse httpResponse = (HttpServletResponse) response;
	String requestURI = httpRequest.getRequestURI();
	if (!Util.nullOrEmtpy(requestURI)) {
	    if (globalAccess.contains(requestURI)) {
		chain.doFilter(request, response);
	    } else {
		HttpSession session = httpRequest.getSession(false);
		if (session != null) {
		    String userName = (String) session.getAttribute(Constant.USERNAME);
		    String groupName = (String) session.getAttribute(Constant.GROUPNAME);
		    if (Util.nullOrEmtpy(userName) || Util.nullOrEmtpy(groupName))
			httpResponse.sendRedirect("/service/login");
		} else {
		    httpResponse.sendRedirect("/service/login");
		}
	    }
	}
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
	globalAccess = new ArrayList<String>();
	globalAccess.add("/service/home");
	globalAccess.add("/service/login");
	globalAccess.add("/service/test");
	globalAccess.add("/service/index");
    }

}
