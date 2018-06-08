package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Account;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Repository
public class CuentaDao {
	
	public List<Account> getAccountsByOwnerDataBase(EntityManager em, Account account) throws BallartelyException {
		try {
			TypedQuery<Account> queryAccounts = em.createQuery(
					"select a from Account a left join fetch a.client c where (a.accountType = '" + Constantes.ACCOUNT_TYPE_C + 
					"' and c.clientId = :clientId) and a.accountStatus = :accountStatus", Account.class);
			queryAccounts.setParameter("clientId", account.getClient().getClientId());
			queryAccounts.setParameter("accountStatus", account.getAccountStatus());
			
			return queryAccounts.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public Account getAccountPrincipalDataBase(EntityManager em) throws BallartelyException {
		try {
			TypedQuery<Account> queryAccount = em.createQuery(
					"select a from Account a where a.accountType = :accountType and a.accountStatus = :accountStatus", Account.class);
			queryAccount.setParameter("accountType", Constantes.ACCOUNT_TYPE_P);
			queryAccount.setParameter("accountStatus", Constantes.STATUS_ACTIVE);
			
			return queryAccount.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
