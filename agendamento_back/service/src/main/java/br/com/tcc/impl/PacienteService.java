package br.com.tcc.impl;

import br.com.tcc.dto.PacienteDto;
import br.com.tcc.entity.Paciente;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.repository.PacienteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteis.Uteis;

@Service("PacienteService")
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;

	public Long cadastrar(PacienteDto pacienteDto) {
		Paciente paciente = gerarPaciente(pacienteDto);
		pacienteRepository.save(paciente);
		return paciente.getId();
	}

	private Paciente gerarPaciente(PacienteDto pacienteDto) {
		Paciente paciente = new Paciente();
		paciente.setNome(pacienteDto.getNome());
		paciente.setSobrenome(pacienteDto.getSobrenome());
		paciente.setDataDeNascimento(pacienteDto.getDataDeNascimento());
		paciente.setCpf(Uteis.removerCaracteresNaoNumericos(pacienteDto.getCpf()));
		paciente.setGenero(pacienteDto.getGenero());
		paciente.setTelefone(pacienteDto.getTelefone());
		paciente.setInformacoesAdicionais(pacienteDto.getInformacoesAdicionais());
		paciente.setLogradouro(pacienteDto.getLogradouro());
		paciente.setBairro(pacienteDto.getBairro());
		paciente.setNumero(pacienteDto.getNumero());
		paciente.setBloco(pacienteDto.getBloco());
		paciente.setParentesco(pacienteDto.getParentesco());
		paciente.setCep(pacienteDto.getCep());

		setarResponsavel(paciente, pacienteDto);

		return paciente;
	}

	public void atualizar(PacienteDto pacienteDto) {
		Paciente paciente = getPaciente(pacienteDto.getId());
		atualizarCampos(paciente, pacienteDto);
		pacienteRepository.save(paciente);
	}

	private void atualizarCampos(Paciente paciente, PacienteDto pacienteDto) {
		if(StringUtils.isNotBlank(pacienteDto.getNome())) {
			paciente.setNome(pacienteDto.getNome());
		}

		if(StringUtils.isNotBlank(pacienteDto.getSobrenome())) {
			paciente.setSobrenome(pacienteDto.getSobrenome());
		}

		if(pacienteDto.getDataDeNascimento() != null) {
			paciente.setDataDeNascimento(pacienteDto.getDataDeNascimento());
		}

		if(StringUtils.isNotBlank(pacienteDto.getGenero())) {
			paciente.setGenero(pacienteDto.getGenero());
		}

		if(StringUtils.isNotBlank(pacienteDto.getTelefone())) {
			paciente.setTelefone(pacienteDto.getTelefone());
		}

		if(StringUtils.isNotBlank(pacienteDto.getInformacoesAdicionais())) {
			paciente.setInformacoesAdicionais(pacienteDto.getInformacoesAdicionais());
		}

		if(StringUtils.isNotBlank(pacienteDto.getLogradouro())) {
			paciente.setLogradouro(pacienteDto.getLogradouro());
		}

		if(StringUtils.isNotBlank(pacienteDto.getBairro())) {
			paciente.setBairro(pacienteDto.getBairro());
		}

		if(pacienteDto.getNumero() != null) {
			paciente.setNumero(pacienteDto.getNumero());
		}

		if(StringUtils.isNotBlank(pacienteDto.getBloco())) {
			paciente.setBloco(pacienteDto.getBloco());
		}

		if(StringUtils.isNotBlank(pacienteDto.getParentesco())) {
			paciente.setParentesco(pacienteDto.getParentesco());
		}

		if(StringUtils.isNotBlank(pacienteDto.getCep())) {
			paciente.setCep(pacienteDto.getCep());
		}

		setarResponsavel(paciente, pacienteDto);
	}

	private void setarResponsavel(Paciente paciente, PacienteDto pacienteDto) {
		Long responsavelId = pacienteDto.getResponsavel_paciente_id();
		if(responsavelId == null || responsavelId == 0) {
			paciente.setParentesco(null);
		}
		paciente.setResponsavelPacienteId(getPaciente(pacienteDto.getResponsavel_paciente_id()));
	}

	private Paciente getPaciente(Long pacienteId) {
		if (pacienteId != null && pacienteId != 0)
			return pacienteRepository.findById(pacienteId).get();

		return null;
	}

	public void deletar(Long id) {
		Paciente paciente = getPaciente(id);
		paciente.setDesabilitado(Boolean.TRUE);

		pacienteRepository.save(paciente);
	}

	public void revertDelete(Long id) {
		Paciente paciente = getPaciente(id);
		paciente.setDesabilitado(Boolean.FALSE);

		pacienteRepository.save(paciente);
	}

	public boolean existsById(Long id) {
		return pacienteRepository.existsById(id);
	}

	public boolean existsByCpf(String cpf) {
		return pacienteRepository.existsByCpf(cpf);
	}

}
