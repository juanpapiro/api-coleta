package br.com.coleta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.coleta.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	List<User> findByIdUser(Long id);
	Page<User> findByIdUser(Long id, Pageable pageable);

	List<User> findByEmail(String email);
	Page<User> findByEmail(String email, Pageable pageable);

	List<User> findByIdUserAndEmail(Long id, String email);
	Page<User> findByIdUserAndEmail(Long id, String email, Pageable pageable);


}
