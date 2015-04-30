package com.ec.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.GroupEntity;
import com.ec.entity.GroupMapEntity;
import com.ec.entity.UserEntity;
import com.ec.util.Constant;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

    public static Logger log = Logger.getLogger(UserDAOImpl.class);

    @Autowired
    private SessionFactory sf;
    
    @Override
    public int addUser(UserEntity userEntity, String groupName) {

	Integer userID = 0;
	try {
	    Serializable save = sf.getCurrentSession().save(userEntity);
	    if (save != null)
		userID = (Integer) save;
	    
	    GroupMapEntity gmE = new GroupMapEntity();
	    gmE.setUserID(userID);
	    gmE.setActive(Constant.TRUE);
	    int groupId = getGroupId(groupName);
	    gmE.setGroupID(groupId);
	    gmE.setCreatedAt(new Date());
	    gmE.setUpdatedAt(new Date());
	    if(userID > 0 ){
		sf.getCurrentSession().save(gmE);
	    }
	} catch (Exception e) {
	    log.error("Error occurred while adding user " + userEntity, e);
	}
	return userID;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserEntity> listUsers() {
	List<UserEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(UserEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while listing users.", e);
	}
	return list;
    }

    @Override
    public UserEntity fetchUser(String user, String password) {

	UserEntity userEntity = null;
	try {
	    userEntity = (UserEntity) sf.getCurrentSession().createCriteria(UserEntity.class)
		    .add(Restrictions.eq("username", user).ignoreCase()).add(Restrictions.eq("password", password))
		    .uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching user with username " + user + " and password ******* ", e);
	}
	return userEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FieldExecutiveEntity> fetchFieldExecutive(int centerID, String assigned) {
	List<FieldExecutiveEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(FieldExecutiveEntity.class).add(Restrictions.eq("centerID", centerID))
		    .add(Restrictions.eq("assigned", assigned)).list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching field executive list for center ID " + centerID, e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FieldExecutiveEntity> fetchFEByID(List<Integer> feIDs) {
	List<FieldExecutiveEntity> fe = null;
	try {
	    fe = sf.getCurrentSession().createCriteria(FieldExecutiveEntity.class).add(Restrictions.in("id", feIDs)).list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching FE by ID list " + feIDs, e);
	}
	return fe;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserEntity> fetchUsers() {
	List<UserEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(UserEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching users ", e);
	}
	return list;

    }

    @Override
    public void updateUser(UserEntity e) throws Exception {
	try {
	    if (e != null) {
		sf.getCurrentSession().update(e);
	    }
	} catch (Exception ex) {
	    log.error("Error occurred while updating password for username " + e.getUsername(), ex);
	    throw new Exception("Error while password update for username " + e.getUsername());
	}
    }

    @Override
    public UserEntity fetchUser(String username) {
	UserEntity userEntity = null;
	try {
	    userEntity = (UserEntity) sf.getCurrentSession().createCriteria(UserEntity.class)
		    .add(Restrictions.eq("username", username).ignoreCase()).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching user with username " + username , e);
	}
	return userEntity;
    }
    
    @Override
    public int getGroupId(String groupname) {
	int id = 0;
	try {
	    GroupEntity group = (GroupEntity)sf.getCurrentSession().createCriteria(GroupEntity.class).add(Restrictions.eq("name",groupname)).uniqueResult();
	    id = group.getId();
	} catch (Exception e) {
	    log.error("Error occurred while updating group map", e);
	}
	return id;
    }
}
