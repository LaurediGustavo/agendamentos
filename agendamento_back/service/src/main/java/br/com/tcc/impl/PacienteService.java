package br.com.tcc.impl;

import br.com.tcc.dto.PacienteDto;
import br.com.tcc.entity.Paciente;
import br.com.tcc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PacienteService")
public class PacienteService {

	@Autowired
	private PacienteRepository pacienteRepository;

	public void cadastrar(PacienteDto pacienteDto) {
		Paciente paciente = gerarPaciente(pacienteDto);
		pacienteRepository.save(paciente);
	}

	private Paciente gerarPaciente(PacienteDto pacienteDto) {
		Paciente paciente = new Paciente();
		paciente.setNome(pacienteDto.getNome());
		paciente.setSobrenome(pacienteDto.getSobrenome());
		paciente.setDataDeNascimento(pacienteDto.getDataDeNascimento());
		paciente.setCpf(pacienteDto.getCpf());
		paciente.setGenero(pacienteDto.getGenero());
		paciente.setTelefone(pacienteDto.getTelefone());
		paciente.setInformacoesAdicionais(pacienteDto.getInformacoesAdicionais());
		paciente.setLogradouro(pacienteDto.getLogradouro());
		paciente.setBairro(pacienteDto.getBairro());
		paciente.setNumero(pacienteDto.getNumero());
		paciente.setBloco(pacienteDto.getBloco());
		paciente.setParentesco(pacienteDto.getParentesco());

		setarResponsavel(paciente, pacienteDto);

		return paciente;
	}

	public void atualizar(PacienteDto pacienteDto) {
		Paciente paciente = getPaciente(pacienteDto.getId());
		atualizarCampos(paciente, pacienteDto);
		pacienteRepository.save(paciente);
	}

	private void atualizarCampos(Paciente paciente, PacienteDto pacienteDto) {
		if(!paciente.getNome().equals(pacienteDto.getNome())) {
			paciente.setNome(pacienteDto.getNome());
		}

		if(!paciente.getSobrenome().equals(pacienteDto.getSobrenome())) {
			paciente.setSobrenome(pacienteDto.getSobrenome());
		}

		if(!paciente.getDataDeNascimento().equals(pacienteDto.getDataDeNascimento())) {
			paciente.setDataDeNascimento(pacienteDto.getDataDeNascimento());
		}

		if(!paciente.getGenero().equals(pacienteDto.getGenero())) {
			paciente.setGenero(pacienteDto.getGenero());
		}

		if(!paciente.getTelefone().equals(pacienteDto.getTelefone())) {
			paciente.setTelefone(pacienteDto.getTelefone());
		}

		if(!paciente.getInformacoesAdicionais().equals(pacienteDto.getInformacoesAdicionais())) {
			paciente.setInformacoesAdicionais(pacienteDto.getInformacoesAdicionais());
		}

		if(!paciente.getLogradouro().equals(pacienteDto.getLogradouro())) {
			paciente.setLogradouro(pacienteDto.getLogradouro());
		}

		if(!paciente.getBairro().equals(pacienteDto.getBairro())) {
			paciente.setBairro(pacienteDto.getBairro());
		}

		if(!paciente.getNumero().equals(pacienteDto.getNumero())) {
			paciente.setNumero(pacienteDto.getNumero());
		}

		if(!paciente.getBloco().equals(pacienteDto.getBloco())) {
			paciente.setBloco(pacienteDto.getBloco());
		}

		if(!paciente.getParentesco().equals(pacienteDto.getParentesco())) {
			paciente.setParentesco(pacienteDto.getParentesco());
		}

		setarResponsavel(paciente, pacienteDto);
	}

	private void setarResponsavel(Paciente paciente, PacienteDto pacienteDto) {
		Long responsavelId = pacienteDto.getResponsavel_paciente_id();
		if(responsavelId != null && responsavelId != 0) {
			paciente.setResponsavelPacienteId(getPaciente(pacienteDto.getResponsavel_paciente_id()));
		}
	}

	private Paciente getPaciente(Long pacienteId) {
		return pacienteRepository.findById(pacienteId).get();
	}

}
