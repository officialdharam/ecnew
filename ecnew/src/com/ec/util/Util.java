package com.ec.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ec.dto.Category;
import com.ec.dto.NameValuePair;
import com.ec.dto.Order;
import com.ec.entity.CategoryEntity;
import com.ec.entity.CustomerEntity;
import com.ec.entity.OrderContentEntity;
import com.ec.entity.OrderEntity;

public class Util {
    public static Logger log = Logger.getLogger(Util.class);

    public static final long ALL_ACCESS = 0;
    public static final long READ_ONLY_ACCESS = -1;

    public static final String OPERATIONS = "OPERATIONS";
    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String CUSTOMERSERVICE = "CUSTOMERSERVICE";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static boolean nullOrEmtpy(String s) {
	if (s == null)
	    return true;
	if (s.length() == 0)
	    return true;

	return false;
    }

    public static String orderDisplay(long orderID){
	String s = "ECOC";
	String orderDisplay = s + String.format("%06d", orderID);
	return orderDisplay;
    }
    
    public static Date parseDate(String dateStr, String pattern) {
	Date date = null;
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	    date = sdf.parse(dateStr);
	} catch (Exception e) {
	    log.error("Cannot parse date " + dateStr + " to format " + pattern, e);
	}
	return date;
    }

    public static String formatDate(Date date, String pattern) {
	String dateStr = null;
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	    dateStr = sdf.format(date);
	} catch (Exception e) {
	    log.error("Cannot parse date " + date + " to format " + pattern, e);
	}
	return dateStr;
    }

    private static List<OrderContentEntity> mergeContentList(List<OrderContentEntity> contentList, int orderId) {
	Map<Integer, OrderContentEntity> mergeMap = new HashMap<Integer, OrderContentEntity>();
	for (OrderContentEntity cE : contentList) {

	    if (cE.getOrderID() == orderId) {

		OrderContentEntity ocE = mergeMap.get(cE.getCategoryID());
		if (ocE != null) {
		    float num = ocE.getQuantity() + cE.getQuantity();
		    cE.setQuantity(num);
		}

		mergeMap.put(cE.getCategoryID(), cE);
	    }
	}

	List<OrderContentEntity> tempList = new ArrayList<OrderContentEntity>();
	for (Entry<Integer, OrderContentEntity> entry : mergeMap.entrySet()) {
	    tempList.add(entry.getValue());
	}

	return tempList;
    }

    public static Order mapOrderToContent(Order order, OrderEntity oEntity, Map<Integer, CategoryEntity> categoryDefinitionsMap,
	    List<OrderContentEntity> contentListoriginal) {
	int orderID = oEntity.getOrderID();
	List<OrderContentEntity> contentList = mergeContentList(contentListoriginal, orderID);
	Set<Category> categories = new HashSet<Category>();
	for (OrderContentEntity ocEntity : contentList) {
	    if (orderID != ocEntity.getOrderID())
		continue;

	    Category category = new Category();
	    CategoryEntity categoryEntity = categoryDefinitionsMap.get(ocEntity.getCategoryID());
	    if (categoryEntity != null) {
		category.setName(categoryEntity.getCategoryName());
		category.setDescription(categoryEntity.getCategoryDescription());
		category.setQuantity(ocEntity.getQuantity() + " " + categoryEntity.getUnit());
		categories.add(category);
	    }
	}
	order.setDeviceID(oEntity.getDeviceID());
	order.setOrderID(oEntity.getOrderID());
	order.setItems(categories);
	return order;
    }

    public static Order mapOrdersToCustomer(Order order, OrderEntity oEntity, CustomerEntity cEntity) {
	if (oEntity == null || cEntity == null) {
	    log.warn("WARNING: got an null customer entity or order entity in the method Util.mapOrdersToCustomer, hence there cannot be a mapping, returning null");
	    return null;
	}

	try {
	    order.setCustomerID(oEntity.getCustomerID());
	    order.setOrderID(oEntity.getOrderID());
	    order.setLastUpdated(oEntity.getLastUpdated());
	    order.setOrderDate(Util.formatDate(oEntity.getOrderDate(), Constant.DB_DATE_FORMAT));
	    Date pickUpDate = oEntity.getPickUpDate();
	    String pickUpDateStr = Util.formatDate(pickUpDate, Constant.DB_DATE_FORMAT);
	    order.setPickUpTime(oEntity.getPickupTime());
	    order.setPickUpDate(pickUpDateStr.substring(0, 10));
	    order.setSpecialComments(oEntity.getSpecialComments());
	    order.setStatus(oEntity.getStatus());
	    order.setUpdatedBy(oEntity.getUpdatedBy());
	    order.setFirstName(cEntity.getFirstName());
	    order.setLastName(cEntity.getLastName());
	    order.setEmail(cEntity.getEmail());
	    order.setAddress(cEntity.getAddress());
	    order.setPhoneNo(cEntity.getPhoneNumber());
	} catch (Exception e) {
	    log.error(
		    "Error occurred while mapping order to customer. Order Entity " + oEntity + " , Customer Entity " + cEntity,
		    e);
	}

	return order;
    }

    public static Map<String, String> prepareMap(List<NameValuePair> list) {
	Map<String, String> map = new HashMap<String, String>();
	for (NameValuePair pair : list) {
	    map.put(pair.getName(), pair.getValue());
	}
	return map;
    }

    public static Date getDateWithTimeDifferenceFromNow(long seconds) {
	long now = System.currentTimeMillis() / 1000;
	long time = now + seconds;
	time = time * 1000;
	Calendar calendar = Calendar.getInstance();
	calendar.setTimeInMillis(time);
	return calendar.getTime();
    }
}
