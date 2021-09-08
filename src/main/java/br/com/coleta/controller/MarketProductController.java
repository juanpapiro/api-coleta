package br.com.coleta.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

import br.com.coleta.dto.MktProductDto;
import br.com.coleta.dto.ResponseFindDto;
import br.com.coleta.dto.ResponseOtherMethods;
import br.com.coleta.entity.LogMarketProduct;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Product;
import br.com.coleta.entity.Stations;
import br.com.coleta.enums.ActionMktProduct;
import br.com.coleta.repository.LogMarketProductRepository;
import br.com.coleta.repository.MarketProductRepository;
import br.com.coleta.repository.ProductRepository;
import br.com.coleta.repository.StationsRepository;
import br.com.coleta.utils.Converters;
import br.com.coleta.utils.Validations;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/marketstuff")
public class MarketProductController {
	
	@Autowired
	private MarketProductRepository marketProductRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private StationsRepository stationsRepository;
	
	@Autowired
	private LogMarketProductRepository logMktProductRepository;

	@Autowired
	private Converters converter;
	
	@Autowired
	private Validations validation;
	

	/**
	 * Exibe produdo e respectivos valor
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<ResponseFindDto> getProductMkt(@PathVariable (required=true) Long id) {
		try {
			List<MarketProduct> mktProduct = marketProductRepository.findByProductIdProduct(id);
			setLogMktProduct(mktProduct.get(0).getProduct(), ActionMktProduct.FIND.getDescAction());
			return formatResponse(mktProduct, 0, 1, 1);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}	
	}
	
	
	/**
	 * Exibe produdos e respectivos valores por posto de coleta
	 * @param productId
	 * @param stationId
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ResponseFindDto> getProductMkt(
			@RequestParam(required=false) Long productId,
			@RequestParam(required=false) Long stationId,
//			@PageableDefault(sort="value", direction = Direction.DESC) Pageable pageable,
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size,
			@RequestParam(required=false) String sort,
			@RequestParam(required=false) String sortDirection) {
	
		sort = validation.existSort(sort, "idMktProduct");
		if(sort.toUpperCase().contains("VALOR")) {sort = "value"; };
		if(sort.toUpperCase().contains("PRODUTO")) {sort = "productIdProduct";}
		if(sort.toUpperCase().contains("POSTO")) {sort = "stationsIdStations";}
		if(sort.toUpperCase().contains("DATEUPDATE")) {sort = "updateDate";}

		size = (size != 0) ? size : (int) marketProductRepository.count();
		Direction direction = converter.sortDirection(sortDirection);
		Pageable pageable = PageRequest.of(page, size, direction, sort);
		Page<MarketProduct> productsPage = null;
		
		try {
			if(productId == null && stationId == null) {
				productsPage = marketProductRepository.findAll(pageable);
				return formatResponse(productsPage);
			}
			if(productId != null && stationId != null) {
				productsPage = marketProductRepository.findByProductIdProductAndStationsIdStations(productId, stationId, pageable);
				setLogMktProduct(productsPage.getContent().get(0), ActionMktProduct.FIND.getDescAction());
				return formatResponse(productsPage);
			}
			if(productId != null) {
				productsPage = marketProductRepository.findByProductIdProduct(productId, pageable);
				setLogMktProduct(productsPage.getContent().get(0).getProduct(), ActionMktProduct.FIND.getDescAction());
				return formatResponse(productsPage);
			}
			if(stationId != null) {
				productsPage = marketProductRepository.findByStationsIdStations(stationId, pageable);
				return formatResponse(productsPage);
			}
		} catch (Exception e) {
			if(e.getMessage().toUpperCase().contains(("INDEX: 0"))) {
				ResponseFindDto resp = new ResponseFindDto();
				resp.setResponseCode(HttpStatus.NOT_FOUND.value());
				resp.setResponseMessage("Nenhum registro foi localizado para os parâmetos de busca enviados.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
			}
			return ResponseEntity.badRequest().build();
		}
	
		return ResponseEntity.ok().build();
	}
	
	
	/**
	 * Adiciona um produto com valor a um fornecedor
	 * @param productMkt
	 * @param uriBuilder
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ResponseOtherMethods> setProductMkt(
			@RequestBody MktProductDto productMkt, UriComponentsBuilder uriBuilder,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization) {
		Optional<Product> product = productRepository.findById(productMkt.getIdProduct());
		Optional<Stations> station = stationsRepository.findById(productMkt.getIdStations());
		List<MarketProduct> existProduct = marketProductRepository.findByProductIdProductAndStationsIdStations(productMkt.getIdProduct(), productMkt.getIdStations());
		try {
			validation.reqProductMkt(product, station, existProduct);
			validation.userValidate(authorization.getFirst("authorization"), station.get().getUser());
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build(); 
		}
		productMkt.setInsertDate(converter.date(LocalDateTime.now()));
		productMkt.setUpdateDate(converter.date(LocalDateTime.now()));
		marketProductRepository.save(converter.toMktProduct(productMkt, station.get(), product.get()));
		existProduct = marketProductRepository.findByProductIdProductAndStationsIdStations(productMkt.getIdProduct(), productMkt.getIdStations());
		List<MktProductDto> productRefresh = converter.toMktProductDto(existProduct);
		URI uri = uriBuilder.path("/marketstuff/{id}").buildAndExpand(productRefresh.get(0).getIdMktProduct()).toUri();
		setLogMktProduct(existProduct.get(0), ActionMktProduct.INSERT.getDescAction());
		return converter.toResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), productRefresh.get(0));
	}
	
	
	/**
	 * Atualiza um produto comercializado pelo posto de coleta
	 * @param idProduct
	 * @return
	 */
	@PutMapping
	public ResponseEntity<ResponseOtherMethods> updateMktProduct(
			@RequestParam(value = "idMktProduct", required = true) Long idMktProduct,
			@RequestParam(value = "value", required = true) BigDecimal value,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization){
		List<MarketProduct> mktProduct = marketProductRepository.findByIdMktProduct(idMktProduct);
		try {
			validation.updateMktProduct(mktProduct);
			validation.userValidate(authorization.getFirst("authorization"), mktProduct.get(0).getStations().getUser());
		} catch (HttpClientErrorException e) {
			return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		MarketProduct mktProductUpdate = mktProduct.get(0);
		mktProductUpdate.setValue(value);
		mktProductUpdate.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
		marketProductRepository.save(mktProductUpdate);
		mktProduct = marketProductRepository.findByIdMktProduct(idMktProduct);
		MktProductDto mktProductDtoFind = converter.toMktProductDto(mktProduct).get(0);
		setLogMktProduct(mktProductUpdate, ActionMktProduct.UPDATE.getDescAction());
		return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), mktProductDtoFind);

	}
	
	
	/**
	 * Exclui produtos comercializados por um posto de coleta
	 * @param idStation
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<ResponseOtherMethods> deleteMktProduct(
			@RequestParam(value = "idStation", required = false) Long idStation,
			@RequestParam(value = "idMktProduct", required = false) Long idMktProduct,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization) {		
		if(idStation == null && idMktProduct == null) {
			return ResponseEntity.badRequest().build();
		}
		if(idStation != null && idMktProduct == null) {
			try {
				List<MarketProduct> mktProductFind = marketProductRepository.findByStationsIdStations(idStation);
				validation.isNotNull(mktProductFind.get(0));
				validation.userValidate(authorization.getFirst("authorization"), mktProductFind.get(0).getStations().getUser());
				marketProductRepository.deleteAll(mktProductFind);			
				return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
						converter.toMktProductDto(mktProductFind).get(0));
			} catch (HttpClientErrorException e) {
				return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
			} catch (Exception e) {
				return ResponseEntity.badRequest().build();
			}
		} else {
			try {
				List<MarketProduct> mktProductFind = marketProductRepository.findByIdMktProduct(idMktProduct);
				validation.isNotNull(marketProductRepository.findByIdMktProduct(idMktProduct).get(0));
				validation.userValidate(authorization.getFirst("authorization"), mktProductFind.get(0).getStations().getUser());
				marketProductRepository.deleteById(idMktProduct);
				MktProductDto responseMktProduct = converter.toMktProductDto(mktProductFind).get(0);
				return converter.toResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), responseMktProduct);				
			} catch (HttpClientErrorException e) {
				return converter.toResponse(e.getStatusCode().value(), e.getStatusText(), null);
			} catch (Exception e) {
				return ResponseEntity.badRequest().build();
			}
		}
	}
	
	/**
	 * Formata o response do endpoint de busca
	 * @param productsPage
	 * @return
	 */
	public ResponseEntity<ResponseFindDto> formatResponse(Page<MarketProduct> productsPage){
		Page<Object> pageResp = null;
		ResponseFindDto respMktProductDto = new ResponseFindDto();
		pageResp = productsPage.map(MktProductDto::new);
		respMktProductDto = converter.toResponseMktProductDto(pageResp);
		return ResponseEntity.ok().body(respMktProductDto);
	}
	
	public ResponseEntity<ResponseFindDto> formatResponse(List<MarketProduct> products, int number, int size, int totalElements){
		List<Object> listResp = null;
		ResponseFindDto respMktProductDto = new ResponseFindDto();
		listResp = products.stream().map(MktProductDto::new).collect(Collectors.toList());
		respMktProductDto = converter.toResponseMktProductDto(listResp, number, size, totalElements);
		return ResponseEntity.ok().body(respMktProductDto);
	}
	
	public void setLogMktProduct(MarketProduct mktProduct, String action) {
		LogMarketProduct log = new LogMarketProduct(mktProduct, action);
		logMktProductRepository.save(log);
	}
	
	public void setLogMktProduct(Product product, String action) {
		LogMarketProduct log = new LogMarketProduct(product, action);
		logMktProductRepository.save(log);
	}
	

}
