package br.com.tcc.service;

import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.model.response.FuncionarioResponse;
import br.com.tcc.model.response.TipoFuncionarioResponse;
import br.com.tcc.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuncionarioTratarResponse {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<FuncionarioResponse> consultarPorNome(String nome, Boolean desabilitado) {
        Optional<List<Funcionario>> funcOptionalList = null;

        if (!Boolean.TRUE.equals(desabilitado)) {
            funcOptionalList = funcionarioRepository
                    .findByNomeContainingAndHabilitado(nome);
        }
        else {
            funcOptionalList = funcionarioRepository
                    .findByNomeContainingAndDesabilitado(nome);
        }

        List<FuncionarioResponse> funcionarioResponseList = funcOptionalList
                .map(funcionario -> funcionario.stream()
                        .map(this::montarFuncionarioResponse)
                        .collect(Collectors.toList()))
                .orElse(null);

        return funcionarioResponseList;
    }


    public FuncionarioResponse consultarPorId(Long id) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);

        return funcionarioOptional.map(this::montarFuncionarioResponse).orElse(null);
    }

    private FuncionarioResponse montarFuncionarioResponse(Funcionario funcionario) {
        FuncionarioResponse func = null;

        if(funcionario != null) {
            func = new FuncionarioResponse(
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getSobrenome(),
                    funcionario.getDataDeNascimento(),
                    funcionario.getCpf(),
                    funcionario.getGenero(),
                    funcionario.getTelefone(),
                    funcionario.getCep(),
                    funcionario.getLogradouro(),
                    funcionario.getBairro(),
                    funcionario.getNumero(),
                    funcionario.getBloco(),
                    getTipoFuncionarioResponse(funcionario.getTipoFuncionario()),
                    funcionario.getEmail()
            );
        }

        return func;
    }

    private TipoFuncionarioResponse getTipoFuncionarioResponse(TipoFuncionario tipoFuncionario) {
        return new TipoFuncionarioResponse(
                tipoFuncionario.getId(),
                tipoFuncionario.getNome()
        );
    }

}
