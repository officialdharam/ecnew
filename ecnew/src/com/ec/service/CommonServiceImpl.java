package com.ec.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.dao.MenuDAO;
import com.ec.dao.OrderDAO;
import com.ec.dao.UserDAO;
import com.ec.dto.Device;
import com.ec.dto.FieldExecutive;
import com.ec.dto.UILink;
import com.ec.dto.User;
import com.ec.entity.CenterEntity;
import com.ec.entity.DeviceEntity;
import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.GroupEntity;
import com.ec.entity.GroupMapEntity;
import com.ec.entity.MenuEntity;
import com.ec.entity.PermissionEntity;
import com.ec.entity.PermissionMapEntity;
import com.ec.entity.UserEntity;
import com.ec.exception.LoginException;
import com.ec.util.Constant;
import com.ec.util.Util;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private UserDAO userDAO;

    private List<MenuEntity> menu = new ArrayList<MenuEntity>();

    private Map<Integer, Set<Integer>> userGroupMap = null;
    private Map<String, Set<String>> groupPermissionMap = null;

    private Map<Integer, PermissionEntity> permissionMap = null;
    private Map<Integer, GroupEntity> groupMap = null;

    @PostConstruct
    public void initialize() {
	menu = menuDAO.listMenus();

	List<GroupMapEntity> groupMapList = menuDAO.listGroupMap();
	buildGroupMap(groupMapList);

	List<GroupEntity> group = menuDAO.listGroups();
	buildGroup(group);

	List<PermissionEntity> permissionList = menuDAO.listPermissions();
	buildPermissions(permissionList);

	List<PermissionMapEntity> permissionsMapList = menuDAO.listPermissionsMap();
	buildPermissionMap(permissionsMapList);
    }

    @Override
    public List<String> fetchGroups() {

	List<String> groupList = new ArrayList<String>();
	List<GroupEntity> groups = menuDAO.listGroups();
	for (GroupEntity group : groups) {
	    groupList.add(group.getName());
	}
	return groupList;

    }

    public User login(String username, String userPassword) throws LoginException {

	User user = null;

	UserEntity validUser = userDAO.fetchUser(username, userPassword);

	if (validUser != null) {
	    initialize();
	    user = new User();
	    user.setfName(validUser.getFirstName());
	    user.setlName(validUser.getLastName());
	    user.setUserName(validUser.getUsername());
	    Set<Integer> groupSet = userGroupMap.get(validUser.getId());
	    if (groupSet == null) {
		initialize();
		groupSet = userGroupMap.get(validUser.getId());
	    }
	    List<Integer> groupList = new ArrayList<Integer>();
	    groupList.addAll(groupSet);
	    Collections.sort(groupList);
	    GroupEntity group = groupMap.get(groupList.get(0));
	    user.setGroupName(group.getName());
	    user.setLandingPage(group.getLandingPage());
	    user.setId(validUser.getId());
	    String centerID = validUser.getCenterID();
	    String[] split = centerID.split(",");
	    user.setCenterIds(Arrays.asList(split));

	} else {
	    throw new LoginException("Unable to find a user with given credentials.");
	}

	return user;
    }

    public Set<UILink> buildMenu(String group) {

	Set<String> permissionsList = groupPermissionMap.get(group);

	Set<UILink> links = new HashSet<UILink>();
	UILink link = null;
	for (MenuEntity me : menu) {
	    if (me != null && me.getParent() == 0 && permissionsList.contains(me.getName())) {
		if (Constant.CUSTOMER_SERVICE.equalsIgnoreCase(group) && Constant.HOME.equalsIgnoreCase(me.getName())) {
		    link = new UILink(me.getDisplayName(), "navigate?nextPage=" + Constant.CS_PAGE, me.getName());
		} else {
		    link = new UILink(me.getDisplayName(), me.getHref(), me.getName());
		}
		link.setChildren(getAllWithParent(me.getId(), menu, group, permissionsList));
		links.add(link);
	    }
	}

	return links;
    }

    private void buildGroup(List<GroupEntity> group) {
	groupMap = new HashMap<Integer, GroupEntity>();
	for (GroupEntity g : group) {
	    groupMap.put(g.getId(), g);
	}
    }

    private void buildGroupMap(List<GroupMapEntity> groupMap) {
	userGroupMap = new HashMap<Integer, Set<Integer>>();
	for (GroupMapEntity gmE : groupMap) {
	    Set<Integer> list = userGroupMap.get(gmE.getUserID());
	    if (list == null) {
		list = new HashSet<Integer>();
		userGroupMap.put(gmE.getUserID(), list);
	    }

	    list.add(gmE.getGroupID());
	}
    }

    private void buildPermissionMap(List<PermissionMapEntity> permissionsMapList) {
	groupPermissionMap = new HashMap<String, Set<String>>();
	for (PermissionMapEntity pmE : permissionsMapList) {
	    Integer groupID = pmE.getGroupID();
	    GroupEntity groupEntity = groupMap.get(groupID);
	    if (groupEntity == null) {
		continue;
	    }
	    Set<String> list = groupPermissionMap.get(groupEntity.getName());

	    if (list == null) {
		list = new HashSet<String>();
		groupPermissionMap.put(groupMap.get(pmE.getGroupID()).getName(), list);
	    }
	    list.add(permissionMap.get(pmE.getPermissionID()).getLink());
	}
    }

    private void buildPermissions(List<PermissionEntity> permissionList) {
	permissionMap = new HashMap<Integer, PermissionEntity>();
	for (PermissionEntity p : permissionList) {
	    permissionMap.put(p.getId(), p);
	}
    }

    private Set<UILink> getAllWithParent(int parentId, List<MenuEntity> menu, String group, Set<String> permissionList) {
	Set<UILink> links = new HashSet<UILink>();
	UILink link = null;
	for (MenuEntity me : menu) {
	    if (me.getParent() == parentId && permissionList.contains(me.getName())) {
		link = new UILink(me.getDisplayName(), me.getHref(), me.getName());
		links.add(link);
	    }
	}
	return links;
    }

    @Override
    public Map<Integer, String> fetchWarehouseNames(String active, String username) {
	Map<Integer, String> centerList = new HashMap<Integer, String>();
	List<CenterEntity> centers = orderDAO.getCenters(active);
	String[] centerIds = null;
	if (!Util.nullOrEmtpy(username)) {
	    UserEntity userEntity = userDAO.fetchUser(username);
	    String centerID = userEntity.getCenterID();
	    centerIds = centerID.split(",");
	}
	if (centerIds != null && centerIds.length > 0) {
	    for (int i = 0; i < centerIds.length; i++) {
		for (CenterEntity ce : centers) {
		    if (ce.getCenterID() == Integer.parseInt(centerIds[i].trim())) {
			centerList.put(ce.getCenterID(), ce.getCenterName());
		    }
		}
	    }
	} else {
	    for (CenterEntity ce : centers) {
		centerList.put(ce.getCenterID(), ce.getCenterName());
	    }
	}
	return centerList;
    }

    @Override
    public boolean changePassword(String username, String password, String oldPassword) {

	try {
	    UserEntity fetchUser = userDAO.fetchUser(username, oldPassword);
	    if (fetchUser != null) {
		fetchUser.setPassword(password);
		userDAO.updateUser(fetchUser);
		return true;
	    }
	} catch (Exception e) {

	}

	return false;
    }

    @Override
    public List<Device> fetchDevicesForCenter(String warehouse, String status) {
	CenterEntity centerNew = orderDAO.getCenter(warehouse);
	List<Device> deviceList = new ArrayList<Device>();
	if (centerNew != null) {
	    List<DeviceEntity> devices = orderDAO.fetchDevicesForCenter(centerNew.getCenterID(), status);
	    if (devices != null) {
		for (DeviceEntity d : devices) {
		    deviceList.add(new Device(d.getDeviceID(), d.getCode(), centerNew.getCenterName()));
		}
	    }
	}

	return deviceList;
    }

    @Override
    public List<FieldExecutive> fetchFieldExecutivesForCenter(String warehouse, String status) {
	CenterEntity centerNew = orderDAO.getCenter(warehouse);
	List<FieldExecutive> feList = new ArrayList<FieldExecutive>();
	if (centerNew != null) {
	    List<FieldExecutiveEntity> fes = userDAO.fetchFieldExecutive(centerNew.getCenterID(), status);
	    if (fes != null) {
		for (FieldExecutiveEntity f : fes) {
		    feList.add(new FieldExecutive(f.getName(), centerNew.getCenterID(), centerNew.getCenterName(), f.getId()));
		}
	    }
	}

	return feList;
    }

}
