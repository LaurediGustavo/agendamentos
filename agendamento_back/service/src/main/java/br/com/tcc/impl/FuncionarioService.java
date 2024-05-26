package br.com.tcc.impl;

import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.email.EmailService;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.TipoFuncionario;
import br.com.tcc.repository.DoutorRepository;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.TipoFuncionarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uteis.Uteis;

@Service("FuncionarioService")
public class FuncionarioService {

    private final String ATENDENTE = "ATENDENTE";

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private TipoFuncionarioRepository tipoFuncionarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Long cadastrar(FuncionarioDto funcionarioDto) {
        Funcionario funcionario = gerarFuncionario(funcionarioDto);
        funcionarioRepository.save(funcionario);

        usuarioService.cadastrar(funcionario);

        return funcionario.getId();
    }

    private Funcionario gerarFuncionario(FuncionarioDto funcionarioDto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(funcionarioDto.getNome());
        funcionario.setSobrenome(funcionarioDto.getSobrenome());
        funcionario.setDataDeNascimento(funcionarioDto.getDataDeNascimento());
        funcionario.setCpf(Uteis.removerCaracteresNaoNumericos(funcionarioDto.getCpf()));
        funcionario.setGenero(funcionarioDto.getGenero());
        funcionario.setTelefone(funcionarioDto.getTelefone());
        funcionario.setLogradouro(funcionarioDto.getLogradouro());
        funcionario.setBairro(funcionarioDto.getBairro());
        funcionario.setNumero(funcionarioDto.getNumero());
        funcionario.setCep(funcionarioDto.getCep());
        funcionario.setBloco(funcionarioDto.getBloco());
        funcionario.setTipoFuncionario(getTipoFuncionario());
        funcionario.setEmail(funcionarioDto.getEmail());

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
                        funcionarioDto.getDataDeNascimento(),
                        Uteis.removerCaracteresNaoNumericos(funcionarioDto.getCpf()),
                        funcionarioDto.getGenero(),
                        funcionarioDto.getTelefone(),
                        funcionarioDto.getCep(),
                        funcionarioDto.getLogradouro(),
                        funcionarioDto.getBairro(),
                        funcionarioDto.getNumero(),
                        funcionarioDto.getBloco(),
                        getTipoFuncionario(),
                        funcionario.getDesabilitado(),
                        funcionarioDto.getEmail()
                ))
                .orElse(null);
    }

    private TipoFuncionario getTipoFuncionario() {
        return tipoFuncionarioRepository.findByNome(this.ATENDENTE);
    }

    public boolean existsById(Long id) {
        return funcionarioRepository.existsById(id);
    }

    public void deletar(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).get();
        funcionario.setDesabilitado(Boolean.TRUE);

        funcionarioRepository.save(funcionario);
    }

    public void revertDelete(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id).get();
        funcionario.setDesabilitado(Boolean.FALSE);

        funcionarioRepository.save(funcionario);
    }

    public boolean existsByCpf(String cpf) {
        return funcionarioRepository.existsByCpf(cpf);
    }

}
