package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Repository
public class CustomerDao{
	
	public List<Customer> getCustomersDataBase(EntityManager em, Customer customer) throws BallartelyException {
		try {
			TypedQuery<Customer> queryCustomers = em.createQuery(
					"select c from Customer c join fetch c.customerStatus cs join fetch c.customerType ct join fetch c.documentType dt "
					+ "where c.customerNames = :customerNames or c.documentNumber =:documentNumber or cs.paramId = :customerStatus or "
					+ "or ct.paramId =:customerType or dt.paramId =:documentType", Customer.class);
			queryCustomers.setParameter("customerNames", customer.getCustomerNames());
			queryCustomers.setParameter("documentNumber", customer.getDocumentNumber());
			queryCustomers.setParameter("customerStatus", customer.getCustomerStatus());
			queryCustomers.setParameter("customerType", customer.getCustomerType());
			queryCustomers.setParameter("documentType", customer.getDocumentType());
			
			return queryCustomers.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public Customer getCustomerByDocNumberDataBase(EntityManager em, Customer customer) throws BallartelyException {
		try {
			TypedQuery<Customer> queryCustomer = em.createQuery(
					"select c from Customer c where c.documentNumber = :documentNumber", Customer.class);
			queryCustomer.setParameter("documentNumber", customer.getDocumentNumber());
			
			return queryCustomer.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
