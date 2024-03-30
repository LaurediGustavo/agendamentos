package br.com.tcc.service;

import br.com.tcc.entity.Paciente;
import br.com.tcc.impl.PacienteService;
import br.com.tcc.model.response.PacienteAgendamentoResponse;
import br.com.tcc.model.response.PacienteResponse;
import br.com.tcc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteTratarResponse {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<PacienteResponse> consultarPorCpfNome(String cpf, String nome, Boolean desabilitado) {
        Optional<List<Paciente>> optionalPacienteList = null;

        if (!Boolean.TRUE.equals(desabilitado)) {
            optionalPacienteList = pacienteRepository.findByCpfNomeHabilitado(cpf, nome);
        }
        else {
            optionalPacienteList = pacienteRepository.findByCpfNomeDesabilitado(cpf, nome);
        }

        List<PacienteResponse> pacienteResponseList = optionalPacienteList
                .map(pacientes -> pacientes.stream()
                        .map(this::montarPacienteResponse)
                        .collect(Collectors.toList()))
                .orElse(null);

        return pacienteResponseList;
    }

    public List<PacienteAgendamentoResponse> consultarPorNome(String nome) {
        Optional<List<Paciente>> optionalPacienteList = pacienteRepository.findByNome(nome);

        List<PacienteAgendamentoResponse> pacienteResponseList = optionalPacienteList
                .map(pacientes -> pacientes.stream()
                        .map(this::montarPacienteAgendamentoResponse)
                        .collect(Collectors.toList()))
                .orElse(null);

        return pacienteResponseList;
    }

    private PacienteAgendamentoResponse montarPacienteAgendamentoResponse(Paciente paciente) {
        if(paciente != null) {
            return new PacienteAgendamentoResponse(
                    paciente.getId(),
                    paciente.getNome(),
                    paciente.getSobrenome(),
                    paciente.getCpf(),
                    paciente.getTelefone()
            );
        }

        return null;
    }

    public PacienteResponse consultarPorId(Long id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);

        return pacienteOptional.map(this::montarPacienteResponse).orElse(null);
    }

    private PacienteResponse montarPacienteResponse(Paciente paciente) {
        if(paciente != null) {
            return new PacienteResponse(
                    paciente.getId(),
                    paciente.getNome(),
                    paciente.getSobrenome(),
                    paciente.getDataDeNascimento(),
                    paciente.getCpf(),
                    paciente.getGenero(),
                    paciente.getTelefone(),
                    paciente.getInformacoesAdicionais(),
                    paciente.getCep(),
                    paciente.getLogradouro(),
                    paciente.getBairro(),
                    paciente.getNumero(),
                    paciente.getBloco(),
                    paciente.getParentesco(),
                    montarPacienteResponse(paciente.getResponsavelPacienteId()));
        }

        return null;
    }
}
