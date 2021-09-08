package br.com.coleta.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserInsertDto {
	
	@NotNull @NotEmpty
	private String email;
	
	@NotNull @NotEmpty @Length(min = 6, max = 18)
	private String password;
		

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return new BCryptPasswordEncoder().encode(password);
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
