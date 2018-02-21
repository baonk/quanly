package com.nv.baonk.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.nv.baonk.login.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private BCryptPasswordEncoder BCryptPass;

	@Autowired
	private UsernameStoringUrlAuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private BaonkAuthenticationSuccessfulHandler authenticationSuccessfulHandler;
	
	@Autowired
	private BaonkLogoutSuccessfulHandler logoutSuccessfulHandler;
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private HttpServletRequest httpRequest;
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return new MandatoryUserDetailsService(userService, httpRequest);
	}

/*	@Override 
	protected void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.usersByUsernameQuery(userQuery)
			.authoritiesByUsernameQuery(roleQuery)
			.dataSource(dataSource)
			.passwordEncoder(BCryptPass);
	}*/

	@Override
	protected void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(BCryptPass);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
				.antMatchers("/", "/login").permitAll()
				/*.antMatchers("/hahaha/**").hasAuthority("ADMIN")
				.antMatchers("/hahaha/**").hasAuthority("USER")*/
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.failureHandler(authenticationFailureHandler)
				.successHandler(authenticationSuccessfulHandler)
				//.failureUrl("/login?test=true")
				//.defaultSuccessUrl("/home")
				.usernameParameter("userid")
				.passwordParameter("password")
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				/*.logoutSuccessUrl("/").and().exceptionHandling()*/
				.logoutSuccessHandler(logoutSuccessfulHandler)
				/*.accessDeniedPage("/access-denied")*/
			.and()
				.headers().frameOptions().sameOrigin()
			.and()
			 	.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/baonk/**");
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
}
