package br.com.coleta.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import br.com.coleta.dto.ProductDto;
import br.com.coleta.dto.RecyclingMarketProductDto;
import br.com.coleta.dto.ResponseRecyclingMktProductDto;
import br.com.coleta.dto.StationDto;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Product;
import br.com.coleta.enums.ActionMktProduct;
import br.com.coleta.repository.LogMarketProductRepository;
import br.com.coleta.repository.MarketProductRepository;
import br.com.coleta.repository.ProductRepository;
import br.com.coleta.utils.Converters;
import br.com.coleta.utils.Validations;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="/recycling")
public class RecyclingMapController {
	
	@Autowired
	private MarketProductRepository marketProductRepository;
	
	@Autowired
	private LogMarketProductRepository logMktProductRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private Validations validation;
	
	@Autowired
	private Converters converter;
	
	
	
	/**
	 * Busca produtos para ordenar por valor
	 * @param idProduct
	 * @param idStation
	 * @param page
	 * @param size
	 * @param sort
	 * @param sortDirection
	 * @return
	 */
	@GetMapping(value="/mktstuffvalue")
	public ResponseEntity<ResponseRecyclingMktProductDto> findMktProduct(
			@RequestParam(required=false) Long idProduct,
			@RequestParam(required=false) Long idStation,
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size,
			@RequestParam(required=false) String sort,
			@RequestParam(required=false) String sortDirection) {
		
		int fullSize = (size == 0) ? (int) marketProductRepository.count() : size;
		Pageable pageable = converter.paramsPageable(page, size, fullSize, sort, "value", sortDirection);
		
		List<MarketProduct> mktProducts = new ArrayList<>();
		
		try {
			if(idProduct == null && idStation == null) {
				fullSize = (int) marketProductRepository.count();
				pageable = converter.paramsPageable(page, size, fullSize, sort, "value", sortDirection);
				Page<MarketProduct> resultRequest = marketProductRepository.findAll(pageable);
				mktProducts.addAll(resultRequest.getContent());
				List<List<MarketProduct>> listMktProductsSegment = getListProductSegments(mktProducts);
				List<RecyclingMarketProductDto> listRecyclingProductsValue = new ArrayList<>();
				Map<Long, List<MarketProduct>> mapListMarketProduct = filterListProducts(listMktProductsSegment);
				mapListMarketProduct.forEach((key,list) -> {
					RecyclingMarketProductDto recyclingProductsValue = getInfoValueProduct(list);
					listRecyclingProductsValue.add(recyclingProductsValue);
				});
				List<RecyclingMarketProductDto> listPaginator = new ArrayList<>();
				listPaginator = converter.paginator(listRecyclingProductsValue, page, size, listRecyclingProductsValue.size());
				listPaginator.sort(Comparator.comparing(p -> p.getMaxValue()));
				Collections.reverse(listPaginator);
				return formatResponseOk(listPaginator, page, size, resultRequest.getTotalElements());
			}			
			if(idProduct != null && idStation == null) {
				mktProducts = marketProductRepository.findByProductIdProduct(idProduct);
				RecyclingMarketProductDto recyclingProductsValue = getInfoValueProduct(mktProducts);
				List<RecyclingMarketProductDto> listRecyclingProductsValue = new ArrayList<>();
				listRecyclingProductsValue.add(recyclingProductsValue);
				return formatResponseOk(listRecyclingProductsValue, 0, 1, 1);
			}			
		} catch(Exception e) {		
			return formatResponseError(null, null);
		}
		return formatResponseError(null, null);
	}
	
	@GetMapping(value="/mktstuffsize")
	public ResponseEntity<ResponseRecyclingMktProductDto> findMktProductSize(
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size) {
		
		List<MarketProduct> mktProducts = new ArrayList<>();
		List<RecyclingMarketProductDto> listRecyclingProductsSize = new ArrayList<>();

		try {
			mktProducts = marketProductRepository.findAll();
			List<List<MarketProduct>> listMktProductsSegment = getListProductSegments(mktProducts);
			Map<Long, List<MarketProduct>> mapListMarketProduct = filterListProducts(listMktProductsSegment);
			mapListMarketProduct.forEach((key, list) -> {
				RecyclingMarketProductDto recyclingProductsSize = getInfoSizeProduct(list);
				listRecyclingProductsSize.add(recyclingProductsSize);
			});
			listRecyclingProductsSize.sort(Comparator.comparing(p -> p.getCountMktProducts()));
			Collections.reverse(listRecyclingProductsSize);
			long totalElements = (long) listRecyclingProductsSize.size();
			size = (size != 0) ? size : (int) totalElements;
			List<RecyclingMarketProductDto> listResponse = converter.paginator(listRecyclingProductsSize, page, size, totalElements);
			return formatResponseOk(listResponse, page, size, totalElements);			
		} catch (HttpClientErrorException ex) {
			return formatResponseError(ex.getStatusCode().value(), ex.getStatusText());
		} catch (Exception e) {
			return formatResponseError(null, null);
		}
		
	}
	
