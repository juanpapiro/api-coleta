package br.com.coleta.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.coleta.dto.MktProductDto;
import br.com.coleta.dto.ResponseFindDto;
import br.com.coleta.dto.ResponseFindStationDto;
import br.com.coleta.dto.ResponseOtherMethods;
import br.com.coleta.dto.StationDto;
import br.com.coleta.entity.Address;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Phones;
import br.com.coleta.entity.Profile;
import br.com.coleta.entity.Stations;
import br.com.coleta.entity.User;
import br.com.coleta.entity.WebContacts;
import br.com.coleta.repository.AddressRepository;
import br.com.coleta.repository.MarketProductRepository;
import br.com.coleta.repository.PhonesRepository;
import br.com.coleta.repository.ProfileRepository;
import br.com.coleta.repository.StationsRepository;
import br.com.coleta.repository.UserRepository;
import br.com.coleta.repository.WebContactsRepository;
import br.com.coleta.utils.Converters;
import br.com.coleta.utils.Validations;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="/stations")
@Api(value="API REST Postos de Reciclagem")
public class StationsController {
	
	@Autowired
	private StationsRepository repository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private PhonesRepository phonesRepository;
	
	@Autowired
	private WebContactsRepository webContactsRepository;
	
	@Autowired
	private MarketProductRepository marketProductRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private Converters converter;
	
