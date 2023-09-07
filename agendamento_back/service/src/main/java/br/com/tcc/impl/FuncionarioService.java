package br.com.tcc.impl;

import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.repository.DoutorRepository;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.TipoFuncionarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FuncionarioService")
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private TipoFuncionarioRepository tipoFuncionarioRepository;

    public void cadastrar(FuncionarioDto funcionarioDto) {
        Funcionario funcionario = gerarFuncionario(funcionarioDto);
        funcionarioRepository.save(funcionario);
    }

    private Funcionario gerarFuncionario(FuncionarioDto funcionarioDto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(funcionarioDto.getNome());
        funcionario.setSobrenome(funcionarioDto.getSobrenome());
        funcionario.setIdade(funcionarioDto.getIdade());
        funcionario.setCpf(funcionarioDto.getCpf());
        funcionario.setGenero(funcionarioDto.getGenero());
        funcionario.setTelefone(funcionarioDto.getTelefone());
        funcionario.setLogradouro(funcionarioDto.getLogradouro());
        funcionario.setBairro(funcionarioDto.getBairro());
        funcionario.setNumero(funcionarioDto.getNumero());
        funcionario.setBloco(funcionarioDto.getBloco());
        funcionario.setTipoFuncionario(getTipoFuncionario(funcionarioDto.getTipo_funcionario_id()));

        return funcionario;
    }

    public void atualizar(FuncionarioDto funcionarioDto) {
        Funcionario funcionario = atualizarFuncionario(funcionarioDto);
        funcionarioRepository.save(funcionario);
    }

    private Funcionario atualizarFuncionario(FuncionarioDto funcionarioDto) {
        return funcionarioRepository.findById(funcionarioDto.getId())
                .map(funcionario -> new Funcionario(
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
                        getTipoFuncionario(funcionarioDto.getTipo_funcionario_id())
                ))
                .orElse(null);
    }

    private TipoFuncionario getTipoFuncionario(Long id) {
        return tipoFuncionarioRepository.findById(id).get();
    }

}
