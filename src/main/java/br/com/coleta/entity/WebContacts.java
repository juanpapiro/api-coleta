package br.com.coleta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_WEB_CONTACTS")
public class WebContacts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Long idWebContact;
	@Column(name="EMAIL")
	private String email;
	@Column(name="EMAIL_ADD")
	private String emailAdd;
	@Column(name="SITE")
	private String site;
	@Column(name="FACEBOOK")
	private String facebook;
	@Column(name="OTHER_URL")
	private String otherUrl;
	@ManyToOne
	private User user;
	public Long getIdWebContact() {
		return idWebContact;
	}
	public void setIdWebContact(Long idWebContact) {
		this.idWebContact = idWebContact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailAdd() {
		return emailAdd;
	}
	public void setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getOtherUrl() {
		return otherUrl;
	}
	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	

	
}
