package pe.com.foxsoft.ballartelyweb.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.User;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Repository	
public class UsuarioDao {
	
	public User getUserDataBase(EntityManager em, User user) throws BallartelyException {
		try {
			TypedQuery<User> queryUser = em.createQuery(
					"select u from User u where u.userName = :userName and u.userPassword = :userPassword", User.class);
			queryUser.setParameter("userName", user.getUserName());
			queryUser.setParameter("userPassword", user.getUserPassword());
			
			return queryUser.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public User getUserDataBase(EntityManager em, String userName) throws BallartelyException {
		try {
			TypedQuery<User> queryUser = em.createQuery(
					"select u from User u where u.userName = :userName", User.class);
			queryUser.setParameter("userName", userName);
			
			return queryUser.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
