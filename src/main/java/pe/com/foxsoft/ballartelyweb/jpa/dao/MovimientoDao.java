package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.math.BigDecimal;
import java.util.Date;
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
			TypedQuery<Movement> queryMovement = em.createQuery(
					"select m from Movement a join fetch a.customer c where (a.accountType = '" + Constantes.ACCOUNT_TYPE_C + 
					"' and c.id = :customerId) or (a.accountType = '" + Constantes.ACCOUNT_TYPE_P +"' and c.id is null) "
							+ "and a.accountStatus = :accountStatus", Movement.class);
			queryMovement.setParameter("customerId", account.getCustomer().getId());
			queryMovement.setParameter("accountStatus", account.getAccountStatus());
			
			return queryMovement.getResultList();
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
	
	public List<Movement> getMovementsByAccountDataBase(EntityManager em, Integer accountId, Date startDate, Date endDate) throws BallartelyException {
		try {
			TypedQuery<Movement> queryMoevement = em.createQuery(
					"select m from Movement m where m.account.id = :accountId " + 
					" and (:startDate is null or :endDate is null) or m.movementDate between :startDate and :endDate "
					+ "order by m.movementDate desc", Movement.class);
			queryMoevement.setParameter("accountId", accountId);
			queryMoevement.setParameter("startDate", startDate);
			queryMoevement.setParameter("endDate", endDate);
			
			return queryMoevement.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
