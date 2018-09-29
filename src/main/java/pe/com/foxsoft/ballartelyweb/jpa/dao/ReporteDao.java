package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Repository
public class ReporteDao {
	
	public List<Object[]> getSalesByDateDataBase(EntityManager em) throws BallartelyException {
		try {
			TypedQuery<Object[]> queryMovement = em.createQuery(
					"select gh.emissionDate, sum(1) from GuideHead gh where gh.guideType = :typeVentas group by gh.emissionDate", Object[].class);
			queryMovement.setParameter("typeVentas", Constantes.GUIDE_TYPE_SALES);
			
			return queryMovement.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Object[]> getSalesByCustomerDataBase(EntityManager em) throws BallartelyException {
		try {
			TypedQuery<Object[]> queryMovement = em.createQuery(
					"select gh.customer.customerNames, year(gh.emissionDate), sum(1) from GuideHead gh where gh.guideType = :typeVentas "
					+ "group by gh.customer.customerNames, year(gh.emissionDate)", Object[].class);
			queryMovement.setParameter("typeVentas", Constantes.GUIDE_TYPE_SALES);
			
			return queryMovement.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Object[]> getSalesByPeriodDataBase(EntityManager em) throws BallartelyException {
		try {
			TypedQuery<Object[]> queryMovement = em.createQuery(
					"select month(gh.emissionDate), sum(1) from GuideHead gh where gh.guideType = :typeVentas group by month(gh.emissionDate)", Object[].class);
			queryMovement.setParameter("typeVentas", Constantes.GUIDE_TYPE_SALES);
			
			return queryMovement.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
}
