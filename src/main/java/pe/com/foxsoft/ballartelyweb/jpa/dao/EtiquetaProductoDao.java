package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Repository
public class EtiquetaProductoDao {
	
	public List<ProductLabel> getProductLabelsDataBase(EntityManager em, ProductLabel productLabel) throws BallartelyException {
		try {
			TypedQuery<ProductLabel> queryProductLabel = em.createQuery(
					"select p from ProductLabel p where (:productLabelCode is null or p.productLabelCode = :productLabelCode) "
					+ "and p.productLabelDescription like :productLabelDescription and (:productLabelStatus is null or "
					+ "p.productLabelStatus = :productLabelStatus)", ProductLabel.class);
			queryProductLabel.setParameter("productLabelCode", Utilitarios.vacioComoNulo(productLabel.getProductLabelCode()));
			queryProductLabel.setParameter("productLabelDescription", "%" + productLabel.getProductLabelDescription() + "%");
			queryProductLabel.setParameter("productLabelStatus", Utilitarios.vacioComoNulo(productLabel.getProductLabelStatus()));
			
			return queryProductLabel.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public List<ProductLabel> getProductLabelSalesDataBase(EntityManager em) throws BallartelyException {
		try {
			TypedQuery<ProductLabel> queryProductLabel = em.createQuery(
					"select p from ProductLabel p where p.productLabelCode not in ( " + Constantes.PRODUCT_LABEL_NOT_SALES + ")"
					+ "and p.productLabelStatus = :productLabelStatus", ProductLabel.class);
			queryProductLabel.setParameter("productLabelStatus", Constantes.STATUS_ACTIVE);
			return queryProductLabel.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
