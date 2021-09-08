package br.com.coleta.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.coleta.dto.ResponseFindDto;
import br.com.coleta.dto.ResponseOtherMethods;
import br.com.coleta.dto.UserDto;
import br.com.coleta.dto.UserInsertDto;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Profile;
import br.com.coleta.entity.Stations;
import br.com.coleta.entity.User;
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
@RequestMapping(value="user")
@Api(value="API REST Postos de Reciclagem")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StationsRepository stationsRepository;
	
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
	
	
	
	/**
	 * Exibir usuário
	 * @return
	 */
	@GetMapping(value="/{id}")
	@ApiOperation(value="Exibe usuários")
	public ResponseEntity<ResponseFindDto> getUser(@PathVariable(required=false) Long id){
			
		List<User> users = null;
		
		try {
			if(id != null) {
				users = userRepository.findByIdUser(id);
				return fotmatResponse(users, 0, 1, 1);
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().build();
	}
	
	
	/**
	 * Exibir usuários
	 * @param id
	 * @param email
	 * @return
	 */
	@GetMapping
	@ApiOperation(value="Exibe usuários")
	public ResponseEntity<ResponseFindDto> getUser(
			@PathVariable(required=false) Long idUser,
			@RequestParam(required=false) Long id,
			@RequestParam(required=false) String email,
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size,
			@RequestParam(required=false) String sort,
			@RequestParam(required=false) String sortDirection){
		
		sort = validation.existSort(sort, "idUser");
		size = (size == 0) ? (int) userRepository.count() : size;
		Direction direction = converter.sortDirection(sortDirection);
		Pageable pageable = PageRequest.of(page, size, direction, sort);
	
		Page<User> users = null;
				
		try {
			if(id == null && email == null) {
				users = userRepository.findAll(pageable);
				return fotmatResponse(users);
			}
			if(id != null && email != null) {
				users = userRepository.findByIdUserAndEmail(id, email, pageable);
				return fotmatResponse(users);
			}
			if(id != null) {
				users = userRepository.findByIdUser(id, pageable);
				return fotmatResponse(users);
			}
			if(email != null) {
				users = userRepository.findByEmail(email, pageable);
				return fotmatResponse(users);
			}			
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().build();
	}
	
	
	
	/**
	 * Método responsável por criar usuário
	 * @param userInsertDto
	 * @param uriBuilder
	 * @return
	 */
	@PostMapping
	public ResponseEntity<UserDto> insertUser(@Valid @RequestBody UserInsertDto userInsertDto, UriComponentsBuilder uriBuilder) {
		List<User> userRef = userRepository.findByEmail(userInsertDto.getEmail());	
		try {
			validation.userNotExist(userRef);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
		}
		try {
			validation.userFormatValid(userInsertDto);
			List<Profile> profiles = profileRepository.findByIdProfile(1L);
			User userSave = new User(userInsertDto.getEmail(), userInsertDto.getPassword(), profiles);
			userRepository.save(userSave);
			userRef= userRepository.findByEmail(userInsertDto.getEmail());
			UserDto userDto = converter.toUserDto(userRef).get(0);
			URI uri = uriBuilder.path("/{id}").buildAndExpand(userDto).toUri();
			return ResponseEntity.created(uri).body(userDto);	
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	
	/**
	 * Método responsável por atualizar senha
	 * @param userInsertDto
	 * @param uriBuilder
	 * @return
	 */
	@PutMapping
	public ResponseEntity<ResponseOtherMethods> updateUserPassword(
			@Valid @RequestBody UserInsertDto userInsertDto,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization,
			UriComponentsBuilder uriBuilder) {
		User userSave = new User(userInsertDto.getEmail(), userInsertDto.getPassword(), null);
		try {
			validation.requestUser(userSave);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
		List<User> userRef = userRepository.findByEmail(userInsertDto.getEmail());
		try {
			validation.refreshUser(userRef);
			validation.userValidate(authorization.getFirst("authorization"), userRef.get(0));
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
		userSave.setIdUser(userRef.get(0).getIdUser());
		userSave.setEmail(userRef.get(0).getEmail());
		userRepository.save(userSave);
		List<UserDto> userDto = converter.toUserDto(userRef);
		return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), userDto.get(0));
	}
	
	
	/**
	 * Exclui um usuário
	 * @param idUser
	 * @param email
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<ResponseOtherMethods> deleteUser(
			@RequestParam(value = "idUser", required = false) Long idUser,
			@RequestParam(value = "email", required = false) String email,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization) {
		
		if(idUser == null && (email == null || email.isEmpty())) {
			return ResponseEntity.badRequest().build();
		}
		User user = new User();
		try {
			if(idUser != null) {
				user = userRepository.findByIdUser(idUser).get(0);
			} else {
				user = userRepository.findByEmail(email).get(0);
			}
			validation.isNotNull(user);
			validation.userValidate(authorization.getFirst("authorization"), user);
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		deleteStation(user); 
		userRepository.deleteById(user.getIdUser());
		UserDto userDto = new UserDto(user);
		return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), userDto);
	}
	
	/**
	 * Exclui dados do posto de coleta, caso existam
	 * @param user
	 */
	public void deleteStation(User user) {
		List<Stations> stationFind = stationsRepository.findByUserEmail(user.getEmail());
		try {
			validation.deleteStation(stationFind);
		} catch (Exception e) {
			return;
		}
		List<MarketProduct> mktProductFind = marketProductRepository.findByStationsIdStations(stationFind.get(0).getIdStations());
		marketProductRepository.deleteAll(mktProductFind);
		stationsRepository.deleteById(stationFind.get(0).getIdStations());
		addressRepository.deleteById(stationFind.get(0).getAddress().getIdAdress());
		webContactsRepository.deleteById(stationFind.get(0).getWebContacts().getIdWebContact());
		phonesRepository.deleteById(stationFind.get(0).getPhones().getIdPhone());
	}

	public ResponseEntity<ResponseFindDto> fotmatResponse(Page<User> users){
		Page<Object> pageResp = null;
		ResponseFindDto respUserDto = new ResponseFindDto();
		pageResp = users.map(UserDto::new);
		respUserDto = converter.toResponseMktProductDto(pageResp);
		return ResponseEntity.ok().body(respUserDto);
	}
	
	public ResponseEntity<ResponseFindDto> fotmatResponse(List<User> users, int number, int size, int totalElements){
		List<Object> userResp = null;
		ResponseFindDto respUserDto = new ResponseFindDto();
		userResp = users.stream().map(UserDto::new).collect(Collectors.toList());
		respUserDto = converter.toResponseMktProductDto(userResp, number, size, totalElements);
		return ResponseEntity.ok().body(respUserDto);
	}
}
