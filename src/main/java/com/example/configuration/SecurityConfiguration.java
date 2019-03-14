package com.example.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
//@CrossOrigin(origins = "*", allowCredentials = "true")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	private final String USERS_QUERY = "select email, password, active " + "from user where email=?";
	private final String ROLES_QUERY = "select u.email, r.role " + "from user u "
			+ "inner join user_role ur on (u.id = ur.user_id) " + "inner join role r on (ur.role_id=r.role_id) "
			+ "where u.email=?";

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(USERS_QUERY).authoritiesByUsernameQuery(ROLES_QUERY)
				.dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.headers().frameOptions().disable();

		http.authorizeRequests()
				.antMatchers("/", "/login",
                        "/products/product/add", "/cart" , "/signup",
                        "/products", "/products/product")
				.permitAll();

		http.authorizeRequests()
				.antMatchers("/order")
				.hasAnyAuthority("USER", "ADMIN");

		http.authorizeRequests()
				.antMatchers("/admin/**")
				.hasAnyAuthority("ADMIN");

		// Configuration for Login Form.
		http.authorizeRequests().and().formLogin().loginPage("/login").failureUrl("/login?error=true")
				.defaultSuccessUrl("/").usernameParameter("email").passwordParameter("password").and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
				.rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(60 * 60).and()
				.exceptionHandling().accessDeniedPage("/access_denied");
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);

		return db;
	}
}
