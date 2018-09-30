package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Repository
public class CustomerDao{
	
	public List<Customer> getCustomersDataBase(EntityManager em, Customer customer) throws BallartelyException {
		try {
			TypedQuery<Customer> queryCustomers = em.createQuery(
					"select c from Customer c where c.customerNames like :customerNames and "
					+ "(:documentNumber is null or c.documentNumber =:documentNumber) and (:customerStatus is null or c.customerStatus = :customerStatus) and "
					+ "(:customerType is null or c.customerType = :customerType) and (:documentType is null or c.documentType = :documentType)", Customer.class);
			queryCustomers.setParameter("customerNames", "%" + customer.getCustomerNames() + "%");
			queryCustomers.setParameter("documentNumber", Utilitarios.vacioComoNulo(customer.getDocumentNumber()));
			queryCustomers.setParameter("customerStatus", Utilitarios.vacioComoNulo(customer.getCustomerStatus()));
			queryCustomers.setParameter("customerType", Utilitarios.vacioComoNulo(customer.getCustomerType()));
			queryCustomers.setParameter("documentType", Utilitarios.vacioComoNulo(customer.getDocumentType()));
			
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
