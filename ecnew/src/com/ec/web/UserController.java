package com.ec.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.ec.response.Response;
import com.ec.service.UserService;
import com.ec.util.Constant;

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

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
