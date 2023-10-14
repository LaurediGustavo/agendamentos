package br.com.tcc.repository;

import br.com.tcc.entity.Consulta;
import br.com.tcc.enumerador.StatusConsultaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	@Query("SELECT COUNT(c.id) FROM Consulta c " +
		   " WHERE ((:dataInicio BETWEEN c.dataHoraInicio AND c.dataHoraFinal) " +
		   " OR (c.dataHoraInicio BETWEEN :dataInicio AND :dataFim) " +
		   " OR (c.dataHoraFinal BETWEEN :dataInicio AND :dataFim)) " +
		   " AND c.doutor.id = :doutor_id " +
		   " AND (c.id <> :consulta_id OR :consulta_id IS NULL) " +
		   " AND c.status NOT IN ('CANCELADO', 'REMARCADO')")
	Optional<Long> consultarPorDataEDoutor(@Param("dataInicio") LocalDateTime dataHoraInicio,
										   @Param("dataFim") LocalDateTime dataHoraFim,
										   @Param("doutor_id") Long doutor_id,
										   @Param("consulta_id") Long consulta_id);

	@Query("SELECT c FROM Consulta c " +
			"INNER JOIN c.paciente p " +
			"WHERE c.status = :status AND p.chatId = :chat_id " +
			"ORDER BY c.dataHoraInicio ASC")
	Optional<Consulta> consultarDataMaisAntiga(@Param("status") StatusConsultaEnum status,
													  @Param("chat_id") Long chat_id);

	@Query("SELECT c FROM Consulta c " +
			" WHERE (:doutorId IS NULL OR c.doutor.id = :doutorId) " +
			" AND (:pacienteId IS NULL OR c.paciente.id = :pacienteId) " +
			" AND c.dataHoraInicio = :horario ")
	Optional<List<Consulta>> consultarPorHorarioDoutorPaciente(@Param("horario") LocalDateTime horario,
													 @Param("doutorId") Long doutorId,
													 @Param("pacienteId") Long pacienteId);

	@Query("SELECT c FROM Consulta c " +
			" WHERE (:doutorId IS NULL OR c.doutor.id = :doutorId) " +
			" AND (:pacienteId IS NULL OR c.paciente.id = :pacienteId) " +
			" AND YEAR(c.dataHoraInicio) = :ano " +
			" AND MONTH(c.dataHoraInicio) = :mes")
	Optional<List<Consulta>> consultarPorDataDoutorPaciente(@Param("doutorId") Long doutorId,
															@Param("pacienteId") Long pacienteId,
															@Param("ano") Integer ano,
															@Param("mes") Integer mes);

	@Query("SELECT c FROM Consulta c " +
			"INNER JOIN c.paciente p " +
			"WHERE c.status = :status " +
			"AND dataHoraInicio BETWEEN :dataInicio AND :dataFim " +
			"AND p.chatId IS NOT NULL " +
			"ORDER BY c.dataHoraInicio ASC")
	Optional<List<Consulta>> consultarProximosAgendamentos(@Param("status") StatusConsultaEnum status,
														   @Param("dataInicio") LocalDateTime dataInicio,
														   @Param("dataFim") LocalDateTime dataFim);

	@Query("SELECT c FROM Consulta c " +
			"INNER JOIN c.paciente p " +
			"WHERE c.status = :status " +
			"AND p.chatId = :chatId " +
			"AND YEAR(c.dataHoraInicio) = :ano " +
			"AND MONTH(c.dataHoraInicio) = :mes " +
			"AND DAY(c.dataHoraInicio) = :dia ")
	Optional<Consulta> consultarAgendamentoPendente(@Param("status") StatusConsultaEnum status,
													@Param("chatId") Long chatId,
													@Param("ano") Integer ano,
													@Param("mes") Integer mes,
													@Param("dia") Integer dia);

	@Query("SELECT c FROM Consulta c " +
			"INNER JOIN c.paciente p " +
			"WHERE c.status IN ('CONFIRMADO', 'AGUARDANDO') " +
			"AND p.cpf = :cpf " +
			"AND c.dataHoraInicio >= CURRENT_TIMESTAMP")
	List<Consulta> findConsultasByStatusAndCpfAndDataHoraInicio(@Param("cpf") String cpf);

	@Query("SELECT COUNT(c) FROM Consulta c " +
			"INNER JOIN c.paciente p " +
			"WHERE c.status IN ('CONFIRMADO', 'AGUARDANDO') " +
			"AND p.cpf = :cpf " +
			"AND c.dataHoraInicio >= CURRENT_TIMESTAMP")
	Long hasConsultasByStatusAndCpfAndDataHoraInicio(@Param("cpf") String cpf);

}
