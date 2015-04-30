package com.ec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ec.dao.CustomerDAO;
import com.ec.dao.OrderDAO;
import com.ec.dto.Customer;
import com.ec.dto.NameValuePair;
import com.ec.dto.Order;
import com.ec.entity.CategoryEntity;
import com.ec.entity.CenterEntity;
import com.ec.entity.CustomerEntity;
import com.ec.entity.DeviceEntity;
import com.ec.entity.OrderContentEntity;
import com.ec.entity.OrderEntity;
import com.ec.entity.PincodeEntity;
import com.ec.util.Constant;
import com.ec.util.OrderStatus;
import com.ec.util.Util;

@Service
public class OrderServiceImpl implements OrderService {

    public static Logger log = Logger.getLogger(OrderService.class);

    Map<Integer, CategoryEntity> categoryDefinitionsMap = null;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public Order getOrderByID(String randomID, int orderID) throws Exception {

	log.info("Request ID " + randomID + " .Parameters -> order ID : " + orderID);

	// fetch order, match by customer and content
	OrderEntity oEntity = orderDAO.fetchOrderByID(orderID);

	log.info("Request ID " + randomID + " .Fetched order entity : " + oEntity);

	int customerID = oEntity.getCustomerID();

	CustomerEntity cEntity = customerDAO.fetchCustomerByID(customerID);

	log.info("Request ID " + randomID + " .Fetched customer entity : " + cEntity);

	Order order = new Order();
	Util.mapOrdersToCustomer(order, oEntity, cEntity);

	log.info("Request ID " + randomID + " .Mapped custommer to order object : " + order);

	Set<Integer> orderIDs = new HashSet<Integer>();
	orderIDs.add(oEntity.getOrderID());

	List<OrderContentEntity> contentList = orderDAO.fetchOrderContent(orderIDs);

	log.info("Request ID " + randomID + " .Fetched order content for : " + contentList);

	Util.mapOrderToContent(order, oEntity, getCategoryDefinitionsMap(randomID), contentList);

	log.info("Request ID " + randomID + " .Mapped order to content list : " + order);

	return order;
    }

    @Override
    public Set<Order> listOrdersPickedByDevice(String randomID, String deviceID) throws Exception {

	Set<Order> orders = null;

	log.info("Request ID " + randomID + " .Parameters -> device ID : " + deviceID);
	DeviceEntity deviceEntity = orderDAO.fetchDeviceByUniqueID(deviceID);

	log.info("Request ID " + randomID + " .Fetched device entity : " + deviceEntity);

	if (deviceEntity != null) {

	    List<OrderEntity> orderList = orderDAO.fetchOrdersByDeviceIDAndStatus(deviceEntity.getDeviceID(),
		    OrderStatus.PICKED.name());

	    log.info("Request ID " + randomID + " .Fetched order list : " + orderList);

	    Set<Integer> orderIDs = new HashSet<Integer>();

	    for (OrderEntity oEntity : orderList) {
		orderIDs.add(oEntity.getOrderID());
	    }

	    List<OrderContentEntity> contentList = orderDAO.fetchOrderContent(orderIDs);

	    log.info("Request ID " + randomID + " .Fetched content list : " + contentList);

	    if (contentList != null) {
		orders = new HashSet<Order>();
		for (OrderEntity oe : orderList) {
		    Order order = new Order();
		    Util.mapOrderToContent(order, oe, getCategoryDefinitionsMap(randomID), contentList);
		    orders.add(order);
		}

		log.info("Request ID " + randomID + " .Mapped order content for all the orders : " + orders);
	    }
	}

	return orders;
    }

    @Override
    public List<Order> getOrderByPhone(String randomID, long phone) {

	List<OrderEntity> customerOrders = null;
	List<Order> orders = null;
	Order order = null;

	log.info("Request ID " + randomID + " .Parameters -> phone number : " + phone);

	CustomerEntity customer = customerDAO.fetchCustomerByPhone(phone);

	log.info("Request ID " + randomID + " .Fetched customer entity : " + customer);

	if (customer != null) {
	    customerOrders = orderDAO.fetchOrdersByCustomerID(customer.getCustomerID());
	}

	log.info("Request ID " + randomID + " .Fetched orders for customer id : " + customerOrders);

	if (customerOrders != null) {
	    orders = new ArrayList<Order>();
	    for (OrderEntity oEntity : customerOrders) {
		order = new Order();
		order.setOrderID(oEntity.getOrderID());
		order.setAmount(oEntity.getAmount());
		String pickUpDateStr = Util.formatDate(oEntity.getPickUpDate(), Constant.DB_DATE_FORMAT);
		order.setPickUpTime(oEntity.getPickupTime());
		order.setPickUpDate(pickUpDateStr.substring(0, 10));
		order.setStatus(oEntity.getStatus());
		order.setFirstName(customer.getFirstName());
		order.setLastName(customer.getLastName());
		order.setPhoneNo(customer.getPhoneNumber());
		orders.add(order);
	    }

	    log.info("Request ID " + randomID + " .Mapped orders to customer and prepared order list : " + orders);
	}

	return orders;
    }

