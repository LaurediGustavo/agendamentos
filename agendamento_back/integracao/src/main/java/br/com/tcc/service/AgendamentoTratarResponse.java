package br.com.tcc.service;

import br.com.tcc.entity.Consulta;
import br.com.tcc.impl.AgendamentoService;
import br.com.tcc.model.response.AgendamentoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoTratarResponse {

    @Autowired
    private AgendamentoService agendamentoService;

    public List<AgendamentoResponse> consultarPorHorarioDoutorPaciente(Long doutorId, Long pacienteId, String horario) {
        Optional<List<Consulta>> consultaOptional = agendamentoService.consultarPorHorarioDoutorPaciente(doutorId, pacienteId, horario);

        return consultaOptional.map(consultas -> consultas.stream()
                .map(agendamento -> new AgendamentoResponse(
                    agendamento.getId(),
                    agendamento.getStatus().name(),
                    agendamento.getDataHoraInicio(),
                    agendamento.getDataHoraFinal(),
                    agendamento.getPaciente().getId(),
                    agendamento.getDoutor().getId(),
                null
                )).collect(Collectors.toList())).orElse(null);
    }

    public AgendamentoResponse consultarPorId(Long id) {
        Optional<Consulta> consulta = agendamentoService.consultarPorId(id);

        return consulta.map(agendamento -> new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getStatus().name(),
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFinal(),
                agendamento.getPaciente().getId(),
                agendamento.getDoutor().getId(),
                null
        )).orElse(null);
    }
}
