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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

	public void persistir(AgendamentoDto agendamentoDto) {
		Consulta consulta = gerarConsulta(agendamentoDto);
		consultaRepository.save(consulta);
	}

	private Consulta gerarConsulta(AgendamentoDto agendamento) {
		Consulta consulta = new Consulta();
		consulta.setId(agendamento.getId());
		consulta.setDoutor(getDoutor(agendamento.getDoutorId()));
		consulta.setPaciente(getPaciente(agendamento.getPacienteId()));
		consulta.setProcedimento(getProcedimento(agendamento.getProcedimentosIds()));
		consulta.setDataHoraInicio(agendamento.getDataHoraInicio());
		consulta.setDataHoraFinal(agendamento.getDataHoraFim());
		consulta.setStatus(agendamento.getStatus() == null? StatusConsultaEnum.AGUARDANDO : agendamento.getStatus());

		return consulta;
	}

	public Optional<List<Consulta>> consultarPorHorarioDoutorPaciente(Long doutorId, Long pacienteId, String horario) {
		LocalDateTime dataHora = DataUteis.getLocalDateTime(horario);

		if(dataHora.getHour() == 0 && dataHora.getMinute() == 0) {
			return consultaRepository.consultarPorDataDoutorPaciente(doutorId, pacienteId,
					dataHora.getYear(), dataHora.getMonthValue(), dataHora.getDayOfMonth());
		}
		else {
			return consultaRepository.consultarPorHorarioDoutorPaciente(dataHora, doutorId, pacienteId);
		}
	}

	public Optional<Consulta> consultarPorId(Long id) {
		return consultaRepository.findById(id);
	}

	private Procedimento getProcedimento(List<Long> procedimentosIds) {
		return procedimentoRepository.findById(1L).get();
	}

	private Paciente getPaciente(Long pacienteId) {
		return pacienteRepository.findById(pacienteId).get();
	}

	private Doutor getDoutor(Long doutorId) {
		return doutorRepository.findById(doutorId).get();
	}

}
