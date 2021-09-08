package br.com.coleta.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.coleta.entity.Stations;

@Repository
public interface StationsRepository extends JpaRepository<Stations, Long>{
	
	List<Stations> findAll();
	List<Stations> findByUserEmail(String email);
	Page<Stations> findByUserEmail(String email, Pageable pageable);
	
	List<Stations> findByNmFantazy(String nmFantazy);
	Page<Stations> findByNmFantazy(String nmFantazy, Pageable pageable);

	List<Stations> findByIdStations(Long idStation);
	Page<Stations> findByIdStations(Long idStation, Pageable pageable);
	
	Page<Stations> findByAddressState(String state, Pageable pageable);
	
	Page<Stations> findByAddressCity(String city, Pageable pageable);

	List<Stations> findByUserEmailAndNmFantazyAndIdStations(String email, String nmFantazy, Long idStation);
	Page<Stations> findByUserEmailAndNmFantazyAndIdStationsAndAddressNeighborhood(String email, String nmFantazy, Long idStation, String neighborhood, Pageable pageable);
	
	
//	Page<Stations> findByAddressNeighborhood(String neighborhood, Pageable pageable);
//	
	@Query(value="SELECT * FROM TB_STATIONS S\r\n" + 
			"INNER JOIN TB_ADDRESS A ON S.ADDRESS_ID = A.ID\r\n" + 
			"INNER JOIN TB_PHONES P ON S.PHONES_ID = P.ID\r\n" + 
			"INNER JOIN TB_WEB_CONTACTS W ON S.WEB_CONTACTS_ID = W.ID\r\n" + 
			"INNER JOIN TB_USER U ON S.USER_ID = U.ID \r\n" + 
			"WHERE A.NEIGHBORHOOD ILIKE COALESCE (?, A.NEIGHBORHOOD)\r\n" +
			"OFFSET((?) * ?) ROWS FETCH NEXT ? ROWS ONLY", nativeQuery = true)
	List<Stations> findLikeNeighborhood(String neighborhood, int page, int size, int size2);
//	
	@Query(value="SELECT COUNT(*) FROM TB_STATIONS S\r\n" + 
			"INNER JOIN TB_ADDRESS A ON S.ADDRESS_ID = A.ID\r\n" + 
			"INNER JOIN TB_PHONES P ON S.PHONES_ID = P.ID\r\n" + 
			"INNER JOIN TB_WEB_CONTACTS W ON S.WEB_CONTACTS_ID = W.ID\r\n" + 
			"INNER JOIN TB_USER U ON S.USER_ID = U.ID \r\n" + 
			"WHERE A.NEIGHBORHOOD ILIKE COALESCE (?, A.NEIGHBORHOOD)", nativeQuery = true)
	int findLikeCountNeighborhood(String neighborhood);
 
	
	@Query(value= "SELECT * FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE ST.ID = COALESCE (?, ST.ID) AND ST.NM_FANTAZY ILIKE COALESCE (?, ST.NM_FANTAZY) \r\n" + 
			" AND W.EMAIL ILIKE COALESCE (?, W.EMAIL) \r\n" + 
			" AND A.NEIGHBORHOOD ILIKE COALESCE (?, A.NEIGHBORHOOD) \r\n" + 
			" AND A.STATE ILIKE COALESCE (?, A.STATE) AND \r\n" + 
			" A.CITY ILIKE COALESCE (?, A.CITY) " + 
			" OFFSET((?) * ?) ROWS FETCH NEXT ? ROWS ONLY", nativeQuery = true)
	List<Stations> findFull(Long idStation, String nmFantazy, String email, String neighborhood, String state, String city, int page, int size, int size2);
	
	@Query(value= "SELECT COUNT(*) FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE ST.ID = COALESCE (?, ST.ID) AND ST.NM_FANTAZY ILIKE COALESCE (?, ST.NM_FANTAZY) \r\n" + 
			" AND W.EMAIL ILIKE COALESCE (?, W.EMAIL) \r\n" + 
			" AND A.NEIGHBORHOOD ILIKE COALESCE (?, A.NEIGHBORHOOD) \r\n" + 
			" AND A.STATE ILIKE COALESCE (?, A.STATE) AND\r\n" + 
			" A.CITY ILIKE COALESCE (?, A.CITY)", nativeQuery = true)
	int findFullCount(Long idStation, String nmFantazy, String email, String neighborhood, String state, String city);
	
	
	@Query(value= "SELECT * FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.NEIGHBORHOOD ILIKE ? AND A.CITY ILIKE ? " + 
			" OFFSET((?) * ?) ROWS FETCH NEXT ? ROWS ONLY", nativeQuery = true)
	List<Stations> findCityNeighborhood(String neighborhood, String city, int page, int size, int size2);
	
	@Query(value= "SELECT COUNT(*) FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.NEIGHBORHOOD ILIKE ? AND A.CITY ILIKE ? ", nativeQuery = true)
	int findCityNeighborhoodCount(String neighborhood, String city);
	
	
	@Query(value= "SELECT * FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.NEIGHBORHOOD ILIKE ? AND A.STATE ILIKE ? " + 
			" OFFSET((?) * ?) ROWS FETCH NEXT ? ROWS ONLY", nativeQuery = true)
	List<Stations> findStateNeighborhood(String neighborhood, String state, int page, int size, int size2);
	
	@Query(value= "SELECT COUNT(*) FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.NEIGHBORHOOD ILIKE ? AND A.STATE ILIKE ? ", nativeQuery = true)
	int findStateNeighborhoodCount(String neighborhood, String state);
	
	
	@Query(value= "SELECT * FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.CITY ILIKE ? AND A.STATE ILIKE ? " + 
			" OFFSET((?) * ?) ROWS FETCH NEXT ? ROWS ONLY", nativeQuery = true)
	List<Stations> findStateCity(String city, String state, int page, int size, int size2);
	
	@Query(value= "SELECT COUNT(*) FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.CITY ILIKE ? AND A.STATE ILIKE ? ", nativeQuery = true)
	int findStateCityCount(String city, String state);
	
	
	
	@Query(value= "SELECT * FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.NEIGHBORHOOD ILIKE ? AND A.CITY ILIKE ? AND A.STATE ILIKE ?" + 
			" OFFSET((?) * ?) ROWS FETCH NEXT ? ROWS ONLY", nativeQuery = true)
	List<Stations> findNeighborhoodCityState(String neighborhood, String city, String state, int page, int size, int size2);
	
	@Query(value= "SELECT COUNT(*) FROM TB_STATIONS ST \r\n" + 
			" INNER JOIN TB_ADDRESS A ON ST.ADDRESS_ID = A.ID\r\n" + 
			" INNER JOIN TB_PHONES P ON ST.PHONES_ID = P.ID \r\n" + 
			" INNER JOIN TB_WEB_CONTACTS W ON ST.WEB_CONTACTS_ID = W.ID\r\n" + 
			" INNER JOIN TB_USER U ON ST.USER_ID = U.ID\r\n" + 
			" WHERE A.NEIGHBORHOOD ILIKE ? AND A.CITY ILIKE ? AND A.STATE ILIKE ?", nativeQuery = true)
	int findNeighborhoodCityStateCount(String neighborhood, String city, String state);
	
//	Optional<Stations> findById(Long id);
	
}
