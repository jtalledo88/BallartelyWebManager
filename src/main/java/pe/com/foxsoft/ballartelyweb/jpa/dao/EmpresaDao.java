package pe.com.foxsoft.ballartelyweb.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Repository	
public class EmpresaDao {
	
	public Enterprise getEnterpriseDataBase(EntityManager em, Enterprise enterprise) throws BallartelyException {
		try {
			TypedQuery<Enterprise> queryEnterprise = em.createQuery(
					"select e from Enterprise e where e.login = :login and e.password = :password", Enterprise.class);
			queryEnterprise.setParameter("login", enterprise.getLogin());
			queryEnterprise.setParameter("password", enterprise.getPassword());
			
			return queryEnterprise.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public Enterprise getEnterpriseDataBase(EntityManager em, String login) throws BallartelyException {
		try {
			TypedQuery<Enterprise> queryEnterprise = em.createQuery(
					"select e from Enterprise e where e.login = :login", Enterprise.class);
			queryEnterprise.setParameter("login", login);
			
			return queryEnterprise.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