    @Override
    public long createOrder(String randomID, List<NameValuePair> order, String userName) {

	long orderID = -1;

	log.info("Request ID " + randomID + " .Parameters -> username  : " + userName + ", order : " + order);

	Map<String, String> orderMap = Util.prepareMap(order);

	String pincode = orderMap.get(Constant.PINCODE);
	PincodeEntity pincodeEntity = orderDAO.fetchPinCode(pincode);

	log.info("Request ID " + randomID + " .Fetched pincode entity : " + pincodeEntity);

	if (pincodeEntity == null || pincodeEntity.getCenterID() <= 0) {
	    log.info("Request ID " + randomID + " .Returning pipncode not servicable : ");
	    return Constant.PINCODE_NOT_SERVICEABLE;
	}

	orderID = createOrder(randomID, orderMap, userName, pincodeEntity.getCenterID());

	log.info("Request ID " + randomID + " .Created order with order id : " + orderID);

	return orderID;
    }

    @Override
    public boolean updateOrder(String randomID, List<NameValuePair> order, String userName) {

	log.info("Request ID " + randomID + " .Parameters -> username  : " + userName + ", order : " + order);

	Map<String, String> orderMap = Util.prepareMap(order);
	boolean orderID = updateOrder(randomID, orderMap, userName);
	log.info("Request ID " + randomID + " .Updated order with order id : " + orderID);
	return orderID;
    }

    @Override
    public Set<Order> listOrders(String randomID, OrderStatus status, long seconds, String warehouseName) {
	Set<Order> orders = new HashSet<Order>();
	Order order = null;
	List<OrderEntity> openOrders = null;
	log.info("Request ID " + randomID + " .Parameters -> seconds  : " + seconds + ", warehouseName : " + warehouseName
		+ " status : " + status.name());

	if (warehouseName != null) {
	    CenterEntity center = orderDAO.getCenter(warehouseName);
	    log.info("Request ID " + randomID + " .Fetched center entity : " + center);

	    if (center != null) {
		int centerID = center.getCenterID();
		// fetch data from order table
		openOrders = orderDAO.fetchOrdersByStatus(status.name(), seconds, centerID);
		log.info("Request ID " + randomID + " .Fetched orders by status : " + openOrders);
	    }
	}

	// for each order get the customer info like phone number , address etc.
	if (openOrders == null)
	    return null;

	Set<Integer> customerIDS = new HashSet<Integer>();
	for (OrderEntity oEntity : openOrders) {
	    customerIDS.add(oEntity.getCustomerID());
	}
	log.info("Request ID " + randomID + " .Prepared list of customer ids : " + customerIDS);

	if (customerIDS != null && customerIDS.size() > 0) {

	    List<CustomerEntity> customers = customerDAO.fetchCustomersByID(customerIDS);
	    log.info("Request ID " + randomID + " .Fetched customer entitities : " + customers);

	    // prepare customer map.
	    Map<Integer, CustomerEntity> customerMap = new HashMap<Integer, CustomerEntity>();
	    for (CustomerEntity cEntity : customers) {
		customerMap.put(cEntity.getCustomerID(), cEntity);
	    }

	    for (OrderEntity oEntity : openOrders) {
		order = new Order();
		Util.mapOrdersToCustomer(order, oEntity, customerMap.get(oEntity.getCustomerID()));
		orders.add(order);
	    }

	    log.info("Request ID " + randomID + " .Mapped all orders to customers : " + orders);

	} else {
	    log.warn("Request ID " + randomID + " .WARNING :  The customer ID list was null for the order status " + status.name()
		    + " and time in seconds " + seconds + ". The order ID list returned is " + openOrders);
	}

	return orders;
    }

