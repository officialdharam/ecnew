package com.ec.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ec.dto.Customer;
import com.ec.dto.NameValuePair;
import com.ec.dto.Order;
import com.ec.response.Response;
import com.ec.service.OrderService;
import com.ec.util.Constant;
import com.ec.util.OrderStatus;
import com.ec.util.Util;

/**
 * This class serves as a web layer to control order functionality. This will
 * involve creating/updating/fetching/deleting orders.
 * <ul>
 * <li><strong>Usecase: </strong> CS guy logs in and makes an entry for a new
 * order.
 * <li><strong>Usecase: </strong> CS guy fetches customer's order based on phone
 * number and informs the customer.
 * <li><strong>Usecase: </strong> OPS guy updates the order.
 * <li><strong>Usecase: </strong> OPS guy fetches all the orders in any give
 * status.
 * 
 * @author dprasad
 * 
 */
@Controller
public class OrderController extends BaseController {

    public static Logger log = Logger.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Value("${listing.dashboard.notaction.time}")
    private long notActionTime;

    @Value("${listing.dashboard.pending.time}")
    private long pendingTime;

    @PostConstruct
    public void printProps() {
	log.info("LOADED PROPERTIES");
	log.info("NOT ACTIONED TIME " + notActionTime);
	log.info("PENDNG TIME " + pendingTime);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.GET)
    public @ResponseBody
    Response<Order> getOrderByID(@PathVariable(value = "orderID") int orderID) {
	Response<Order> resp = new Response<Order>();
	String randomUUID = UUID.randomUUID().toString();
	try {
	    log.info("Request ID " + randomUUID + ". Received a request for getOrderByID for orderID : " + orderID);
	    Order orderByID = orderService.getOrderByID(randomUUID , orderID);
	    List<Order> orderList = new ArrayList<Order>();
	    orderList.add(orderByID);

	    if (orderByID != null) {
		resp.setResults(orderList);
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    }
	    log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred ", e);
	}
	return resp;
    }

