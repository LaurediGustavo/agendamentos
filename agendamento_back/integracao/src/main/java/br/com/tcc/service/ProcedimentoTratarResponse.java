package br.com.tcc.service;

import br.com.tcc.entity.Procedimento;
import br.com.tcc.model.response.ProcedimentoResponse;
import br.com.tcc.repository.ProcedimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcedimentoTratarResponse {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    public List<ProcedimentoResponse> consultarPorTratamento(String tratamento, Boolean desabilitado) {
        Optional<List<Procedimento>> optionalProcedimentoList = null;

        if (!Boolean.TRUE.equals(desabilitado)) {
            optionalProcedimentoList = procedimentoRepository
                    .findByTratamentoLikeHabilitado(tratamento);
        }
        else {
            optionalProcedimentoList = procedimentoRepository
                    .findByTratamentoLikeDesabilitado(tratamento);
        }

        return optionalProcedimentoList
                .map(procedimentos -> procedimentos.stream()
                        .map(this::montarProcedimento)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    public ProcedimentoResponse consultarPorId(Long id) {
        Optional<Procedimento> procedimento = procedimentoRepository.findById(id);

        return procedimento.map(this::montarProcedimento).orElse(null);
    }

    private ProcedimentoResponse montarProcedimento(Procedimento procedimento) {
        if(procedimento != null) {
            return new ProcedimentoResponse(
                    procedimento.getId(),
                    procedimento.getTratamento(),
                    procedimento.getTempo(),
                    procedimento.getValor()
            );
        }

        return null;
    }

}
