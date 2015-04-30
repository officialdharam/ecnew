package com.ec.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ec.dto.DeviceAssignment;
import com.ec.entity.AssignDeviceEntity;
import com.ec.entity.CategoryEntity;
import com.ec.entity.CenterEntity;
import com.ec.entity.DeviceEntity;
import com.ec.entity.FieldExecutiveEntity;
import com.ec.entity.OrderContentEntity;
import com.ec.entity.OrderEntity;
import com.ec.entity.OutboundEntity;
import com.ec.entity.PincodeEntity;
import com.ec.util.Constant;
import com.ec.util.OrderStatus;
import com.ec.util.Util;

@Repository
@Transactional
public class OrderDAOImpl implements OrderDAO {

    public static Logger log = Logger.getLogger(OrderDAOImpl.class);

    @Autowired
    private SessionFactory sf;

    @Autowired
    private UserDAO userDAO;

    @Override
    public Integer saveOrder(OrderEntity entity) {
	Integer orderID = 0;
	try {
	    Serializable save = sf.getCurrentSession().save(entity);
	    if (save != null)
		orderID = (Integer) save;
	} catch (Exception e) {
	    log.error("Error occurred while saving order. " + entity, e);
	}
	return orderID;
    }

    @Override
    public OrderEntity fetchOrderByID(int orderID) {
	OrderEntity oEntity = null;
	try {
	    oEntity = (OrderEntity) sf.getCurrentSession().createCriteria(OrderEntity.class)
		    .add(Restrictions.eq("orderID", orderID)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching order by id ." + orderID, e);
	}
	return oEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderEntity> fetchOrdersByDeviceIDAndStatus(int deviceID, String status) {
	List<OrderEntity> resultList = null;
	try {
	    Criteria c = sf.getCurrentSession().createCriteria(OrderEntity.class).add(Restrictions.eq("deviceID", deviceID))
		    .add(Restrictions.eq("status", status));
	    resultList = c.list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching order by deviceID " + deviceID + " and status " + status, e);
	}
	return resultList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderEntity> fetchOrdersByCustomerID(int customerID) {
	List<OrderEntity> resultList = null;
	try {
	    Criteria c = sf.getCurrentSession().createCriteria(OrderEntity.class).add(Restrictions.eq("customerID", customerID));
	    resultList = c.list();
	} catch (Exception e) {
	    log.error("Error occurredwhile fetching order by customer ID " + customerID, e);
	}
	return resultList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderEntity> fetchOrdersByStatus(String status, long minutes, int centerID) {
	List<OrderEntity> orders = new ArrayList<OrderEntity>();
	try {
	    List<OrderEntity> resultList = null;
	    Criteria c = sf.getCurrentSession().createCriteria(OrderEntity.class).add(Restrictions.eq("status", status))
		    .add(Restrictions.eq("centerID", centerID));

	    c.add(Restrictions.le("pickUpDate", toNight()));
	    resultList = c.list();

	    if (minutes != 0) {
		for (OrderEntity entity : resultList) {
		    String pickupTime = entity.getPickupTime();
		    int pickupMinute = absoluteMinutes(pickupTime);
		    int minutesNow = minutesNow();

		    if (OrderStatus.FORWARDED.name().equals(status)) {
			if (pickupMinute + 15 <= minutesNow) {
			    orders.add(entity);
			} else if (entity.getPickUpDate().before(new Date())) {
			    orders.add(entity);
			}
		    } else if (OrderStatus.OPEN.name().equals(status)) {
			if (pickupMinute < minutesNow || pickupMinute <= minutes + minutesNow) {
			    orders.add(entity);
			} else if (entity.getPickUpDate().before(new Date())) {
			    orders.add(entity);
			}
		    }
		}
	    } else {
		orders.addAll(resultList);
	    }
	} catch (Exception e) {
	    log.error("Error occurred while fetching order by status " + status, e);
	}
	return orders;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderEntity> fetchAllOrdersByStatus(String status, int centerID) {
	List<OrderEntity> resultList = null;
	try {
	    Criteria c = sf.getCurrentSession().createCriteria(OrderEntity.class).add(Restrictions.eq("status", status))
		    .add(Restrictions.eq("centerID", centerID));
	    resultList = c.list();

	} catch (Exception e) {
	    log.error("Error occurred while fetching all order by status " + status, e);
	}
	return resultList;
    }

    private Date toNight() {
	Calendar instance = Calendar.getInstance(TimeZone.getTimeZone(Constant.IST), Locale.ENGLISH);
	instance.set(Calendar.HOUR_OF_DAY, 23);
	instance.set(Calendar.MINUTE, 59);
	instance.set(Calendar.SECOND, 59);
	return instance.getTime();
    }

    private int minutesNow() {
	Calendar instance = Calendar.getInstance(TimeZone.getTimeZone(Constant.IST), Locale.ENGLISH);
	int i = instance.get(Calendar.MINUTE);
	int j = instance.get(Calendar.HOUR_OF_DAY);
	return j * 60 + i;
    }

    private int absoluteMinutes(String pickTime) {
	int time = 0;
	if (pickTime != null && pickTime.length() > 0) {

	    String[] split = pickTime.split(" ");
	    String[] split2 = split[0].split(":");
	    int hour = Integer.parseInt(split2[0]);
	    int minute = Integer.parseInt(split2[1]);
	    time = hour * 60 + minute;

	    if (split[1].trim().equals("PM")) {
		time += 720;
	    }
	}

	return time;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderEntity> fetchOrdersOutbound(String status1, String status2, int centerID) {
	List<OrderEntity> resultList = null;
	try {
	    Criteria c = sf.getCurrentSession().createCriteria(OrderEntity.class).add(Restrictions.eq("status", status1))
		    .add(Restrictions.ne("status", status2)).add(Restrictions.eq("centerID", centerID));
	    resultList = c.list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching order between status " + status1 + " and " + status2, e);
	}
	return resultList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderContentEntity> fetchOrderContent(Set<Integer> orderIDs) {
	List<OrderContentEntity> resultList = null;
	if (orderIDs != null && orderIDs.size() > 0) {
	    try {
		Criteria c = sf.getCurrentSession().createCriteria(OrderContentEntity.class)
			.add(Restrictions.in("orderID", orderIDs));
		resultList = c.list();
	    } catch (Exception e) {
		log.error("Error occurred while fetching order content for order IDs " + orderIDs, e);
	    }
	} else {
	    log.warn("The order ids set supplied is empty. Hence fetchOrderContent(Set<Integer> orderIDs) returns null.");
	}
	return resultList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoryEntity> fetchCategoryDefition() {
	List<CategoryEntity> resultList = null;
	try {
	    resultList = sf.getCurrentSession().createCriteria(CategoryEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching category definitions.", e);
	}
	return resultList;
    }

    @Override
    public boolean updateOrder(OrderEntity orderEntity) {
	boolean status = false;
	try {
	    sf.getCurrentSession().update(orderEntity);
	    status = true;
	} catch (Exception e) {
	    log.error("Error occurred while updating order " + orderEntity, e);
	}
	return status;
    }

    @Override
    public Integer saveCenter(CenterEntity centerEntity) {

	Integer centerID = 0;
	try {
	    if (centerEntity != null) {
		centerEntity.setActive(Constant.ACTIVE);
		Serializable save = sf.getCurrentSession().save(centerEntity);
		if (save != null)
		    centerID = (Integer) save;
	    }
	} catch (Exception e) {
	    log.error("Error occurred while saving center." + centerEntity, e);
	}
	return centerID;
    }

    @Override
    public Integer updateCenter(CenterEntity centerEntity) {
	Integer centerID = 0;

	try {
	    sf.getCurrentSession().saveOrUpdate(centerEntity);
	    centerID = 1;
	} catch (Exception e) {
	    log.error("Exception occurred while updating the warehosue details", e);
	}
	return centerID;
    }

    @Override
    public CenterEntity getCenter(String warehouseName) {
	CenterEntity uniqueResult = null;
	try {
	    uniqueResult = (CenterEntity) sf.getCurrentSession().createCriteria(CenterEntity.class)
		    .add(Restrictions.eq("centerName", warehouseName)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while getting center information for name " + warehouseName, e);
	}
	return uniqueResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CenterEntity> getCenters(String active) {
	List<CenterEntity> centerEntityList = null;
	try {
	    if (Constant.ACTIVE.equalsIgnoreCase(active))
		centerEntityList = sf.getCurrentSession().createCriteria(CenterEntity.class)
			.add(Restrictions.eq("active", Constant.ACTIVE)).list();
	    else
		centerEntityList = sf.getCurrentSession().createCriteria(CenterEntity.class).list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching centers with status." + active, e);
	}
	return centerEntityList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PincodeEntity> getPincodes(String warehouseName) {

	CenterEntity center = null;
	List<PincodeEntity> list = null;
	int centerID = 0;
	try {

	    center = (CenterEntity) sf.getCurrentSession().createCriteria(CenterEntity.class)
		    .add(Restrictions.eq("centerName", warehouseName)).uniqueResult();
	    if (center != null) {
		centerID = center.getCenterID();
	    }

	    if (centerID > 0) {
		list = sf.getCurrentSession().createCriteria(PincodeEntity.class).add(Restrictions.eq("centerID", centerID))
			.list();
	    }
	} catch (Exception e) {
	    log.error("Error occurred while getting piincodes for warehouse " + warehouseName, e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DeviceEntity> getDevices(String warehouseName, String status) {
	int centerID = 0;
	List<DeviceEntity> list = null;

	try {
	    CenterEntity center = (CenterEntity) sf.getCurrentSession().createCriteria(CenterEntity.class)
		    .add(Restrictions.eq("centerName", warehouseName)).uniqueResult();
	    if (center != null) {
		centerID = center.getCenterID();
	    }

	    if (centerID > 0) {
		Criteria c = sf.getCurrentSession().createCriteria(DeviceEntity.class).add(Restrictions.eq("centerID", centerID));
		if (!Util.nullOrEmtpy(status)) {
		    c.add(Restrictions.eq("assigned", status));
		}
		list = c.list();
	    }

	} catch (Exception e) {
	    log.error("Error occurred while fetching devices for warehouse " + warehouseName, e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<OrderEntity> fetchOrderByIds(List<Integer> ids) {
	List<OrderEntity> orderList = null;
	if (ids != null && ids.size() > 0) {
	    try {
		orderList = sf.getCurrentSession().createCriteria(OrderEntity.class).add(Restrictions.in("orderID", ids)).list();
	    } catch (Exception e) {
		log.error("Error occurred while fetching orders for ids." + ids, e);
	    }
	} else {
	    log.warn("The order ids list supplied is empty. Hence fetchOrderByIds(List<Integer> ids) returns null.");
	}
	return orderList;
    }

    @Override
    public boolean deletePincodeFromCenter(int center, String pincode) {
	boolean status = false;
	try {
	    PincodeEntity pinEntity = (PincodeEntity) sf.getCurrentSession().createCriteria(PincodeEntity.class)
		    .add(Restrictions.eq("pincode", pincode)).add(Restrictions.eq("centerID", center)).uniqueResult();

	    if (pinEntity != null) {
		pinEntity.setCenterID(0);
		sf.getCurrentSession().update(pinEntity);
		status = true;
	    } else
		status = false;
	} catch (Exception e) {
	    log.error("Error occurred while deleting pincode from center ID " + center + " and pincode " + pincode, e);
	}
	return status;
    }

    @Override
    public int addPincodeToCenter(int center, String pincode) {
	Integer id = 0;
	try {

	    PincodeEntity pEntity = (PincodeEntity) sf.getCurrentSession().createCriteria(PincodeEntity.class)
		    .add(Restrictions.eq("pincode", pincode)).uniqueResult();
	    if (pEntity != null) {
		if (pEntity.getCenterID() == 0) {
		    pEntity.setCenterID(center);
		    sf.getCurrentSession().update(pEntity);
		} else {
		    return Constant.PINCODE_IS_ALREADY_ASSIGNED_TO_ANOTHER_CENTER;
		}
	    } else {
		return Constant.PINCODE_DOES_NOT_EXIST;
	    }
	} catch (Exception e) {
	    log.error("Error occurred while adding pincode " + pincode + " to center ID " + center, e);
	    return -1;
	}
	return id;
    }

    @Override
    public boolean transferPincodeToCenter(int centerOld, int centerNew, String pincode) {

	boolean status = false;
	try {

	    PincodeEntity pinEntity = (PincodeEntity) sf.getCurrentSession().createCriteria(PincodeEntity.class)
		    .add(Restrictions.eq("pincode", pincode)).add(Restrictions.eq("centerID", centerOld)).uniqueResult();

	    if (pinEntity != null) {
		pinEntity.setCenterID(centerNew);
	    }
	    sf.getCurrentSession().update(pinEntity);
	    status = true;

	} catch (Exception e) {
	    log.error("Error occurred while transfering pincode " + pincode + " from old center ID " + centerOld
		    + " to new center ID " + centerNew, e);
	}
	return status;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DeviceEntity> fetchDevicesForCenter(int centerID, String status) {
	List<DeviceEntity> list = null;
	try {

	    Criteria c = sf.getCurrentSession().createCriteria(DeviceEntity.class).add(Restrictions.eq("centerID", centerID));
	    if (!Util.nullOrEmtpy(status))
		c.add(Restrictions.eq("assigned", status));
	    list = c.list();

	} catch (Exception e) {
	    log.error("Error occurred while fetching devices for center ID " + centerID, e);
	}
	return list;
    }

    @Override
    public DeviceEntity fetchDeviceByUniqueID(String id) {
	DeviceEntity device = null;
	try {
	    device = (DeviceEntity) sf.getCurrentSession().createCriteria(DeviceEntity.class).add(Restrictions.eq("code", id))
		    .uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching device by ID " + id, e);
	}
	return device;
    }

    @Override
    public int assignDevice(DeviceEntity device, int id) {
	AssignDeviceEntity adE = new AssignDeviceEntity();
	adE.setAssignmentDate(new Date());
	adE.setUserID(id);
	int result = 0;
	List<FieldExecutiveEntity> fetchFEByID = null;
	try {

	    adE.setDeviceID(device.getDeviceID());
	    Serializable save = sf.getCurrentSession().save(adE);
	    if (save != null) {
		result = (Integer) save;
		if (result > 0) {
		    device.setAssigned(Constant.TRUE);
		    sf.getCurrentSession().update(device);
		    List<Integer> ids = new ArrayList<Integer>(1);
		    ids.add(id);
		    fetchFEByID = userDAO.fetchFEByID(ids);
		    if (fetchFEByID != null && fetchFEByID.size() > 0) {
			FieldExecutiveEntity feByID = fetchFEByID.get(0);
			if (feByID != null) {
			    feByID.setAssigned(Constant.TRUE);
			    sf.getCurrentSession().update(feByID);
			}
		    }
		}
	    }
	} catch (Exception e) {
	    log.error("Error occurred while assigning device " + device.getCode() + " to user ID " + id, e);

	}
	return result;
    }

    @Override
    public int addDeviceToCenter(int centerID, String addedDevice) {
	Integer id = 0;
	try {
	    DeviceEntity deviceEntity = new DeviceEntity();
	    deviceEntity.setCenterID(centerID);
	    deviceEntity.setActive(Constant.ACTIVE);
	    deviceEntity.setCreatedAt(new Date());
	    deviceEntity.setUpdatedAt(new Date());
	    deviceEntity.setCode(addedDevice);
	    deviceEntity.setAssigned(Constant.FALSE);
	    Serializable save = sf.getCurrentSession().save(deviceEntity);
	    if (save != null)
		id = (Integer) save;

	} catch (Exception e) {
	    log.error("Error occurred while adding device " + addedDevice + " to center ID " + centerID, e);
	}
	return id;
    }

    @Override
    public boolean deleteDeviceFromWarehouse(int centerID, String device) {
	boolean status = false;
	try {
	    DeviceEntity deviceEntity = (DeviceEntity) sf.getCurrentSession().createCriteria(DeviceEntity.class)
		    .add(Restrictions.eq("code", device)).add(Restrictions.eq("centerID", centerID)).uniqueResult();

	    if (deviceEntity != null) {
		sf.getCurrentSession().delete(deviceEntity);
		status = true;
	    } else {
		status = false;
	    }
	} catch (Exception e) {
	    log.error("Error occurred while deleting device " + device + " from center ID " + centerID, e);
	}
	return status;
    }

    @Override
    public boolean transferDeviceToCenter(int centerID, int centerIDNew, String device) {
	boolean status = false;
	try {

	    DeviceEntity deviceEntity = (DeviceEntity) sf.getCurrentSession().createCriteria(DeviceEntity.class)
		    .add(Restrictions.eq("code", device)).add(Restrictions.eq("centerID", centerID)).uniqueResult();

	    if (deviceEntity != null) {
		deviceEntity.setCenterID(centerIDNew);
	    }
	    sf.getCurrentSession().update(deviceEntity);
	    status = true;
	} catch (Exception e) {
	    log.error("Error occurred while transferring device " + device + " from old center ID " + centerID
		    + " to new center ID " + centerIDNew, e);
	}
	return status;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FieldExecutiveEntity> getExecutives(String warehouseName) {

	int centerID = 0;
	List<FieldExecutiveEntity> list = null;
	try {
	    CenterEntity center = (CenterEntity) sf.getCurrentSession().createCriteria(CenterEntity.class)
		    .add(Restrictions.eq("centerName", warehouseName)).uniqueResult();
	    if (center != null) {
		centerID = center.getCenterID();
	    }

	    if (centerID > 0) {
		list = sf.getCurrentSession().createCriteria(FieldExecutiveEntity.class)
			.add(Restrictions.eq("centerID", centerID)).list();
	    }
	} catch (Exception e) {
	    log.error("Error occurred while fetching executives for warehouse name " + warehouseName, e);
	}
	return list;
    }

    @Override
    public int addFEToCenter(int centerID, String addedFE) {
	Integer id = 0;
	try {
	    FieldExecutiveEntity feEntity = new FieldExecutiveEntity();
	    feEntity.setCenterID(centerID);
	    feEntity.setActive(Constant.ACTIVE);
	    feEntity.setAssigned(Constant.FALSE);
	    feEntity.setName(addedFE);
	    Serializable save = sf.getCurrentSession().save(feEntity);
	    if (save != null)
		id = (Integer) save;
	} catch (Exception e) {
	    log.error("Error occurred while adding FE " + addedFE + " to center " + centerID, e);
	}
	return id;
    }

    @Override
    public boolean transferFEToCenter(int centerOld, int centerNew, String fe) {
	boolean status = false;
	try {

	    FieldExecutiveEntity feEntity = (FieldExecutiveEntity) sf.getCurrentSession()
		    .createCriteria(FieldExecutiveEntity.class).add(Restrictions.eq("name", fe))
		    .add(Restrictions.eq("centerID", centerOld)).uniqueResult();

	    if (feEntity != null) {
		feEntity.setCenterID(centerNew);
	    }
	    sf.getCurrentSession().update(feEntity);
	    status = true;
	} catch (Exception e) {
	    log.error("Error occurredd while  transferring FE " + fe + " from old center ID " + centerOld + " to new center ID "
		    + centerNew, e);
	}
	return status;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DeviceEntity> assignedDevicesInCenter(int centerId) {
	List<DeviceEntity> list = null;
	try {
	    list = sf.getCurrentSession().createCriteria(DeviceEntity.class).add(Restrictions.eq("centerID", centerId))
		    .add(Restrictions.eq("assigned", Constant.TRUE)).list();
	} catch (Exception e) {
	    log.error("Error occurred while fetching the devices assigned to center ID " + centerId, e);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AssignDeviceEntity> deviceAssignment(List<Integer> deviceIds) {
	List<AssignDeviceEntity> list = null;
	if (deviceIds != null && deviceIds.size() > 0) {
	    try {

		list = sf.getCurrentSession().createCriteria(AssignDeviceEntity.class)
			.add(Restrictions.in("deviceID", deviceIds)).list();
	    } catch (Exception e) {
		log.error("Error occurred while fetching the device assignment for device ID list " + deviceIds, e);
	    }
	} else {
	    log.warn("The device ids list supplied is empty. Hence deviceAssignment(List<Integer> deviceIds) returns null.");
	}
	return list;
    }

    @Override
    public boolean deleteAssigment(DeviceAssignment assignment) {
	boolean status = false;
	try {

	    AssignDeviceEntity d = (AssignDeviceEntity) sf.getCurrentSession().get(AssignDeviceEntity.class,
		    assignment.getAssignmentID());
	    if (d != null && d.getUserID() == assignment.getFe() && d.getDeviceID() == assignment.getDeviceID()) {
		sf.getCurrentSession().delete(d);
		status = true;
	    } else {
		status = false;
	    }
	} catch (Exception e) {
	    log.error("Error occurred while deleting device assignment " + assignment, e);
	}
	return status;
    }

    @Override
    public boolean deAssignDevice(DeviceAssignment assignment) {
	boolean status = false;
	try {
	    FieldExecutiveEntity fe = (FieldExecutiveEntity) sf.getCurrentSession().get(FieldExecutiveEntity.class,
		    assignment.getFe());
	    if (fe != null) {
		fe.setAssigned(Constant.FALSE);
		sf.getCurrentSession().update(fe);
	    }

	    DeviceEntity de = (DeviceEntity) sf.getCurrentSession().get(DeviceEntity.class, assignment.getDeviceID());
	    if (de != null) {
		de.setAssigned(Constant.FALSE);
		sf.getCurrentSession().update(de);
	    }
	    status = true;
	} catch (Exception e) {
	    log.error("Error occurred while de assigning device " + assignment, e);
	}
	return status;
    }

    @Override
    public boolean deleteFEFromWarehouse(int centerID, String fe) {
	boolean status = false;
	try {
	    FieldExecutiveEntity feEntity = (FieldExecutiveEntity) sf.getCurrentSession()
		    .createCriteria(FieldExecutiveEntity.class).add(Restrictions.eq("name", fe))
		    .add(Restrictions.eq("centerID", centerID)).uniqueResult();

	    if (feEntity != null) {
		sf.getCurrentSession().delete(feEntity);
		status = true;
	    } else {
		status = false;
	    }
	} catch (Exception e) {
	    log.error("Error occurred while deleting executive " + fe + " from center ID " + centerID, e);
	}
	return status;
    }

    @Override
    public PincodeEntity fetchPinCode(String pincode) {
	PincodeEntity pEntity = null;
	try {
	    pEntity = (PincodeEntity) sf.getCurrentSession().createCriteria(PincodeEntity.class)
		    .add(Restrictions.eq("pincode", pincode)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching pincode by id ." + pincode, e);
	}
	return pEntity;
    }

    @Override
    public boolean saveOrderContents(List<OrderContentEntity> entities) {
	Session session = null;
	try {

	    if (entities != null && entities.size() > 0) {

		session = sf.openSession();
		Transaction tx = session.beginTransaction();
		for (int i = 0; i < entities.size(); i++) {
		    session.save(entities.get(i));
		}
		session.flush();
		session.clear();
		tx.commit();
	    }
	} catch (Exception e) {
	    log.error("Error occurred while saving order contents. ", e);
	    return false;
	} finally {
	    session.close();
	}
	return true;
    }

    @Override
    public AssignDeviceEntity fetchDeviceAssignment(int feID) {
	AssignDeviceEntity assignment = null;
	try {
	    assignment = (AssignDeviceEntity) sf.getCurrentSession().createCriteria(AssignDeviceEntity.class)
		    .add(Restrictions.eq("userID", feID)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching device by id. ", e);
	}
	return assignment;
    }

    @Override
    public DeviceEntity fetchDeviceById(int deviceID) {
	DeviceEntity deviceEntity = null;
	try {
	    deviceEntity = (DeviceEntity) sf.getCurrentSession().createCriteria(DeviceEntity.class)
		    .add(Restrictions.eq("deviceID", deviceID)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching device by id. ", e);
	}
	return deviceEntity;
    }

    @Override
    public List<CenterEntity> getCenters(String... warehouseName) {
	try {
	    @SuppressWarnings("unchecked")
	    List<CenterEntity> list = (List<CenterEntity>) sf.getCurrentSession().createCriteria(CenterEntity.class)
		    .add(Restrictions.in("centerName", warehouseName)).list();
	    return list;
	} catch (Exception e) {
	    log.error("Error occurred while fetching centers for multiple ids. ", e);
	}
	return null;
    }

    @Override
    public List<OutboundEntity> fetchOutbound(int centerID) {
	try {

	    @SuppressWarnings("unchecked")
	    List<OutboundEntity> list = (List<OutboundEntity>) sf.getCurrentSession().createCriteria(OutboundEntity.class)
		    .add(Restrictions.eq("centerId", centerID)).list();
	    return list;
	} catch (Exception e) {
	    log.error("Error occurred while fetching outbound details.", e);
	}
	return null;
    }

    @Override
    public boolean saveOutbounds(List<OutboundEntity> outbounds) {
	Session session = null;
	try {

	    if (outbounds != null && outbounds.size() > 0) {

		session = sf.openSession();
		Transaction tx = session.beginTransaction();
		for (int i = 0; i < outbounds.size(); i++) {
		    session.saveOrUpdate(outbounds.get(i));
		}
		session.flush();
		session.clear();
		tx.commit();
	    }
	} catch (Exception e) {
	    log.error("Error occurred while saving order contents. ", e);
	    return false;
	} finally {
	    session.close();
	}
	return true;
    }

    @Override
    public CenterEntity getCenterByPin(String pincode) {
	CenterEntity uniqueResult = null;
	try {
	    uniqueResult = (CenterEntity) sf.getCurrentSession().createCriteria(CenterEntity.class)
		    .add(Restrictions.eq("pincode", pincode)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while getting center information for pincode " + pincode, e);
	}
	return uniqueResult;
    }

}
