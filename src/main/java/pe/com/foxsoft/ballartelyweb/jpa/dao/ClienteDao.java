package pe.com.foxsoft.ballartelyweb.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import pe.com.foxsoft.ballartelyweb.jpa.data.Client;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Repository
public class ClienteDao{
	
	public List<Client> getClientsDataBase(EntityManager em, Client client) throws BallartelyException {
		try {
			TypedQuery<Client> queryClients = em.createQuery(
					"select c from Client c join fetch c.clientStatus cs join fetch c.clientType ct join fetch c.documentType dt "
					+ "where c.clientNames = :clientNames or c.documentNumber =:documentNumber or cs.paramId = :clientStatus or "
					+ "or ct.paramId =:clientType or dt.paramId =:documentType", Client.class);
			queryClients.setParameter("clientNames", client.getClientNames());
			queryClients.setParameter("documentNumber", client.getDocumentNumber());
			queryClients.setParameter("clientStatus", client.getClientStatus());
			queryClients.setParameter("clientType", client.getClientType());
			queryClients.setParameter("documentType", client.getDocumentType());
			
			return queryClients.getResultList();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}
	
	public Client getClientByDocNumberDataBase(EntityManager em, Client client) throws BallartelyException {
		try {
			TypedQuery<Client> queryClient = em.createQuery(
					"select c from Client c where c.documentNumber = :documentNumber", Client.class);
			queryClient.setParameter("documentNumber", client.getDocumentNumber());
			
			return queryClient.getSingleResult();
		} catch (NoResultException nre) {
			throw new BallartelyException(BallartelyException.NO_RESULT_ERROR, nre.getMessage());
		} catch (Exception e) {
			throw new BallartelyException(BallartelyException.GENERAL_ERROR, e.getMessage());
		}
	}

}
