package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.coleta.entity.WebContacts;

public interface WebContactsRepository extends JpaRepository<WebContacts, Long>{

	List<WebContacts> findByUserIdUser(Long idUser);
}
