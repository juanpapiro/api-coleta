package br.com.coleta.dto;

public class ErrorRequestDto {
	
	private String param;
	private String error;
	
	public ErrorRequestDto() {}
	
	public ErrorRequestDto(String param, String error) {
		super();
		this.param = param;
		this.error = error;
	}

	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
