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
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		try {
			pe.com.foxsoft.ballartelyweb.jpa.data.User userInfo = usuarioService.getUser(username);

			GrantedAuthority authority = new SimpleGrantedAuthority(Constantes.ROLE_DEFAULT);
			userDetails = (UserDetails) new User(userInfo.getUserName(), userInfo.getUserPassword(),
					Arrays.asList(authority));
		} catch (BallartelyException e) {
			e.printStackTrace();
		}
		return userDetails;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
