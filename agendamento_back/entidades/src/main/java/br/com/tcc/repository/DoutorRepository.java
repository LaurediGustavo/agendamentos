package br.com.tcc.repository;

import br.com.tcc.entity.Doutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface DoutorRepository extends JpaRepository<Doutor, Long> {

    Optional<List<Doutor>> findByNomeContaining(String nome);

    @Query("SELECT d FROM Doutor d WHERE d.id NOT IN " +
            "(SELECT c.doutor.id FROM Consulta c " +
            "WHERE (:dataInicio BETWEEN c.dataHoraInicio AND c.dataHoraFinal) " +
            "AND (:dataFim BETWEEN c.dataHoraInicio AND c.dataHoraFinal))")
    List<Doutor> findDoutoresDisponiveis(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT d FROM Doutor d " +
            "JOIN d.procedimentos p " +
            "WHERE p.id in (:procedimentosId) " +
            "AND d.nome like %:nome%")
    Optional<List<Doutor>> findByNomeAndProcedimentoId(@Param("nome") String nome, @Param("procedimentosId") Long[] procedimentoId);
}
