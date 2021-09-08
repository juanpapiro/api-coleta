package br.com.coleta.dto;

import br.com.coleta.entity.User;

public class TokenDto {
	
	private String token;
	private String type;
	private String email;
	private Long idUser;

	public TokenDto() {}
	
	public TokenDto(String token, String type) {
		this.token = token;
		this.type = type;
	}
	
	public TokenDto(String token, String type, User user) {
		this.token = token;
		this.type = type;
		this.email = user.getEmail();
		this.idUser = user.getIdUser();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

}
