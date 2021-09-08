package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.coleta.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	List<Product> findByIdProduct(Long idProduct);
	Page<Product> findByIdProduct(Long idProduct, Pageable pageable);

	List<Product> findByProduct(String desc);
	Page<Product> findByProduct(String desc, Pageable pageable);

}
