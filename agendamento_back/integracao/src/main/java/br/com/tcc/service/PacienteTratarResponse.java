package br.com.tcc.service;

import br.com.tcc.entity.Paciente;
import br.com.tcc.impl.PacienteService;
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

    public List<PacienteResponse> consultarPorCpfNome(String cpf, String nome) {
        Optional<List<Paciente>> optionalPacienteList = pacienteRepository.findByCpfNome(cpf, nome);


        List<PacienteResponse> pacienteResponseList = optionalPacienteList
                .map(pacientes -> pacientes.stream()
                        .map(this::montarPacienteResponse)
                        .collect(Collectors.toList()))
                .orElse(null);

        return pacienteResponseList;
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
                    paciente.getIdade(),
                    paciente.getCpf(),
                    paciente.getGenero(),
                    paciente.getTelefone(),
                    paciente.getInformacoesAdicionais(),
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
