package br.com.coleta.dto;

import java.util.List;

public class ResponseRecyclingMktProductDto {

	private int responseCode;
	private String responseMessage;
	private int page;
	private int size;
	private long totalElements;
	private List<RecyclingMarketProductDto> listRecyclingProductsValue;
	private List<LogMarketProductUpdateFind> logsMarketUpdate;
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public List<RecyclingMarketProductDto> getListRecyclingProductsValue() {
		return listRecyclingProductsValue;
	}
	public void setListRecyclingProductsValue(List<RecyclingMarketProductDto> listRecyclingProductsValue) {
		this.listRecyclingProductsValue = listRecyclingProductsValue;
	}
	public List<LogMarketProductUpdateFind> getLogsMarketUpdate() {
		return logsMarketUpdate;
	}
	public void setLogsMarketUpdate(List<LogMarketProductUpdateFind> logsMarketUpdate) {
		this.logsMarketUpdate = logsMarketUpdate;
	}
	
}
