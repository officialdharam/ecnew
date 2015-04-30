package com.ec.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ec.dto.Customer;
import com.ec.dto.NameValuePair;
import com.ec.dto.Order;
import com.ec.entity.CategoryEntity;
import com.ec.util.OrderStatus;

public interface OrderService {

    public Order getOrderByID(String randomId, int orderID) throws Exception;

    public List<Order> getOrderByPhone(String randomId, long phone);

    public long createOrder(String randomId, List<NameValuePair> order, String username);

    public boolean updateOrder(String randomId, List<NameValuePair> order, String userName);

    public Set<Order> listOrders(String randomId, OrderStatus status, long seconds, String warehouseName);

    public Set<Order> listOrders(String randomId, List<Integer> ids);

    public Map<Integer, CategoryEntity> getCategoryDefinitionsMap(String randomId) throws Exception;

    public Customer getCustomer(String randomId, long phone);

    Set<Order> listOrdersPickedByDevice(String randomId, String deviceID) throws Exception;

}
