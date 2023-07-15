package br.com.tcc.repository;

import java.util.Optional;

import br.com.tcc.entity.Ususario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Ususario, Long> {
	
	public Optional<Ususario> findByUserName(String userName);
	
}
