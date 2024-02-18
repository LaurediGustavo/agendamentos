package br.com.tcc.impl;

import br.com.tcc.dto.DoutorDto;
import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.entity.*;
import br.com.tcc.repository.DoutorRepository;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.ProcedimentoRepository;
import br.com.tcc.repository.TipoFuncionarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("DoutorService")
public class DoutorService {

    private final String DOUTOR = "DOUTOR";

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private TipoFuncionarioRepository tipoFuncionarioRepository;

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    public Long cadastrar(DoutorDto doutorDto) {
        Doutor doutor = gerarDoutor(doutorDto);
        doutorRepository.save(doutor);
        return doutor.getId();
    }

    private Doutor gerarDoutor(DoutorDto doutorDto) {
        Doutor doutor = new Doutor();

        doutor.setNome(doutorDto.getFuncionario().getNome());
        doutor.setSobrenome(doutorDto.getFuncionario().getSobrenome());
        doutor.setDataDeNascimento(doutorDto.getFuncionario().getDataDeNascimento());
        doutor.setCpf(doutorDto.getFuncionario().getCpf());
        doutor.setGenero(doutorDto.getFuncionario().getGenero());
        doutor.setTelefone(doutorDto.getFuncionario().getTelefone());
        doutor.setLogradouro(doutorDto.getFuncionario().getLogradouro());
        doutor.setBairro(doutorDto.getFuncionario().getBairro());
        doutor.setNumero(doutorDto.getFuncionario().getNumero());
        doutor.setBloco(doutorDto.getFuncionario().getBloco());
        doutor.setTipoFuncionario(getTipoFuncionario());
        doutor.setCro(doutorDto.getCro());
        doutor.setCep(doutorDto.getFuncionario().getCep());
        doutor.setProcedimentos(getProcedimentos(doutorDto.getProcedimentos()));

        return doutor;
    }

    public void atualizar(DoutorDto doutorDto) {
        Doutor doutor = atualizarDoutor(doutorDto);
        doutorRepository.save(doutor);
    }

    private Doutor atualizarDoutor(DoutorDto doutorDto) {
        return doutorRepository.findById(doutorDto.getId())
                .map(doutor -> {
                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getNome()))
                        doutor.setNome(doutorDto.getFuncionario().getNome());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getSobrenome()))
                        doutor.setSobrenome(doutorDto.getFuncionario().getSobrenome());

                    if (doutorDto.getFuncionario().getDataDeNascimento() != null)
                        doutor.setDataDeNascimento(doutorDto.getFuncionario().getDataDeNascimento());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getCpf()))
                        doutor.setCpf(doutorDto.getFuncionario().getCpf());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getGenero()))
                        doutor.setGenero(doutorDto.getFuncionario().getGenero());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getCep()))
                        doutor.setCep(doutorDto.getFuncionario().getCep());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getTelefone()))
                        doutor.setTelefone(doutorDto.getFuncionario().getTelefone());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getLogradouro()))
                        doutor.setLogradouro(doutorDto.getFuncionario().getLogradouro());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getBairro()))
                        doutor.setBairro(doutorDto.getFuncionario().getBairro());

                    if (doutorDto.getFuncionario().getNumero() != null)
                        doutor.setNumero(doutorDto.getFuncionario().getNumero());

                    if (StringUtils.isNotBlank(doutorDto.getFuncionario().getBloco()))
                        doutor.setBloco(doutorDto.getFuncionario().getBloco());

                    if (StringUtils.isNotBlank(doutorDto.getCro()))
                        doutor.setCro(doutorDto.getCro());

                    if (doutorDto.getProcedimentos() != null)
                        doutor.setProcedimentos(getProcedimentos(doutorDto.getProcedimentos()));

                    return doutor;
                })
                .orElse(null);
    }

    private List<Procedimento> getProcedimentos(List<Long> procedimentosIds) {
        return procedimentosIds.stream().map(
                id -> procedimentoRepository.findById(id).get()
        ).collect(Collectors.toList());
    }

    private TipoFuncionario getTipoFuncionario() {
        return tipoFuncionarioRepository.findByNome(this.DOUTOR);
    }

    public void validarDoutoresPorProcedimento(Optional<List<Doutor>> optionalDoutorList, Long[] procedimentos) {
        if(optionalDoutorList.isPresent()) {
            List<Doutor> doutors = optionalDoutorList.get();
            List<Doutor> listaRetorno = new ArrayList<>();

            for (Doutor doutor : doutors) {
                List<Procedimento> procedimentoList = doutor.getProcedimentos();

                int qtdProcedimentos = 0;
                for (Procedimento procedimento : procedimentoList) {
                    for (Long procedimentoId : procedimentos) {
                        if(Objects.equals(procedimentoId, procedimento.getId())) {
                            qtdProcedimentos++;
                            break;
                        }
                    }
                }

                if(qtdProcedimentos == procedimentos.length) {
                    listaRetorno.add(doutor);
                }
            }

            doutors.clear();
            doutors.addAll(listaRetorno);
        }
    }

    public void deletar(Long id) {
        Doutor doutor = doutorRepository.findById(id).get();
        doutor.setDesabilitado(Boolean.TRUE);

        doutorRepository.save(doutor);
    }

    public boolean existsById(Long id) {
        return doutorRepository.existsById(id);
    }

}
