package com.ec.dao;

import java.util.List;

import com.ec.entity.GroupEntity;
import com.ec.entity.GroupMapEntity;
import com.ec.entity.MenuEntity;
import com.ec.entity.PermissionEntity;
import com.ec.entity.PermissionMapEntity;

public interface MenuDAO {

    public List<MenuEntity> listMenus();

    public List<PermissionEntity> listPermissions();

    public List<PermissionMapEntity> listPermissionsMap();
    
    public List<GroupMapEntity> listGroupMap();
    
    public boolean updateGroupMap(GroupMapEntity gmE);
    
    public List<GroupEntity> listGroups();

}