    @Transactional
    private int getCustomerID(String randomID, Map<String, String> orderMap) {

	int customerID = -1;
	boolean valid = false;
	try {
	    String phoneNo = orderMap.get(Constant.PHONE_NO);
	    // get the customer details
	    long longPhone = Long.parseLong(phoneNo);
	    String pincode = orderMap.get(Constant.PINCODE);

	    if (!Util.nullOrEmtpy(pincode)) {
		PincodeEntity pin = orderDAO.fetchPinCode(pincode);
		log.info("Request ID " + randomID + " .Fetched pincode Entity : " + pin);
		if (pin.getCenterID() > 0) {
		    valid = true;
		}
	    }

	    if (!valid) {
		log.info("Request ID " + randomID + " .Returning pincode not servicable : " + valid);
		return Constant.PINCODE_NOT_SERVICEABLE;
	    } else {

		CustomerEntity customerEntity = customerDAO.fetchCustomerByPhone(longPhone);
		log.info("Request ID " + randomID + " .Fetched customer for phone : " + customerEntity);

		if (customerEntity == null) {
		    // create customer
		    customerEntity = new CustomerEntity();
		    customerEntity.setFirstName(orderMap.get(Constant.FIRST_NAME));
		    customerEntity.setLastName(orderMap.get(Constant.LAST_NAME));
		    customerEntity.setEmail(orderMap.get(Constant.EMAIL));
		    customerEntity.setPincode(pincode);
		    customerEntity.setAddress(orderMap.get(Constant.ADDRESS));
		    customerEntity.setPhoneNumber(longPhone);
		    customerEntity.setUpdatedAt(new Date());
		    customerID = customerDAO.saveCustomer(customerEntity);
		    log.info("Request ID " + randomID + " .Created customer : " + customerID);
		} else {
		    customerID = customerEntity.getCustomerID();
		}
	    }

	} catch (Exception e) {
	    log.info("Request ID " + randomID + " .Error occurred while getting customer ID : ", e);
	}

	return customerID;
    }

    @Transactional
    private long createOrder(String randomID, Map<String, String> orderMap, String userName, int centerID) {
	long orderID = -1;
	int customerID = getCustomerID(randomID, orderMap);
	log.info("Request ID " + randomID + " .Got customer ID for ordermap : " + customerID);

	if (customerID > 0) {
	    // create an order entry.
	    OrderEntity orderEntity = new OrderEntity();
	    orderEntity.setStatus(OrderStatus.OPEN.name());
	    orderEntity.setCenterID(centerID);
	    orderEntity.setOrderDate(new Date());
	    orderEntity.setPickUpDate(Util.parseDate(orderMap.get(Constant.PICKUP_DATE), Constant.ORDER_DATE_FORMAT));
	    orderEntity.setCustomerID(customerID);
	    orderEntity.setSpecialComments(orderMap.get(Constant.SPCL_COMMENTS));
	    orderEntity.setLastUpdated(new Date());
	    orderEntity.setPickupTime(orderMap.get(Constant.PICKUP_TIME));
	    orderEntity.setUpdatedBy(userName);
	    orderID = orderDAO.saveOrder(orderEntity);
	    log.info("Request ID " + randomID + " .Saved order with order ID : " + orderID);

	}
	return orderID;
    }

    @Transactional
    private boolean updateOrder(String randomID, Map<String, String> orderMap, String userName) {
	boolean orderStatus = false;
	// create an order entry.
	try {
	    String orderID = orderMap.get("orderID");
	    log.info("Request ID " + randomID + " .Order ID to update : " + orderID);
	    int orderIDLong = 0;
	    if (orderID != null && orderID.length() > 0) {
		orderIDLong = Integer.parseInt(orderID);
	    }

	    if (orderIDLong == 0)
		return false;

	    OrderEntity orderEntity = orderDAO.fetchOrderByID(orderIDLong);
	    log.info("Request ID " + randomID + " .Fetched order entity for order id : " + orderEntity);

	    orderEntity.setStatus(orderMap.get("status"));
	    orderEntity.setLastUpdated(new Date());
	    orderEntity.setUpdatedBy(userName);
	    orderStatus = orderDAO.updateOrder(orderEntity);
	    log.info("Request ID " + randomID + " .Updated order entity status : " + orderStatus);
	} catch (Exception e) {
	    log.error("Request ID " + randomID + " .Error occurred while updating order : ", e);
	}
	return orderStatus;
    }

