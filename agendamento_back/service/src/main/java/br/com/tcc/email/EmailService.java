package br.com.tcc.email;

import br.com.tcc.entity.Ususario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Async("emailTaskExecutor")
    public void sendSimpleMessage(Ususario usuario) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(usuario.getEmail());
            mensagem.setSubject("Senha do Sistema de Agendamentos");
            mensagem.setText(getText(usuario));

            emailSender.send(mensagem);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getText(Ususario ususario) {
        StringBuilder sb = new StringBuilder();

        sb.append("Olá, seu cadastro foi realizado com sucesso!")
                .append("\n\n")
                .append("Usuário: " + ususario.getUsername())
                .append("\n")
                .append("Senha: " + ususario.getPasswordSemCriptografia());

        return sb.toString();
    }

    @Async("emailTaskExecutor")
    public void sendSimpleMessage(String to, String subject, String body) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setTo(to);
            mensagem.setSubject(subject);
            mensagem.setText(body);

            emailSender.send(mensagem);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
