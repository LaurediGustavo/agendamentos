package br.com.tcc.email;

import br.com.tcc.entity.Ususario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(Ususario usuario) {
        String email = "agendamento466@gmail.com";
        String senha = "ckci jnlk ldkn litn";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(usuario.getEmail());
        message.setSubject("Senha de acesso do sistema de agendamento");
        message.setText(getText(usuario));
        emailSender.send(message);
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

}
