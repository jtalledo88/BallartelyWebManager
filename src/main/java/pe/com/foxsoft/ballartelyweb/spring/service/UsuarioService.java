package pe.com.foxsoft.ballartelyweb.spring.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.com.foxsoft.ballartelyweb.jpa.dao.UsuarioDao;
import pe.com.foxsoft.ballartelyweb.jpa.data.User;
import pe.com.foxsoft.ballartelyweb.jpa.repository.UsuarioRepository;
import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;

@Service
public class UsuarioService {
	
	
	private EntityManager em;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	public EntityManager getEm() {
		return em;
	}
	
	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public UsuarioRepository getUsuarioJPA() {
		return usuarioRepository;
	}
	
	public void setUsuarioJPA(UsuarioRepository UsuarioJPA) {
		this.usuarioRepository = UsuarioJPA;
	}
	
	public User getUser(User user) throws BallartelyException {
		return usuarioDao.getUserDataBase(em, user);
	}
	
	public User getUser(String userName) throws BallartelyException {
		return usuarioDao.getUserDataBase(em, userName);
	}
	
	@Transactional(readOnly=false, rollbackFor=Throwable.class)
	public User editarUsuario(User user) throws BallartelyException {
		return usuarioRepository.save(user);
	}

	public UsuarioRepository getUsuarioRepository() {
		return usuarioRepository;
	}

	public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

}
