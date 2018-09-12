package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Repository
public class TransporteDao {
	
	public List<Transport> getTransportsDataBase(EntityManager em, Transport transport) throws BallartelyException{
		try {
			TypedQuery<Transport> queryTransports = em.createQuery(
					"select t from Transport t where t.carNumber = :carNumber or "
					+ "t.driverLicenseNumber = :driverLicenseNumber or t.driverNames like :driverNames", Transport.class);
			queryTransports.setParameter("carNumber", transport.getCarNumber());
			queryTransports.setParameter("driverLicenseNumber", transport.getDriverLicenseNumber());
			queryTransports.setParameter("driverNames", "%" + transport.getDriverNames() + "%");
			
			return queryTransports.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Transport> getProviderTransportsDataBase(EntityManager em, int providerId) throws BallartelyException{
		try {
			TypedQuery<Transport> queryProviderTransports = em.createQuery(
					"select pt.transport from ProviderTransport pt "
					+ "where pt.provider.id = :providerId", Transport.class);
			queryProviderTransports.setParameter("providerId", providerId);
			
			return queryProviderTransports.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Transport> getEnterpriseTransportsDataBase(EntityManager em, int enterpriseId) throws BallartelyException{
		try {
			TypedQuery<Transport> queryEnterpriseTransports = em.createQuery(
					"select et.transport from EnterpriseTransport et "
					+ "where et.enterprise.id = :enterpriseId", Transport.class);
			queryEnterpriseTransports.setParameter("enterpriseId", enterpriseId);
			
			return queryEnterpriseTransports.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Transport> getTransportsProviderAvailableDataBase(EntityManager em) throws BallartelyException{
		try {
			TypedQuery<Transport> queryTransports = em.createQuery(
					"select pt.transport from ProviderTransport pt where pt.transport.transportStatus = :transportStatus", Transport.class);
			queryTransports.setParameter("transportStatus", Constantes.STATUS_ACTIVE);
			return queryTransports.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public List<Transport> getTransportsEnterpriseAvailableDataBase(EntityManager em) throws BallartelyException{
		try {
			TypedQuery<Transport> queryTransports = em.createQuery(
					"select et.transport from EnterpriseTransport et where et.transport.transportStatus = :transportStatus", Transport.class);
			queryTransports.setParameter("transportStatus", Constantes.STATUS_ACTIVE);
			return queryTransports.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public int deleteProviderTransportsDataBase(EntityManager em, int providerId) throws BallartelyException{
		try {
			Query queryDeleteProviderTransports = em.createQuery(
					"delete from ProviderTransport pt where pt.provider.id = :providerId");
			queryDeleteProviderTransports.setParameter("providerId", providerId);
			return queryDeleteProviderTransports.executeUpdate();
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public int deleteEnterpriseTransportsDataBase(EntityManager em, int enterpriseId) throws BallartelyException{
		try {
			Query queryDeleteEnterpriseTransports = em.createQuery(
					"delete from EnterpriseTransport et where et.enterprise.id = :enterpriseId");
			queryDeleteEnterpriseTransports.setParameter("enterpriseId", enterpriseId);
			return queryDeleteEnterpriseTransports.executeUpdate();
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
