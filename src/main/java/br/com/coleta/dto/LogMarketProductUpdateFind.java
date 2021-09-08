package br.com.coleta.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

public class LogMarketProductUpdateFind {

	private long idProduct;
	
	private long countLogs;

	public LogMarketProductUpdateFind() {}
	
	
	public LogMarketProductUpdateFind(Long idProduct, Long countLogs) {
		super();
		this.idProduct = idProduct;
		this.countLogs = countLogs;
	}


	public long getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(long idProduct) {
		this.idProduct = idProduct;
	}

	public long getCountLogs() {
		return countLogs;
	}

	public void setCountLogs(long countLogs) {
		this.countLogs = countLogs;
	}
	
	
	
}
