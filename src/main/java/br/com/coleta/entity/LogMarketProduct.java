package br.com.coleta.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="TB_LOG_MARKET_PRODUCT")
public class LogMarketProduct {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Long idLogMktProduct;
	
	@Column(name="VALUE")
	private BigDecimal value;
	
	@Column(name="DATE")
	private Timestamp date;
	
	@Column(name="PRODUCT_ID")
	private Long idProduct;
	
	@Column(name="PRODUCT_NM")
	private String nameProduct;
	
	@Column(name="STATIONS_ID")
	private Long idStations;
	
	@Column(name="STATIONS_NM")
	private String nameStation;
	
	@Column(name="ACTION")
	private String action;
	
	
	public LogMarketProduct() {}
	
	public LogMarketProduct(MarketProduct mktProduct, String action) {
		super();
		this.value = mktProduct.getValue();
		this.date = Timestamp.valueOf(LocalDateTime.now());
		this.idProduct = mktProduct.getProduct().getIdProduct();
		this.nameProduct = mktProduct.getProduct().getProduct();
		this.idStations = mktProduct.getStations().getIdStations();
		this.nameStation = mktProduct.getStations().getNmFantazy();
		this.action = action;
	}
	
	public LogMarketProduct(Product product, String action) {
		super();
		this.date = Timestamp.valueOf(LocalDateTime.now());
		this.idProduct = product.getIdProduct();
		this.nameProduct = product.getProduct();
		this.action = action;
	}
	
	public Long getIdLogMktProduct() {
		return idLogMktProduct;
	}

	public void setIdLogMktProduct(Long idLogMktProduct) {
		this.idLogMktProduct = idLogMktProduct;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Long getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Long idProduct) {
		this.idProduct = idProduct;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

	public Long getIdStations() {
		return idStations;
	}

	public void setIdStations(Long idStations) {
		this.idStations = idStations;
	}

	public String getNameStation() {
		return nameStation;
	}

	public void setNameStation(String nameStation) {
		this.nameStation = nameStation;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}
