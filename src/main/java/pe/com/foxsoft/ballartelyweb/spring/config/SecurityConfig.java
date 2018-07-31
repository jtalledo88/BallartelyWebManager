package pe.com.foxsoft.ballartelyweb.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pe.com.foxsoft.ballartelyweb.spring.service.AuthenticationService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// require all requests to be authenticated except for the resources
		http.authorizeRequests().antMatchers("/javax.faces.resource/**", "/recursos/**").permitAll().anyRequest()
				.authenticated();
		// login
		http.formLogin().loginPage("/login.xhtml").permitAll().failureUrl("/login.xhtml?error=true");
		// logout
		http.logout().logoutSuccessUrl("/login.xhtml");
		// not needed as JSF 2.2 is implicitly protected against CSRF
		http.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService)
		  .passwordEncoder(new BCryptPasswordEncoder());
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
}
