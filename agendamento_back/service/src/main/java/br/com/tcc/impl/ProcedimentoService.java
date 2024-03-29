package br.com.tcc.impl;

import br.com.tcc.dto.ProcedimentoDto;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.repository.ProcedimentoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("ProcedimentoService")
public class ProcedimentoService {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    public Long cadastrar(ProcedimentoDto procedimentoDto) {
        Procedimento procedimento = gerarProcedimento(procedimentoDto);
        procedimentoRepository.save(procedimento);
        return procedimento.getId();
    }

    private Procedimento gerarProcedimento(ProcedimentoDto procedimentoDto) {
        Procedimento procedimento = new Procedimento();
        procedimento.setTratamento(procedimentoDto.getTratamento());
        procedimento.setTempo(procedimentoDto.getTempo());
        procedimento.setValor(procedimentoDto.getValor());
        procedimento.setDesabilitado(procedimentoDto.getDesabilitado() != null && procedimentoDto.getDesabilitado());

        return procedimento;
    }

    public void atualizar(ProcedimentoDto procedimentoDto) {
        Procedimento procedimento = gerarProcedimento(procedimentoDto.getId());
        atualizarCampos(procedimento, procedimentoDto);
        procedimentoRepository.save(procedimento);
    }

    private void atualizarCampos(Procedimento procedimento, ProcedimentoDto procedimentoDto) {
        if (StringUtils.isNotBlank(procedimentoDto.getTratamento())) {
            procedimento.setTratamento(procedimentoDto.getTratamento());
        }

        if (StringUtils.isNotBlank(procedimentoDto.getTempo())) {
            procedimento.setTempo(procedimentoDto.getTempo());
        }

        if (procedimentoDto.getValor() != null && !BigDecimal.ZERO.equals(procedimentoDto.getValor())) {
            procedimento.setValor(procedimentoDto.getValor());
        }

        procedimento.setDesabilitado(procedimentoDto.getDesabilitado() != null && procedimentoDto.getDesabilitado());
    }

    private Procedimento gerarProcedimento(Long procedimentoId) {
        return procedimentoRepository.findById(procedimentoId).get();
    }

    public void deletar(Long id) {
        Procedimento procedimento = gerarProcedimento(id);
        procedimento.setDesabilitado(Boolean.TRUE);

        procedimentoRepository.save(procedimento);
    }

    public void revertDelete(Long id) {
        Procedimento procedimento = gerarProcedimento(id);
        procedimento.setDesabilitado(Boolean.FALSE);

        procedimentoRepository.save(procedimento);
    }

    public boolean existsById(Long id) {
        return procedimentoRepository.existsById(id);
    }
}

