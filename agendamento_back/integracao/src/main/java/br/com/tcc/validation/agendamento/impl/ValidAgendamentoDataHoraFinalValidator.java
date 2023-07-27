package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.model.AgendamentoRequest;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDataHoraFinal;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoDoutor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidAgendamentoDataHoraFinalValidator implements ConstraintValidator<ValidAgendamentoDataHoraFinal, AgendamentoRequest> {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public boolean isValid(AgendamentoRequest value, ConstraintValidatorContext context) {
        if (value.getDataHoraInicio() != null) {
            LocalDateTime dataIncio = value.getDataHoraInicio();
            LocalDateTime dataFinal = value.getDataHoraFim();

            if (dataFinal.isAfter(dataIncio)) {
                boolean disponivel = validarHorario(dataIncio, dataFinal, value.getDoutorId());

                if(!disponivel) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{agendamento.horarioIndisponivel}")
                            .addConstraintViolation();
                }

                return disponivel;
            }
        }

        return false;
    }

    private boolean validarHorario(LocalDateTime dataInicio, LocalDateTime dataFinal, Long idDoutor) {
        Long possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, idDoutor).get();
        if(possuiAgendamento > 0) {
            return false;
        }

        possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataFinal, idDoutor).get();
        if(possuiAgendamento > 0) {
            return false;
        }

        return true;
    }

}
