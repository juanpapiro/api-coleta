package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.coleta.entity.Phones;

public interface PhonesRepository extends JpaRepository<Phones, Long> {
	
	List<Phones> findByUserIdUser(Long idUser);

}