	@Autowired
	private Validations validation;
	
	
	@GetMapping
	@ApiOperation(value="Retorna uma lista de postos de coleta")
	public ResponseEntity<ResponseFindDto> getPostos(
			@RequestParam(required=false) String email,
			@RequestParam(required=false) String nmFantazy,
			@RequestParam(required=false) Long idStation,
			@RequestParam(required=false) String neighborhood,
			@RequestParam(required=false) String state,
			@RequestParam(required=false) String city,
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size,
			@RequestParam(required=false) String sort,
			@RequestParam(required=false) String sortDirection) {
		
		sort = validation.existSort(sort, "idStations");
		Direction direction = converter.sortDirection(sortDirection);
		size = (size == 0) ? (int) repository.count() : size;
		Pageable pageable = PageRequest.of(page, size, direction, sort);
		Page<Stations> stations = null;
		
		try {
			
			if((email == null || email.isEmpty()) && (nmFantazy == null || nmFantazy.isEmpty())
					&& (idStation == null) && (neighborhood == null || neighborhood.isEmpty())
					&& (state == null || state.isEmpty()) && (city == null || city.isEmpty())) {
				stations = repository.findAll(pageable);
				return formatResponse(stations);
			}
			if(email != null && !email.isEmpty() && (nmFantazy == null || nmFantazy.isEmpty())
					&& (idStation == null) && (neighborhood == null || neighborhood.isEmpty())
					&& (state == null || state.isEmpty()) && (city == null || city.isEmpty())) {
				stations = repository.findByUserEmail(email, pageable);
				if(stations.getContent().size() == 0) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				}
				return formatResponse(stations);
			}
			if((nmFantazy != null && !nmFantazy.isEmpty() && (email == null || email.isEmpty()))
					&& (idStation == null) && (neighborhood == null || neighborhood.isEmpty())
					&& (state == null || state.isEmpty()) && (city == null || city.isEmpty())) {
				stations = repository.findByNmFantazy(nmFantazy, pageable);
				return formatResponse(stations);
			}
			if((idStation != null) && (email == null || email.isEmpty())
					&& (nmFantazy == null || nmFantazy.isEmpty()) && (neighborhood == null || neighborhood.isEmpty())
					&& (state == null || state.isEmpty()) && (city == null || city.isEmpty())) {
				stations = repository.findByIdStations(idStation, pageable);
				return formatResponse(stations);
			}
			if((neighborhood != null && !neighborhood.isEmpty()) && (idStation == null) && (email == null || email.isEmpty())
					&& (nmFantazy == null || nmFantazy.isEmpty()) && (state == null || state.isEmpty()) && (city == null || city.isEmpty())) {		
				neighborhood = (neighborhood != null && !neighborhood.isEmpty()) ? "%"+neighborhood+"%" : "%"+"%";
				int count = repository.findLikeCountNeighborhood(neighborhood);
				List<Stations> listStation = repository.findLikeNeighborhood(neighborhood, page, size, size);
				return formatResponse(listStation, page, size, count);
			}
			if((state != null && !state.isEmpty()) && (email == null || email.isEmpty()) && (idStation == null) 
					&& (nmFantazy == null || nmFantazy.isEmpty()) && (neighborhood == null || neighborhood.isEmpty())
					&& (neighborhood == null || neighborhood.isEmpty()) && (city == null || city.isEmpty())) {
				stations = repository.findByAddressState(state, pageable);
				return formatResponse(stations);
			}
			if((city != null && !city.isEmpty()) && (email == null || email.isEmpty()) && (idStation == null) 
					&& (nmFantazy == null || nmFantazy.isEmpty()) && (neighborhood == null || neighborhood.isEmpty())
					&& (neighborhood == null || neighborhood.isEmpty()) && (state == null || state.isEmpty())) {
				stations = repository.findByAddressCity(city, pageable);
				return formatResponse(stations);
			}
			if((city != null && !city.isEmpty()) && (neighborhood != null && !neighborhood.isEmpty()) 
					&& (nmFantazy == null || nmFantazy.isEmpty()) && (state == null || state.isEmpty()) 
				    && (email == null || email.isEmpty()) && (idStation == null)) {
				neighborhood = (neighborhood != null && !neighborhood.isEmpty()) ? "%"+neighborhood+"%" : null;
				city = (city != null && !city.isEmpty()) ? "%"+city+"%" : null;
				int totalElements = repository.findCityNeighborhoodCount(neighborhood, city);
				List<Stations> listStation = repository.findCityNeighborhood(neighborhood, city, page, size, size);
				return formatResponse(listStation, page, size, totalElements);
			}
			if((state != null && !state.isEmpty()) && (neighborhood != null && !neighborhood.isEmpty()) 
					&& (city == null || city.isEmpty()) && (nmFantazy == null || nmFantazy.isEmpty()) 
				    && (email == null || email.isEmpty()) && (idStation == null)) {
				neighborhood = (neighborhood != null && !neighborhood.isEmpty()) ? "%"+neighborhood+"%" : null;
				state = (state != null && !state.isEmpty()) ? "%"+state+"%" : null;
				int totalElements = repository.findStateNeighborhoodCount(neighborhood, state);
				List<Stations> listStation = repository.findStateNeighborhood(neighborhood, state, page, size, size);
				return formatResponse(listStation, page, size, totalElements);
			}
			if((state != null && !state.isEmpty()) && (city != null && !city.isEmpty())
					&& (neighborhood == null || neighborhood.isEmpty()) && (nmFantazy == null || nmFantazy.isEmpty()) 
				    && (email == null || email.isEmpty()) && (idStation == null)) {
				city = (city != null && !city.isEmpty()) ? "%"+city+"%" : null;
				state = (state != null && !state.isEmpty()) ? "%"+state+"%" : null;
				int totalElements = repository.findStateCityCount(city, state);
				List<Stations> listStation = repository.findStateCity(city, state, page, size, size);
				return formatResponse(listStation, page, size, totalElements);
			}
			if((state != null && !state.isEmpty()) && (neighborhood != null && !neighborhood.isEmpty()) 
					&& (city != null && !city.isEmpty()) && (nmFantazy == null || nmFantazy.isEmpty()) 
				    && (email == null || email.isEmpty()) && (idStation == null)) {
				neighborhood = (neighborhood != null && !neighborhood.isEmpty()) ? "%"+neighborhood+"%" : null;
				city = (city != null && !city.isEmpty()) ? "%"+city+"%" : null;
				state = (state != null && !state.isEmpty()) ? "%"+state+"%" : null;
				int totalElements = repository.findNeighborhoodCityStateCount(neighborhood, city, state);
				List<Stations> listStation = repository.findNeighborhoodCityState(neighborhood, city, state, page, size, size);
				return formatResponse(listStation, page, size, totalElements);
			}
			
			
			if(email != null || nmFantazy != null || idStation != null || neighborhood != null || state != null || city != null) {
				nmFantazy = (nmFantazy != null && !nmFantazy.isEmpty()) ? "%"+nmFantazy+"%" : null;
				email = (email != null && !email.isEmpty()) ? "%"+email+"%" : null;
				neighborhood = (neighborhood != null && !neighborhood.isEmpty()) ? "%"+neighborhood+"%" : null;
				state = (state != null && !state.isEmpty()) ? "%"+state+"%" : null;
				city = (city != null && !city.isEmpty()) ? "%"+city+"%" : null;
				int totalElements = repository.findFullCount(idStation, nmFantazy, email, neighborhood, state, city);
				List<Stations> listStation = repository.findFull(idStation, nmFantazy, email, neighborhood, state, city, page, size, size);
				return formatResponse(listStation, page, size, totalElements);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}
	
	
//	@GetMapping(value="findAdmin")
	@GetMapping(value="admin")
	@ApiOperation(value="Retorna uma lista de postos de coleta")
	public ResponseEntity<ResponseFindStationDto> getPostosAdmin(
			@RequestParam(required=false) String email,
			@RequestParam(required=false) Long idStation,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization) {
		
		List<Stations> stationsFind = null;
		
		try {
			List<User> userRef = userRepository.findByEmail(email);
			validation.userValidate(authorization.getFirst("authorization"), userRef.get(0));
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		try {		
			if(email != null && !email.isEmpty()) {
				stationsFind = repository.findByUserEmail(email);
				if(stationsFind.size() == 0) {
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				}
				List<StationDto> stations = stationsFind.stream().map(StationDto :: new).collect(Collectors.toList());
				stations.stream().forEach(station -> {
					List<MarketProduct> products = marketProductRepository.findByStationsIdStations(station.getIdStation());
					List<MktProductDto> mktProducts = converter.toMktProductDto(products);
					station.setMktProducts(mktProducts);
				});
				ResponseFindStationDto response = new ResponseFindStationDto();
				response.setNumber(0);
				response.setResponse(stations);
				response.setResponseCode(HttpStatus.OK.value());
				response.setResponseMessage(HttpStatus.OK.getReasonPhrase());
				response.setSize(stationsFind.size());
				response.setTotalElements(stationsFind.size());
				return ResponseEntity.ok().body(response);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}
	
	
	/**
	 * Insere informações de cadastro de um posto de coleta.
	 * @param stationDto
	 * @param uriBuilder
	 * @return
	 */
	@PostMapping
	@ApiOperation(value="Insere um posto de coleta")
	public ResponseEntity<ResponseOtherMethods> insertStation(
			@RequestBody StationDto stationDto,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization,
			UriComponentsBuilder uriBuilder) {
		List<Stations> station = repository.findByUserEmail(stationDto.getEmailUser());
		List<User> userRef = userRepository.findByEmail(stationDto.getEmailUser());	
		try {
			validation.insertStation(station, userRef);
			validation.userValidate(authorization.getFirst("authorization"), userRef.get(0));
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch(Exception e) {			
			return ResponseEntity.badRequest().build();
		}		
		User user = userRef.get(0);	
		try {
			Stations stationSave = mountStation(stationDto, user);			
			repository.save(stationSave);
		} catch (Exception e) {
			ResponseOtherMethods resp = new ResponseOtherMethods();
			resp.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
			resp.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(resp);
		}
		List<Stations> stationResponse = repository.findByUserEmail(stationDto.getEmailUser());
		List<StationDto> stationDtoResponse = converter.toStationDto(stationResponse);
		URI uri = uriBuilder.path("/insert/{id}").buildAndExpand(stationDtoResponse.get(0)).toUri();
		this.updateUserProfile(user);
		return converter.toResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), stationDtoResponse.get(stationDtoResponse.size()-1));
	}
	

	/**
	 * Atualiza informações de cadastro de um posto de coleta.
	 * @param posto
	 */
	@PutMapping
	@ApiOperation(value="Atualiza um posto de coleta")
	public ResponseEntity<ResponseOtherMethods> updateStations(
			@RequestBody StationDto stationDto,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization,
			UriComponentsBuilder uriBuilder) {
		List<Stations> stationFind = repository.findByIdStations(stationDto.getIdStation());
		try {
			validation.isNotNull(stationFind.get(0));
			validation.userValidate(authorization.getFirst("authorization"), stationFind.get(0).getUser());
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		StationDto stationDtoFind = new StationDto();
		try {
			stationDtoFind = converter.toStationDto(stationFind).get(0);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		stationDto = converter.toStationDtoUpdate(stationDto, stationDtoFind);
		User userUpdate = converter.toUserUpdate(stationDto, stationFind.get(0).getUser());
		userRepository.save(userUpdate);
		try {
			Stations stationUpdate = mountStationUpdate(stationDto, userUpdate);
			repository.save(stationUpdate);			
		} catch (Exception e) {
			ResponseOtherMethods resp = new ResponseOtherMethods();
			resp.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
			resp.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(resp);
		}
		URI uri = uriBuilder.path("/update/{id}").buildAndExpand(stationDto).toUri();
		return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), stationDto);
	}
	
	
	/**
	 * Exlusão de posto de coleta.
	 * @param idStation
	 */
	@DeleteMapping
	@ApiOperation(value="Exclui um posto de coleta")
	public ResponseEntity<ResponseOtherMethods> deleteStations(
			@RequestParam(value="idStation") Long idStation,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization) {
		List<Stations> stationFind = repository.findByIdStations(idStation);
		try {
			validation.deleteStation(stationFind);
			validation.userValidate(authorization.getFirst("authorization"), stationFind.get(0).getUser());
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		List<MarketProduct> mktProductFind = marketProductRepository.findByStationsIdStations(idStation);
		marketProductRepository.deleteAll(mktProductFind);
		repository.deleteById(idStation);
		addressRepository.deleteById(stationFind.get(0).getAddress().getIdAdress());
		webContactsRepository.deleteById(stationFind.get(0).getWebContacts().getIdWebContact());
		phonesRepository.deleteById(stationFind.get(0).getPhones().getIdPhone());		
		return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), idStation);
	}
	
	@GetMapping(value="/states")
	@ApiOperation(value="Retorna uma lista dos estatdos dos postos de coleta cadastrados")
	public ResponseEntity<Set<String>> getStates() {
		Set<String> states = new HashSet<>();
		try {
			List<Stations>stations = repository.findAll();
			stations.stream().forEach(station -> states.add(station.getAddress().getState()));
			return ResponseEntity.ok().body(states);			
		} catch(Exception e) {
			return ResponseEntity.ok().body(states);
		}
	}
	
	@GetMapping(value="/cities")
	@ApiOperation(value="Retorna uma lista das cidades dos postos de coleta cadastrados")
	public ResponseEntity<Set<String>> getCities() {
		Set<String> cities = new HashSet<>();
		try {
			List<Stations>stations = repository.findAll();
			stations.stream().forEach(station -> cities.add(station.getAddress().getCity()));
			return ResponseEntity.ok().body(cities);			
		} catch(Exception e) {
			return ResponseEntity.ok().body(cities);			
		}
	}
	

	/**
	 * Método auxiliar que persiste Address, Phones e WebContacts
	 * @param stationDto
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public Stations mountStation(StationDto stationDto, User user) throws Exception {
		Phones phones = converter.toPhone(stationDto, user);
		phones = phonesRepository.save(phones);
	
		WebContacts webContacts = converter.toWebContacts(stationDto, user);
		webContacts = webContactsRepository.save(webContacts);
		
		Address address = converter.toAddress(stationDto, user);
		address = addressRepository.save(address);
		
		Stations station = converter.toStations(stationDto, user, phones, webContacts, address);
		return station;
	}
	
	/**
	 * Método auxiliar que atualiza Address, Phones e WebContacts
	 * @param stationDto
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public Stations mountStationUpdate(StationDto stationDto, User user) throws Exception {
		Phones phones = converter.toPhone(stationDto, user);
		phonesRepository.save(phones);
	
		WebContacts webContacts = converter.toWebContacts(stationDto, user);
		webContactsRepository.save(webContacts);
		
		Address address = converter.toAddress(stationDto, user);
		addressRepository.save(address);
		
		Stations station = converter.toStations(stationDto, user, phones, webContacts, address);
		return station;
	}
	
	/**
	 * Formata response para busca de postos de coleta
	 * @param stations
	 * @return
	 */
	public ResponseEntity<ResponseFindDto> formatResponse(Page<Stations> stations){
		Page<Object> pageResp = null;
		ResponseFindDto respStationsDto = new ResponseFindDto();
		pageResp = stations.map(StationDto::new);
		respStationsDto = converter.toResponseMktProductDto(pageResp);
		return ResponseEntity.ok().body(respStationsDto);
	}
	
	/**
	 * Formata response para busca de postos de coleta
	 * @param stations
	 * @return
	 */
	public ResponseEntity<ResponseFindDto> formatResponse(List<Stations> stations, int page, int size, int totalElements){
		List<Object> pageResp = null;
		ResponseFindDto respStationsDto = new ResponseFindDto();
		pageResp = stations.stream().map(StationDto::new).collect(Collectors.toList());
		respStationsDto = converter.toResponseMktProductDto(pageResp, page, size, totalElements);
		return ResponseEntity.ok().body(respStationsDto);
	}
	

	
	/**
	 * Atualiza perfil de acesso de usuário após inserir um posto de coleta
	 * @param user
	 */
	private void updateUserProfile(User user) {
		List<Profile> profiles = profileRepository.findByIdProfile(2L);
		user.getProfiles().addAll(profiles);
		userRepository.save(user);
	}
	
}
