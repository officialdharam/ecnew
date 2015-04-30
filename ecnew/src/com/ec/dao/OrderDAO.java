package com.ec.dao;

import java.util.List;
import java.util.Set;

import com.ec.dto.DeviceAssignment;
import com.ec.entity.AssignDeviceEntity;
import com.ec.entity.CategoryEntity;
import com.ec.entity.CenterEntity;
import com.ec.entity.DeviceEntity;
import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.OrderContentEntity;
import com.ec.entity.OrderEntity;
import com.ec.entity.OutboundEntity;
import com.ec.entity.PincodeEntity;

public interface OrderDAO {

    public Integer saveOrder(OrderEntity orderEntity);
    
    public boolean updateOrder(OrderEntity orderEntity);
    
    public OrderEntity fetchOrderByID(int orderID);
    
    public List<OrderEntity> fetchOrdersByDeviceIDAndStatus(int deviceID, String status);
    
    public List<OrderEntity> fetchOrdersByCustomerID(int customerID);
    
    public List<OrderEntity> fetchOrdersByStatus(String status, long seconds, int centerID);
    
    public List<OrderEntity> fetchOrderByIds(List<Integer> ids);
    
    public List<OrderContentEntity> fetchOrderContent(Set<Integer> orderIDs);

    public List<CategoryEntity> fetchCategoryDefition();
    
    public Integer saveCenter(CenterEntity centerEntity);
    
    public CenterEntity getCenter(String warehouseName);
    
    public CenterEntity getCenterByPin(String pincode);
    
    public List<CenterEntity> getCenters(String ...warehouseName);
    
    public boolean deletePincodeFromCenter(int center, String pincode);
    
    public int addPincodeToCenter(int center, String pincode);
    
    public boolean transferPincodeToCenter(int centerOld, int centerNew, String pincode);
    
    public List<CenterEntity> getCenters(String active);
    
    public List<PincodeEntity> getPincodes(String warehouseName);
    
    public List<DeviceEntity> fetchDevicesForCenter(int centerID, String object);
    
    public DeviceEntity fetchDeviceByUniqueID(String id);

    public int assignDevice(DeviceEntity deviceEntity, int id);

    public Integer updateCenter(CenterEntity centerEntity);

    public List<DeviceEntity> getDevices(String warehouseName, String status);

    public int addDeviceToCenter(int centerID, String addedDevice);

    public boolean deleteDeviceFromWarehouse(int centerID, String device);

    public boolean transferDeviceToCenter(int centerID, int centerNew, String device);

    public List<FieldExecutiveEntity> getExecutives(String warehouseName);

    public int addFEToCenter(int centerID, String addedFE);

    public boolean transferFEToCenter(int centerID, int centerID2, String fe);

    public List<DeviceEntity> assignedDevicesInCenter(int centerId);

    public List<AssignDeviceEntity> deviceAssignment(List<Integer> deviceIds);

    public boolean deleteAssigment(DeviceAssignment assignment);

    public boolean deAssignDevice(DeviceAssignment assignment);

    public boolean deleteFEFromWarehouse(int centerID, String fe);

    public PincodeEntity fetchPinCode(String pincode);

    public List<OrderEntity> fetchOrdersOutbound(String status1, String status2, int centerID);
    
    public boolean saveOrderContents(List<OrderContentEntity> entities);

    public AssignDeviceEntity fetchDeviceAssignment(int id);

    public DeviceEntity fetchDeviceById(int deviceID);
    
    public List<OutboundEntity> fetchOutbound(int centerID);

    public boolean saveOutbounds(List<OutboundEntity> outbounds);

    public List<OrderEntity> fetchAllOrdersByStatus(String status, int centerID);
}
