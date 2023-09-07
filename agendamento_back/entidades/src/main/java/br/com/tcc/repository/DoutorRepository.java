package br.com.tcc.repository;

import br.com.tcc.entity.Doutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DoutorRepository extends JpaRepository<Doutor, Long> {

    Optional<List<Doutor>> findByNomeContaining(String nome);

}
