package br.com.coleta.dto;

import br.com.coleta.entity.Product;

public class ProductDto {
	
	private Long id;
	private String nameProduct;
	
	public ProductDto() {}
	
	public ProductDto(Product product) {
		super();
		this.id = product.getIdProduct();
		this.nameProduct = product.getProduct();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProduct() {
		return nameProduct;
	}
	public void setProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}
	
}
