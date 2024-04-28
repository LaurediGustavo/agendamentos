package br.com.tcc.repository;

import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.ConsultaEstendida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultaEstendidaRepository extends JpaRepository<ConsultaEstendida, Long> {

    @Query("SELECT c.consultaEstendidaDe FROM ConsultaEstendida c " +
           "INNER JOIN c.consultaEstendidaPara cp " +
           "WHERE cp.id = :id ")
    Optional<Consulta> consultarConsultaDePorPara(@Param("id") Long consultaId);

}
