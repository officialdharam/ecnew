package com.ec.service;

import java.util.List;

import com.ec.dto.NameValuePair;
import com.ec.dto.User;

public interface UserService {

    public int addUser(List<NameValuePair> user);

    public List<User> fetchUsers();

    boolean updateUser(List<NameValuePair> user);
    
    public List<String> exportDatabase();
}
