package com.ec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ec.dao.MenuDAO;
import com.ec.dao.OrderDAO;
import com.ec.dao.UserDAO;
import com.ec.dto.NameValuePair;
import com.ec.dto.User;
import com.ec.entity.CenterEntity;
import com.ec.entity.GroupEntity;
import com.ec.entity.GroupMapEntity;
import com.ec.entity.UserEntity;
import com.ec.util.Constant;
import com.ec.util.Util;

@Service
public class UserServiceImpl implements UserService {

    public static Logger log = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public int addUser(List<NameValuePair> user) {
	UserEntity uEntity = new UserEntity();
	Map<String, String> userMap = Util.prepareMap(user);

	// create user
	uEntity.setFirstName(userMap.get(Constant.FIRST_NAME));
	uEntity.setLastName(userMap.get(Constant.LAST_NAME));
	uEntity.setEmail(userMap.get(Constant.EMAIL));
	uEntity.setUsername(userMap.get(Constant.USERNAME));
	uEntity.setPassword(userMap.get(Constant.PASSWORD));
	uEntity.setActive(userMap.get(Constant.STATUS));
	String warehouseName = userMap.get(Constant.WAREHOUSE);
	String groupName = userMap.get(Constant.GROUPNAME);
	CenterEntity center = orderDAO.getCenter(warehouseName);
	uEntity.setCenterID(center.getCenterID() + "");
	uEntity.setUpdatedAt(new Date());
	uEntity.setCreatedAt(new Date());

	return userDAO.addUser(uEntity, groupName);
    }

    @Override
    @Transactional
    public boolean updateUser(List<NameValuePair> user) {
	UserEntity uEntity = null;
	try {
	    Map<String, String> userMap = Util.prepareMap(user);
	    String username = userMap.get(Constant.USERNAME);
	    if (!Util.nullOrEmtpy(username)) {
		uEntity = userDAO.fetchUser(username);
	    }

	    if (uEntity != null) {
		// update user
		uEntity.setFirstName(userMap.get(Constant.FIRST_NAME));
		uEntity.setFirstName(userMap.get(Constant.FIRST_NAME));
		uEntity.setLastName(userMap.get(Constant.LAST_NAME));
		uEntity.setActive(userMap.get(Constant.STATUS));
		String groupName = userMap.get(Constant.GROUPNAME);
		String centers = userMap.get(Constant.CENTERS);
		uEntity.setUpdatedAt(new Date());
		List<CenterEntity> centerList = orderDAO.getCenters(centers.split(" "));
		StringBuilder sb = new StringBuilder();
		if (centerList != null && centerList.size() > 0) {
		    sb.append(centerList.get(0).getCenterID());
		}
		for (int i = 1; i < centerList.size(); i++) {
		    sb.append(",");
		    sb.append(centerList.get(i).getCenterID());
		}

		uEntity.setCenterID(sb.toString());
		userDAO.updateUser(uEntity);

		if (!Util.nullOrEmtpy(groupName)) {
		    GroupEntity usersGroup = null;
		    List<GroupEntity> groups = menuDAO.listGroups();
		    List<GroupMapEntity> groupMap = menuDAO.listGroupMap();
		    for (GroupEntity ge : groups) {
			if (ge.getName().equalsIgnoreCase(groupName)) {
			    usersGroup = ge;
			    break;
			}
		    }

		    boolean updateRequired = true;
		    GroupMapEntity gmEUser = null;
		    for (GroupMapEntity gmE : groupMap) {
			int groupID = gmE.getGroupID();
			int userID = gmE.getUserID();
			gmEUser = gmE;
			if (groupID == usersGroup.getId() && userID == uEntity.getId()) {
			    updateRequired = false;
			    break;
			}
		    }

		    if (updateRequired) {
			gmEUser.setGroupID(usersGroup.getId());
			menuDAO.updateGroupMap(gmEUser);
		    }
		}
		return true;
	    }

	} catch (Exception e) {
	    log.error("Error occurred while updating user " + user, e);
	}
	return false;
    }

    @Override
    public List<User> fetchUsers() {
	List<User> userList = null;
	try {
	    List<UserEntity> users = userDAO.fetchUsers();
	    List<GroupMapEntity> groupMapList = menuDAO.listGroupMap();
	    List<GroupEntity> listGroups = menuDAO.listGroups();

	    Map<Integer, GroupEntity> groupMap = buildGroup(listGroups);
	    Map<Integer, UserEntity> userMap = buildUserMap(users);
	    userList = new ArrayList<User>();
	    for (Entry<Integer, UserEntity> entry : userMap.entrySet()) {
		UserEntity userEntity = entry.getValue();
		User user = new User();
		user.setfName(userEntity.getFirstName());
		user.setlName(userEntity.getLastName());
		user.setUserName(userEntity.getUsername());
		String group = null;
		for (GroupMapEntity gm : groupMapList) {
		    int userID = gm.getUserID();
		    if (userID == userEntity.getId()) {
			GroupEntity groupEntity = groupMap.get(gm.getGroupID());
			group = groupEntity.getName();
			break;
		    }
		}

		user.setGroupName(group == null ? "NO GROUP" : group);
		userList.add(user);
	    }

	} catch (Exception e) {

	}
	return userList;
    }

    private Map<Integer, GroupEntity> buildGroup(List<GroupEntity> group) {
	Map<Integer, GroupEntity> groupMap = new HashMap<Integer, GroupEntity>();
	for (GroupEntity g : group) {
	    groupMap.put(g.getId(), g);
	}
	return groupMap;
    }

    private Map<Integer, UserEntity> buildUserMap(List<UserEntity> users) {
	Map<Integer, UserEntity> userMap = new HashMap<Integer, UserEntity>();
	for (UserEntity g : users) {
	    userMap.put(g.getId(), g);
	}
	return userMap;
    }
}
