package br.com.tcc.service;

import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.impl.AgendamentoService;
import br.com.tcc.model.response.AgendamentoResponse;
import br.com.tcc.model.response.ProcedimentoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
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
                    tempoTotalAproximado(agendamento),
                    montarAgendamentoResponse(agendamento.getConsultaRemarcadaPara()),
                    getConsultaEstendida(agendamento.getConsultasEstendidasDe()));

        return null;
    }

    private LocalTime tempoTotalAproximado(Consulta consulta) {
        Duration duration1 = Duration.between(LocalTime.MIDNIGHT, LocalTime.of(0, 0));

        if (consulta.getConsultasEstendidasDe() != null &&
                !consulta.getConsultasEstendidasDe().isEmpty()) {
            duration1 = Duration.between(LocalTime.MIDNIGHT, consulta.getConsultasEstendidasDe().get(0).getTempoAproximado());
        }
        Duration duration2 = Duration.between(LocalTime.MIDNIGHT, consulta.getTempoAproximado());

        Duration totalDuration = duration1.plus(duration2);

        return LocalTime.MIDNIGHT.plus(totalDuration);
    }

    private AgendamentoResponse getConsultaEstendida(List<Consulta> agendamento) {
        return agendamento != null? agendamento.size() > 0? montarAgendamentoResponse(agendamento.get(0)) : null : null;
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
