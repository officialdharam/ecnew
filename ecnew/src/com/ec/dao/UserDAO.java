package com.ec.dao;

import java.util.List;

import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.UserEntity;

public interface UserDAO {

    public int addUser(UserEntity userEntity, String groupName);

    public List<UserEntity> listUsers();

    public UserEntity fetchUser(String user, String password);

    public List<FieldExecutiveEntity> fetchFieldExecutive(int centerID, String assigned);

    public List<FieldExecutiveEntity> fetchFEByID(List<Integer> feIDs);

    public List<UserEntity> fetchUsers();
    
    public void updateUser(UserEntity e) throws Exception;

    public UserEntity fetchUser(String username);

    public int getGroupId(String groupname);

    
}
