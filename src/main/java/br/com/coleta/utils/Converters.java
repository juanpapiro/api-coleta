package br.com.coleta.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import br.com.coleta.dto.MktProductDto;
import br.com.coleta.dto.ProductDto;
import br.com.coleta.dto.RecyclingMarketProductDto;
import br.com.coleta.dto.ResponseFindDto;
import br.com.coleta.dto.ResponseOtherMethods;
import br.com.coleta.dto.StationDto;
import br.com.coleta.dto.UserDto;
import br.com.coleta.dto.ZipResponse;
import br.com.coleta.entity.Address;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Phones;
import br.com.coleta.entity.Product;
import br.com.coleta.entity.Stations;
import br.com.coleta.entity.User;
import br.com.coleta.entity.WebContacts;
import br.com.coleta.service.ZipService;

@Component
public class Converters {
	
	@Autowired
	private ZipService zipService;
	
	@Autowired
	private Validations validation;
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	
	public List<ProductDto> toProductDto(List<Product> products) {
		return products.stream().map(ProductDto::new).collect(Collectors.toList());
	}
	
	public List<MktProductDto> toMktProductDto(List<MarketProduct> products) {
		return products.stream().map(MktProductDto::new).collect(Collectors.toList());
	}
	
	public ResponseFindDto toResponseMktProductDto(Page<Object> page) {
		ResponseFindDto response = new ResponseFindDto(page);
		return response;
	}
	
	public ResponseFindDto toResponseMktProductDto(List<Object> listResponse, int number, int size, int totalElements) {
		ResponseFindDto response = new ResponseFindDto(listResponse, number, size, totalElements);
		return response;
	}
	
	public MarketProduct toMktProduct(MktProductDto productMkt, Stations stations, Product product) {
		MarketProduct mktProduct = new MarketProduct();
		mktProduct.setProduct(product);
		mktProduct.setStations(stations);
		mktProduct.setValue(productMkt.getValue());
		if(productMkt.getInsertDate() != null) {
			mktProduct.setInsertDate(date(productMkt.getInsertDate()));	
		}
		if(productMkt.getUpdateDate() != null) {
			mktProduct.setUpdateDate(date(productMkt.getUpdateDate()));	
		}
		return mktProduct;
	}
	
	public List<UserDto> toUserDto(List<User> users) {
		return users.stream().map(UserDto::new).collect(Collectors.toList());
	}
	
	public List<StationDto> toStationDto(List<Stations> stations) {
		return stations.stream().map(StationDto::new).collect(Collectors.toList());
	}
	
	public Phones toPhone(StationDto stationDto, User user) {
		Phones phones = new Phones();
		phones.setIdPhone((stationDto.getIdPhones() != null) ? stationDto.getIdPhones() : null);
		phones.setCellPhone(stationDto.getCellPhone());
		phones.setCellPhoneAdd(stationDto.getCellPhoneAdd());
		phones.setPhone(stationDto.getPhone());
		phones.setPhoneAdd(stationDto.getPhoneAdd());
		phones.setUser(user);
		return phones;
	}
	
	public WebContacts toWebContacts(StationDto stationDto, User user) {
		WebContacts webContacts = new WebContacts();
		webContacts.setIdWebContact((stationDto.getIdWebContacts() != null) ? stationDto.getIdWebContacts() : null);
		webContacts.setEmail(stationDto.getEmail());
		webContacts.setEmailAdd(stationDto.getEmailAdd());
		webContacts.setFacebook(stationDto.getFacebook());
		webContacts.setSite(stationDto.getSite());
		webContacts.setOtherUrl(stationDto.getOtherUrl());
		webContacts.setUser(user);
		return webContacts;
	}
	
