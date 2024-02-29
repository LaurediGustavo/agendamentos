package br.com.tcc.service;

import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.impl.AgendamentoService;
import br.com.tcc.impl.DoutorService;
import br.com.tcc.model.response.*;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.DoutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uteis.DataUteis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoutorTratarResponse {

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private DoutorService doutorService;

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<DoutorResponse> consultarPorNome(String nome) {
        Optional<List<Doutor>> optionalDoutorList = doutorRepository
                .findByNomeContaining(nome);

        List<DoutorResponse> doutorResponseList = optionalDoutorList
                .map(doutor -> doutor.stream()
                        .map(this::montarDoutorResponse)
                        .collect(Collectors.toList()))
                .orElse(null);

        return doutorResponseList;
    }


    public DoutorResponse consultarPorId(Long id) {
        Optional<Doutor> funcionarioOptional = doutorRepository.findById(id);

        return funcionarioOptional.map(this::montarDoutorResponse).orElse(null);
    }

    private DoutorResponse montarDoutorResponse(Doutor doutor) {
        DoutorResponse func = null;

        if(doutor != null) {
            func = new DoutorResponse(
                    doutor.getId(),
                    getFuncionarioResponse(doutor),
                    doutor.getCro(),
                    getProcedimentos(doutor)
            );
        }

        return func;
    }

    private List<ProcedimentoResponse> getProcedimentos(Doutor doutor) {
        return doutor.getProcedimentos().stream()
                .map(procedimento -> new ProcedimentoResponse(
                        procedimento.getId(),
                        procedimento.getTratamento(),
                        procedimento.getTempo(),
                        procedimento.getValor()
                ))
                .collect(Collectors.toList());
    }

    private FuncionarioResponse getFuncionarioResponse(Doutor doutor) {
        return new FuncionarioResponse(
                doutor.getId(),
                doutor.getNome(),
                doutor.getSobrenome(),
                doutor.getDataDeNascimento(),
                doutor.getCpf(),
                doutor.getGenero(),
                doutor.getTelefone(),
                doutor.getCep(),
                doutor.getLogradouro(),
                doutor.getBairro(),
                doutor.getNumero(),
                doutor.getBloco(),
                getTipoFuncionarioResponse(doutor.getTipoFuncionario()),
                doutor.getEmail()
        );
    }

    private TipoFuncionarioResponse getTipoFuncionarioResponse(TipoFuncionario tipoFuncionario) {
        return new TipoFuncionarioResponse(
                tipoFuncionario.getId(),
                tipoFuncionario.getNome()
        );
    }

    public List<DoutorAgendamentoResponse> consultarPorNomeEProcedimento(String nome, Long[] procedimentos) {
        Optional<List<Doutor>> optionalDoutorList = doutorRepository
                .findByNomeAndProcedimentoId(nome, procedimentos);

        doutorService.validarDoutoresPorProcedimento(optionalDoutorList, procedimentos);

        return optionalDoutorList.map(doutores -> doutores.stream()
                .map(this::montarDoutorAgendamentoResponse)
                .collect(Collectors.toList()))
                .orElse(null);
    }

    private DoutorAgendamentoResponse montarDoutorAgendamentoResponse(Doutor doutor) {
        if(doutor != null) {
            return new DoutorAgendamentoResponse(
                    doutor.getId(),
                    doutor.getNome(),
                    doutor.getSobrenome()
            );
        }

        return null;
    }

    public List<HorariosDoutorResponse> consultarHorariosDoutor(Long doutorId, String data) {
        LocalDate dataHora = DataUteis.getLocalDate(data);

        Optional<List<Consulta>> consultas = this.consultaRepository.findByConsultasByDoutorDias(doutorId,
                dataHora.getYear(), dataHora.getMonthValue(), dataHora.getDayOfMonth());

        return consultas.map(consulta -> consulta.stream()
                .map(this::montarDatasAgendamentos)
                .collect(Collectors.toList()))
                .orElse(null);
    }

    private HorariosDoutorResponse montarDatasAgendamentos(Consulta consulta) {
        if(consulta != null) {
            return new HorariosDoutorResponse(
                    consulta.getDoutor().getId(),
                    consulta.getDataHoraInicio(),
                    consulta.getDataHoraFinal());
        }

        return null;
    }

}
