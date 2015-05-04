package com.ec.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ec.dto.DeviceAssignment;
import com.ec.entity.CategoryEntity;
import com.ec.entity.CenterEntity;
import com.ec.entity.CustomerEntity;
import com.ec.entity.DeviceEntity;
import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.GroupEntity;
import com.ec.entity.GroupMapEntity;
import com.ec.entity.OrderContentEntity;
import com.ec.entity.OrderEntity;
import com.ec.entity.OutboundEntity;
import com.ec.entity.PermissionEntity;
import com.ec.entity.PermissionMapEntity;
import com.ec.entity.UserEntity;

@Repository
public class ExportDAOImpl implements ExportDAO {
    public static Logger log = Logger.getLogger(CustomerDAOImpl.class);

    @Autowired
    private SessionFactory sf;

    @Override
    @SuppressWarnings("unchecked")
    public List<List<? extends Object>> exportDatabase() {

	Criteria userCriteria = sf.getCurrentSession().createCriteria(UserEntity.class);
	Criteria customerCriteria = sf.getCurrentSession().createCriteria(CustomerEntity.class);
	Criteria feCriteria = sf.getCurrentSession().createCriteria(FieldExecutiveEntity.class);
	Criteria centerCriteria = sf.getCurrentSession().createCriteria(CenterEntity.class);
	Criteria categoryCriteria = sf.getCurrentSession().createCriteria(CategoryEntity.class);
	Criteria deviceCriteria = sf.getCurrentSession().createCriteria(DeviceEntity.class);
	Criteria deviceAssignmentCriteria = sf.getCurrentSession().createCriteria(DeviceAssignment.class);
	Criteria groupCriteria = sf.getCurrentSession().createCriteria(GroupEntity.class);
	Criteria groupMapCriteria = sf.getCurrentSession().createCriteria(GroupMapEntity.class);
	Criteria permissionCriteria = sf.getCurrentSession().createCriteria(PermissionEntity.class);
	Criteria permissionMapCriteria = sf.getCurrentSession().createCriteria(PermissionMapEntity.class);
	Criteria orderCriteria = sf.getCurrentSession().createCriteria(OrderEntity.class);
	Criteria orderContentCriteria = sf.getCurrentSession().createCriteria(OrderContentEntity.class);
	Criteria outBoundCriteria = sf.getCurrentSession().createCriteria(OutboundEntity.class);

	List<UserEntity> userList = (List<UserEntity>) userCriteria.list();
	List<CustomerEntity> customerList = (List<CustomerEntity>) customerCriteria.list();
	List<FieldExecutiveEntity> feList = (List<FieldExecutiveEntity>) feCriteria.list();
	List<CenterEntity> centerList = (List<CenterEntity>) centerCriteria.list();
	List<CategoryEntity> categoryList = (List<CategoryEntity>) categoryCriteria.list();
	List<DeviceEntity> deviceList = (List<DeviceEntity>) deviceCriteria.list();
	List<DeviceAssignment> deviceAssgnmentList = (List<DeviceAssignment>) deviceAssignmentCriteria.list();
	List<GroupEntity> groupList = (List<GroupEntity>) groupCriteria.list();
	List<GroupMapEntity> groupMapList = (List<GroupMapEntity>) groupMapCriteria.list();
	List<PermissionEntity> permissionList = (List<PermissionEntity>) permissionCriteria.list();
	List<PermissionMapEntity> permissionMapList = (List<PermissionMapEntity>) permissionMapCriteria.list();
	List<OrderEntity> orderList = (List<OrderEntity>) orderCriteria.list();
	List<OrderContentEntity> orderContentList = (List<OrderContentEntity>) orderContentCriteria.list();
	List<OutboundEntity> outboundList = (List<OutboundEntity>) outBoundCriteria.list();

	List<List<? extends Object>> tables = new ArrayList<List<? extends Object>>();
	tables.add(userList);
	tables.add(customerList);
	tables.add(feList);
	tables.add(centerList);
	tables.add(categoryList);
	tables.add(deviceList);
	tables.add(deviceAssgnmentList);
	tables.add(groupList);
	tables.add(groupMapList);
	tables.add(permissionList);
	tables.add(permissionMapList);
	tables.add(orderList);
	tables.add(orderContentList);
	tables.add(outboundList);

	return tables;
    }
}
