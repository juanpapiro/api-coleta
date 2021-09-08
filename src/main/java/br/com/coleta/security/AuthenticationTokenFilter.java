package br.com.coleta.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.coleta.entity.User;
import br.com.coleta.repository.UserRepository;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private UserRepository userRepository;
	
	private TokenService tokenService;

	public AuthenticationTokenFilter(TokenService tokenService, UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recoverToken(request);
		boolean valid = tokenService.isTokenValid(token);
		if(valid) {
			autenticatedClient(token);
		}
		
		filterChain.doFilter(request,  response);
	}

	private void autenticatedClient(String token) {
		Long idUser = tokenService.getIdUser(token);
		Optional<User> user = userRepository.findById(idUser);
		User userToken = new User();
		if(user.isPresent()) {
			userToken = user.get();
		}
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userToken, null, userToken.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private String recoverToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;			
		}
		return token.substring(7, token.length());
	}

	
	
}
