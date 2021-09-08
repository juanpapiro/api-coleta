package br.com.coleta.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.coleta.dto.MktProductDto;

@Entity
@Table(name="TB_MARKET_PRODUCT")
public class MarketProduct {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID", length=14)
	private Long idMktProduct;
	
	@Column(name="VALUE", length=14)
	private BigDecimal value;
	
	@Column(name="DT_INSERT")
	private Timestamp insertDate;
	
	@Column(name="DT_UPDATE")
	private Timestamp updateDate;
	
	@ManyToOne
	private Stations stations;
	
	@OneToOne
	private Product product;
	
	
	
	public MarketProduct() {
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

	public Timestamp getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Stations getStations() {
		return stations;
	}

	public void setStations(Stations stations) {
		this.stations = stations;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	

}
