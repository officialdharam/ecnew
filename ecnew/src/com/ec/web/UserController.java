package com.ec.web;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ec.dto.NameValuePair;
import com.ec.dto.User;
import com.ec.exception.AuthorizationException;
import com.ec.response.Response;
import com.ec.service.UserService;
import com.ec.util.Constant;
import com.ec.util.Role;
import com.ec.util.Util;

@Controller
public class UserController extends BaseController {

    public static Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/exportDB", method = RequestMethod.GET)
    public @ResponseBody
    byte[] exportDB(HttpServletRequest request, HttpServletResponse response) throws AuthorizationException {
	byte[] bytes = null;
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for DB export : ");
	HttpSession session = request.getSession();
	session = request.getSession(true);
	String authenticated = (String) session.getAttribute("authenticated");
	String userName = (String) session.getAttribute(Util.USERNAME);
	String group = (String) session.getAttribute("group");

	if (Util.nullOrEmtpy(authenticated) || Util.nullOrEmtpy(userName) || Util.nullOrEmtpy(group)
		|| !Role.ADMIN.name().equals(group)) {
	    log.info("Request ID " + randomUUID + " resulted in authorization exception.");
	    throw new AuthorizationException(String.format(
		    "User %s is not eligible for exporting reports from the DB. Only ADMIN is allowed", userName));
	}
	List<String> exportDatabase = null;
	try {
	    exportDatabase = userService.exportDatabase();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    for (String s : exportDatabase) {
		baos.write(s.getBytes());
	    }
	    bytes = baos.toByteArray();

	    log.info("Request ID " + randomUUID + ". Successfully fetched the complete database.");
	} catch (Exception e) {
	    log.info("Request ID " + randomUUID + ". Resulted in exception while fetching the complete database.", e);
	    bytes = "Your request resulted in some error, please try after sometime".getBytes();
	}

	response.setHeader("Content-Disposition", "attachment; filename=\"export.csv\"");
	response.setContentLength(bytes.length);
	response.setContentType("text/csv");

	return bytes;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public @ResponseBody
    Response<String> addUser(HttpServletRequest request, @RequestBody List<NameValuePair> user) {

	Response<String> resp = new Response<String>();
	int userID = userService.addUser(user);
	if (userID < 1) {
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	} else {
	    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    List<String> userIDList = new ArrayList<String>();
	    userIDList.add(String.valueOf(userID));
	    resp.setResults(userIDList);
	}
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public @ResponseBody
    Response<String> updateUser(HttpServletRequest request, @RequestBody List<NameValuePair> user) {

	Response<String> resp = new Response<String>();
	boolean status = userService.updateUser(user);
	if (!status) {
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	} else {
	    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	}
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/user/fetch", method = RequestMethod.GET)
    public @ResponseBody
    Response<User> fetchUsers(HttpServletRequest request) {

	Response<User> resp = new Response<User>();
	try {
	    List<User> fetchUsers = userService.fetchUsers();
	    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    resp.setResults(fetchUsers);
	} catch (Exception e) {
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	}
	return resp;

    }
}
