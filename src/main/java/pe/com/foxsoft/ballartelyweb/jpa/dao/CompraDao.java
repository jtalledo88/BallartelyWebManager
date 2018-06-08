package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Movement;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetail;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingDetailLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ShippingHead;
import pe.com.foxsoft.ballartelyweb.jpa.repository.CompraCabeceraRepository;
import pe.com.foxsoft.ballartelyweb.jpa.util.JPAUtil;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Repository
public class CompraDao{
	
	@Autowired
	private CompraCabeceraRepository compraRepository;
	
	/**Method to persist shipping in database
	 * @param em
	 * @param shippinghead
	 * @param lstShippingDetails
	 * @param movement
	 * @return Confirmation message
	 * @throws BallartelyException
	 */
	public String insertShippingDataBase(EntityManager em, ShippingHead shippinghead, List<ShippingDetail> lstShippingDetails, Movement movement) throws BallartelyException {
		try {
			/** Listamos los registros de compra anteriores **/
			List<ShippingHead> lstShippingExisting = compraRepository.findAll(new Sort(Sort.Direction.DESC, "shippingCreationDate"));
			/** Actualizamos el estado de los registros previos a un estado Frio**/
			for(int i=0; i<lstShippingExisting.size(); i++) {
				ShippingHead head = lstShippingExisting.get(i);
				head.setShippingStatus(Constantes.STATUS_PRODUCT_COLD + (i+1));
				compraRepository.save(head);
			}
			/** Grabamos la cabecera a BD **/
			compraRepository.save(shippinghead);
			/** Obtenemos el id de la cabecera generado en BD **/
			int shippingId = getShippingIdDataBase(em, shippinghead.getPaymentDocumentNumber());
			
			/** Registramos el detalle de la compra en BD y actualizamos el STOCK del producto registrado **/
			for(ShippingDetail detail: lstShippingDetails) {
				if(detail.getShippingQuantityBenefit() == 0) {
					continue;
				}
				detail.setShippingDetailId(0);
				shippinghead.setShippingId(shippingId);
				detail.setShippingHead(shippinghead);
				JPAUtil.persistEntity(em, detail);
				ProductStock productStock = JPAUtil.findEntity(em, ProductStock.class, detail.getProductLabel().getProductLabelId());
				productStock.setProductStockCant(productStock.getProductStockCant() + detail.getShippingQuantityBenefit());
				JPAUtil.mergeEntity(em, productStock);
			}
			/** Obtenemos los detalles de la compra previamente registrados **/
			List<ShippingDetail> lstShippingDetailLabel = getShippingsDetailsDataBase(em, shippingId);
			/** Registramos la etiqueta de producto original en BD**/
			for(ShippingDetail detail: lstShippingDetailLabel) {
				ShippingDetailLabel detailLabel = new ShippingDetailLabel();
				detailLabel.setShippingDetail(detail);
				detailLabel.setProductLabel(detail.getProductLabel());
				detailLabel.setShippingDetailLabelType(Constantes.DETAIL_LABEL_TYPE_ORIGIN);
				JPAUtil.persistEntity(em, detailLabel);
			}
			
			/** Registramos el movimiento en BD**/
			JPAUtil.persistEntity(em, movement);
			return Constantes.MESSAGE_PERSIST_SUCCESS;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	/**
	 * @param em
	 * @param paymentDocumentNumber
	 * @return
	 * @throws BallartelyException
	 */
	private int getShippingIdDataBase(EntityManager em, String paymentDocumentNumber) throws BallartelyException{
		try {
			TypedQuery<Integer> queryShippingId = em.createQuery(
					"select s.shippingId from ShippingHead s where s.paymentDocumentNumber = :paymentDocumentNumber", Integer.class);
			queryShippingId.setParameter("paymentDocumentNumber", paymentDocumentNumber);
				
			return queryShippingId.getSingleResult().intValue();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	/**
	 * @param em
	 * @param shippingHeadId
	 * @return
	 * @throws BallartelyException
	 */
	public List<ShippingDetail> getShippingsDetailsDataBase(EntityManager em, int shippingHeadId) throws BallartelyException{
		try {
			TypedQuery<ShippingDetail> queryShippingDetail = em.createQuery(
					"select s from ShippingDetail s join fetch s.shippingHead sh where sh.shippingId = :shippingId", ShippingDetail.class);
			queryShippingDetail.setParameter("shippingId", shippingHeadId);
			return queryShippingDetail.getResultList();
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
			for(ShippingDetailLabel detailLabel: lstEtiquetasMain) {
				if(!Constantes.DETAIL_LABEL_TYPE_ORIGIN.equals(detailLabel.getShippingDetailLabelType()) &&
						detailLabel.getShippingDetailLabelCreationDate() == null) {
					JPAUtil.removeEntity(em, detailLabel);
				}
			}
			
			return Constantes.MESSAGE_PERSIST_SUCCESS;
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

	public CompraCabeceraRepository getCompraRepository() {
		return compraRepository;
	}

	public void setCompraRepository(CompraCabeceraRepository compraRepository) {
		this.compraRepository = compraRepository;
	}

}
