package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.CustomerDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.Customer;
import pe.com.foxsoft.ballartelyweb.jpa.repository.ClienteRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Component
public class ClienteService {
	
	private EntityManager em;
	
	@Inject
	private ClienteRepository clienteRepository;
	
	@Inject
	private CustomerDao clienteDao;
	
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
	
	public List<Customer> buscarClientes(Customer client) throws BallartelyException {		
		return clienteDao.getCustomersDataBase(em, client);
	}
	
	@Transactional(readOnly=false, noRollbackFor=BallartelyException.class)
	public Customer agregarCliente(Customer client) throws BallartelyException {
		return clienteRepository.save(client);
	}
	
	public Customer obtenerCliente(Customer client) throws BallartelyException {
		return clienteDao.getCustomerByDocNumberDataBase(em, client);
	}
	
	public Customer obtenerCliente(int itemClient) throws BallartelyException {
		return clienteRepository.findOne(itemClient);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Customer editarCliente(Customer client) throws BallartelyException {
		return clienteRepository.save(client);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarCliente(Customer client) throws BallartelyException {
		clienteRepository.delete(client);
	}

	public List<Customer> getListaClientes() throws BallartelyException {
		return clienteRepository.findAll();
	}

	public ClienteRepository getClienteRepository() {
		return clienteRepository;
	}

	public void setClienteRepository(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public CustomerDao getClienteDao() {
		return clienteDao;
	}

	public void setClienteDao(CustomerDao clienteDao) {
		this.clienteDao = clienteDao;
	}

}
