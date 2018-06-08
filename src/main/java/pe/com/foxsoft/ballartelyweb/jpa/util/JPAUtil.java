package pe.com.foxsoft.ballartelyweb.jpa.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

public class JPAUtil {
	
	public static final String NAMED_QUERY_ALL_PRODUCT_LABEL = "ProductLabel.findAll";
	public static final String NAMED_QUERY_ALL_GENERAL_PARAMETER = "GeneralParameter.findAll";
	public static final String NAMED_QUERY_ALL_CLIENT = "Client.findAll";
	public static final String NAMED_QUERY_ALL_ACCOUNT = "Account.findAll";
	public static final String NAMED_QUERY_ALL_PROVIDER = "Provider.findAll";
	public static final String NAMED_QUERY_ALL_SHIPPING = "ShippingHead.findAll";
	public static final String NAMED_QUERY_ALL_SHIPPING_DETAIL = "ShippingDetail.findAll";
	
	public static <T> List<T> executeQueryList(EntityManager em, Class<T> clasz, String namedQuery) throws BallartelyException {
		try {
			TypedQuery<T> queryProductLabel = em.createNamedQuery(namedQuery, clasz);
			
			return queryProductLabel.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public static String persistEntity(EntityManager em, Object objEntity) throws BallartelyException {
		try {
			em.persist(objEntity);
			return Constantes.MESSAGE_PERSIST_SUCCESS;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public static <T> T findEntity(EntityManager em, Class<T> clasz, Object idEntity) throws BallartelyException {
		try {
			return em.find(clasz, idEntity);
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public static String mergeEntity(EntityManager em, Object objEntity) throws BallartelyException {
		try {
			em.merge(objEntity);
			return Constantes.MESSAGE_MERGE_SUCCESS;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public static String removeEntity(EntityManager em, Object objEntity) throws BallartelyException {
		try {
			Object objRemove = em.merge(objEntity);
			em.remove(objRemove);
			return Constantes.MESSAGE_REMOVE_SUCCESS;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
}
