package br.com.tcc.repository;

import br.com.tcc.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    @Query("SELECT p FROM Procedimento p WHERE p.tratamento LIKE %:tratamento%")
    Optional<List<Procedimento>> findByTratamentoLike(String tratamento);

}
