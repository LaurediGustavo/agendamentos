package br.com.tcc.repository;

import br.com.tcc.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	@Query("SELECT COUNT(c.id) FROM Consulta c " +
		   " WHERE :data BETWEEN c.dataHoraInicio AND c.dataHoraFinal " +
		   " AND c.doutor.id = :doutor_id")
	public Optional<Long> consultarPorDataEDoutor(@Param("data") LocalDateTime dataHora,
												  @Param("doutor_id") Long doutor_id);
	
}
