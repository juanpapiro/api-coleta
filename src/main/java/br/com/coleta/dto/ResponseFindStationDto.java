package br.com.coleta.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class ResponseFindStationDto {
	
	List<StationDto> response = new ArrayList<>();
	private long totalElements;
    private Integer totalPages;
    private int size;
    private int number;
    private String responseMessage;
    private Integer responseCode;
	
	public ResponseFindStationDto() {}
	
	public ResponseFindStationDto(Page<StationDto> page) {
		super();
		this.response = page.getContent();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.size = page.getSize();
		this.number = page.getNumber();
	}
	
	public ResponseFindStationDto(List<StationDto> listResponse, int number, int size, int totalElements) {
		super();
		this.response = listResponse;
		this.totalElements = totalElements;
		this.size = size;
		this.number = number;
		if(totalElements != 0 && size != 0) {
			int mod = totalElements % size;
			this.totalPages = totalElements / size;
			this.totalPages = (mod != 0) ? this.totalPages + 1 : this.totalPages;
		}
	}
	
	public List<StationDto> getResponse() {
		return response;
	}
	public void setResponse(List<StationDto> products) {
		this.response = products;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

}
