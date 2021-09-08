package br.com.coleta.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.coleta.dto.LoginDto;
import br.com.coleta.dto.TokenDto;
import br.com.coleta.entity.User;
import br.com.coleta.repository.UserRepository;
import br.com.coleta.security.TokenService;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins="**")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseEntity<TokenDto> authenticate(@RequestBody LoginDto form) {
		UsernamePasswordAuthenticationToken dataLogin = form.convert();
		List<User> user = new ArrayList<>();
		try {
			Authentication authentication = authenticationManager.authenticate(dataLogin);
			String token = tokenService.tokenGenerated(authentication);
			if(token != null) {
				user = userRepository.findByEmail(form.getEmail());
			}
			return ResponseEntity.ok(new TokenDto(token, "Bearer", user.get(0)));			
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	
}
