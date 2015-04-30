package com.ec.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ec.entity.GroupEntity;
import com.ec.entity.GroupMapEntity;
import com.ec.entity.MenuEntity;
import com.ec.entity.PermissionEntity;
import com.ec.entity.PermissionMapEntity;

@Repository
@Transactional
public class MenuDAOImpl implements MenuDAO {

    public static Logger log = Logger.getLogger(MenuDAOImpl.class);

    @Autowired
    private SessionFactory sf;

    @SuppressWarnings("unchecked")
    @Override
    public List<MenuEntity> listMenus() {
	List<MenuEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(MenuEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while listing menus", e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PermissionEntity> listPermissions() {
	List<PermissionEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(PermissionEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while listing permissions", e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PermissionMapEntity> listPermissionsMap() {
	List<PermissionMapEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(PermissionMapEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while listing permission map", e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GroupEntity> listGroups() {
	List<GroupEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(GroupEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while listing groups", e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GroupMapEntity> listGroupMap() {
	List<GroupMapEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(GroupMapEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while listing group map", e);
	}
	return list;
    }

    @Override
    public boolean updateGroupMap(GroupMapEntity gmE) {
	try {
	    sf.getCurrentSession().update(gmE);
	    return true;
	} catch (Exception e) {
	    log.error("Error occurred while updating group map", e);
	}
	return false;
    }

}
