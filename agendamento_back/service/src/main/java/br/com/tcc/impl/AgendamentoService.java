package br.com.tcc.impl;

import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.Paciente;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.DoutorRepository;
import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.repository.ProcedimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteis.DataUteis;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("AgendamentoService")
public class AgendamentoService {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private DoutorRepository doutorRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private ProcedimentoRepository procedimentoRepository;

	public Consulta persistir(AgendamentoDto agendamentoDto) {
		if(StatusConsultaEnum.REMARCADO.equals(agendamentoDto.getStatus())) {
			remarcar(agendamentoDto);
			agendamentoDto.setId(0L);
			agendamentoDto.setStatus(StatusConsultaEnum.AGUARDANDO);
		}

		Consulta consulta = gerarConsulta(agendamentoDto);

		return consultaRepository.save(consulta);
	}

	private void remarcar(AgendamentoDto agendamentoDto) {
		Consulta consultaOriginal = consultaRepository.findById(agendamentoDto.getId()).get();
		consultaOriginal.setStatus(agendamentoDto.getStatus());

		consultaRepository.save(consultaOriginal);
	}

	private Consulta gerarConsulta(AgendamentoDto agendamento) {
		Consulta consulta = new Consulta();
		consulta.setId(agendamento.getId());
		consulta.setDoutor(getDoutor(agendamento.getDoutorId()));
		consulta.setPaciente(getPaciente(agendamento.getPacienteId()));
		consulta.setProcedimentos(getProcedimentos(agendamento.getProcedimentosIds()));
		consulta.setDataHoraInicio(agendamento.getDataHoraInicio());
		consulta.setDataHoraFinal(agendamento.getDataHoraFim());
		consulta.setStatus(agendamento.getStatus() == null? StatusConsultaEnum.AGUARDANDO : agendamento.getStatus());

		calcularValorTotal(consulta);
		calcularTempoAproximado(consulta);

		return consulta;
	}

	private void calcularTempoAproximado(Consulta consulta) {
		consulta.setTempoAproximado(LocalTime.of(0, 0));

		consulta.getProcedimentos().forEach(procedimento -> {
			Duration duration = Duration.ofMinutes(Integer.parseInt(procedimento.getTempo()));
			LocalTime tempo = consulta.getTempoAproximado().plus(duration);
			consulta.setTempoAproximado(tempo);
		});
	}

	private void calcularValorTotal(Consulta consulta) {
		consulta.setValorTotal(BigDecimal.ZERO);

		consulta.getProcedimentos().forEach(procedimento -> {
			BigDecimal valorTotal = consulta.getValorTotal().add(procedimento.getValor());
			consulta.setValorTotal(valorTotal);
		});
	}

	public Optional<List<Consulta>> consultarPorHorarioDoutorPaciente(Long doutorId, Long pacienteId, String horario) {
		LocalDateTime dataHora = DataUteis.getLocalDateTime(horario);

		if(dataHora != null) {
			return consultaRepository.consultarPorDataDoutorPaciente(doutorId, pacienteId,
					dataHora.getYear(), dataHora.getMonthValue());
		}
		else {
			return consultaRepository.consultarPorHorarioDoutorPaciente(dataHora, doutorId, pacienteId);
		}
	}

	public Optional<Consulta> consultarPorId(Long id) {
		return consultaRepository.findById(id);
	}

	private List<Procedimento> getProcedimentos(List<Long> procedimentosIds) {
		return procedimentosIds.stream().map(
						id -> procedimentoRepository.findById(id).get()
				).collect(Collectors.toList());
	}

	private Paciente getPaciente(Long pacienteId) {
		return pacienteRepository.findById(pacienteId).get();
	}

	private Doutor getDoutor(Long doutorId) {
		return doutorRepository.findById(doutorId).get();
	}

}
