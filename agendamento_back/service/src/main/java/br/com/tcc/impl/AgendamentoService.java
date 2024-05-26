package br.com.tcc.impl;

import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.entity.*;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteis.DataUteis;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

	@Autowired
	private ConsultaEstendidaRepository consultaEstendidaRepository;

	@Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRED)
	public Consulta persistir(AgendamentoDto agendamentoDto) {
		Consulta consultaRemarcada = null;
		if(StatusConsultaEnum.REMARCADO.equals(agendamentoDto.getStatus())) {
			consultaRemarcada = remarcar(agendamentoDto);
			agendamentoDto.setId(0L);
			agendamentoDto.setStatus(StatusConsultaEnum.AGUARDANDO);
		}

		Consulta consulta = gerarConsulta(agendamentoDto);
		consulta = consultaRepository.save(consulta);

		if (consultaRemarcada != null) {
			consultaRemarcada.setConsultaRemarcadaPara(consulta);
			consultaRepository.save(consultaRemarcada);
		}

		adicionarConsultaEstendida(consulta, agendamentoDto);

		return consulta;
	}

	private Consulta remarcar(AgendamentoDto agendamentoDto) {
		Consulta consultaOriginal = consultaRepository.findById(agendamentoDto.getId()).get();
		consultaOriginal.setStatus(agendamentoDto.getStatus());

		return consultaRepository.save(consultaOriginal);
	}

	private Consulta gerarConsulta(AgendamentoDto agendamento) {
		Consulta consulta = this.consultarPorId(agendamento.getId()).orElse(new Consulta());
		consulta.setId(agendamento.getId());
		consulta.setDoutor(getDoutor(agendamento.getDoutorId()));
		consulta.setPaciente(getPaciente(agendamento.getPacienteId()));
		consulta.setProcedimentos(getProcedimentos(agendamento.getProcedimentosIds(), agendamento.getConsultaEstendidaDeId()));
		consulta.setDataHoraInicio(agendamento.getDataHoraInicio());
		consulta.setDataHoraFinal(agendamento.getDataHoraFim());
		consulta.setStatus(agendamento.getStatus() == null? StatusConsultaEnum.AGUARDANDO : agendamento.getStatus());

		preencherConsultaEstendida(consulta, agendamento.getConsultaEstendidaDeId());

		calcularValorTotal(consulta);
		calcularTempoAproximado(consulta);

		return consulta;
	}

	private void preencherConsultaEstendida(Consulta consulta, Long consultaEstendidaDeId) {
		if (consultaEstendidaDeId != null) {
			List<Consulta> consultas = new ArrayList<>();
			consultas.add(consultarPorId(consultaEstendidaDeId).get());

			consulta.setConsultasEstendidasDe(consultas);
		}
	}

	@Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRED)
	private void adicionarConsultaEstendida(Consulta consulta, AgendamentoDto agendamento) {
		if (agendamento.getConsultaEstendidaDeId() != null && consulta.getConsultasEstendidasDe() == null) {
			ConsultaEstendida consultaEstendida = new ConsultaEstendida();
			consultaEstendida.setConsultaEstendidaDe(consultarPorId(agendamento.getConsultaEstendidaDeId()).get());
			consultaEstendida.setConsultaEstendidaPara(consulta);

			this.consultaEstendidaRepository.save(consultaEstendida);
		}
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

	public Optional<List<Consulta>> consultarPorStatusPaciente(Long pacienteId, StatusConsultaEnum status) {
		return consultaRepository.consultarStatusPaciente(pacienteId, status);
	}

	public Optional<Consulta> consultarPorId(Long id) {
		id = id == null? 0L : id;

		return consultaRepository.findById(id);
	}

	private List<Procedimento> getProcedimentos(List<Long> procedimentosIds, Long consultaEstendidaId) {
		removerProcedimentosCadastradosNoPai(procedimentosIds, consultaEstendidaId);

		return procedimentosIds.stream().map(
						id -> procedimentoRepository.findById(id).get()
				).collect(Collectors.toList());
	}

	private void removerProcedimentosCadastradosNoPai(List<Long> procedimentosIds, Long consultaEstendidaId) {
		if (consultaEstendidaId != null) {
			Optional<Consulta> consultaOptional = consultarPorId(consultaEstendidaId);

			if (consultaOptional.isPresent()) {
				List<Procedimento> procedimentos = consultaOptional.get().getProcedimentos();

				List<Long> ids = procedimentos.stream().map(Procedimento::getId).toList();
				procedimentosIds.removeAll(ids);
			}
		}
	}

	private Paciente getPaciente(Long pacienteId) {
		return pacienteRepository.findById(pacienteId).get();
	}

	private Doutor getDoutor(Long doutorId) {
		return doutorRepository.findById(doutorId).get();
	}

}
