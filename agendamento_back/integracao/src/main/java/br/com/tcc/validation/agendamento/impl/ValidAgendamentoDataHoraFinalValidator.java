package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.entity.Consulta;
import br.com.tcc.model.request.AgendamentoRequest;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDataHoraFinal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class ValidAgendamentoDataHoraFinalValidator implements ConstraintValidator<ValidAgendamentoDataHoraFinal, AgendamentoRequest> {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public boolean isValid(AgendamentoRequest value, ConstraintValidatorContext context) {
        if (value.getDataHoraInicio() != null) {
            LocalDateTime dataIncio = value.getDataHoraInicio();
            LocalDateTime dataFinal = value.getDataHoraFim();

            if (isDataValida(dataIncio, dataFinal, context)) {
                boolean disponivel = validarHorarioDoutor(dataIncio, dataFinal, value);

                if(!disponivel) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{agendamento.horarioIndisponivel.doutor}")
                            .addConstraintViolation();
                }

                disponivel = validarHorarioPaciente(dataIncio, dataFinal, value);
                if(!disponivel) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{agendamento.horarioIndisponivel.paciente}")
                            .addConstraintViolation();
                }

                return disponivel;
            }
        }

        return false;
    }

    private boolean isDataValida(LocalDateTime dataIncio, LocalDateTime dataFinal, ConstraintValidatorContext context) {
        LocalDateTime dataCorrente = LocalDateTime.now();

        if(dataCorrente.isAfter(dataIncio)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{agendamento.dataHoraInicio.menorQueDataCorrente}")
                    .addConstraintViolation();
            return false;
        }

        if(dataFinal.isBefore(dataIncio)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{agendamento.dataHoraFinal.menorQueDataInicial}")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean validarHorarioPaciente(LocalDateTime dataInicio, LocalDateTime dataFinal, AgendamentoRequest agendamento) {
        if(agendamento.getId() == null || agendamento.getId() == 0 || mudancaDeData(agendamento, dataInicio, dataFinal)) {
            Long possuiAgendamento = consultaRepository.consultarPorDataEPaciente(dataInicio, dataFinal, agendamento.getPacienteId(), agendamento.getId()).get();
            if(possuiAgendamento > 0) {
                return false;
            }

            possuiAgendamento = consultaRepository.consultarPorDataEPaciente(dataInicio, dataFinal, agendamento.getPacienteId(), agendamento.getId()).get();
            if(possuiAgendamento > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean validarHorarioDoutor(LocalDateTime dataInicio, LocalDateTime dataFinal, AgendamentoRequest agendamento) {
        if(agendamento.getId() == null || agendamento.getId() == 0 || mudancaDeData(agendamento, dataInicio, dataFinal)) {
            Long possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, dataFinal, agendamento.getDoutorId(), agendamento.getId()).get();
            if(possuiAgendamento > 0) {
                return false;
            }

            possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, dataFinal, agendamento.getDoutorId(), agendamento.getId()).get();
            if(possuiAgendamento > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean mudancaDeData(AgendamentoRequest agendamentoRequest, LocalDateTime dataInicio, LocalDateTime dataFinal) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(agendamentoRequest.getId());

        if(consultaOptional.isPresent()) {
            Consulta consulta = consultaOptional.get();
            if(!consulta.getDataHoraInicio().equals(dataInicio) ||
                    !consulta.getDataHoraFinal().equals(dataFinal)) {
                return true;
            }
        }

        return false;
    }

}
