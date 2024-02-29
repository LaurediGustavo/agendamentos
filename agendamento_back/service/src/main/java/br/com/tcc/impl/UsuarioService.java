package br.com.tcc.impl;

import br.com.tcc.email.EmailService;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.Role;
import br.com.tcc.entity.Ususario;
import br.com.tcc.enumerador.RoleNameEnum;
import br.com.tcc.repository.RoleRepository;
import br.com.tcc.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service("UsuarioService")
public class UsuarioService {

    private final Integer TAMANHO_DA_SENHA = 12;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional(rollbackOn = Exception.class)
    public void cadastrar(Funcionario funcionario) {
        Ususario usuario = createUser(funcionario);
        userRepository.save(usuario);

        emailService.sendSimpleMessage(usuario);
    }

    private Ususario createUser(Funcionario funcionario) {
        Ususario usuario = new Ususario();
        usuario.setFuncionario(funcionario);
        usuario.setUserName(funcionario.getCpf());

        List<Role> roles = new ArrayList<>();
        roles.add(getRole(funcionario));
        usuario.setRoles(roles);

        preecherSenha(usuario);

        return usuario;
    }

    private void preecherSenha(Ususario usuario) {
        String senha = gerarSenha();
        String senhaCriptografada = passwordEncoder.encode(senha);

        usuario.setPassword(senhaCriptografada);
        usuario.setPasswordSemCriptografia(senha);
    }

    private Role getRole(Funcionario funcionario) {
        if (funcionario instanceof Doutor)
            return roleRepository.findByRoleName(RoleNameEnum.ROLE_DOUTOR);
        else
            return roleRepository.findByRoleName(RoleNameEnum.ROLE_ATENDENTE);
    }

    private String gerarSenha() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < TAMANHO_DA_SENHA; i++) {
            int index = random.nextInt(caracteres.length());
            senha.append(caracteres.charAt(index));
        }

        return senha.toString();
    }

}