    @RequestMapping(value = "/searchOrders", method = RequestMethod.POST)
    public @ResponseBody
    Response<Order> searchOrders(HttpServletRequest request, @RequestBody List<String> ids) throws Exception {
	Response<Order> resp = new Response<Order>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for searchOrders for orderIDs : " + ids);
	List<Integer> idsList = new ArrayList<Integer>();
	for (String id : ids) {
	    idsList.add(Integer.parseInt(id.trim()));
	}
	Set<Order> listOrders = orderService.listOrders(randomUUID , idsList);
	List<Order> orderList = new ArrayList<Order>();
	if (listOrders != null) {
	    orderList.addAll(listOrders);
	    resp.setResults(orderList);
	    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	} else {
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public @ResponseBody
    Response<Order> getOrderByPhone(HttpServletRequest request, @RequestParam(value = "phoneNumber") long phone) throws Exception {
	Response<Order> resp = new Response<Order>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for getOrderByPhone : " + phone);
	List<Order> orderByPhone = orderService.getOrderByPhone(randomUUID , phone);
	if (orderByPhone != null) {
	    resp.setResults(orderByPhone);
	    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	} else {
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @RequestMapping(value = "/order/list/{status}", method = RequestMethod.GET)
    public @ResponseBody
    Response<Order> getOrdersByStatus(HttpServletRequest request, @PathVariable(value = "status") String status) throws Exception {
	Response<Order> resp = new Response<Order>();
	Set<Order> openOrders = null;
	String randomUUID = UUID.randomUUID().toString();
	if (request != null) {
	    log.info("Request ID " + randomUUID + ". Received a request for getOrdersByStatus : " + status);
	    HttpSession session = request.getSession(false);
	    if (session != null) {
		String warehouseName = (String) request.getSession().getAttribute("centerName");
		if (OrderStatus.OPEN.name().equalsIgnoreCase(status)) {
		    openOrders = orderService.listOrders(randomUUID , OrderStatus.OPEN, 0, warehouseName);
		} else if (OrderStatus.FORWARDED.name().equalsIgnoreCase(status)) {
		    openOrders = orderService.listOrders(randomUUID , OrderStatus.FORWARDED, 0, warehouseName);
		} else if (OrderStatus.PICKED.name().equalsIgnoreCase(status)) {
		    openOrders = orderService.listOrders(randomUUID , OrderStatus.PICKED, 0, warehouseName);
		}
		List<Order> orderList = new ArrayList<Order>();
		if (openOrders != null) {
		    orderList.addAll(openOrders);
		    resp.setResults(orderList);
		    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		} else {
		    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		}
	    } else {
		log.error("Request ID " + randomUUID + ". No valid session exists");
		throw new Exception("Session doesn't exist");
	    }
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @RequestMapping(value = "/order/list/pending", method = RequestMethod.GET)
    public @ResponseBody
    Response<Order> getOrdersPending(HttpServletRequest request) throws Exception {
	Response<Order> resp = new Response<Order>();
	Set<Order> openOrders = null;
	String randomUUID = UUID.randomUUID().toString();
	if (request != null) {
	    log.info("Request ID " + randomUUID + ". Received a request for getOrdersPending : ");
	    HttpSession session = request.getSession(false);
	    if (session != null) {
		String warehouseName = (String) request.getSession().getAttribute("centerName");
		openOrders = orderService.listOrders(randomUUID , OrderStatus.FORWARDED, pendingTime, warehouseName);
		List<Order> orderList = new ArrayList<Order>();
		if (openOrders != null) {
		    orderList.addAll(openOrders);
		    resp.setResults(orderList);
		    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		} else {
		    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		}
	    } else {
		log.error("Request ID " + randomUUID + ". No valid session exists");
		throw new Exception("Session doesn't exist");
	    }
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @RequestMapping(value = "/order/list/notaction", method = RequestMethod.GET)
    public @ResponseBody
    Response<Order> getOrdersNotActioned(HttpServletRequest request) throws Exception {
	Response<Order> resp = new Response<Order>();
	Set<Order> openOrders = null;
	String randomUUID = UUID.randomUUID().toString();
	if (request != null) {
	    log.info("Request ID " + randomUUID + ". Received a request for getOrdersNotActioned : ");
	    HttpSession session = request.getSession(false);
	    if (session != null) {
		String warehouseName = (String) request.getSession().getAttribute("centerName");
		openOrders = orderService.listOrders(randomUUID , OrderStatus.OPEN, notActionTime, warehouseName);
		List<Order> orderList = new ArrayList<Order>();
		if (openOrders != null) {
		    orderList.addAll(openOrders);
		    resp.setResults(orderList);
		    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		} else {
		    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		}
	    } else {

		log.error("Request ID " + randomUUID + ". No valid session exists");
		throw new Exception("Session doesn't exist");
	    }
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public @ResponseBody
    Response<String> createOrder(HttpServletRequest request, @RequestBody List<NameValuePair> order) throws Exception {

	HttpSession session = request.getSession();
	Response<String> resp = new Response<String>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for createOrder : " + order);
	if (session == null) {
	    log.error("Request ID " + randomUUID + ". No valid session exists.");
	    throw new Exception("No session exists");
	}

	String userName = fetchUserName(request);
	try {

	    long orderID = orderService.createOrder(randomUUID , order, userName);
	    List<String> orderIDList = new ArrayList<String>();

	    if (orderID == Constant.PINCODE_NOT_SERVICEABLE) {
		resp.setStatusMsg(Constant.PINCODE_NOT_SERVICEABLE_MESSAGE);
	    } else if (orderID > 0) {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		orderIDList.add(Util.orderDisplay(orderID));
	    }

	    resp.setResults(orderIDList);
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while creating order.", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @RequestMapping(value = "/order", method = RequestMethod.PUT)
    public @ResponseBody
    Response<Void> updateOrder(HttpServletRequest request, @RequestBody List<NameValuePair> order) {
	String randomUUID = UUID.randomUUID().toString();
	Response<Void> resp = new Response<Void>();
	log.info("Request ID " + randomUUID + ". Received a request for updateOrder : " + order);
	try {
	    String userName = fetchUserName(request);
	    boolean updateOrder = orderService.updateOrder(randomUUID , order, userName);
	    if (!updateOrder) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while updating order.", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    private String fetchUserName(HttpServletRequest request) {
	HttpSession session = null;
	String userName = null;
	if (request != null) {
	    session = request.getSession(false);
	    userName = (String) session.getAttribute(Util.USERNAME);
	}

	return userName;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public @ResponseBody
    Response<Customer> fetchCustomer(HttpServletRequest request, @RequestParam(value = "phone") long phone) {
	Response<Customer> resp = new Response<Customer>();
	List<Customer> cList = new ArrayList<Customer>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for fetchCustomer : " + phone);
	try {
	    Customer c = orderService.getCustomer(randomUUID , phone);
	    if (c == null) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		resp.setResults(cList);
	    } else {
		cList.add(c);
		resp.setResults(cList);
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while fetching customer.", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }
}