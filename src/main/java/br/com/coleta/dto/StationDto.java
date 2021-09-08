package br.com.coleta.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.com.coleta.entity.Stations;

public class StationDto {
	
	@NotNull
	private Long idStation;
	private String nmFantazy;
	private String doc;
	private String typeDoc;

	
	/**
	 * Parâmetro do objeto User
	 */
	private Long idUser;
	private String emailUser;
	
	/**
	 * Parâmetros do objeto Address
	 */
	private Long idAdress;
	private String street;
	private Integer number;
	private String complement;
	private String neighborhood;
	private String city;
	private String state;
	private String uf;
	private String zip;
	
	/**
	 * Parâmetros do objeto Phones
	 */
	private Long idPhones;
	private String phone;
	private String phoneAdd;
	private String cellPhone;
	private String cellPhoneAdd;
	
	/**
	 * Parâmetros do objeto de Web Contacts
	 */
	private Long idWebContacts;
	private String email;
	private String emailAdd;
	private String site;
	private String facebook;
	private String otherUrl;
	
	private List<MktProductDto> mktProducts = new ArrayList<>();
	
	public StationDto() {}
	
	public StationDto(Stations stations) {
		super();	
		this.idStation = stations.getIdStations();
		this.nmFantazy = stations.getNmFantazy();
		this.doc = stations.getDoc();
		this.typeDoc = stations.getTypeDoc();
		this.idUser = stations.getUser().getIdUser();
		this.emailUser = stations.getUser().getEmail();
		this.idAdress = stations.getAddress().getIdAdress();
		this.street = stations.getAddress().getStreet();
		this.number = stations.getAddress().getNumber();
		this.complement = stations.getAddress().getComplement();
		this.neighborhood = stations.getAddress().getNeighborhood();
		this.city = stations.getAddress().getCity();
		this.state = stations.getAddress().getState();
		this.uf = stations.getAddress().getUf();
		this.zip = stations.getAddress().getZip();
		this.idPhones = stations.getPhones().getIdPhone();
		this.phone = stations.getPhones().getPhone();
		this.phoneAdd = stations.getPhones().getPhoneAdd();
		this.cellPhone = stations.getPhones().getCellPhone();
		this.cellPhoneAdd = stations.getPhones().getCellPhoneAdd();
		this.idWebContacts = stations.getWebContacts().getIdWebContact();
		this.email = stations.getWebContacts().getEmail();
		this.emailAdd = stations.getWebContacts().getEmailAdd();
		this.site = stations.getWebContacts().getSite();
		this.facebook = stations.getWebContacts().getFacebook();
		this.otherUrl = stations.getWebContacts().getOtherUrl();
	}

	public Long getIdStation() {
		return idStation;
	}

	public void setIdStation(Long idStation) {
		this.idStation = idStation;
	}

	public String getNmFantazy() {
		return nmFantazy;
	}

	public void setNmFantazy(String nmFantazy) {
		this.nmFantazy = nmFantazy;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getTypeDoc() {
		return typeDoc;
	}

	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}

	public Long getIdAdress() {
		return idAdress;
	}

	public void setIdAdress(Long idAdress) {
		this.idAdress = idAdress;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Long getIdPhones() {
		return idPhones;
	}

	public void setIdPhones(Long idPhones) {
		this.idPhones = idPhones;
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

	public Long getIdWebContacts() {
		return idWebContacts;
	}

	public void setIdWebContacts(Long idWebContacts) {
		this.idWebContacts = idWebContacts;
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

	public List<MktProductDto> getMktProducts() {
		return mktProducts;
	}

	public void setMktProducts(List<MktProductDto> mktProducts) {
		this.mktProducts = mktProducts;
	}
	

}
