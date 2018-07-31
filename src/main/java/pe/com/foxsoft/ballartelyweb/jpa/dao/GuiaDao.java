package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.GuideDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.GuideHead;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetailLabel;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaCabeceraRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.GuiaDetalleRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.MovimientoRepository;
import pe.com.foxsoft.ballartelyweb.jpa.util.JPAUtil;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Repository
public class GuiaDao{
	
	@Autowired
	private GuiaCabeceraRepository guiaCabeceraRepository;
	
	@Autowired
	private GuiaDetalleRepository guiaDetalleRepository;
	
	@Autowired 
	private MovimientoRepository movimientoRepository;
	
	
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
	
	/**
	 * @param em
	 * @param shippingDetailId
	 * @return
	 * @throws BallartelyException
	 */
	public List<ShippingDetailLabel> getShippingsDetailsLabelDataBase(EntityManager em, int shippingDetailId) throws BallartelyException{
		try {
			TypedQuery<ShippingDetailLabel> queryShippingDetailLabel = em.createQuery(
					"select s from ShippingDetailLabel s join fetch s.shippingDetail sd where sd.shippingDetailId = :shippingDetailId", ShippingDetailLabel.class);
			queryShippingDetailLabel.setParameter("shippingDetailId", shippingDetailId);
			return queryShippingDetailLabel.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public String grabarCompraDetalleLabel(EntityManager em, List<ShippingDetailLabel> lstEtiquetasMain) throws BallartelyException{
		try {
			TypedQuery<ShippingDetailLabel> queryDeleteShippingDetailLabel = em.createQuery(
					"delete from ShippingDetailLabel s join fetch s.shippingDetail sd where sd.shippingDetailId = :shippingDetailId "
					+ "and s.shippingDetailLabelType <> : shippingDetailLabelType", ShippingDetailLabel.class);
//			queryDeleteShippingDetailLabel.setParameter("shippingDetailId", lstEtiquetasMain.get(0).getShippingDetail().getShippingDetailId());
//			queryDeleteShippingDetailLabel.setParameter("shippingDetailLabelType", Constantes.DETAIL_LABEL_TYPE_ORIGIN);
			queryDeleteShippingDetailLabel.executeUpdate();
			for(ShippingDetailLabel detailLabel: lstEtiquetasMain) {
				if(detailLabel.getShippingDetailLabelCreationDate() == null) {
					JPAUtil.persistEntity(em, detailLabel);
				}else {
					JPAUtil.mergeEntity(em, detailLabel);
				}
			}
			
			return Constantes.MESSAGE_PERSIST_SUCCESS;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public GuiaCabeceraRepository getGuiaCabeceraRepository() {
		return guiaCabeceraRepository;
	}

	public void setGuiaCabeceraRepository(GuiaCabeceraRepository guiaCabeceraRepository) {
		this.guiaCabeceraRepository = guiaCabeceraRepository;
	}

	public GuiaDetalleRepository getGuiaDetalleRepository() {
		return guiaDetalleRepository;
	}

	public void setGuiaDetalleRepository(GuiaDetalleRepository guiaDetalleRepository) {
		this.guiaDetalleRepository = guiaDetalleRepository;
	}

	public MovimientoRepository getMovimientoRepository() {
		return movimientoRepository;
	}

	public void setMovimientoRepository(MovimientoRepository movimientoRepository) {
		this.movimientoRepository = movimientoRepository;
	}

}
