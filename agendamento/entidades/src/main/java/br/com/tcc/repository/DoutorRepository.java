package br.com.tcc.repository;

import br.com.tcc.entity.Doutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DoutorRepository extends JpaRepository<Doutor, Long> {

}
