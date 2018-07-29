package pe.com.foxsoft.ballartelyweb.spring.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.com.foxsoft.ballartelyweb.spring.exception.BallartelyException;
import pe.com.foxsoft.ballartelyweb.spring.util.Constantes;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private EmpresaService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		try {
			pe.com.foxsoft.ballartelyweb.jpa.data.Enterprise enterpriseInfo = usuarioService.getEnterprise(username);

			GrantedAuthority authority = new SimpleGrantedAuthority(Constantes.ROLE_DEFAULT);
			userDetails = (UserDetails) new User(enterpriseInfo.getLogin(), enterpriseInfo.getPassword(),
					Arrays.asList(authority));
		} catch (BallartelyException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		return userDetails;
	}

	public EmpresaService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(EmpresaService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
