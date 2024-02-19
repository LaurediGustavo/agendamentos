package br.com.tcc.repository;

import br.com.tcc.entity.Doutor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface DoutorRepository extends JpaRepository<Doutor, Long> {

    @Query("SELECT d FROM Doutor d " +
            "WHERE d.nome LIKE %:nome% " +
            "AND (d.desabilitado IS NULL OR d.desabilitado = false) " +
            "ORDER BY d.nome")
    Optional<List<Doutor>> findByNomeContaining(String nome);

    @Query("SELECT d FROM Doutor d " +
            "JOIN d.procedimentos p " +
            "WHERE d.id NOT IN (SELECT c.doutor.id FROM Consulta c " +
            "WHERE (:dataInicio BETWEEN c.dataHoraInicio AND c.dataHoraFinal) " +
            "OR (:dataFim BETWEEN c.dataHoraInicio AND c.dataHoraFinal)) " +
            "AND (p.id IN (:procedimentosId))" +
            "AND (d.desabilitado IS NULL OR d.desabilitado = false)")
    List<Doutor> findDoutoresDisponiveis(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim, @Param("procedimentosId") Long[] procedimentoId);

    @Query("SELECT d FROM Doutor d " +
            "JOIN d.procedimentos p " +
            "WHERE ((p.id in (:procedimentosId) " +
            "AND d.nome like %:nome%)) " +
            "AND (d.desabilitado IS NULL OR d.desabilitado = false) ")
    Optional<List<Doutor>> findByNomeAndProcedimentoId(@Param("nome") String nome, @Param("procedimentosId") Long[] procedimentoId);

    @Query("SELECT d FROM Doutor d " +
            "JOIN d.procedimentos p " +
            "WHERE p.id in (:procedimentosId) " +
            "AND (d.desabilitado IS NULL OR d.desabilitado = false) ")
    List<Doutor> findAllToProcedureHabilitados(@Param("procedimentosId") Long[] procedimentoId);
}
