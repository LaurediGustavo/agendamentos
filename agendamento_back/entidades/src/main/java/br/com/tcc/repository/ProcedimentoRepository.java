package br.com.tcc.repository;

import br.com.tcc.entity.Procedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {

    @Query("SELECT p FROM Procedimento p " +
            "WHERE p.tratamento LIKE %:tratamento% " +
            "AND (p.desabilitado IS NULL OR p.desabilitado = false) " +
            "ORDER BY p.tratamento")
    Optional<List<Procedimento>> findByTratamentoLikeHabilitado(String tratamento);

    @Query("SELECT p FROM Procedimento p " +
            "WHERE p.tratamento LIKE %:tratamento% " +
            "AND (p.desabilitado = true) " +
            "ORDER BY p.tratamento")
    Optional<List<Procedimento>> findByTratamentoLikeDesabilitado(String tratamento);

    @Query("SELECT p FROM Procedimento p " +
            "WHERE p.desabilitado IS NULL OR p.desabilitado = false " +
            "ORDER BY p.tratamento")
    List<Procedimento> findAllHabilitados();

    @Query("SELECT p FROM Procedimento p " +
            "WHERE p.desabilitado IS NULL OR p.desabilitado = false " +
            "ORDER BY p.id")
    List<Procedimento> findAllHabilitadosOrderById();

    @Query("SELECT p FROM Procedimento p " +
            "WHERE p.id = :id " +
            "AND (p.desabilitado IS NULL OR p.desabilitado = false)")
    Optional<Procedimento> findByIdHabilitado(@Param("id") Long id);

}
