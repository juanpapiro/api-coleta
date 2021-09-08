package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.coleta.entity.Address;
import br.com.coleta.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUserEmail(String email);
	List<Address> findByUserIdUser(Long idUser);
}
