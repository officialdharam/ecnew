package com.ec.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ec.dto.Device;
import com.ec.dto.FieldExecutive;
import com.ec.dto.UILink;
import com.ec.dto.User;
import com.ec.exception.LoginException;
import com.ec.response.Response;
import com.ec.service.CommonService;
import com.ec.service.WarehouseService;
import com.ec.util.Constant;
import com.ec.util.Util;

@Controller
public class HomeController extends BaseController {
    public static Logger log = Logger.getLogger(HomeController.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private WarehouseService warehouseService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(HttpServletRequest request) throws Exception {

	HttpSession session = request.getSession(true);
	session.invalidate();
	return "home";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request) throws Exception {

	HttpSession session = request.getSession(true);
	session.invalidate();
	return "index";
    }

    @ResponseStatus(value = HttpStatus.TEMPORARY_REDIRECT)
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    String test(HttpServletRequest request) throws Exception {

	HttpSession session = request.getSession(true);
	session.invalidate();
	return "home";
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request) throws Exception {
	Map<Integer, String> warehouseNames = null;
	String group = null;
	String landingPage = null;
	HttpSession session = request.getSession();

	// mostly there will always be a session
	if (session != null) {
	    String authenticated = (String) session.getAttribute("authenticated");
	    String chosenWarehouse = null;
	    // if the user is already authenticated
	    if (authenticated != null && "authenticated".equals(authenticated)) {
		String chooseCenter = (String) session.getAttribute("chooseCenter");
		chosenWarehouse = request.getParameter("warehouse");
		group = (String) session.getAttribute("group");
		landingPage = (String) session.getAttribute("landingPage");
		session.setAttribute("centerName", chosenWarehouse);
		// if the previous request wanted to choose a center
		if ("true".equals(chooseCenter)) {
		    // we requested to select a center, hence save the new
		    // center.
		    String selectedCenter = request.getParameter("warehouse");
		    session.setAttribute("chooseCenter", "false");
		    /* session.setAttribute("centerName", selectedCenter); */
		    /* System.out.println(selectedCenter); */
		} else {
		    System.out.println("Control should not be here");
		}

		// if the user was not authenticated earlier
	    } else {
		session.invalidate();
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = commonService.login(username, password);

		if (user != null) {

		    landingPage = user.getLandingPage();
		    group = user.getGroupName();

		    List<String> centerNames = getCentersForGroup(group, warehouseNames, username);

		    // create a session after login
		    session = request.getSession(true);
		    session.setMaxInactiveInterval(10*60);
		    session.setAttribute("centers", centerNames);
		    session.setAttribute("authenticated", "authenticated");
		    session.setAttribute(Util.USERNAME, username);
		    session.setAttribute("group", group);
		    session.setAttribute("landingPage", landingPage);
		} else {
		    throw new LoginException("Unable to make a valid login");
		}
	    }

	    Set<UILink> links = commonService.buildMenu(group);
	    List<String> groups = commonService.fetchGroups();
	    session.setAttribute("uiLinks", links);
	    session.setAttribute("groups", groups);
	    if (landingPage == null) {
		landingPage = (String) session.getAttribute("contentPage");
	    }
	    session.setAttribute("contentPage", landingPage);
	}

	// if the session is null, which is unlikely
	else {

	}

	return "landing";
    }

    @RequestMapping(value = "/fetchCenters", method = RequestMethod.GET)
    public @ResponseBody
    Response<String> fetchCenters(HttpServletRequest request, @RequestParam(value = "active") String active) throws Exception {

	HttpSession session = request.getSession(true);
	Map<Integer, String> names = commonService.fetchWarehouseNames(active, null);
	List<String> centerNames = new ArrayList<String>();
	for (Entry<Integer, String> warehouseEntry : names.entrySet()) {
	    centerNames.add(warehouseEntry.getValue());
	}

	Response<String> resp = new Response<String>();
	resp.setStatusMsg("SUCCESS");
	resp.setResults(centerNames);
	return resp;
    }

    @RequestMapping(value = "/navigate", method = RequestMethod.GET)
    public String navigate(HttpServletRequest request, @RequestParam(value = "nextPage") String nextPage,
	    @RequestParam(value = "warehouse", required = false) String warehouse) throws Exception {

	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for navigate for nextPage : " + nextPage
		+ " , warehouse " + warehouse);

	Map<Integer, String> warehouseNames = null;
	HttpSession session = request.getSession(true);
	if (session != null) {
	    String group = (String) session.getAttribute("group");
	    String username = (String)session.getAttribute(Util.USERNAME);
	    List<String> centerNames = getCentersForGroup(group, warehouseNames, username);

	    // create a session after login
	    session.setAttribute("centers", centerNames);

	    session.setAttribute("contentPage", nextPage);
	    session.setAttribute("warehouse", warehouse);

	    if ("editDetails.jsp".equals(nextPage)) {
		Map<String, String> warehouseDetails = warehouseService.getWarehouse(randomUUID, warehouse);
		session.setAttribute("warehouseDetails", warehouseDetails);
	    } else if ("deviceAssignment.jsp".equals(nextPage)) {
		if (warehouse == null || warehouse.length() == 0) {
		    warehouse = (String) session.getAttribute("centerName");
		}
		List<Device> devicesForCenter = commonService.fetchDevicesForCenter(warehouse, Constant.FALSE);
		List<FieldExecutive> fesForCenter = commonService.fetchFieldExecutivesForCenter(warehouse, Constant.FALSE);
		request.setAttribute("devices", devicesForCenter);
		request.setAttribute("fes", fesForCenter);
	    }
	}

	return "landing";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(HttpServletRequest request) throws Exception {

	HttpSession session = request.getSession();
	if (session == null)
	    throw new Exception("No session exists");

	boolean status = false;
	String username = (String) session.getAttribute(Util.USERNAME);
	String oldpassword = (String) request.getParameter("oldpassword");
	String password = (String) request.getParameter("newpassword");
	String confirmPassword = (String) request.getParameter("confirmPassword");
	System.out.println(username + " " + password + " " + oldpassword);
	if (!Util.nullOrEmtpy(username) && !Util.nullOrEmtpy(password) && !Util.nullOrEmtpy(confirmPassword)
		&& password.equals(confirmPassword)) {
	    status = commonService.changePassword(username, password, oldpassword);
	}

	if (status) {
	    return "redirect:home?passwordChanged=true";
	}

	return "redirect:landing?passwordChanged=false;";
    }

    @RequestMapping(value = "/signout", method = RequestMethod.GET)
    public String signout(HttpServletRequest request) throws Exception {

	HttpSession session = request.getSession();
	if (session != null)
	    session.invalidate();

	request.setAttribute("logout", "true");
	return "home";
    }

    private List<String> getCentersForGroup(String group, Map<Integer, String> warehouseNames, String username) {
	List<String> centerNames = new ArrayList<String>();
	if (warehouseNames == null || warehouseNames.isEmpty()) {
	    if (Constant.ADMIN.equals(group))
		warehouseNames = commonService.fetchWarehouseNames("", null);
	    else{
		warehouseNames = commonService.fetchWarehouseNames(Constant.ACTIVE, username);
	    }
	}
	if (Constant.ADMIN.equalsIgnoreCase(group)) {
	    for (Entry<Integer, String> warehouseEntry : warehouseNames.entrySet()) {
		centerNames.add(warehouseEntry.getValue());
	    }

	} else {
	    /*
	     * List<Integer> integers = new ArrayList<Integer>(); List<String>
	     * centerIds = user.getCenterIds(); for (String s : centerIds) {
	     * integers.add(Integer.parseInt(s)); }
	     */

	    for (Entry<Integer, String> warehouseEntry : warehouseNames.entrySet()) {
		centerNames.add(warehouseEntry.getValue());
	    }
	}
	Collections.sort(centerNames);
	return centerNames;
    }
}
