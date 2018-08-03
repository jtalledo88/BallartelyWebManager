package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideCotization;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
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
}
