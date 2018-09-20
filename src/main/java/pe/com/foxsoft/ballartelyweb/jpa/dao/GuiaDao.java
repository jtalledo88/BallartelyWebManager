package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideCotization;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetailSales;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.spring.domain.ProductGuide;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Repository
public class GuiaDao{
	
	/**
	 * @param em
	 * @param objGuideSearch
	 * @param emissionDateInit
	 * @param emissionDateEnd
	 * @param creationDateInit
	 * @param creationDateEnd
	 * @return
	 * @throws BallartelyException
	 */
	public List<GuideHead> getGuideHeadsDataBase(EntityManager em, GuideHead objGuideSearch, Date emissionDateInit, Date emissionDateEnd, 
			Date creationDateInit, Date creationDateEnd) throws BallartelyException{
		try {
			StringBuilder query = new StringBuilder();
			query.append("select gh from GuideHead gh where (:guideNumber is null or gh.guideNumber = :guideNumber) ");
			query.append("and (:guideStatus is null or gh.guideStatus = :guideStatus) ");
			query.append("and (:guideBenefied is null or gh.guideBenefied = :guideBenefied) ");
			query.append("and (:guideType is null or gh.guideType = :guideType) ");
			if(emissionDateInit != null && emissionDateEnd != null) {
				query.append("and gh.emissionDate between :emissionDateInit and :emissionDateEnd ");
			}
			if(creationDateInit != null && creationDateEnd != null) {
				query.append("and gh.guideCreationDate between :guideCreationDateInit and :guideCreationDateEnd ");
			}
			query.append("order by gh.guideCreationDate");
			TypedQuery<GuideHead> queryGuideHead = em.createQuery(query.toString(), GuideHead.class);
			queryGuideHead.setParameter("guideNumber", Utilitarios.vacioComoNulo(objGuideSearch.getGuideNumber()));
			queryGuideHead.setParameter("guideStatus", Utilitarios.vacioComoNulo(objGuideSearch.getGuideStatus()));
			queryGuideHead.setParameter("guideBenefied", Utilitarios.vacioComoNulo(objGuideSearch.getGuideBenefied()));
			queryGuideHead.setParameter("guideType", Utilitarios.vacioComoNulo(objGuideSearch.getGuideType()));
			
			if(emissionDateInit != null && emissionDateEnd != null) {
				queryGuideHead.setParameter("emissionDateInit", emissionDateInit);
				queryGuideHead.setParameter("emissionDateEnd", emissionDateEnd);
			}
			if(creationDateInit != null && creationDateEnd != null) {
				queryGuideHead.setParameter("guideCreationDateInit", creationDateInit);
				queryGuideHead.setParameter("guideCreationDateEnd", creationDateEnd);
			}
			
			return queryGuideHead.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	/**
	 * @param em
	 * @param guideHeadId
	 * @return
	 * @throws BallartelyException
	 */
	public List<GuideDetail> getGuideDetailsDataBase(EntityManager em, int guideHeadId) throws BallartelyException{
		try {
			TypedQuery<GuideDetail> queryGuideDetail = em.createQuery(
					"select gd from GuideDetail gd join fetch gd.guideHead gh where gh.id = :guideHeadId", GuideDetail.class);
			queryGuideDetail.setParameter("guideHeadId", guideHeadId);
			
			
			return queryGuideDetail.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	/**
	 * @param em
	 * @param guideHeadId
	 * @return
	 * @throws BallartelyException
	 */
	public List<GuideDetailSales> getGuideDetailSalesDataBase(EntityManager em, int guideHeadId) throws BallartelyException{
		try {
			TypedQuery<GuideDetailSales> queryGuideDetail = em.createQuery(
					"select gd from GuideDetailSales gd join fetch gd.guideHead gh where gh.id = :guideHeadId", GuideDetailSales.class);
			queryGuideDetail.setParameter("guideHeadId", guideHeadId);
			
			
			return queryGuideDetail.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public List<GuideCotization> getListaGuiaCotizacion(EntityManager em, int guideHeadId) throws BallartelyException{
		try {
			TypedQuery<GuideCotization> queryGuideCotization = em.createQuery(
					"select gc from GuideCotization gc join fetch gc.guideHead gh where gh.id = :guideHeadId", GuideCotization.class);
			queryGuideCotization.setParameter("guideHeadId", guideHeadId);
			
			
			return queryGuideCotization.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProductGuide> getListaProductoGuia(EntityManager em) throws BallartelyException {
		try {
			Query queryProductGuide = em.createNativeQuery(
					"select a.id, b.product_label_id,b.product_stock_cant, c.total_unit_cost " + 
					"from guide_head a join product_stock b on a.id = b.guide_head_id  " + 
					"join guide_cotization c on a.id = c.guide_head_id " + 
					"order by a.id ");
			
			List<Object[]>  result = queryProductGuide.getResultList();
			List<ProductGuide> lstProductGuide = new ArrayList<>();
			for(Object[] o: result) {
				ProductGuide productGuide = new ProductGuide();
				productGuide.setFirstColumn(String.valueOf((Integer)o[0]));
				productGuide.setId((Integer)o[1]);
				productGuide.setStockInput(0);
				productGuide.setStockProduct((Integer)o[2]);
				productGuide.setCostProduct((BigDecimal)o[3]);
				
				lstProductGuide.add(productGuide);
			}
			
			return lstProductGuide;
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
		
	}

	public int actualizarStockGuia(EntityManager em, ProductGuide productGuide) throws BallartelyException {
		try {
			Query queryProductGuideStock = em.createQuery(
					"update ProductStock ps set ps.productStockCant = ps.productStockCant - :productStockCant " +
					"where ps.guideHead.id = :guideHeadId and ps.productLabel.id = :productLabelId");
			queryProductGuideStock.setParameter("productStockCant", productGuide.getStockInput());
			queryProductGuideStock.setParameter("guideHeadId", Integer.parseInt(productGuide.getFirstColumn()));
			queryProductGuideStock.setParameter("productLabelId", productGuide.getId());
			
			return queryProductGuideStock.executeUpdate();
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public Integer getGuideQuantityDataBase(EntityManager em, Integer guideHeadId) throws BallartelyException {
		try {
			TypedQuery<Long> queryAmountAccount = em.createQuery(
					"select sum(gd.productQuantity) from GuideDetail gd where gd.guideHead.id = :guideHeadId", Long.class);
			queryAmountAccount.setParameter("guideHeadId", guideHeadId);
			
			Integer result = queryAmountAccount.getSingleResult().intValue();
			
			return result != null ? result : 0;
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public Integer getStockGuideQuantityDataBase(EntityManager em, Integer guideHeadId) throws BallartelyException {
		try {
			TypedQuery<Long> queryQuantity = em.createQuery(
					"select sum(ps.productStockCant) from ProductStock ps where ps.guideHead.id = :guideHeadId "
					+ "and ps.productLabel.productLabelCode <> :productLabelCode", Long.class);
			queryQuantity.setParameter("guideHeadId", guideHeadId);
			queryQuantity.setParameter("productLabelCode", Constantes.PRODUCT_LABEL_DEAD);
			
			Integer result = queryQuantity.getSingleResult().intValue();
			
			return result != null ? result : 0;
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
}
