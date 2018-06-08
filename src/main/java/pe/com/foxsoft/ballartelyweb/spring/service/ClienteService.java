package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.ClienteDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Client;
import pe.com.foxsoft.ballartelyweb.jpa.repository.ClienteRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Component
public class ClienteService {
	
	private EntityManager em;
	
	@Inject
	private ClienteRepository clienteRepository;
	
	@Inject
	private ClienteDao clienteDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public ClienteRepository getClienteJPA() {
		return clienteRepository;
	}

	public void setClienteJPA(ClienteRepository clienteJPA) {
		this.clienteRepository = clienteJPA;
	}
	
	public List<Client> buscarClientes(Client client) throws BallartelyException {		
		return clienteDao.getClientsDataBase(em, client);
	}
	
	@Transactional(readOnly=false, noRollbackFor=BallartelyException.class)
	public Client agregarCliente(Client client) throws BallartelyException {
		return clienteRepository.save(client);
	}
	
	public Client obtenerCliente(Client client) throws BallartelyException {
		return clienteDao.getClientByDocNumberDataBase(em, client);
	}
	
	public Client obtenerCliente(int itemClient) throws BallartelyException {
		return clienteRepository.findOne(itemClient);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Client editarCliente(Client client) throws BallartelyException {
		return clienteRepository.save(client);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarCliente(Client client) throws BallartelyException {
		clienteRepository.delete(client);
	}

	public List<Client> getListaClientes() throws BallartelyException {
		return clienteRepository.findAll();
	}

	public ClienteRepository getClienteRepository() {
		return clienteRepository;
	}

	public void setClienteRepository(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public ClienteDao getClienteDao() {
		return clienteDao;
	}

	public void setClienteDao(ClienteDao clienteDao) {
		this.clienteDao = clienteDao;
	}

}
