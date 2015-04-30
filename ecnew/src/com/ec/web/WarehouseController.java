package com.ec.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ec.dto.DeviceAssignment;
import com.ec.dto.NameValuePair;
import com.ec.dto.Order;
import com.ec.response.Response;
import com.ec.service.OrderService;
import com.ec.service.WarehouseService;
import com.ec.util.Constant;
import com.ec.util.Util;

@Controller
public class WarehouseController extends BaseController {

    public static Logger log = Logger.getLogger(WarehouseController.class);

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private OrderService orderService;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/editDetails", method = RequestMethod.POST)
    public @ResponseBody
    Response<Void> editDetails(HttpServletRequest request, @RequestBody List<NameValuePair> editDetails) {
	Response<Void> resp = new Response<Void>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for editDetails : " + editDetails);
	try {

	    int warehouseID = warehouseService.updateWarehouse(randomUUID, editDetails);
	    log.info("Request ID " + randomUUID + ". Updated details status : " + warehouseID);

	    if (warehouseID < 1) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.info("Request ID " + randomUUID + ". Error occurred while updating details : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/device", method = RequestMethod.GET)
    public @ResponseBody
    Response<String> fetchDeviceForFE(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName,
	    @RequestParam(value = "feName") String feName) {
	Response<String> resp = new Response<String>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for fetchDeviceForFE : warehouseName " + warehouseName
		+ ", feName " + feName);
	try {

	    String deviceID = warehouseService.fetchDeviceForFE(randomUUID, warehouseName, feName);
	    log.info("Request ID " + randomUUID + ". Fetched deviceID for warehouse and feName : " + deviceID);

	    if (!Util.nullOrEmtpy(deviceID)) {
		List<String> results = new ArrayList<String>();
		results.add(deviceID);
		resp.setResults(results);
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    } else {
		List<String> results = warehouseService.fetchDevices(randomUUID, warehouseName, "FALSE");
		log.info("Request ID " + randomUUID
			+ ". No device returned for the fe and warehosue combination. Fetched devices for warehouse : " + results);
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		resp.setResults(results);
	    }
	} catch (Exception e) {
	    log.info("Request ID " + randomUUID + ". Error occurred while updating details : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @RequestMapping(value = "/warehouse/orders/{deviceID}", method = RequestMethod.GET)
    public @ResponseBody
    Response<Order> getOrderByDevice(@PathVariable(value = "deviceID") String deviceID) throws Exception {
	Response<Order> resp = new Response<Order>();
	String randomUUID = UUID.randomUUID().toString();

	log.info("Request ID " + randomUUID + ". Received a request for getOrderByDevice : " + deviceID);
	Set<Order> deviceOrders = orderService.listOrdersPickedByDevice(randomUUID, deviceID);
	log.info("Request ID " + randomUUID + ". Fetched orders for device ID : " + deviceOrders);

	List<Order> orderList = new ArrayList<Order>();

	if (deviceOrders != null) {
	    orderList.addAll(deviceOrders);
	    resp.setResults(orderList);
	    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	} else {
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	}

	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/pickOrder", method = RequestMethod.POST)
    public String pickOrder(HttpServletRequest request) {
	Response<Void> resp = new Response<Void>();
	boolean pickUp = false;
	String warehouse = null;
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for pickOrder : ");
	try {

	    HttpSession session = request.getSession();
	    if (session == null) {
		log.warn("Request ID " + randomUUID + ". No valid session exists : ");
		throw new Exception("No session exists");
	    }

	    String username = (String) session.getAttribute(Util.USERNAME);

	    String orderId = request.getParameter("orderId");
	    String fe = request.getParameter("fe");
	    warehouse = request.getParameter("warehouse");
	    String billAmount = request.getParameter("billAmount");
	    String deviceId = request.getParameter("deviceID");

	    String newsPaper = request.getParameter("newsPaper");
	    String books = request.getParameter("books");
	    String iron = request.getParameter("iron");
	    String plastic = request.getParameter("plastic");
	    String gBottle = request.getParameter("gBottle");
	    String bBottle = request.getParameter("bBottle");
	    String carton = request.getParameter("carton");
	    String aluminium = request.getParameter("aluminium");
	    String steel = request.getParameter("steel");
	    String copper = request.getParameter("copper");
	    String brass = request.getParameter("brass");

	    Map<String, String> quantities = new HashMap<String, String>();

	    if (!Util.nullOrEmtpy(orderId) && !Util.nullOrEmtpy(fe) && !Util.nullOrEmtpy(warehouse)
		    && !Util.nullOrEmtpy(billAmount)) {
		if (!Util.nullOrEmtpy(newsPaper)) {
		    quantities.put(Constant.NEWS_PAPER, newsPaper);
		}
		if (!Util.nullOrEmtpy(books)) {
		    quantities.put(Constant.BOOKS_N_COPIES, books);
		}
		if (!Util.nullOrEmtpy(iron)) {
		    quantities.put(Constant.IRON, iron);
		}
		if (!Util.nullOrEmtpy(plastic)) {
		    quantities.put(Constant.PLASTIC, plastic);
		}
		if (!Util.nullOrEmtpy(gBottle)) {
		    quantities.put(Constant.GLASS_BOTTLE, gBottle);
		}
		if (!Util.nullOrEmtpy(bBottle)) {
		    quantities.put(Constant.BEER_BOTTLE, bBottle);
		}
		if (!Util.nullOrEmtpy(carton)) {
		    quantities.put(Constant.CARTON, carton);
		}
		if (!Util.nullOrEmtpy(aluminium)) {
		    quantities.put(Constant.ALUMINIUM, aluminium);
		}
		if (!Util.nullOrEmtpy(steel)) {
		    quantities.put(Constant.STEEL, steel);
		}
		if (!Util.nullOrEmtpy(copper)) {
		    quantities.put(Constant.COPPER, copper);
		}
		if (!Util.nullOrEmtpy(brass)) {
		    quantities.put(Constant.BRASS, brass);
		}

		pickUp = warehouseService.pickUp(randomUUID, username, orderId, fe, warehouse, billAmount, deviceId, quantities);
		log.info("Request ID " + randomUUID + ". Picked up order status : " + pickUp);

	    }

	    if (pickUp) {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.info("Request ID " + randomUUID + ". Error occurred while picking up order : ", e);
	}

	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=ops.jsp&warehouse=" + warehouse + "&addDeviceResult=" + pickUp);
	return "redirect:/service/navigate?nextPage=ops.jsp&warehouse=" + warehouse + "&addDeviceResult=" + pickUp;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/addPincode", method = RequestMethod.POST)
    public String addPincode(HttpServletRequest request) {
	String warehouse = request.getParameter("warehouse");
	String addedPincode = request.getParameter("addedPincode");
	boolean result = false;

	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for addPincode : ");

	if (warehouse != null && addedPincode != null) {
	    warehouse = warehouse.trim();
	    addedPincode = addedPincode.trim();
	    result = warehouseService.addPincodeToWarehouse(randomUUID, warehouse, addedPincode);
	    log.info("Request ID " + randomUUID + ". Add pincode to warehouse result : " + result);
	}

	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=managePincode.jsp&warehouse=" + warehouse + "&addPincodeResult=" + result);
	return "redirect:/service/navigate?nextPage=managePincode.jsp&warehouse=" + warehouse + "&addPincodeResult=" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/addDevice", method = RequestMethod.POST)
    public String addDevice(HttpServletRequest request) {
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for addDevice : ");

	String result = "";
	String warehouse = request.getParameter("warehouse");
	String addedDevice = request.getParameter("addedDevice");
	if (warehouse != null && addedDevice != null) {
	    warehouse = warehouse.trim();
	    addedDevice = addedDevice.trim();
	    result = warehouseService.addDeviceToWarehouse(randomUUID, warehouse, addedDevice);
	    log.info("Request ID " + randomUUID + ". Add device to warehouse status : " + result);
	}

	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=manageDevice.jsp&warehouse=" + warehouse + "&addDeviceResult=" + result);
	return "redirect:/service/navigate?nextPage=manageDevice.jsp&warehouse=" + warehouse + "&addDeviceResult=" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/transferPincode", method = RequestMethod.POST)
    public String transferPincode(HttpServletRequest request) {
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for transferPincode : ");

	String warehouse = request.getParameter("warehouse");
	String pincode = request.getParameter("pincode");
	String newWarehouse = request.getParameter("whNew");
	boolean result = false;
	if (warehouse != null && pincode != null && newWarehouse != null) {
	    warehouse = warehouse.trim();
	    pincode = pincode.trim();
	    newWarehouse = newWarehouse.trim();
	    result = warehouseService.transferPincodeToWarehouse(randomUUID, newWarehouse, warehouse, pincode);
	    log.info("Request ID " + randomUUID + ".Transfer pincode status : " + result);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=managePincode.jsp&warehouse=" + warehouse + "&transferPincodeResult="
		+ result);
	return "redirect:/service/navigate?nextPage=managePincode.jsp&warehouse=" + warehouse + "&transferPincodeResult="
		+ result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/deletePincode", method = RequestMethod.GET)
    public @ResponseBody
    String deletePincode(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName,
	    @RequestParam(value = "pincode") String pincode) {
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for deletePincode : ");

	boolean result = warehouseService.deletePincodeFromWarehouse(randomUUID, warehouseName, pincode);
	log.info("Request ID " + randomUUID + ". Delete pincode status : " + result);
	log.info("Request ID " + randomUUID + ". Successfully responding ");
	return "" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/fetchPincodes", method = RequestMethod.GET)
    public @ResponseBody
    Response<String> fetchPincodes(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName) {
	Response<String> resp = new Response<String>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for fetchPincodes for warehouseName : " + warehouseName);
	try {

	    List<String> pins = warehouseService.fetchPincodes(randomUUID, warehouseName);
	    log.info("Request ID " + randomUUID + ". Fetched pincodes : " + pins);

	    if (pins == null || pins.size() < 0) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		resp.setResults(pins);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while fetching fetchPincodes for warehouseName : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/fetchDevices", method = RequestMethod.GET)
    public @ResponseBody
    Response<String> fetchDevices(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName) {
	Response<String> resp = new Response<String>();
	String randomUUID = UUID.randomUUID().toString();
	try {
	    log.info("Request ID " + randomUUID + ". Received a request for fetchDevices for warehouseName : " + warehouseName);
	    List<String> devices = warehouseService.fetchDevices(randomUUID, warehouseName, null);
	    log.info("Request ID " + randomUUID + ". Fetched devices for warehouse : " + devices);

	    if (devices == null || devices.size() < 0) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		resp.setResults(devices);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while fetching devices for warehouseName : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/add", method = RequestMethod.POST)
    public @ResponseBody
    Response<String> addWarehouse(HttpServletRequest request, @RequestBody List<NameValuePair> warehouse) {
	Response<String> resp = new Response<String>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for addWarehouse  : " + warehouse);
	try {

	    int warehouseID = warehouseService.addWarehouse(randomUUID, warehouse);
	    log.info("Request ID " + randomUUID + ". Add warehouse id  : " + warehouseID);

	    if (warehouseID < 1) {
		resp.setStatusMsg(Constant.CENTER_ALREADY_EXISTS);
		List<String> warehouseIDList = new ArrayList<String>();
		resp.setResults(warehouseIDList);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		List<String> warehouseIDList = new ArrayList<String>();
		warehouseIDList.add(String.valueOf(warehouseID));
		resp.setResults(warehouseIDList);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while adding warehouse  : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/deleteDevice", method = RequestMethod.GET)
    public @ResponseBody
    String deleteDevice(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName,
	    @RequestParam(value = "device") String device) {

	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for deleteDevice  : with warehouseName " + warehouseName
		+ " and device " + device);

	boolean result = warehouseService.deleteDeviceFromWarehouse(randomUUID, warehouseName, device);
	log.info("Request ID " + randomUUID + ". Delete device status : " + result);
	log.info("Request ID " + randomUUID + ". Successfully responding ");
	return "" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/transferDevice", method = RequestMethod.POST)
    public String transferDevice(HttpServletRequest request) {
	String warehouse = request.getParameter("warehouse");
	String device = request.getParameter("device");
	String newWarehouse = request.getParameter("whNew");
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for transferDevice  : ");

	boolean result = false;
	if (warehouse != null && device != null && newWarehouse != null) {
	    warehouse = warehouse.trim();
	    device = device.trim();
	    newWarehouse = newWarehouse.trim();
	    result = warehouseService.transferDeviceToWarehouse(randomUUID, newWarehouse, warehouse, device);
	    log.info("Request ID " + randomUUID + ". Transfer device status : " + result);
	}

	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=manageDevice.jsp&warehouse=" + warehouse + "&transferDeviceResult="
		+ result);

	return "redirect:/service/navigate?nextPage=manageDevice.jsp&warehouse=" + warehouse + "&transferDeviceResult=" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/device/assign", method = RequestMethod.POST)
    public @ResponseBody
    Response<Void> assignDevice(HttpServletRequest request, @RequestBody DeviceAssignment assignment) {
	Response<Void> resp = new Response<Void>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for assignDevice  : " + assignment);
	try {

	    int assignmentID = warehouseService.assignDevice(randomUUID, assignment.getDevice(), assignment.getFe());
	    log.info("Request ID " + randomUUID + ". Assignment ID : " + assignmentID);

	    if (assignmentID == 0) {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.info("Request ID " + randomUUID + ". Error occurred while assigning device ", e);
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/reconcile/inbound", method = RequestMethod.GET)
    public @ResponseBody
    Response<NameValuePair> getInbound(HttpServletRequest request) throws Exception {
	Response<NameValuePair> resp = new Response<NameValuePair>();
	List<NameValuePair> fetchInbound = new ArrayList<NameValuePair>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for getInbound  : ");
	if (request != null) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
		String warehouseName = (String) request.getSession().getAttribute("centerName");
		fetchInbound = warehouseService.fetchInbound(randomUUID, warehouseName);
		log.info("Request ID " + randomUUID + ". Fetched Inbound  : " + fetchInbound);

		if (fetchInbound != null) {
		    resp.setResults(fetchInbound);
		    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		} else {
		    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		    resp.setResults(fetchInbound);
		}
	    } else {
		log.error("Request ID " + randomUUID + ". No session exists : ");
		throw new Exception("No session exists");
	    }
	} else {
	    log.error("Request ID " + randomUUID + ". No session exists: ");
	}

	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/reconcile/outbound", method = RequestMethod.GET)
    public @ResponseBody
    Response<NameValuePair> getOutbound(HttpServletRequest request) throws Exception {
	Response<NameValuePair> resp = new Response<NameValuePair>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for getOutbound  : ");
	if (request != null) {
	    HttpSession session = request.getSession(false);

	    if (session != null) {
		String warehouseName = (String) request.getSession().getAttribute("centerName");

		List<NameValuePair> fetchInbound = warehouseService.fetchOutbound(randomUUID, warehouseName);
		log.info("Request ID " + randomUUID + ". Fetched Outbound  : " + fetchInbound);

		if (fetchInbound != null) {
		    resp.setResults(fetchInbound);
		    resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		} else {
		    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		}
	    } else {
		log.error("Request ID " + randomUUID + ". No session exists: ");
		throw new Exception("No session exists");
	    }
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/fetchExecutives", method = RequestMethod.GET)
    public @ResponseBody
    Response<String> fetchExecutives(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName) {
	Response<String> resp = new Response<String>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for fetchExecutives for warehouse : " + warehouseName);
	try {

	    List<String> executives = new ArrayList<String>();

	    executives = warehouseService.fetchExecutives(randomUUID, warehouseName);
	    log.info("Request ID " + randomUUID + ". Fetched executives  : " + executives);

	    if (executives == null || executives.size() < 0) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
		resp.setResults(executives);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		resp.setResults(executives);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while fetching executives ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/addFE", method = RequestMethod.POST)
    public String addFE(HttpServletRequest request) {
	String warehouse = request.getParameter("warehouse");
	String addedFE = request.getParameter("addedFE");
	String result = "";
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for addFE for warehouse : ");
	if (warehouse != null && addedFE != null) {
	    warehouse = warehouse.trim();
	    addedFE = addedFE.trim();
	    result = warehouseService.addFEToWarehouse(randomUUID, warehouse, addedFE);
	    log.info("Request ID " + randomUUID + ". Add FE to warehouse result  : " + result);
	}

	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=manageFE.jsp&warehouse=" + warehouse + "&addFEResult=" + result);
	return "redirect:/service/navigate?nextPage=manageFE.jsp&warehouse=" + warehouse + "&addFEResult=" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/deleteFE", method = RequestMethod.GET)
    public @ResponseBody
    String deleteFE(HttpServletRequest request, @RequestParam(value = "warehouseName") String warehouseName,
	    @RequestParam(value = "executives") String fe) {
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for deleteFE for warehouse : " + warehouseName);
	boolean result = warehouseService.deleteFEFromWarehouse(randomUUID, warehouseName, fe);
	log.info("Request ID " + randomUUID + ". Delete FE to warehouse result  : " + result);
	log.info("Request ID " + randomUUID + ". Successfully responding ");
	return "" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/transferFE", method = RequestMethod.POST)
    public String transferFE(HttpServletRequest request) {
	String warehouse = request.getParameter("warehouse");
	String fe = request.getParameter("fe");
	String newWarehouse = request.getParameter("whNew");
	boolean result = false;

	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for transferFE : ");
	if (warehouse != null && fe != null && newWarehouse != null) {
	    warehouse = warehouse.trim();
	    fe = fe.trim();
	    newWarehouse = newWarehouse.trim();
	    result = warehouseService.transferFEToWarehouse(randomUUID, newWarehouse, warehouse, fe);
	    log.info("Request ID " + randomUUID + ". Transfer FE to warehouse result  : " + result);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding "
		+ "redirect:/service/navigate?nextPage=manageFE.jsp&warehouse=" + warehouse + "&transferFEResult=" + result);
	return "redirect:/service/navigate?nextPage=manageFE.jsp&warehouse=" + warehouse + "&transferFEResult=" + result;

    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/deviceAssignments", method = RequestMethod.GET)
    public @ResponseBody
    Response<DeviceAssignment> deviceAssignments(HttpServletRequest request,
	    @RequestParam(value = "warehouseName") String warehouseName) {
	Response<DeviceAssignment> resp = new Response<DeviceAssignment>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for deviceAssignments for warehouse : " + warehouseName);

	try {

	    List<DeviceAssignment> deviceAssignments = new ArrayList<DeviceAssignment>();
	    deviceAssignments = warehouseService.getAssignedDeviceFEList(randomUUID, warehouseName);
	    log.info("Request ID " + randomUUID + ". Fetched device Assigment  : " + deviceAssignments);
	    if (deviceAssignments == null || deviceAssignments.size() == 0) {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		resp.setResults(deviceAssignments);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
		resp.setResults(deviceAssignments);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while fetching device assignments  : ", e);
	    resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/deassign", method = RequestMethod.POST)
    public @ResponseBody
    Response<Void> deassign(HttpServletRequest request, @RequestBody DeviceAssignment assignment) {
	Response<Void> resp = new Response<Void>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for deassign for assignment : " + assignment);
	try {

	    Integer deleted = warehouseService.deleteAssignment(randomUUID, assignment);
	    log.info("Request ID " + randomUUID + ". Delete assignment result : " + deleted);
	    if (deleted == null || deleted < 1) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while de assigning : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/warehouse/buildload", method = RequestMethod.POST)
    public @ResponseBody
    Response<Void> buildload(HttpServletRequest request, @RequestBody List<NameValuePair> reqBody) {
	Response<Void> resp = new Response<Void>();
	String randomUUID = UUID.randomUUID().toString();
	log.info("Request ID " + randomUUID + ". Received a request for building load : ");
	try {
	    String warehouse = null;
	    Map<String, Float> quantities = new HashMap<String, Float>();
	    for (NameValuePair pair : reqBody) {

		if ("warehouse".equals(pair.getName())) {
		    warehouse = pair.getValue();
		}
		if ("newsPaper".equals(pair.getName())) {
		    quantities.put(Constant.NEWS_PAPER, getFloat(pair.getValue(), randomUUID));
		}
		if ("books".equals(pair.getName())) {
		    quantities.put(Constant.BOOKS_N_COPIES, getFloat(pair.getValue(), randomUUID));
		}
		if ("iron".equals(pair.getName())) {
		    quantities.put(Constant.IRON, getFloat(pair.getValue(), randomUUID));
		}
		if ("plastic".equals(pair.getName())) {
		    quantities.put(Constant.PLASTIC, getFloat(pair.getValue(), randomUUID));
		}
		if ("gBottle".equals(pair.getName())) {
		    quantities.put(Constant.GLASS_BOTTLE, getFloat(pair.getValue(), randomUUID));
		}
		if ("bBottle".equals(pair.getName())) {
		    quantities.put(Constant.BEER_BOTTLE, getFloat(pair.getValue(), randomUUID));
		}
		if ("carton".equals(pair.getName())) {
		    quantities.put(Constant.CARTON, getFloat(pair.getValue(), randomUUID));
		}
		if ("aluminium".equals(pair.getName())) {
		    quantities.put(Constant.ALUMINIUM, getFloat(pair.getValue(), randomUUID));
		}
		if ("steel".equals(pair.getName())) {
		    quantities.put(Constant.STEEL, getFloat(pair.getValue(), randomUUID));
		}
		if ("copper".equals(pair.getName())) {
		    quantities.put(Constant.COPPER, getFloat(pair.getValue(), randomUUID));
		}
		if ("brass".equals(pair.getName())) {
		    quantities.put(Constant.BRASS, getFloat(pair.getValue(), randomUUID));
		}
	    }

	    boolean result = warehouseService.updateOutbound(randomUUID, warehouse, quantities);
	    log.info("Request ID " + randomUUID + ". Update outbound result : " + result);
	    if (!result) {
		resp.setStatusMsg(Constant.FAILURE_RESPONSE_MGS);
	    } else {
		resp.setStatusMsg(Constant.SUCCESS_RESPONSE_MGS);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomUUID + ". Error occurred while de assigning : ", e);
	}
	log.info("Request ID " + randomUUID + ". Successfully responding " + resp);
	return resp;
    }

    private float getFloat(String s, String randomID) {
	float f = 0;
	try {
	    if (!Util.nullOrEmtpy(s)) {
		f = Float.parseFloat(s);
	    }
	} catch (Exception e) {
	    log.error("Request ID " + randomID + ". parse error for float : " + s, e);
	}
	return f;
    }
}