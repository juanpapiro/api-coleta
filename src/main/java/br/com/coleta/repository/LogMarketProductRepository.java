package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.coleta.entity.LogMarketProduct;

public interface LogMarketProductRepository extends JpaRepository<LogMarketProduct, Long> {

	@Query(value = "select * from tb_log_market_product where DATE "
			+ "between TO_DATE(?, 'dd-MM-rrrr HH24:mi:ss')"
			+ "and TO_DATE(?, 'dd-MM-rrrr HH24:mi:ss')", nativeQuery = true)
	List<LogMarketProduct> findByDate(String date1, String date2);
	
	@Query(value = "select * from tb_log_market_product where DATE "
			+ "between TO_DATE(?, 'dd-MM-rrrr HH24:mi:ss') "
			+ "and TO_DATE(?, 'dd-MM-rrrr HH24:mi:ss') "
			+ "and Action like '%Atualização%'", nativeQuery = true)
	List<LogMarketProduct> findByDateAndAction(String date1, String date2, String action);
	
	@Query(value = "select product_id, count(*) as count from tb_log_market_product "
			+ "where (DATE >= TO_DATE(?, 'dd-MM-rrrr HH24:mi:ss') "
			+ "or DATE <= TO_DATE(?, 'dd-MM-rrrr HH24:mi:ss')) "
			+ "and Action like '%Atualização%' "
			+ "group by product_id order by count desc", nativeQuery = true)
	List<List<Long>> findByDateAndActionCount(String date1, String date2, String action);
}
