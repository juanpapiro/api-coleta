package br.com.coleta.utils;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import br.com.coleta.dto.StationDto;
import br.com.coleta.dto.UserInsertDto;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Product;
import br.com.coleta.entity.Stations;
import br.com.coleta.entity.User;
import br.com.coleta.security.TokenService;

@Component
public class Validations {
	
	@Autowired
	private TokenService tokenService;
	
	public boolean existProductName(List<Product> products, String nameProduct) throws Exception {
		Collator collator = Collator.getInstance (new Locale ("pt", "BR"));
		collator.setStrength(Collator.PRIMARY);
		for(Product product : products) {
//			if(product.getProduct().equalsIgnoreCase(nameProduct)) {
			if(collator.compare(product.getProduct(), nameProduct) == 0) {
				throw new Exception("Já existe um produco com este nome.");
			}
		}
		return true;
	}
	
	public boolean reqProductMkt(Optional<Product> product,
			Optional<Stations> station, List<MarketProduct> existProduct) throws Exception {
		if(!product.isPresent()) {
			throw new Exception("Produto não localizado.");
		}
		if(!station.isPresent()) {
			throw new Exception("Ponto de coleta não localizado.");
		}
		if(existProduct != null && existProduct.size() > 0) {
			throw new Exception("Produto já existente.");
		}
		return true;
	}
	
	public boolean updateMktProduct(List<MarketProduct> product) throws Exception {
		if(product == null || product.size() == 0) {
			throw new Exception("Nenhum produto encontrado.");
		}
		return true;
	}
	
	public boolean userNotExist(List<User> userRef) throws Exception {
		if(userRef != null && userRef.size() > 0) {
			throw new Exception("Email já cadastrado");
		}
		return true;
	}
	
	public boolean userFormatValid(UserInsertDto userInsertDto) throws Exception {
		if(userInsertDto.getEmail() == null || userInsertDto.getEmail().isEmpty()
		   || userInsertDto.getPassword() == null || userInsertDto.getPassword().isEmpty()) {
			throw new Exception("Parâmetros inválidos para criação de usuário.");
		}
		if(!userInsertDto.getEmail().contains("@") || !userInsertDto.getEmail().contains(".")) {
			throw new Exception("Parâmetros inválidos para criação de usuário.");
		}
		return true;
	}
	
	public boolean refreshUser(List<User> userRef) throws Exception {
		if(userRef == null || userRef.size() == 0) {
			throw new Exception("Usuário não localizado.");
		}
		return true;
	}
	
	public boolean requestUser(User user) throws Exception {
		if(user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new Exception("E-mail não pode ser nulo");
		}
		if(user.getPassword().length() < 6) {
			throw new Exception("A senha deve ter ao menos 6 caracteres.");
		}
		return true;
	}
	
	public boolean insertStation(List<Stations> station, List<User> user) throws Exception {
//		if(station.size() > 0) {
//			throw new Exception("Endereço já cadastrado.");
//		}
		if(user == null || user.size() == 0) {
			throw new Exception("Usuário não cadastrado.");
		}
		return true;
	}
	
	public boolean updateStation(StationDto stationDto) throws Exception {
		if(stationDto.getIdStation() == null || stationDto.getIdStation() == 0) {
			throw new Exception("Usuário não cadastrado.");
		}
		return true;
	}
	
	public boolean deleteStation(List<Stations> station) throws Exception {
		if(station == null || station.size() == 0) {
			throw new Exception("Ponto de coleta não localizado.");
		}
		return true;
	}
	
	public boolean isNotNull(Object obj) throws Exception {
		if(obj == null) {
			throw new Exception("Id não localizado.");
		}
		return true;
	}
	
	public String existSort(String sort, String param) {
		return (sort != null && !sort.replaceAll(" ", "").isEmpty()) ? sort : param;
	}
	
	public Direction sortDirection(String sortDirection) {
		return (sortDirection != null && !sortDirection.replaceAll(" ", "").isEmpty()
				&& sortDirection.equalsIgnoreCase("asc")) ? Direction.ASC : Direction.DESC;
	}
	
	public int existSize(int size, int fullSize) {
		return (size != 0) ? size : fullSize;
	}

	public int existPage(int page) {
		return (page != 0) ? page : 0;
	}

	public boolean userValidate(String token, User user) {
		if(token.startsWith("Bearer ")) {
			token = token.substring(7, token.length());
		}
		if(user.getIdUser() != tokenService.getIdUser(token)) {
			System.out.println(tokenService.getIdUser(token));
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Usuário diferente do cadastro que deseja alterar informações.");
		}
		return true;
	}
	
	public boolean zipAddress(String street, String zipStreet) throws Exception {
		if(!street.toLowerCase().contains(zipStreet.toLowerCase()) && 
		   !zipStreet.toLowerCase().contains(street.toLowerCase())) {
			throw new Exception("Cep parece incorreto, o cep informado pertence ao logradouro " + zipStreet + ".");
		}
		return true;
	}

}
