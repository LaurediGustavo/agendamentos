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

import java.util.List;

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
		consulta.setDoutor(getDoutor(agendamento.getDoutorId()));
		consulta.setPaciente(getPaciente(agendamento.getPacienteId()));
		consulta.setProcedimento(getProcedimento(agendamento.getProcedimentosIds()));
		consulta.setDataHoraInicio(agendamento.getDataHoraInicio());
		consulta.setDataHoraFinal(agendamento.getDataHoraFim());
		consulta.setStatus(StatusConsultaEnum.AGUARDANDO);

		return consulta;
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
