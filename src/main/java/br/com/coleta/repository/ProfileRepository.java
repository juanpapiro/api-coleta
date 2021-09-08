package br.com.coleta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.coleta.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{

	List<Profile> findByIdProfile(Long idProfile);
}
