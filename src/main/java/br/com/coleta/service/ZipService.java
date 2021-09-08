package br.com.coleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.coleta.dto.ZipResponse;

@Service
public class ZipService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public ZipResponse getZipAddress(String zip) throws Exception {
		
		ZipResponse zipResponse = null;
		try {
			zip = zip.replaceAll("[^0-9]+", "");
			ResponseEntity<ZipResponse> response = restTemplate.exchange("https://viacep.com.br/ws/" + zip + "/json/", HttpMethod.GET, null, ZipResponse.class);
			zipResponse = response.getBody();	
		} catch (RestClientException ex) {
			throw new Exception("Falha ao consultar cep.");
		} catch (Exception e) {
			throw new Exception("Falha ao consultar cep.");
		}
		return zipResponse;
	}
	

}
