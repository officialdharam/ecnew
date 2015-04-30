package com.ec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ec.dao.OrderDAO;
import com.ec.dao.UserDAO;
import com.ec.dto.Category;
import com.ec.dto.DeviceAssignment;
import com.ec.dto.NameValuePair;
import com.ec.dto.Order;
import com.ec.entity.AssignDeviceEntity;
import com.ec.entity.CategoryEntity;
import com.ec.entity.CenterEntity;
import com.ec.entity.DeviceEntity;
import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.OrderContentEntity;
import com.ec.entity.OrderEntity;
import com.ec.entity.OutboundEntity;
import com.ec.entity.PincodeEntity;
import com.ec.util.Constant;
import com.ec.util.OrderStatus;
import com.ec.util.Util;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    public static Logger log = Logger.getLogger(WarehouseServiceImpl.class);

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OrderService orderService;

    /**
     * The one which is yet to come. PICKED weight
     */
    @Override
    public List<NameValuePair> fetchInbound(String randomID, String warehouseName) {
	List<OrderEntity> pickedOrders = null;
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName);
	CenterEntity center = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center : " + center);
	try {
	    if (center != null) {
		pickedOrders = orderDAO.fetchAllOrdersByStatus(OrderStatus.PICKED.name(), center.getCenterID());
		log.info("Request ID " + randomID + " .Fetched picked orders for 0 minutes and center id : " + pickedOrders);
	    }

	    Set<Order> orders = process(randomID, pickedOrders);
	    log.info("Request ID " + randomID + " .Processed picked orders : " + orders);

	    Map<String, Float> summedUp = sumUp(randomID, orders);
	    log.info("Request ID " + randomID + " .Summed up all the orders : " + summedUp);

	    List<NameValuePair> nameValue = prepareNameValue(randomID, summedUp);
	    log.info("Request ID " + randomID + " .Prepared name value pairs : " + nameValue);

	    return nameValue;
	} catch (Exception e) {
	    log.error("Request ID " + randomID + " .Error occurred while fetching inbound orders for warehhouse ", e);
	}

	return null;
    }

    /**
     * The one which is closed. Already in WAREHOUSE
     * 
     * @throws Exception
     */
    @Override
    public List<NameValuePair> fetchOutbound(String randomID, String warehouseName) {
	List<OrderEntity> closedOrders = null;
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName);

	CenterEntity center = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center for warehouseName : " + center);

	try {
	    if (center != null) {
		closedOrders = orderDAO.fetchAllOrdersByStatus(OrderStatus.CLOSED.name(), center.getCenterID());
		log.info("Request ID " + randomID + " .Fetched outbound orders for warehouse : " + closedOrders);
	    }
	    Set<Order> orders = process(randomID, closedOrders);
	    log.info("Request ID " + randomID + " .Processed picked orders : " + orders);

	    Map<String, Float> summedUp = sumUp(randomID, orders);
	    log.info("Request ID " + randomID + " .Summed up all the orders : " + summedUp);

	    List<OutboundEntity> outbounds = orderDAO.fetchOutbound(center.getCenterID());
	    log.info("Request ID " + randomID + " .Fetched the current outbound for centerID: " + outbounds);

	    Map<String, Float> outboundMap = prepareMap(outbounds);
	    log.info("Request ID " + randomID + " .Prepared category-quantity map for the current outbound : " + outboundMap);

	    Map<String, Float> finalMap = subtract(summedUp, outboundMap);
	    log.info("Request ID " + randomID
		    + " .Prepared final map after subtraction for category-quantity map for the current outbound : " + finalMap);

	    List<NameValuePair> nameValue = prepareNameValue(randomID, finalMap);
	    log.info("Request ID " + randomID + " .Prepared name value pairs : " + nameValue);

	    return nameValue;
	} catch (Exception e) {
	    log.error("Request ID " + randomID + " .Error occurred while fetching outbound orders ", e);
	}
	return null;
    }

    @Override
    public boolean updateOutbound(String randomID, String warehouseName, Map<String, Float> load) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName + " , load : " + load);
	try {

	    CenterEntity center = orderDAO.getCenter(warehouseName);
	    log.info("Request ID " + randomID + " .Fetched center for warehouseName : " + center);

	    List<OutboundEntity> outbounds = orderDAO.fetchOutbound(center.getCenterID());
	    log.info("Request ID " + randomID + " .Fetched the current outbound for centerID: " + outbounds);

	    Map<String, Float> outboundMap = prepareMap(outbounds);
	    log.info("Request ID " + randomID + " .Prepared category-quantity map for the current outbound : " + outboundMap);

	    Map<String, Float> finalMap = add(load, outboundMap);
	    log.info("Request ID " + randomID
		    + " .Prepared final map after subtraction for category-quantity map for the current outbound : " + finalMap);

	    List<OutboundEntity> entityList = new ArrayList<OutboundEntity>();

	    for (Entry<String, Float> entry : finalMap.entrySet()) {
		float val = entry.getValue();
		OutboundEntity oTemp = new OutboundEntity();
		oTemp.setCategoryName(entry.getKey());
		oTemp.setCenterId(center.getCenterID());
		oTemp.setQuantity(val);
		oTemp.setLastUpdated(new Date());
		entityList.add(oTemp);
	    }

	    orderDAO.saveOutbounds(entityList);
	    return true;
	} catch (Exception e) {
	    log.error("Request ID " + randomID + " .Error occurred while updating outbound orders ", e);
	}
	return false;
    }

    private Map<String, Float> add(Map<String, Float> summedUp, Map<String, Float> outboundMap) {
	Map<String, Float> map = new HashMap<String, Float>();
	for (Entry<String, Float> entry : summedUp.entrySet()) {
	    String key = entry.getKey();
	    Float value = entry.getValue();
	    Float outboundValue = outboundMap.get(key);
	    float f = 0;
	    if (value != null) {
		f = value;
		if (outboundValue != null) {
		    f += outboundValue;
		}
	    }
	    map.put(key, f);

	}
	return map;
    }

    private Map<String, Float> subtract(Map<String, Float> summedUp, Map<String, Float> outboundMap) {
	Map<String, Float> map = new HashMap<String, Float>();
	for (Entry<String, Float> entry : summedUp.entrySet()) {
	    String key = entry.getKey();
	    Float value = entry.getValue();
	    Float outboundValue = outboundMap.get(key);
	    float f = 0;
	    if (value != null) {
		f = value;
		if (outboundValue != null) {
		    f -= outboundValue;
		}
	    }
	    map.put(key, f);

	}
	return map;
    }

    private Map<String, Float> prepareMap(List<OutboundEntity> outbounds) {
	Map<String, Float> map = new HashMap<String, Float>();
	for (OutboundEntity ob : outbounds) {
	    map.put(ob.getCategoryName(), ob.getQuantity());
	}
	return map;
    }

    @Override
    public int addWarehouse(String randomID, List<NameValuePair> warehouse) {
	CenterEntity cEntity = new CenterEntity();
	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse);

	Map<String, String> centerMap = Util.prepareMap(warehouse);
	String warehouseName = centerMap.get(Constant.WAREHOUSE);
	cEntity.setAddress(centerMap.get(Constant.ADDRESS));
	cEntity.setCenterName(centerMap.get(Constant.WAREHOUSE));
	cEntity.setCity(centerMap.get(Constant.CITY));
	cEntity.setActive(centerMap.get(Constant.STATUS));
	cEntity.setState(centerMap.get(Constant.STATE));
	cEntity.setPincode(centerMap.get(Constant.PINCODE));

	CenterEntity center = orderDAO.getCenter(warehouseName);
	
	if(center == null)
	    center = orderDAO.getCenterByPin(centerMap.get(Constant.PINCODE));
	
	log.info("Request ID " + randomID + " .Fetched center : " + cEntity);

	if (center != null && center.getPincode().equals(cEntity.getPincode())) {
	    return 0;
	} else {
	    Integer saveCenter = orderDAO.saveCenter(cEntity);
	    log.info("Request ID " + randomID + " .Saved center result: " + saveCenter);
	    return saveCenter;
	}
    }

    private List<NameValuePair> prepareNameValue(String randomID, Map<String, Float> summedUp) {

	List<NameValuePair> listNmVlPair = new ArrayList<NameValuePair>();
	for (Entry<String, Float> entry : summedUp.entrySet()) {
	    listNmVlPair.add(new NameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
	}

	return listNmVlPair;
    }

    private Map<String, Float> sumUp(String randomUUID, Set<Order> orders) {
	Map<String, Float> contentMap = new HashMap<String, Float>();
	for (Order order : orders) {
	    Set<Category> items = order.getItems();
	    for (Category c : items) {
		Float value = contentMap.get(c.getName());
		String quantity = c.getQuantity();
		if (quantity != null && quantity.length() > 0) {
		    quantity = quantity.substring(0, quantity.indexOf(" "));
		    if (value == null) {

			contentMap.put(c.getName(), Float.valueOf(quantity));
		    } else {
			contentMap.put(c.getName(), value + Float.valueOf(quantity));
		    }
		}
	    }
	}

	return contentMap;
    }

    /**
     * Adds content to the orders
     * 
     * @param randomID
     * @param orderList
     * @return
     * @throws Exception
     */
    public Set<Order> process(String randomID, List<OrderEntity> orderList) throws Exception {
	Set<Integer> orderIDs = new HashSet<Integer>();
	Set<Order> orders = new HashSet<Order>();
	for (OrderEntity oEntity : orderList) {
	    orderIDs.add(oEntity.getOrderID());
	}
	List<OrderContentEntity> contentListComplete = orderDAO.fetchOrderContent(orderIDs);
	log.info("Request ID " + randomID + " .Fetched order content for order Ids [ " + orderIDs + " ] : " + contentListComplete);
	if (contentListComplete != null) {

	    List<OrderContentEntity> contentList = null;
	    Order order = null;
	    for (OrderEntity oEntity : orderList) {
		contentList = new ArrayList<OrderContentEntity>();

		for (OrderContentEntity cEntity : contentListComplete) {
		    if (cEntity.getOrderID() == oEntity.getOrderID()) {
			contentList.add(cEntity);
		    }
		}

		order = new Order();
		Util.mapOrderToContent(order, oEntity, orderService.getCategoryDefinitionsMap(randomID), contentList);
		orders.add(order);
	    }

	}

	return orders;
    }

    @Override
    public Map<String, String> getWarehouse(String randomID, String warehouseName) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName);
	CenterEntity center = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center for warehouseName : " + center);
	Map<String, String> map = new HashMap<String, String>();
	map.put("warehouse", center.getCenterName());
	map.put("pincode", center.getPincode());
	map.put("address", center.getAddress());
	map.put("state", center.getState());
	map.put("city", center.getCity());
	map.put("status", center.getActive());
	return map;
    }

    @Override
    public List<String> fetchPincodes(String randomID, String warehouseName) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName);
	List<PincodeEntity> pincodes = orderDAO.getPincodes(warehouseName);
	log.info("Request ID " + randomID + " .Fetched pincodes for warehouse : " + pincodes);

	List<String> pins = new ArrayList<String>();
	for (PincodeEntity pincode : pincodes) {
	    pins.add(pincode.getPincode());
	}

	return pins;
    }

    @Override
    public List<String> fetchDevices(String randomID, String warehouseName, String status) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName);
	List<DeviceEntity> devices = orderDAO.getDevices(warehouseName, status);
	log.info("Request ID " + randomID + " .Fetched devices for warehouse : " + devices);

	List<String> pins = new ArrayList<String>();
	for (DeviceEntity device : devices) {
	    pins.add(device.getCode());
	}

	return pins;
    }

    @Override
    public boolean deletePincodeFromWarehouse(String randomID, String warehouse, String pincode) {
	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse + " , pincode : " + pincode);
	CenterEntity center = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched center for warehouse : " + center);
	boolean result = orderDAO.deletePincodeFromCenter(center.getCenterID(), pincode);
	log.info("Request ID " + randomID + " .Delete pincode from center result : " + result);
	return result;
    }

    @Override
    public boolean addPincodeToWarehouse(String randomID, String warehouse, String pincode) {
	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse + " , pincode : " + pincode);
	CenterEntity center = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched center for warehouse : " + center);
	int id = orderDAO.addPincodeToCenter(center.getCenterID(), pincode);
	log.info("Request ID " + randomID + " .Add pincode to center result : " + id);

	if (id == 0)
	    return true;

	return false;
    }

    @Override
    public boolean transferPincodeToWarehouse(String randomID, String warehouse, String oldWarehouse, String pincode) {
	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse + " , oldWarehouse : " + oldWarehouse
		+ " , pincode : " + pincode);
	CenterEntity centerOld = orderDAO.getCenter(oldWarehouse);
	log.info("Request ID " + randomID + " .Fetched old center for warehouse : " + centerOld);
	CenterEntity centerNew = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched new center for warehouse : " + centerOld);

	if (centerOld != null && centerNew != null) {

	    boolean result = orderDAO.transferPincodeToCenter(centerOld.getCenterID(), centerNew.getCenterID(), pincode);
	    log.info("Request ID " + randomID + " .Transfer pincode to center result : " + result);
	    return result;

	} else {
	    return false;
	}

    }

    @Override
    public int assignDevice(String randomID, String device, int id) {
	log.info("Request ID " + randomID + " .Parameters -> device : " + device + " , id : " + id);
	DeviceEntity deviceEntity = orderDAO.fetchDeviceByUniqueID(device);
	log.info("Request ID " + randomID + " .Fetched device for unique id : " + deviceEntity);
	if (deviceEntity != null) {
	    int assignDevice = orderDAO.assignDevice(deviceEntity, id);
	    log.info("Request ID " + randomID + " .Assign device result : " + assignDevice);
	}
	return 0;
    }

    @Override
    public int updateWarehouse(String randomID, List<NameValuePair> editDetails) {
	log.info("Request ID " + randomID + " .Parameters -> editDetails : " + editDetails);
	Map<String, String> centerMap = Util.prepareMap(editDetails);

	String warehouseName = centerMap.get(Constant.WAREHOUSE);

	CenterEntity cEntity = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center for warehouseName : [ " + warehouseName + " ] center : " + cEntity);

	cEntity.setAddress(centerMap.get(Constant.ADDRESS));
	cEntity.setCenterName(centerMap.get(Constant.OLD_WH_CODE));
	cEntity.setCity(centerMap.get(Constant.CITY));
	cEntity.setActive(centerMap.get(Constant.STATUS));
	cEntity.setState(centerMap.get(Constant.STATE));
	cEntity.setPincode(centerMap.get(Constant.PINCODE));

	Integer updateCenter = orderDAO.updateCenter(cEntity);
	log.info("Request ID " + randomID + " .Update center result : " + updateCenter);
	return updateCenter;
    }

    @Override
    public String addDeviceToWarehouse(String randomID, String warehouse, String addedDevice) {
	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse + " , addedDevice : " + addedDevice);
	CenterEntity center = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched center : " + center);

	if (center != null && addedDevice != null) {
	    addedDevice = addedDevice.toUpperCase();
	    DeviceEntity device = orderDAO.fetchDeviceByUniqueID(addedDevice);
	    log.info("Request ID " + randomID + " .Fetched device by unique ID : " + device);
	    if (device != null && device.getCode().equalsIgnoreCase(addedDevice)) {
		log.info("Request ID " + randomID + " .Device already exists : ");
		return "DEVICE ALREADY EXISTS";
	    }
	    int id = orderDAO.addDeviceToCenter(center.getCenterID(), addedDevice);
	    log.info("Request ID " + randomID + " .Add device to center result : " + id);

	    if (id > 0)
		return "TRUE";
	}
	return "FALSE";

    }

    @Override
    public boolean deleteDeviceFromWarehouse(String randomID, String warehouseName, String device) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName + " , device : " + device);
	CenterEntity center = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center for warehouseName : " + center);
	boolean deleteDeviceFromWarehouse = orderDAO.deleteDeviceFromWarehouse(center.getCenterID(), device);
	log.info("Request ID " + randomID + " .Delete device from center result : " + deleteDeviceFromWarehouse);
	return deleteDeviceFromWarehouse;
    }

    @Override
    public boolean transferDeviceToWarehouse(String randomID, String newWarehouse, String warehouse, String device) {
	log.info("Request ID " + randomID + " .Parameters -> newWarehouse : " + newWarehouse + " , warehouse : " + warehouse
		+ " , device : " + device);

	CenterEntity centerOld = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched old center for warehouse : " + centerOld);

	CenterEntity centerNew = orderDAO.getCenter(newWarehouse);
	log.info("Request ID " + randomID + " .Fetched new center for newWarehouse : " + centerNew);

	if (centerOld != null && centerNew != null) {

	    boolean transferDeviceToCenter = orderDAO.transferDeviceToCenter(centerOld.getCenterID(), centerNew.getCenterID(),
		    device);
	    log.info("Request ID " + randomID + " .Tranfer device to center result : " + transferDeviceToCenter);
	    return transferDeviceToCenter;

	} else {
	    log.info("Request ID " + randomID
		    + " .Cannot transfer device to new center either old center or new center is null : ");
	    return false;
	}

    }

    @Override
    public List<String> fetchExecutives(String randomID, String warehouseName) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName);
	List<FieldExecutiveEntity> executives = orderDAO.getExecutives(warehouseName);
	log.info("Request ID " + randomID + " .Fetched executives for warehouseName : " + executives);

	List<String> executiveList = new ArrayList<String>();
	for (FieldExecutiveEntity executive : executives) {
	    executiveList.add(executive.getName());
	}
	return executiveList;
    }

    @Override
    public String addFEToWarehouse(String randomID, String warehouse, String addedFE) {
	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse + " , addedFE : " + addedFE);
	CenterEntity center = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched center  for warehouse : " + center);

	if (center != null && addedFE != null) {
	    addedFE = addedFE.toUpperCase();
	    int id = orderDAO.addFEToCenter(center.getCenterID(), addedFE);
	    log.info("Request ID " + randomID + " .Add FE to center result : " + id);
	    if (id > 0)
		return "TRUE";
	}
	return "FALSE";
    }

    @Override
    public boolean transferFEToWarehouse(String randomID, String newWarehouse, String warehouse, String fe) {
	try {

	    log.info("Request ID " + randomID + " .Parameters -> newWarehouse : " + newWarehouse + " , warehouse : " + warehouse
		    + " , fe : " + fe);
	    CenterEntity centerOld = orderDAO.getCenter(warehouse);
	    log.info("Request ID " + randomID + " .Fetched old center  for warehouse : " + centerOld);

	    CenterEntity centerNew = orderDAO.getCenter(newWarehouse);
	    log.info("Request ID " + randomID + " .Fetched new center  for newWarehouse : " + centerNew);

	    if (centerOld != null && centerNew != null) {
		boolean transferFEToCenter = orderDAO.transferFEToCenter(centerOld.getCenterID(), centerNew.getCenterID(), fe);
		log.info("Request ID " + randomID + " .Transfer FE to new center result : " + transferFEToCenter);
		return transferFEToCenter;
	    } else {
		return false;
	    }
	} catch (Exception e) {

	}
	return false;
    }

    @Override
    public List<DeviceAssignment> getAssignedDeviceFEList(String randomID, String warehouse) {

	log.info("Request ID " + randomID + " .Parameters -> warehouse : " + warehouse);

	CenterEntity center = orderDAO.getCenter(warehouse);
	log.info("Request ID " + randomID + " .Fetched center for warehouse : " + center);
	List<DeviceAssignment> deviceAssignments = new ArrayList<DeviceAssignment>();

	if (center != null) {
	    List<DeviceEntity> devices = orderDAO.assignedDevicesInCenter(center.getCenterID());
	    log.info("Request ID " + randomID + " .Fetched devices for center : " + devices);

	    List<Integer> deviceIds = new ArrayList<Integer>();
	    Map<Integer, String> deviceIDMap = new HashMap<Integer, String>();
	    for (DeviceEntity d : devices) {
		deviceIDMap.put(d.getDeviceID(), d.getCode());
		deviceIds.add(d.getDeviceID());
	    }

	    if (deviceIds.size() > 0) {
		List<AssignDeviceEntity> assignments = orderDAO.deviceAssignment(deviceIds);
		log.info("Request ID " + randomID + " .Fetched assignments for deviceIds : [ " + deviceIds + " ] , "
			+ assignments);
		List<Integer> feIDS = new ArrayList<Integer>();
		for (AssignDeviceEntity de : assignments) {
		    feIDS.add(de.getUserID());
		}

		if (feIDS.size() > 0) {
		    Map<Integer, String> feIDMap = new HashMap<Integer, String>();
		    List<FieldExecutiveEntity> fes = userDAO.fetchFEByID(feIDS);
		    log.info("Request ID " + randomID + " .Fetched FEs by IDs : [ " + feIDS + " ] , " + fes);
		    for (FieldExecutiveEntity f : fes) {
			feIDMap.put(f.getId(), f.getName());
		    }

		    for (AssignDeviceEntity ad : assignments) {
			DeviceAssignment da = new DeviceAssignment();
			da.setDevice(deviceIDMap.get(ad.getDeviceID()));
			da.setName(feIDMap.get(ad.getUserID()));
			da.setDeviceID(ad.getDeviceID());
			da.setFe(ad.getUserID());
			da.setAssignmentID(ad.getId());
			deviceAssignments.add(da);

		    }
		}
	    }
	}

	return deviceAssignments;
    }

    @Override
    public Integer deleteAssignment(String randomID, DeviceAssignment assignment) {
	log.info("Request ID " + randomID + " .Parameters -> assignment : " + assignment);
	boolean deleteAssigment = orderDAO.deleteAssigment(assignment);
	log.info("Request ID " + randomID + " .Delete device assignment result : " + deleteAssigment);
	if (deleteAssigment) {
	    boolean deAssignDevice = orderDAO.deAssignDevice(assignment);
	    log.info("Request ID " + randomID + " .Deassign device from FE result : " + deAssignDevice);
	    if (deAssignDevice)
		return 1;
	}

	return null;
    }

    @Override
    public boolean deleteFEFromWarehouse(String randomID, String warehouseName, String fe) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName + " , fe : " + fe);

	CenterEntity center = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center for warehouseName : " + center);
	boolean delete = orderDAO.deleteFEFromWarehouse(center.getCenterID(), fe);
	log.info("Request ID " + randomID + " .Delete FE from center result : " + delete);
	return delete;
    }

    @Override
    public boolean pickUp(String randomID, String username, String orderId, String fe, String warehouse, String billAmount,
	    String deviceID, Map<String, String> quantities) {

	log.info("Request ID " + randomID + " .Parameters -> username : " + username + " , orderId : " + orderId + " , fe : "
		+ fe + " , warehouse : " + warehouse + " , billAmount : " + billAmount + " , deviceID : " + deviceID
		+ " , quantities : " + quantities);

	boolean pickingOrder = false;
	try {
	    int oId = Integer.parseInt(orderId);
	    OrderEntity orderEntity = orderDAO.fetchOrderByID(oId);
	    log.info("Request ID " + randomID + " .Fetched order By Id : " + orderEntity);
	    if (orderEntity != null) {
		CenterEntity center = orderDAO.getCenter(warehouse);
		log.info("Request ID " + randomID + " .Fetched center By warehouse : " + center);

		if (center != null) {
		    List<DeviceEntity> devices = orderDAO.fetchDevicesForCenter(center.getCenterID(), null);
		    log.info("Request ID " + randomID + " .Fetched devices for center : " + devices);
		    int deviceId = -1;
		    for (DeviceEntity d : devices) {
			if (d.getCode().equalsIgnoreCase(deviceID)) {
			    deviceId = d.getDeviceID();
			    break;
			}
		    }

		    if (deviceId > -1) {
			pickingOrder = pickingOrder(randomID, username, warehouse, billAmount, deviceId, quantities, oId,
				orderEntity, center);
			log.info("Request ID " + randomID + " .Picking order result : " + pickingOrder);
		    } else {
			log.warn("Request ID " + randomID + "The device " + deviceID + " entered doesn't belong to warehouse "
				+ warehouse);
		    }
		}

	    } else {
		log.warn("Request ID " + randomID
			+ "Trying to pick up a wrong order. The order in DB is not matching the order ID passed " + orderId);
	    }

	} catch (Exception e) {
	    pickingOrder = false;
	    log.error("Request ID " + randomID + "Error occurred while picking up order", e);
	}
	return pickingOrder;
    }

    @Transactional
    private boolean pickingOrder(String randomID, String username, String warehouse, String billAmount, int deviceID,
	    Map<String, String> quantities, int oId, OrderEntity orderEntity, CenterEntity center) {
	boolean status = false;
	try {
	    if (orderEntity.getCenterID() == center.getCenterID()) {
		float bAmount = Float.parseFloat(billAmount);
		orderEntity.setAmount(bAmount);
		orderEntity.setDeviceID(deviceID);
		orderEntity.setStatus(OrderStatus.PICKED.name());
		orderEntity.setLastUpdated(new Date());
		orderEntity.setUpdatedBy(username);

		boolean updateOrder = orderDAO.updateOrder(orderEntity);
		log.info("Request ID " + randomID + " .Update order result : " + updateOrder);
		if (updateOrder) {
		    List<CategoryEntity> categories = orderDAO.fetchCategoryDefition();
		    log.info("Request ID " + randomID + " .Fetched category definitions : " + categories);
		    Map<String, CategoryEntity> map = categoryMap(categories);
		    List<OrderContentEntity> entities = new ArrayList<OrderContentEntity>();
		    for (Entry<String, String> entry : quantities.entrySet()) {
			OrderContentEntity entity = new OrderContentEntity();
			entity.setOrderID(oId);
			entity.setCategoryID(map.get(entry.getKey()).getCategoryID());
			entity.setQuantity(Float.parseFloat(entry.getValue()));
			entity.setCreateDate(new Date());
			entity.setUpdateDate(new Date());
			entities.add(entity);
		    }
		    status = orderDAO.saveOrderContents(entities);
		    log.info("Request ID " + randomID + " .Save order contents result : " + status);
		}
	    } else {
		log.warn("Request ID " + randomID + "Trying to pick up a wrong order. No warehouse with name " + warehouse
			+ " found in the system, or the center of the order doesn't match the center of the agent picking it up.");
		status = false;
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomID + "Error occurred while updatinng the status to picked.", e);
	    status = false;
	}
	return status;
    }

    private Map<String, CategoryEntity> categoryMap(List<CategoryEntity> list) {
	Map<String, CategoryEntity> map = new HashMap<String, CategoryEntity>();
	for (CategoryEntity c : list) {
	    map.put(c.getCategoryName(), c);
	}
	return map;
    }

    @Override
    public String fetchDeviceForFE(String randomID, String warehouseName, String feName) {
	log.info("Request ID " + randomID + " .Parameters -> warehouseName : " + warehouseName + " , feName : " + feName);

	CenterEntity center = orderDAO.getCenter(warehouseName);
	log.info("Request ID " + randomID + " .Fetched center for warehouseName : " + center);

	List<FieldExecutiveEntity> fes = userDAO.fetchFieldExecutive(center.getCenterID(), "TRUE");
	log.info("Request ID " + randomID + " .Fetched fes list for center : " + fes);

	for (FieldExecutiveEntity fe : fes) {
	    if (feName.equals(fe.getName()) && "TRUE".equals(fe.getAssigned())) {
		// fetch the assignment for this guy.
		AssignDeviceEntity assignment = orderDAO.fetchDeviceAssignment(fe.getId());
		log.info("Request ID " + randomID + " .Fetched assignment for FE id : " + assignment);
		if (assignment == null) {
		    log.warn("Request ID " + randomID + " .Device Assignment for the FE is null.");
		    return null;
		} else {
		    int deviceID = assignment.getDeviceID();
		    if (deviceID > 0) {
			DeviceEntity device = orderDAO.fetchDeviceById(deviceID);
			log.debug("Request ID " + randomID + " .Fetched device entity for the deviceID." + device);
			if (device != null) {
			    return device.getCode();
			}
		    }
		}
	    }
	}

	return null;
    }
}
