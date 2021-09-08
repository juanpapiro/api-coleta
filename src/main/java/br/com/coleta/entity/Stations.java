package br.com.coleta.entity;

import javax.persistence.CascadeType;
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
@Table(name="TB_STATIONS")
public class Stations {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", length=255)
	private Long idStations;
	@Column(name="NM_FANTAZY", nullable=false)
	private String nmFantazy;
	@Column(name="TYPEDOC", nullable=false)
	private String typeDoc;
	@Column(name="DOC", nullable=false)
	private String doc;
	@ManyToOne
	private User user;
	@OneToOne
	@JoinColumn(unique=true)
	private Address address;
	@OneToOne
	@JoinColumn(unique=true)
	private Phones phones;
	@OneToOne
	@JoinColumn(unique=true)
	private WebContacts webContacts;
	
	
	public Stations() {
	}
	
	public Long getIdStations() {
		return idStations;
	}
	public void setIdStations(Long idStations) {
		this.idStations = idStations;
	}
	public String getNmFantazy() {
		return nmFantazy;
	}
	public void setNmFantazy(String nmFantazy) {
		this.nmFantazy = nmFantazy;
	}
	public String getTypeDoc() {
		return typeDoc;
	}
	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Phones getPhones() {
		return phones;
	}
	public void setPhones(Phones phones) {
		this.phones = phones;
	}
	public WebContacts getWebContacts() {
		return webContacts;
	}
	public void setWebContacts(WebContacts webContacts) {
		this.webContacts = webContacts;
	}

	
}