    @Override
    public Set<Order> listOrders(String randomID, List<Integer> orderIds) {
	Order order = null;
	Set<Order> orders = new HashSet<Order>();
	log.info("Request ID " + randomID + " .Parameters -> orderIds  : " + orderIds);

	// fetch data from order table
	List<OrderEntity> openOrders = orderDAO.fetchOrderByIds(orderIds);
	log.info("Request ID " + randomID + " .Fetched orders by IDs : " + openOrders);

	// for each order get the customer info like phone number , address etc.
	if (openOrders == null)
	    return null;

	Set<Integer> customerIDS = new HashSet<Integer>();
	for (OrderEntity oEntity : openOrders) {
	    customerIDS.add(oEntity.getCustomerID());
	}

	log.info("Request ID " + randomID + " .Prepared customer Ids list : " + customerIDS);
	List<CustomerEntity> customers = customerDAO.fetchCustomersByID(customerIDS);
	log.info("Request ID " + randomID + " .Fetched customers by IDs : " + customers);

	if (customers != null && customers.size() > 0) {
	    // prepare customer map.
	    Map<Integer, CustomerEntity> customerMap = new HashMap<Integer, CustomerEntity>();
	    for (CustomerEntity cEntity : customers) {
		customerMap.put(cEntity.getCustomerID(), cEntity);
	    }

	    for (OrderEntity oEntity : openOrders) {
		order = new Order();
		Util.mapOrdersToCustomer(order, oEntity, customerMap.get(oEntity.getCustomerID()));
		orders.add(order);
	    }

	    log.info("Request ID " + randomID + " .Mapped orders to customers : " + orders);
	} else {
	    log.info("Request ID "
		    + randomID
		    + ". Returning empty order list because got a null or empty customer List from customer DAO for customer IDs "
		    + customerIDS);
	}

	return orders;
    }

    @PostConstruct
    @Transactional
    public void createCategoryMap() {
	List<CategoryEntity> categoryDefinitions = null;
	if (categoryDefinitionsMap == null) {
	    categoryDefinitions = orderDAO.fetchCategoryDefition();

	    log.info("FETCHED CATEGORY DEFINITIONS  " + categoryDefinitions);

	    if (categoryDefinitions != null) {
		categoryDefinitionsMap = new HashMap<Integer, CategoryEntity>();
		for (CategoryEntity entity : categoryDefinitions) {
		    categoryDefinitionsMap.put(entity.getCategoryID(), entity);
		}
	    }
	}

	log.info("CREATED CATEGORY DEFINITIONS MAP  " + categoryDefinitionsMap);
    }

    @Override
    public Map<Integer, CategoryEntity> getCategoryDefinitionsMap(String randomID) throws Exception {
	int count = 0;
	while (categoryDefinitionsMap == null) {
	    createCategoryMap();
	    count++;
	    log.info("Request ID " + randomID + " .Fetching category definitions count is : " + count);
	    if (count == 10){
		log.error("Request ID " + randomID + " .Unable to fetch category definitions count is : " + count);
		throw new Exception("Unable to fetch the category definitions.");
	    }
	}
	return categoryDefinitionsMap;
    }

    @Override
    public Customer getCustomer(String randomID, long phone) {
	CustomerEntity cEntity = null;
	Customer c = new Customer();
	log.info("Request ID " + randomID + " .Parameters -> phone  : " + phone);
	try {
	    cEntity = customerDAO.fetchCustomerByPhone(phone);
	    log.info("Request ID " + randomID + " .Fetched customer entity by phone : " + cEntity);
	    if (cEntity != null) {
		mapCustomer(cEntity, c);
	    }
	    log.info("Request ID " + randomID + " .Mapped customer from entity to DTO : " + c);
	} catch (Exception e) {
	    log.error("Request ID " + randomID + " .Error occurred whihle fetching customer by phone : ", e);
	}

	return c;
    }

    private void mapCustomer(CustomerEntity ce, Customer c) {
	if (ce != null) {
	    c.setPhoneNumber(ce.getPhoneNumber());
	    c.setEmail(ce.getEmail());
	    c.setAddress(ce.getAddress());
	    c.setFirstName(ce.getFirstName());
	    c.setLastName(ce.getLastName());
	    c.setPincode(ce.getPincode());
	}
    }
}
