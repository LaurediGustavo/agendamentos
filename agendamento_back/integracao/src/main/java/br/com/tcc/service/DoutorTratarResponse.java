package br.com.tcc.service;

import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.model.response.DoutorResponse;
import br.com.tcc.model.response.FuncionarioResponse;
import br.com.tcc.model.response.TipoFuncionarioResponse;
import br.com.tcc.repository.DoutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoutorTratarResponse {

    @Autowired
    private DoutorRepository doutorRepository;

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
                    doutor.getCro()
            );
        }

        return func;
    }

    private FuncionarioResponse getFuncionarioResponse(Doutor doutor) {
        return new FuncionarioResponse(
                doutor.getId(),
                doutor.getNome(),
                doutor.getSobrenome(),
                doutor.getIdade(),
                doutor.getCpf(),
                doutor.getGenero(),
                doutor.getTelefone(),
                doutor.getLogradouro(),
                doutor.getBairro(),
                doutor.getNumero(),
                doutor.getBloco(),
                getTipoFuncionarioResponse(doutor.getTipoFuncionario())
        );
    }

    private TipoFuncionarioResponse getTipoFuncionarioResponse(TipoFuncionario tipoFuncionario) {
        return new TipoFuncionarioResponse(
                tipoFuncionario.getId(),
                tipoFuncionario.getNome()
        );
    }

}
