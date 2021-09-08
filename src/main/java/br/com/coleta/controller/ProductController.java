package br.com.coleta.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.util.UriComponentsBuilder;

import br.com.coleta.dto.MktProductDto;
import br.com.coleta.dto.ProductDto;
import br.com.coleta.dto.ResponseFindDto;
import br.com.coleta.entity.MarketProduct;
import br.com.coleta.entity.Product;
import br.com.coleta.repository.MarketProductRepository;
import br.com.coleta.repository.ProductRepository;
import br.com.coleta.repository.UserRepository;
import br.com.coleta.utils.Converters;
import br.com.coleta.utils.Validations;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="/stuff")
public class ProductController {
	
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MarketProductRepository marketProductRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Converters converter;
	
	@Autowired
	private Validations validation;

	
	
	/**
	 * Exibe produtos por tipo
	 * @param productId
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ResponseFindDto> getProduct(
			@RequestParam(value = "idProduct", required=false) Long idProduct,
			@RequestParam(value = "page", required=true) int page,
			@RequestParam(value = "size", required=true) int size,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "sortDirection", required=false) String sortDirection) {
		
		sort = validation.existSort(sort, "idProduct");
		size = (size == 0) ? (int) productRepository.count() : size;
		Direction direction = converter.sortDirection(sortDirection);
		Pageable pageable = PageRequest.of(page, size, direction, sort);
		Page<Product> resp = null;
		
		try {
			if(idProduct != null) {
				resp = productRepository.findByIdProduct(idProduct, pageable);
				return formatResponse(resp);
			}
			resp = productRepository.findAll(pageable);
			return formatResponse(resp);			
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	
	/**
	 * Adiciona novo tipo de produto
	 * @param productDesc
	 * @param uriBuilder
	 * @return
	 */
	@PostMapping
	public ResponseEntity<ProductDto> setProductType(@RequestBody Product productDesc, UriComponentsBuilder uriBuilder,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization){
		List<Product> resp = productRepository.findAll();
		try {
			validation.existProductName(resp, productDesc.getProduct());
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		productRepository.save(productDesc);
		ProductDto productDto = new ProductDto(productDesc);
		URI uri = uriBuilder.path("/setProductType/{id}").buildAndExpand(productDesc.getIdProduct()).toUri();
		return ResponseEntity.created(uri).body(productDto);
	}
	
	
	/**
	 * Atualiza um tipo de produto
	 * @param idProduct
	 * @param nameProduct
	 * @return
	 */
	@PutMapping
	public ResponseEntity<ProductDto> updateProduct(
			@RequestParam(value = "idProduct", required = true) Long idProduct,
			@RequestParam(value = "nameProduct", required = true) String nameProduct,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization){
		if(idProduct == null && (nameProduct == null || nameProduct.isEmpty())) {
			return ResponseEntity.badRequest().build();
		}
		List<Product> productFind = productRepository.findAll();
		try {
			validation.existProductName(productFind, nameProduct);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		try {
			Product product = productRepository.findByIdProduct(idProduct).get(0);
			validation.isNotNull(product);
			nameProduct = nameProduct.substring(0, 1).toUpperCase().concat(nameProduct.substring(1, nameProduct.length()).toLowerCase());
			Product productUpdate = new Product(idProduct, nameProduct);
			productRepository.save(productUpdate);
			ProductDto productDto = new ProductDto(product);
			return ResponseEntity.status(HttpStatus.OK).body(productDto);	
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	
	/**
	 * Exclui um tipo de produto
	 * @param idProduct
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<ProductDto> deleteProduct(
			@RequestParam(value = "idProduct", required = true) Long idProduct,
			@RequestHeader(required=true) MultiValueMap<String, String> authorization) {

		try {
			validation.isNotNull(idProduct);
			List<Product> product = productRepository.findByIdProduct(idProduct);
			validation.isNotNull(product.get(0));
			List<MarketProduct> marketProduct = marketProductRepository.findByProductIdProduct(idProduct);
			if(marketProduct != null && marketProduct.size() > 0) {
				marketProductRepository.deleteAll(marketProduct);
			}
			productRepository.deleteById(idProduct);
			return ResponseEntity.status(HttpStatus.OK).body(converter.toProductDto(product).get(0));	
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	public ResponseEntity<ResponseFindDto> formatResponse(Page<Product> products){
		Page<Object> pageResp = null;
		ResponseFindDto respProductDto = new ResponseFindDto();
		pageResp = products.map(ProductDto::new);
		respProductDto = converter.toResponseMktProductDto(pageResp);
		return ResponseEntity.ok().body(respProductDto);
	}
	

}
