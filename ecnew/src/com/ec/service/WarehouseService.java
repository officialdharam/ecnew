package com.ec.service;

import java.util.List;
import java.util.Map;

import com.ec.dto.DeviceAssignment;
import com.ec.dto.NameValuePair;

public interface WarehouseService {

    public List<NameValuePair> fetchInbound(String randomID, String warehouseName);

    public List<NameValuePair> fetchOutbound(String randomID, String warehouseName);

    public int addWarehouse(String randomID, List<NameValuePair> warehouse);

    public boolean deletePincodeFromWarehouse(String randomID, String warehouse, String pincode);

    public boolean addPincodeToWarehouse(String randomID, String warehouse, String pincode);

    public boolean transferPincodeToWarehouse(String randomID, String warehouse, String oldWarehouse, String pincode);

    public Map<String, String> getWarehouse(String randomID, String warehouseName);

    public List<String> fetchPincodes(String randomID, String warehouseName);

    public int assignDevice(String randomID, String device, int id);

    public int updateWarehouse(String randomID, List<NameValuePair> editDetails);

    public List<String> fetchDevices(String randomID, String warehouseName, String status);

    public String addDeviceToWarehouse(String randomID, String warehouse, String addedDevice);

    public boolean deleteDeviceFromWarehouse(String randomID, String warehouseName, String device);

    public boolean transferDeviceToWarehouse(String randomID, String newWarehouse, String warehouse, String device);

    public List<String> fetchExecutives(String randomID, String warehouseName);

    public String addFEToWarehouse(String randomID, String warehouse, String addedFE);

    public boolean transferFEToWarehouse(String randomID, String newWarehouse, String warehouse, String fe);

    public List<DeviceAssignment> getAssignedDeviceFEList(String randomID, String warehouse);

    public Integer deleteAssignment(String randomID, DeviceAssignment assignment);

    public boolean deleteFEFromWarehouse(String randomID, String warehouseName, String fe);

    public boolean pickUp(String randomID, String username, String orderId, String fe, String warehouse, String billAmount,
	    String deviceID, Map<String, String> quantities);

    public String fetchDeviceForFE(String randomUUID, String warehouseName, String feName);

    public boolean updateOutbound(String randomID, String warehouseName, Map<String, Float> load);
}
