package br.com.tcc.validation.agendamento.impl;

import br.com.tcc.entity.Consulta;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.validation.agendamento.interfaces.ValidAgendamentoEstendido;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ValidAgendamentoEstendidoValidator implements ConstraintValidator<ValidAgendamentoEstendido, Long> {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(value == null || value == 0) {
            return true;
        }
        else {
            Optional<Consulta> consultaOptional = consultaRepository.findById(value);

            if(consultaOptional.isPresent()) {
                Consulta consulta = consultaOptional.get();

                if(StatusConsultaEnum.EM_ANDAMENTO.equals(consulta.getStatus())) {
                    return true;
                }
                else {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{agendamento.agendamentoEstendido.statusInvalido}")
                            .addConstraintViolation();
                }
            }
        }

        return false;
    }

}
