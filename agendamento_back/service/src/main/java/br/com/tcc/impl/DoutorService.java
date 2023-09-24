package br.com.tcc.impl;

import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.repository.DoutorRepository;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.TipoFuncionarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("DoutorService")
public class DoutorService {

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private TipoFuncionarioRepository tipoFuncionarioRepository;

    public void cadastrar(FuncionarioDto funcionarioDto) {
        Doutor doutor = gerarDoutor(funcionarioDto);
        doutorRepository.save(doutor);
    }

    private Doutor gerarDoutor(FuncionarioDto funcionarioDto) {
        Doutor doutor = new Doutor();

        doutor.setNome(funcionarioDto.getNome());
        doutor.setSobrenome(funcionarioDto.getSobrenome());
        doutor.setIdade(funcionarioDto.getIdade());
        doutor.setCpf(funcionarioDto.getCpf());
        doutor.setGenero(funcionarioDto.getGenero());
        doutor.setTelefone(funcionarioDto.getTelefone());
        doutor.setLogradouro(funcionarioDto.getLogradouro());
        doutor.setBairro(funcionarioDto.getBairro());
        doutor.setNumero(funcionarioDto.getNumero());
        doutor.setBloco(funcionarioDto.getBloco());
        doutor.setTipoFuncionario(getTipoFuncionario(funcionarioDto.getTipo_funcionario_id()));
        doutor.setCro(funcionarioDto.getCro());

        return doutor;
    }

    public void atualizar(FuncionarioDto funcionarioDto) {
        Doutor doutor = atualizarDoutor(funcionarioDto);
        doutorRepository.save(doutor);
    }

    private Doutor atualizarDoutor(FuncionarioDto funcionarioDto) {
        return doutorRepository.findById(funcionarioDto.getId())
                .map(doutor -> new Doutor(
                        funcionarioDto.getId(),
                        funcionarioDto.getNome(),
                        funcionarioDto.getSobrenome(),
                        funcionarioDto.getIdade(),
                        funcionarioDto.getCpf(),
                        funcionarioDto.getGenero(),
                        funcionarioDto.getTelefone(),
                        funcionarioDto.getLogradouro(),
                        funcionarioDto.getBairro(),
                        funcionarioDto.getNumero(),
                        funcionarioDto.getBloco(),
                        getTipoFuncionario(funcionarioDto.getTipo_funcionario_id()),
                        funcionarioDto.getCro()
                ))
                .orElse(null);
    }

    private TipoFuncionario getTipoFuncionario(Long id) {
        return tipoFuncionarioRepository.findById(id).get();
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

}
