package br.com.coleta.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.coleta.entity.MarketProduct;

public class MktProductDto {
	
	private Long idMktProduct;
	private Long idProduct;
	private String product;
	private BigDecimal value;
	private Long idStations;
	private String nmFantazy;
	private String insertDate;
	private String updateDate;
	

	public MktProductDto() {}
	
	
	public MktProductDto(MarketProduct product) {
		super();
		this.idMktProduct = product.getIdMktProduct();
		this.value = product.getValue();
		this.idStations = product.getStations().getIdStations();
		this.nmFantazy = product.getStations().getNmFantazy();
		this.idProduct = product.getProduct().getIdProduct();
		this.product = product.getProduct().getProduct();
		this.insertDate = formatDate(product.getInsertDate());
		this.updateDate = formatDate(product.getUpdateDate());
	}


	public Long getIdMktProduct() {
		return idMktProduct;
	}
	public void setIdMktProduct(Long idMktProduct) {
		this.idMktProduct = idMktProduct;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
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
	public Long getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(Long idProduct) {
		this.idProduct = idProduct;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	public Timestamp formatDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		Timestamp dt = null;
		if(date != null) {
			dt = Timestamp.valueOf(LocalDateTime.parse(date, formatter));
		}
		return dt;
	}
	
	public String formatDate(Timestamp date) {
		String dt = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		if(date != null) {
			dt = date.toLocalDateTime().format(formatter);
		}
		return dt;
	}
	
}
