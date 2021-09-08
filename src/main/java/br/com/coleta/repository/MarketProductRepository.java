package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.coleta.entity.MarketProduct;

@Repository
public interface MarketProductRepository extends JpaRepository<MarketProduct, Long>{
	
	List<MarketProduct> findAll();
	List<MarketProduct> findByIdMktProduct(Long idMktProduct);
	Page<MarketProduct> findByIdMktProduct(Long idMktProduct, Pageable pageable);
	List<MarketProduct> findByStationsIdStations(Long stationId);
	Page<MarketProduct> findByStationsIdStations(Long stationId, Pageable pageable);
	List<MarketProduct> findByProductIdProduct(Long productId);
	Page<MarketProduct> findByProductIdProduct(Long productId, Pageable pageable);
	List<MarketProduct> findByProductIdProductAndStationsIdStations(Long productId, Long stationId);
	Page<MarketProduct> findByProductIdProductAndStationsIdStations(Long productId, Long stationId, Pageable pageable);
	void deleteByStationsIdStations(Long idStation);
	
}
