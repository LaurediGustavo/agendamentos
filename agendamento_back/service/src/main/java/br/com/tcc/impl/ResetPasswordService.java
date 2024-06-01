package br.com.tcc.impl;

import br.com.tcc.email.EmailService;
import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.Ususario;
import br.com.tcc.exception.ConsistirException;
import br.com.tcc.repository.FuncionarioRepository;
import br.com.tcc.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Service
public class ResetPasswordService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendEmail(String email) throws Exception {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByEmail(email);

        String code = String.format("%05d", new Random().nextInt(100000));

        funcionarioOptional.ifPresentOrElse(
                funcionario -> emailService.sendSimpleMessage(funcionario.getEmail(), "Alteração de Senha", getBodyEmail(funcionario, code)),
                () -> { throw new RuntimeException("Funcionário não encontrado"); }
        );

        atualizarFuncionarioComCodigo(funcionarioOptional, code);
    }

    private void atualizarFuncionarioComCodigo(Optional<Funcionario> funcionarioOptional, String codigo) {
        Funcionario func = funcionarioOptional.get();
        func.setCodigoEmail(montarCodigoParaFuncionario(codigo));

        funcionarioRepository.save(func);
    }

    private String getBodyEmail(Funcionario x, String code) {
        StringBuilder sb = new StringBuilder();
        sb.append("Olá, ").append(x.getNome()).append(" ").append(x.getSobrenome());
        sb.append("\n\n");
        sb.append("Código para alterar a senha: ").append(code);
        sb.append("\n\n");
        sb.append("O código tem validade de 5 minutos.");

        return sb.toString();
    }

    private String montarCodigoParaFuncionario(String codigo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        return codigo + "|" + formattedDate + "|" + 5;
    }

    public void validatorCode(String code, String email) throws ConsistirException {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByEmail(email);

        validarCodigo(code, funcionarioOptional.get().getCodigoEmail());
    }

    private void validarCodigo(String code, String codigoEmail) throws ConsistirException {
        boolean isValid = false;
        ConsistirException consistirException = new ConsistirException();

        if (StringUtils.isNotBlank(codigoEmail) && StringUtils.isNotBlank(code)) {
            String[] codigoEmailSplit = codigoEmail.split("\\|");

            if (codigoEmailSplit[0].equals(code)) {
                LocalDateTime tempoDeVidaDoToken = tempoDeVidaDoToken(codigoEmailSplit[1], codigoEmailSplit[2]);
                LocalDateTime agora = LocalDateTime.now();

                if (!agora.isAfter(tempoDeVidaDoToken)) {
                    isValid = true;
                }
            }
        }

        if (!isValid) {
            consistirException.addErroSimples("Código inválido!");
            throw consistirException;
        }
    }

    private LocalDateTime tempoDeVidaDoToken(String data, String tempo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime dataHora = LocalDateTime.parse(data, formatter);

        return dataHora.plusMinutes(Integer.parseInt(tempo));
    }

    public void restPassword(String email, String senhaNova) {
        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByEmail(email);

        Optional<Ususario> userOptional = userRepository.findByUserName(funcionarioOptional.get().getCpf());

        if (userOptional.isPresent()) {
            Ususario user = userOptional.get();
            user.setPassword(passwordEncoder.encode(senhaNova));

            userRepository.save(user);
        }
    }

}