	@GetMapping(value="/mktstuffmaxvalueandstation")
	public ResponseEntity<ResponseRecyclingMktProductDto> getMaxValueSatation(
			@RequestParam(required=false) Long idProduct,
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size){
		
		int fullSize = (size == 0) ? (int) marketProductRepository.count() : size;
		Pageable pageable = PageRequest.of(page, fullSize, Direction.DESC, "value");
		
		try {
			Page<MarketProduct> mktProductsPage = marketProductRepository.findAll(pageable);
			RecyclingMarketProductDto productMavValue = getInfoValueProduct(mktProductsPage.getContent());
			StationDto stationDto = new StationDto(mktProductsPage.getContent().get(0).getStations());
			productMavValue.setStation(stationDto);
			List<RecyclingMarketProductDto> productResponse = new ArrayList<>();
			productResponse.add(productMavValue);
			return formatResponseOk(productResponse, page, fullSize, 1);
		} catch (Exception e) {
			return formatResponseError(null, null);
		}	
		
	}
	
	@GetMapping(value="/logmktstuffupdate")
	public ResponseEntity<ResponseRecyclingMktProductDto> getLogMktProduct(
			@RequestParam(required=false) Long idProduct,
			@RequestParam(required=true) int page,
			@RequestParam(required=true) int size){
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		LocalDateTime ldlStart = null;
		LocalDateTime ldlEnd  = null;
		if(ldt.getMonthValue() != 12) {
			ldlStart = LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), 1, 0, 0, 0);
			ldlEnd = LocalDateTime.of(ldt.getYear(), ldt.getMonthValue() + 1, 1, 23, 59, 59).minusHours(24);	
		} else {
			ldlStart = LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), 1, 0, 0, 0);
			ldlEnd = LocalDateTime.of(ldt.getYear()+1, 1, 1, 23, 59, 59).minusHours(24);
		}
		List<List<Long>> logs = logMktProductRepository.findByDateAndActionCount(ldlStart.format(f),
				ldlEnd.format(f), ActionMktProduct.UPDATE.getDescAction());
		
		HashSet<Long> idsProducts = new HashSet<>();
		logs.stream().forEach(log -> idsProducts.add(log.get(0)));
		List<Product> products = productRepository.findAllById(idsProducts);
		Map<Long, Product> mapProducts = new HashMap<>();
		products.stream().forEach(p -> mapProducts.put(p.getIdProduct(), p));

		List<RecyclingMarketProductDto> listLogsMktProduct = new ArrayList<>();
	
		logs.stream().forEach(listLogs -> {
			RecyclingMarketProductDto log = new RecyclingMarketProductDto();
			log.setProduct(new ProductDto(mapProducts.get(listLogs.get(0))));
			log.setCountLogs(listLogs.get(1));
			listLogsMktProduct.add(log);
		});
		
		List<RecyclingMarketProductDto> listPaginator = new ArrayList<>();
		listPaginator = converter.paginator(listLogsMktProduct, page, size, (long) listLogsMktProduct.size());
		
		return formatResponseOk(listPaginator, page, size, (long) listLogsMktProduct.size());
	}
	
	
	/**
	 * Segmenta lista de materiais
	 * @param fullProducts
	 * @return
	 */
	public List<List<MarketProduct>> getListProductSegments(List<MarketProduct> fullProducts) {
		List<List<MarketProduct>> listPoductsSegment = new ArrayList<>();
		fullProducts.stream().forEach(p -> {
			listPoductsSegment.add(fullProducts.stream()
					.filter(product -> product.getProduct().getIdProduct() == p.getProduct().getIdProduct())
					.collect(Collectors.toList()));
		});
		return listPoductsSegment;
	}
	
	
	
	/**
	 * Filtra lista de produtos em um Map
	 * @param listPoductsSegment
	 * @return
	 */
	public Map<Long, List<MarketProduct>> filterListProducts(List<List<MarketProduct>> listPoductsSegment) {	
		Map<Long, List<MarketProduct>> mapProducts = new LinkedHashMap<>();	
		listPoductsSegment.stream().forEach(list -> {
			if(!mapProducts.containsKey(list.get(0).getProduct().getIdProduct())) {
				mapProducts.put(list.get(0).getProduct().getIdProduct(), list);
			}
		});
		return mapProducts;
	}
	
	
	/**
	 * Obtém média de valores dos materiais
	 * @param listPoductsSegment
	 * @return
	 */
	public Map<Long, BigDecimal> getListProductsOrderValue(List<List<MarketProduct>> listPoductsSegment) {
		Map<Long, BigDecimal> mapAverage = new LinkedHashMap<>();
		listPoductsSegment.stream().forEach(list -> {
			mapAverage.put(list.get(0).getProduct().getIdProduct(), getAverage(list));
		});
		return mapAverage;
	}
	
	
	/**
	 * Obtem a média de valor a partir da lista de um segmento de produtos
	 * @param products
	 * @return
	 */
	public BigDecimal getAverage(List<MarketProduct> products) {
		OptionalDouble average = products.stream().mapToDouble(p -> p.getValue().doubleValue()).average();
		BigDecimal response = (average.isPresent()) ? new BigDecimal(average.getAsDouble()) : new BigDecimal(0); 
		return response;
	}
	
	
	/**
	 * Calcula a média de valor pago pelos materiais nos postos de coleta
	 * @param mktProducts
	 * @return
	 */
	public BigDecimal extractValue(List<MarketProduct> mktProducts) {
		BigDecimal fullValue = new BigDecimal(0);
		BigDecimal averageValue = new BigDecimal(0);
		BigDecimal divide = new BigDecimal(mktProducts.size());
		for(MarketProduct p : mktProducts) {
			fullValue = fullValue.add(p.getValue());			
		}
		averageValue = fullValue.divide(divide, 2);
		return averageValue;	
	}
	
	/**
	 * Processa informações de valor por produto a partir de uma lista de tipo de produto
	 * @param mktProducts
	 * @return
	 */
	public RecyclingMarketProductDto getInfoValueProduct(List<MarketProduct> mktProducts) {
		RecyclingMarketProductDto recyclingMktProduct = new RecyclingMarketProductDto();
		ProductDto productDto = new ProductDto(mktProducts.get(0).getProduct());
		if(mktProducts.size() > 1) {
			mktProducts.sort(Comparator.comparing(p -> p.getValue()));			
		}
		Collections.reverse(mktProducts);
		recyclingMktProduct.setAverageValue(converter.formatValue(getAverage(mktProducts)));
		recyclingMktProduct.setMaxValue(converter.formatValue(mktProducts.get(0).getValue()));
		recyclingMktProduct.setMinValue(converter.formatValue(mktProducts.get(mktProducts.size()-1).getValue()));
		recyclingMktProduct.setProduct(productDto);
		return recyclingMktProduct;
	}
	
	public RecyclingMarketProductDto getInfoSizeProduct(List<MarketProduct> mktProducts) {
		RecyclingMarketProductDto recyclingMktProduct = new RecyclingMarketProductDto();
		ProductDto productDto = new ProductDto(mktProducts.get(0).getProduct());
		if(mktProducts.size() > 1) {
			mktProducts.sort(Comparator.comparing(p -> p.getValue()));			
		}
		Collections.reverse(mktProducts);
		recyclingMktProduct.setCountMktProducts(mktProducts.size());
		recyclingMktProduct.setProduct(productDto);
		return recyclingMktProduct;
	}
	
	/**
	 * Formata response de sucesso
	 * @param listRecyclingProductsValue
	 * @return
	 */
	public ResponseEntity<ResponseRecyclingMktProductDto> formatResponseOk(List<RecyclingMarketProductDto> listRecyclingProductsValue, int page, int size, long totalElements){
		ResponseRecyclingMktProductDto resp = new ResponseRecyclingMktProductDto();
		resp.setListRecyclingProductsValue(listRecyclingProductsValue);
		resp.setResponseCode(HttpStatus.OK.value());
		resp.setResponseMessage(HttpStatus.OK.getReasonPhrase());
		resp.setPage(page);
		resp.setSize(size);
		resp.setTotalElements(totalElements);
		return ResponseEntity.ok().body(resp);
	}
	
	public ResponseEntity<ResponseRecyclingMktProductDto> formatResponseError(Integer code, String message){
		ResponseRecyclingMktProductDto resp = new ResponseRecyclingMktProductDto();
		resp.setResponseCode((code != null) ? code : HttpStatus.BAD_REQUEST.value());
		resp.setResponseMessage((message != null) ? message : HttpStatus.BAD_REQUEST.getReasonPhrase());
		return ResponseEntity.badRequest().body(resp);
	}

}
