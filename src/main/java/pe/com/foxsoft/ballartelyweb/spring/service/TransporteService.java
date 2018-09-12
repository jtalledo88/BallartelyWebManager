package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.TransporteDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.EnterpriseTransport;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProviderTransport;
import pe.com.foxsoft.ballartelyweb.jpa.data.Transport;
import pe.com.foxsoft.ballartelyweb.jpa.repository.TransporteEmpresaRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.TransporteProveedorRepository;
import pe.com.foxsoft.ballartelyweb.jpa.repository.TransporteRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;
import pe.com.foxsoft.ballartelyweb.spring.util.Utilitarios;

@Service
public class TransporteService {
	
	private EntityManager em;
	
	@Autowired
	private TransporteRepository transporteRepository;
	
	@Autowired
	private TransporteProveedorRepository transporteProveedorRepository;
	
	@Autowired
	private TransporteEmpresaRepository transporteEmpresaRepository;
	
	@Autowired
	private TransporteDao transporteDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public TransporteRepository getTransporteRepository() {
		return transporteRepository;
	}

	public void setTransporteRepository(TransporteRepository transporteRepository) {
		this.transporteRepository = transporteRepository;
	}
	
	public List<Transport> buscarTransportes(Transport transport) throws BallartelyException {		
		return transporteDao.getTransportsDataBase(em, transport);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Transport agregarTransporte(Transport transport) throws BallartelyException {
		return transporteRepository.save(transport);
	}
	
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public String gestionarTransporteProveedores(int itemProveedor, List<ProviderTransport> lstProviderTransport) throws BallartelyException {
		transporteDao.deleteProviderTransportsDataBase(em, itemProveedor);
		List<ProviderTransport> lstRecords = transporteProveedorRepository.save(lstProviderTransport);
		return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_LIST_SUCCESS, lstRecords.size(), lstProviderTransport.size());
	}
	
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public String gestionarTransporteEmpresa(int itemEmpresa, List<EnterpriseTransport> lstEnterpriseTransport) throws BallartelyException {
		transporteDao.deleteEnterpriseTransportsDataBase(em, itemEmpresa);
		List<EnterpriseTransport> lstRecords = transporteEmpresaRepository.save(lstEnterpriseTransport);
		return Utilitarios.reemplazarMensaje(Constantes.MESSAGE_PERSIST_LIST_SUCCESS, lstRecords.size(), lstEnterpriseTransport.size());
	}
	
	public Transport obtenerTransporte(int itemTransporte) throws BallartelyException {
		return transporteRepository.findOne(itemTransporte);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public Transport editarTransporte(Transport transport) throws BallartelyException {
		return transporteRepository.save(transport);
	}

	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public void eliminarTransporte(Transport transport) throws BallartelyException {
		transporteRepository.delete(transport);
	}
	
	public List<Transport> getListaTransportes() throws BallartelyException {
		Transport transport = new Transport();
		transport.setTransportStatus(Constantes.STATUS_ACTIVE);
		Example<Transport> eTransport = Example.of(transport);
		return transporteRepository.findAll(eTransport);
	}
	
	public List<Transport> getListaTransportesProveedor(int itemProveedor) throws BallartelyException {
		return transporteDao.getProviderTransportsDataBase(em, itemProveedor);
	}
	
	public List<Transport> getListaTransportesEmpresa(int itemEmpresa) throws BallartelyException {
		return transporteDao.getEnterpriseTransportsDataBase(em, itemEmpresa);
	}
	
	public List<Transport> getListaTransportesDisponibles() throws BallartelyException {
		List<Transport> lstTransports = getListaTransportes();
		List<Transport> lstTransportsProvider = transporteDao.getTransportsProviderAvailableDataBase(em);
		List<Transport> lstTransportsEnterprise = transporteDao.getTransportsEnterpriseAvailableDataBase(em);
		
		for(Transport t1: lstTransportsProvider) {
			for(Transport t2: lstTransports) {
				if(t1.getId() == t2.getId()) {
					lstTransports.remove(t2);
					break;
				}
			}
		}
		for(Transport t1: lstTransportsEnterprise) {
			for(Transport t2: lstTransports) {
				if(t1.getId() == t2.getId()) {
					lstTransports.remove(t2);
					break;
				}
			}
		}
		return lstTransports;
	}

	public TransporteDao getTransporteDao() {
		return transporteDao;
	}

	public void setTransporteDao(TransporteDao transporteDao) {
		this.transporteDao = transporteDao;
	}

	public TransporteProveedorRepository getTransporteProveedorRepository() {
		return transporteProveedorRepository;
	}

	public void setTransporteProveedorRepository(TransporteProveedorRepository transporteProveedorRepository) {
		this.transporteProveedorRepository = transporteProveedorRepository;
	}

	public TransporteEmpresaRepository getTransporteEmpresaRepository() {
		return transporteEmpresaRepository;
	}

	public void setTransporteEmpresaRepository(TransporteEmpresaRepository transporteEmpresaRepository) {
		this.transporteEmpresaRepository = transporteEmpresaRepository;
	}

}
