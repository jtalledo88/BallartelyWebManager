package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Provider;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Repository
public class ProveedorDao {
	
	public List<Provider> getProvidersDataBase(EntityManager em, Provider provider) throws BallartelyException {
		try {
			TypedQuery<Provider> queryProviders = em.createQuery(
					"select p from Provider p where p.providerSocialReason like :providerSocialReason or "
					+ "p.providerRuc = :providerRuc or p.providerStatus = :providerStatus", Provider.class);
			queryProviders.setParameter("providerSocialReason", "%" + provider.getProviderSocialReason() + "%");
			queryProviders.setParameter("providerRuc", provider.getProviderRuc());
			queryProviders.setParameter("providerStatus", provider.getProviderStatus());
			
			return queryProviders.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
