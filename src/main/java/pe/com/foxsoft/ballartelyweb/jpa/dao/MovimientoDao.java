package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Repository
public class MovimientoDao {
	
	public List<Movement> getAccountsByOwnerDataBase(EntityManager em, Account account) throws BallartelyException {
		try {
			TypedQuery<Movement> queryUser = em.createQuery(
					"select m from Movement a join fetch a.customer c where (a.accountType = '" + Constantes.ACCOUNT_TYPE_C + 
					"' and c.id = :customerId) or (a.accountType = '" + Constantes.ACCOUNT_TYPE_P +"' and c.id is null) "
							+ "and a.accountStatus = :accountStatus", Movement.class);
			queryUser.setParameter("customerId", account.getCustomer().getId());
			queryUser.setParameter("accountStatus", account.getAccountStatus());
			
			return queryUser.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public BigDecimal getAmountAccountDataBase(EntityManager em, Integer itemAccount, String movementType) throws BallartelyException {
		try {
			TypedQuery<BigDecimal> queryAmountAccount = em.createQuery(
					"select sum(m.movementAmount) from Movement m where m.account.id = :accountId and m.movementType = :movementType", BigDecimal.class);
			queryAmountAccount.setParameter("accountId", itemAccount);
			queryAmountAccount.setParameter("movementType", movementType);
			
			BigDecimal result = queryAmountAccount.getSingleResult();
			
			return result != null ? result : BigDecimal.ZERO;
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