	public Address toAddress(StationDto stationDto, User user) throws Exception {
		Address address = new Address();
		address.setIdAdress((stationDto.getIdAdress() != null) ? stationDto.getIdAdress() : null);
		ZipResponse zipResponse = getZipAddress(stationDto);
		address.setZip(zipResponse.getCep());
		address.setNeighborhood(zipResponse.getBairro());
		address.setCity(zipResponse.getLocalidade());
		address.setUf(zipResponse.getUf());
		address.setState(stationDto.getState());
		address.setComplement(stationDto.getComplement());
		address.setNumber(stationDto.getNumber());
		address.setStreet(stationDto.getStreet());
		address.setUser(user);
		return address;
	}
	
	public ZipResponse getZipAddress(StationDto stationDto) throws Exception {
		ZipResponse resp = zipService.getZipAddress(stationDto.getZip());
		validation.zipAddress(stationDto.getStreet(), resp.getLogradouro());			
		return resp;
	}
	
	public Stations toStations(StationDto stationDto, User user, Phones phones, WebContacts webContacts, Address address) {
		Stations station = new Stations();
		station.setIdStations((stationDto.getIdStation() != null) ? stationDto.getIdStation() : null);
		station.setDoc(stationDto.getDoc());
		station.setTypeDoc(stationDto.getTypeDoc());
		station.setNmFantazy(stationDto.getNmFantazy());
		station.setAddress(address);
		station.setPhones(phones);
		station.setUser(user);
		station.setWebContacts(webContacts);
		return station;
	}
	
	public User toUserUpdate(StationDto stationDto, User user) {
		user.setIdUser(isNull(stationDto.getIdUser(), user.getIdUser()));
		user.setEmail(isNullAndIsEmpty(stationDto.getEmailUser(), user.getEmail()));
		user.setPassword(user.getPassword());
		return user;
	}
	
	public StationDto toStationDtoUpdate(StationDto stationDtoIn, StationDto stationDtoFind) {
		stationDtoIn.setIdStation(isNull(stationDtoIn.getIdStation(), stationDtoFind.getIdStation()));
		stationDtoIn.setNmFantazy(isNullAndIsEmpty(stationDtoIn.getNmFantazy(), stationDtoFind.getNmFantazy()));
		stationDtoIn.setDoc(isNullAndIsEmpty(stationDtoIn.getDoc(), stationDtoFind.getDoc()));
		stationDtoIn.setTypeDoc(isNullAndIsEmpty(stationDtoIn.getTypeDoc(), stationDtoFind.getTypeDoc()));
		stationDtoIn.setIdUser(stationDtoFind.getIdUser());
		stationDtoIn.setEmailUser(isNullAndIsEmpty(stationDtoIn.getEmailUser(), stationDtoFind.getEmailUser()));
		stationDtoIn.setIdAdress(stationDtoFind.getIdAdress());
		stationDtoIn.setStreet(isNullAndIsEmpty(stationDtoIn.getStreet(), stationDtoFind.getStreet()));
		stationDtoIn.setNumber(isNull(stationDtoIn.getNumber(), stationDtoFind.getNumber()));
		stationDtoIn.setComplement(isNullAndIsEmpty(stationDtoIn.getComplement(), stationDtoFind.getComplement()));
		stationDtoIn.setNeighborhood(isNullAndIsEmpty(stationDtoIn.getNeighborhood(), stationDtoFind.getNeighborhood()));
		stationDtoIn.setCity(isNullAndIsEmpty(stationDtoIn.getCity(), stationDtoFind.getCity()));
		stationDtoIn.setState(isNullAndIsEmpty(stationDtoIn.getState(),  stationDtoFind.getState()));
		stationDtoIn.setUf(isNullAndIsEmpty(stationDtoIn.getUf(), stationDtoFind.getUf()));
		stationDtoIn.setZip(isNullAndIsEmpty(stationDtoIn.getZip(),  stationDtoFind.getZip()));
		stationDtoIn.setIdPhones(stationDtoFind.getIdPhones());
		stationDtoIn.setPhone(isNullAndIsEmpty(stationDtoIn.getPhone(), stationDtoFind.getPhone()));
		stationDtoIn.setCellPhoneAdd(isNullAndIsEmpty(stationDtoIn.getPhoneAdd(), stationDtoFind.getPhoneAdd()));
		stationDtoIn.setCellPhone(isNullAndIsEmpty(stationDtoIn.getCellPhone(), stationDtoFind.getCellPhone()));
		stationDtoIn.setCellPhoneAdd(isNullAndIsEmpty(stationDtoIn.getCellPhoneAdd(), stationDtoFind.getCellPhoneAdd()));
		stationDtoIn.setIdWebContacts(stationDtoFind.getIdWebContacts());
		stationDtoIn.setEmail(isNullAndIsEmpty(stationDtoIn.getEmail(),  stationDtoFind.getEmail()));
		stationDtoIn.setEmailAdd(isNullAndIsEmpty(stationDtoIn.getEmailAdd(),  stationDtoFind.getEmailAdd()));
		stationDtoIn.setSite(isNullAndIsEmpty(stationDtoIn.getSite(),  stationDtoFind.getSite()));
		stationDtoIn.setFacebook(isNullAndIsEmpty(stationDtoIn.getFacebook(),  stationDtoFind.getFacebook()));
		stationDtoIn.setOtherUrl(isNullAndIsEmpty(stationDtoIn.getOtherUrl(),  stationDtoFind.getOtherUrl()));
		return stationDtoIn;
	}
	
