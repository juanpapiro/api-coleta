package br.com.coleta.dto;

public class RecyclingMarketProductDto {
	
	private String averageValue;
	private String maxValue;
	private String minValue;
	private int countMktProducts;
	private long countLogs;
	private ProductDto product;
	private StationDto station;
	
	public String getAverageValue() {
		return averageValue;
	}
	public void setAverageValue(String averageValue) {
		this.averageValue = averageValue;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public int getCountMktProducts() {
		return countMktProducts;
	}
	public void setCountMktProducts(int countMktProducts) {
		this.countMktProducts = countMktProducts;
	}
	public long getCountLogs() {
		return countLogs;
	}
	public void setCountLogs(long countLogs) {
		this.countLogs = countLogs;
	}
	public ProductDto getProduct() {
		return product;
	}
	public void setProduct(ProductDto product) {
		this.product = product;
	}
	public StationDto getStation() {
		return station;
	}
	public void setStation(StationDto station) {
		this.station = station;
	}
	
	
}
