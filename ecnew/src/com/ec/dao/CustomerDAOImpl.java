package com.ec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ec.entity.CustomerEntity;

@Repository
@Transactional
public class CustomerDAOImpl implements CustomerDAO {

    public static Logger log = Logger.getLogger(CustomerDAOImpl.class);

    @Autowired
    private SessionFactory sf;

    @Override
    public Integer saveCustomer(CustomerEntity entity) {
	Integer customerID = 0;
	try {
	    Serializable save = sf.getCurrentSession().save(entity);
	    if (save != null)
		customerID = (Integer) save;
	} catch (Exception e) {
	    log.error("Error occurred while saving customer " + entity, e);
	}
	return customerID;
    }

    @Override
    public CustomerEntity fetchCustomerByPhone(long phoneNumber) {
	CustomerEntity customer = null;
	try {
	    Criteria c = sf.getCurrentSession().createCriteria(CustomerEntity.class)
		    .add(Restrictions.eq("phoneNumber", phoneNumber));
	    Object result = c.uniqueResult();
	    if (result instanceof CustomerEntity) {
		customer = (CustomerEntity) result;
	    }
	} catch (Exception e) {
	    log.error("Error occurred while fetching customer by phone number " + phoneNumber, e);
	}
	return customer;
    }

    @Override
    public CustomerEntity fetchCustomerByID(int id) {
	CustomerEntity cEntity = null;
	try {
	    cEntity = (CustomerEntity) sf.getCurrentSession().createCriteria(CustomerEntity.class)
		    .add(Restrictions.eq("customerID", id)).uniqueResult();
	} catch (Exception e) {
	    log.error("Error occurred while fetching customer by customer ID" + id+ ". Returning null from fetchCustomerByID(int id)", e);
	}
	return cEntity;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CustomerEntity> fetchCustomersByID(Set<Integer> ids) {

	List<CustomerEntity> customers = null;

	if (ids != null && ids.size() > 0) {
	    try {
		Criteria c = sf.getCurrentSession().createCriteria(CustomerEntity.class).add(Restrictions.in("customerID", ids));
		customers = c.list();
	    } catch (Exception e) {
		log.error("Error occurred while fetching customers by ID list " + ids, e);
	    }
	} else {
	    log.warn("The customer ids set supplied is empty. Hence fetchCustomersByID(Set<Integer> ids) returns null.");
	}
	return customers;
    }
}
