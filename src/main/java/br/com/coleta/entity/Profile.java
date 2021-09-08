package br.com.coleta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name="TB_PROFILE")
public class Profile implements GrantedAuthority {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID_PROFILE")
	private Long idProfile;
	@Column(name="NM_PROFILE")
	private String nameProfile;
	
	public Profile() {}
		
	public Long getId() {
		return idProfile;
	}
	public void setId(Long idProfile) {
		this.idProfile = idProfile;
	}
	public String getNameProfile() {
		return nameProfile;
	}
	public void setNameProfile(String nameProfile) {
		this.nameProfile = nameProfile;
	}
	
	@Override
	public String getAuthority() {
		return nameProfile;
	}
	

}
