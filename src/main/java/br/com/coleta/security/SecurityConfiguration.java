package br.com.coleta.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import br.com.coleta.repository.UserRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthenticationService authenticationService;
	
	//autowired para injetar o token no AuthenticationTokenFilter
	@Autowired
	private TokenService tokenService;
	
	//autowired para injetar o userRepository no AuthenticationTokenFilter
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Gera o authentication manager que é usado como autowired no authentication controller
	 */
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	/**
	 * configurações de autenticação
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/**
	 * configuração de Autorização
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/stations").permitAll()
		.antMatchers(HttpMethod.GET, "/stations/**").permitAll()
		.antMatchers(HttpMethod.GET, "/marketstuff").permitAll()
		.antMatchers(HttpMethod.GET, "/marketstuff/**").permitAll()
		.antMatchers(HttpMethod.GET, "/stuff").permitAll()
		.antMatchers(HttpMethod.GET, "/stuff/**").permitAll()
		.antMatchers(HttpMethod.GET, "/user").permitAll()
		.antMatchers(HttpMethod.GET, "/user/**").permitAll()
		.antMatchers(HttpMethod.POST, "/user").permitAll()
		.antMatchers(HttpMethod.POST, "/user/**").permitAll()
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.antMatchers(HttpMethod.GET, "/auth").permitAll()
		.antMatchers(HttpMethod.POST, "/auth/**").permitAll()
		.antMatchers(HttpMethod.GET, "/recycling").permitAll()
		.antMatchers(HttpMethod.GET, "/recycling/**").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/webjars/**", "/swagger-resources/**", "/v2/api-docs/**", "/swagger-ui.html/**").permitAll()
		.anyRequest().authenticated()
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().headers().frameOptions().disable()
		.and().addFilterBefore(new AuthenticationTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class);
        http.cors().configurationSource(request -> 
        	new CorsConfiguration(getCorsConfiguration())
        );

	}
	
	/**
	 * configuração de recursos estáticos(js, css, imagens, etc)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/v2/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
	}
	
	public CorsConfiguration getCorsConfiguration() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.addAllowedMethod(HttpMethod.GET);
		configuration.addAllowedMethod(HttpMethod.POST);
		configuration.addAllowedMethod(HttpMethod.OPTIONS);
		configuration.addAllowedMethod(HttpMethod.HEAD);
		configuration.addAllowedMethod(HttpMethod.PUT);
		configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.setAllowedOrigins(Arrays.asList("*"));
	    return configuration;      
	}
	
//	  @Bean
//	    public FilterRegistrationBean corsFilter() {
//	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
//	        config.addAllowedMethod(HttpMethod.GET);
//	        config.addAllowedMethod(HttpMethod.POST);
//	        config.addAllowedMethod(HttpMethod.PUT);
//	        config.addAllowedMethod(HttpMethod.DELETE);
//	        source.registerCorsConfiguration("/**", config);
//	        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//	        bean.setOrder(0);
//	        return bean;
//	    }
	
//	 @Bean
//	 public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;      
//	  }
	

}
