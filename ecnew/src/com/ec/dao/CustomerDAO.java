package com.ec.dao;

import java.util.List;
import java.util.Set;

import com.ec.entity.CustomerEntity;

public interface CustomerDAO {

    public Integer saveCustomer(CustomerEntity entity);
    
    public CustomerEntity fetchCustomerByPhone(long phoneNumber);
    
    public CustomerEntity fetchCustomerByID(int id);
    
    public List<CustomerEntity> fetchCustomersByID(Set<Integer> ids);

}
