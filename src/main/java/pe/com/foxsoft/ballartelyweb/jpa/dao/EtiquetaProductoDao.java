package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.jpa.util.JPAUtil;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Repository
public class EtiquetaProductoDao {
	
	public List<ProductLabel> getProductLabelsDataBase(EntityManager em, ProductLabel productLabel) throws BallartelyException {
		try {
			TypedQuery<ProductLabel> queryProductLabel = em.createQuery(
					"select p from ProductLabel p join fetch p.productLabelStatus ps where p.productLabelCode = :productLabelCode "
					+ "or p.productLabelDescription like :productLabelDescription or ps.paramId =:productLabelStatus", ProductLabel.class);
			queryProductLabel.setParameter("productLabelCode", productLabel.getProductLabelCode());
			queryProductLabel.setParameter("productLabelDescription", productLabel.getProductLabelDescription());
			queryProductLabel.setParameter("productLabelStatus", productLabel.getProductLabelStatus());
			
			return queryProductLabel.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public int getProductLabelIdDataBase(EntityManager em, String productLabelCode) throws BallartelyException {
		try {
			TypedQuery<Integer> queryProductLabel = em.createQuery(
					"select p.productLabelId from ProductLabel p where p.productLabelCode = :productLabelCode", Integer.class);
			queryProductLabel.setParameter("productLabelCode", productLabelCode);
			
			return queryProductLabel.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public String persistProductLabelDataBase(EntityManager em, ProductLabel productLabel) throws BallartelyException {
		try {
			em.getTransaction().begin();
			JPAUtil.persistEntity(em, productLabel);
			
			int productLabelId = getProductLabelIdDataBase(em, productLabel.getProductLabelCode());
			ProductStock productStock = new ProductStock();
			productStock.setProductLabelId(productLabelId);
			productLabel.setProductLabelId(productLabelId);
			productStock.setProductStockCant(0);
			
			JPAUtil.persistEntity(em, productStock);
			em.getTransaction().commit();
		} catch (BallartelyException e) {
			em.getTransaction().rollback();
			throw e;
		}
		return null;
	}

}
