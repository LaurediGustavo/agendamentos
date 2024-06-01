package br.com.tcc.impl;

import br.com.tcc.dto.FuncionarioDto;
import br.com.tcc.dto.UsuarioProfileDTO;
import br.com.tcc.email.EmailService;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.Role;
import br.com.tcc.entity.Ususario;
import br.com.tcc.enumerador.RoleNameEnum;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.RoleRepository;
import br.com.tcc.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uteis.Uteis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("UsuarioService")
public class UsuarioService {

    private final Integer TAMANHO_DA_SENHA = 12;

    private final String UPLOAD_DIR = "C:/img/";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Transactional(rollbackOn = Exception.class)
    public void cadastrar(Funcionario funcionario) {
        Ususario usuario = createUser(funcionario);
        userRepository.save(usuario);

        emailService.sendSimpleMessage(usuario);
    }

    private Ususario createUser(Funcionario funcionario) {
        Ususario usuario = new Ususario();
        usuario.setFuncionario(funcionario);
        usuario.setUserName(Uteis.removerCaracteresNaoNumericos(funcionario.getCpf()));
        usuario.setEmail(funcionario.getEmail());

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

    public void alterarSenha(String senhaNova) {
        Ususario user = getUsuarioLogado();
        user.setPassword(passwordEncoder.encode(senhaNova));
        userRepository.save(user);
    }

    public void alterarImagem(MultipartFile image) throws IOException {
        removerImagem();

        Path directory = Paths.get(UPLOAD_DIR);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Ususario ususario = getUsuarioLogado();

        String nome = ususario.getUsername() + "." + Uteis.getFileExtension(image.getOriginalFilename());

        Path filePath = Paths.get(UPLOAD_DIR, nome);
        Files.write(filePath, image.getBytes());

        ususario.setImagePath(filePath.toString());
        userRepository.save(ususario);
    }

    public void removerImagem() {
        Ususario ususario = getUsuarioLogado();

        if (StringUtils.isNotBlank(ususario.getImagePath())) {
            File file = new File(ususario.getImagePath());
            if (file.exists()) {
                boolean deleted = file.delete();

                if (deleted) {
                    ususario.setImagePath(null);
                    userRepository.save(ususario);
                }
            }
        }
    }

    private Ususario getUsuarioLogado() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(userName).get();
    }

    public void alterar(UsuarioProfileDTO usuarioProfileDTO) {
        Funcionario funcionario = getFuncionario(usuarioProfileDTO);
        funcionarioRepository.save(funcionario);
    }

    private Funcionario getFuncionario(UsuarioProfileDTO usuarioProfileDTO) {
        Funcionario funcionario = funcionarioRepository.findById(usuarioProfileDTO.getId()).get();
        funcionario.setNome(usuarioProfileDTO.getNome());
        funcionario.setSobrenome(usuarioProfileDTO.getSobrenome());
        funcionario.setDataDeNascimento(usuarioProfileDTO.getDataDeNascimento());
        funcionario.setGenero(usuarioProfileDTO.getGenero());
        funcionario.setTelefone(usuarioProfileDTO.getTelefone());
        funcionario.setLogradouro(usuarioProfileDTO.getLogradouro());
        funcionario.setBairro(usuarioProfileDTO.getBairro());
        funcionario.setNumero(usuarioProfileDTO.getNumero());
        funcionario.setCep(usuarioProfileDTO.getCep());
        funcionario.setBloco(usuarioProfileDTO.getBloco());
        funcionario.setEmail(usuarioProfileDTO.getEmail());

        return funcionario;
    }

}
