package br.com.tcc.service;

import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.impl.AgendamentoService;
import br.com.tcc.model.response.AgendamentoResponse;
import br.com.tcc.model.response.ProcedimentoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
                .map(this::montarAgendamentoResponse).collect(Collectors.toList())).orElse(null);
    }

    public List<AgendamentoResponse> consultarPorStausPaciente(Long pacienteId, StatusConsultaEnum status) {
        Optional<List<Consulta>> consultaOptional = agendamentoService.consultarPorStatusPaciente(pacienteId, status);

        return consultaOptional.map(consultas -> consultas.stream()
                .map(this::montarAgendamentoResponse).collect(Collectors.toList())).orElse(null);
    }

    public AgendamentoResponse consultarPorId(Long id) {
        Optional<Consulta> consulta = agendamentoService.consultarPorId(id);

        return consulta.map(this::montarAgendamentoResponse
        ).orElse(null);
    }

    private AgendamentoResponse montarAgendamentoResponse(Consulta agendamento) {
        if (agendamento != null)
            return new AgendamentoResponse(
                    agendamento.getId(),
                    agendamento.getStatus().name(),
                    agendamento.getDataHoraInicio(),
                    agendamento.getDataHoraFinal(),
                    agendamento.getPaciente().getId(),
                    agendamento.getPaciente().getNome(),
                    agendamento.getDoutor().getId(),
                    getProcedimentos(agendamento),
                    agendamento.getValorTotal(),
                    agendamento.getTempoAproximado(),
                    montarAgendamentoResponse(agendamento.getConsultaRemarcadaPara()));

        return null;
    }

    public List<ProcedimentoResponse> getProcedimentos(Consulta consulta) {
        List<Procedimento> procedimentos = consulta.getProcedimentos();

        if (consulta.getConsultasEstendidasDe() != null) {
            consulta.getConsultasEstendidasDe().stream()
                    .flatMap(x -> x.getProcedimentos().stream())
                    .forEach(procedimentos::add);
        }

        return procedimentos.stream().map(procedimento -> new ProcedimentoResponse(
                procedimento.getId(),
                procedimento.getTratamento(),
                procedimento.getTempo(),
                procedimento.getValor()
        )).collect(Collectors.toList());
    }
}
