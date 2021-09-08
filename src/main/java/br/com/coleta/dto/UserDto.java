package br.com.coleta.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.coleta.entity.User;

public class UserDto {
	
	private Long id;
	@NotNull @NotEmpty
	private String email;
	
	public UserDto(){
	}
	
	public UserDto(User user) {
		super();
		this.id = user.getIdUser();
		this.email = user.getEmail();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

}
