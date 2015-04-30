package com.ec.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ec.dto.Device;
import com.ec.dto.FieldExecutive;
import com.ec.dto.UILink;
import com.ec.dto.User;
import com.ec.exception.LoginException;

public interface CommonService {
    public Set<UILink> buildMenu(String group);
    public User login(String username, String userPassword) throws LoginException;
    public Map<Integer, String> fetchWarehouseNames(String active, String username);
    public boolean changePassword(String username, String password, String oldpassword);
    public List<FieldExecutive> fetchFieldExecutivesForCenter(String warehouse, String status);
    public List<String> fetchGroups();
    List<Device> fetchDevicesForCenter(String warehouse, String status);
}