	public static String isNullAndIsEmpty(String strIn, String strRef) {
		return (strIn != null && !strIn.isEmpty()) ? strIn : strRef;
	}
	
	public static Long isNull(Long idIn, Long idRef) {
		return (idIn != null) ? idIn : idRef;
	}
	
	public static Integer isNull(Integer idIn, Integer idRef) {
		return (idIn != null) ? idIn : idRef;
	}
	
	public Direction sortDirection(String sortDirection) {
		return (sortDirection != null && !sortDirection.replaceAll(" ", "").isEmpty()
				&& sortDirection.equalsIgnoreCase("asc")) ? Direction.ASC : Direction.DESC;
	}
	
	public Pageable paramsPageable(int page, int size, int fullSize, String sort, String param, String sortDirection) {
		size = (size != 0) ? size : fullSize;
		page = (page != 0) ? page : 0;
		sort = (sort != null && !sort.replaceAll(" ", "").isEmpty()) ? sort : param;
		Direction direction = (sortDirection != null && !sortDirection.replaceAll(" ", "").isEmpty()
				&& sortDirection.equalsIgnoreCase("asc")) ? Direction.ASC : Direction.DESC;
		Pageable pageable = PageRequest.of(page, fullSize, direction, sort);
		return pageable;
	}
	
	public String formatValue(BigDecimal number) {
		String value = "0,00";
		if(number != null) {
			value = String.format("%.2f", number);
		}
		return value;
	}
	
	public List<RecyclingMarketProductDto> paginator(List<RecyclingMarketProductDto> list,
			int page, int size, long totalElements) throws HttpClientErrorException {
		List<RecyclingMarketProductDto> listResponse = new ArrayList<>();
		int init = (page == 0) ? 0 : page * size;
		int limit = (page == 0) ? size : init + size;
		limit = (limit < totalElements) ? limit : (int) totalElements;
		if (init > totalElements || limit > totalElements) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Não há mais registros");
		}
		listResponse = list.subList(init, limit);
		if(listResponse.size() == 0) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Não há mais registros");
		}
		return listResponse;
	}
	
	public String date(LocalDateTime ldt) {
		return ldt.format(FORMATTER);
	}
	
	public Timestamp date(String date) {
		return Timestamp.valueOf(LocalDateTime.parse(date, FORMATTER));
	}
	
	public ResponseEntity<ResponseOtherMethods> toResponse(Integer code, String message, Object obj) {
		ResponseOtherMethods resp = new ResponseOtherMethods();
		resp.setCode((code != null) ? code : HttpStatus.BAD_REQUEST.value());
		resp.setMessage((message != null) ? message : HttpStatus.BAD_REQUEST.getReasonPhrase());
		resp.setResponse(obj);
		return ResponseEntity.status(code).body(resp);
	}

}
