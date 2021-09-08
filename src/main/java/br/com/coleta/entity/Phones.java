package br.com.coleta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_PHONES")
public class Phones {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Long idPhone;
	@Column(name="PHONE")
	private String phone;
	@Column(name="PHONE_ADD")
	private String phoneAdd;
	@Column(name="CELL_PHONE")
	private String cellPhone;
	@Column(name="CELL_PHONE_ADD")
	private String cellPhoneAdd;
	@ManyToOne
	private User user;
	public Long getIdPhone() {
		return idPhone;
	}
	public void setIdPhone(Long idPhone) {
		this.idPhone = idPhone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneAdd() {
		return phoneAdd;
	}
	public void setPhoneAdd(String phoneAdd) {
		this.phoneAdd = phoneAdd;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getCellPhoneAdd() {
		return cellPhoneAdd;
	}
	public void setCellPhoneAdd(String cellPhoneAdd) {
		this.cellPhoneAdd = cellPhoneAdd;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	


}
