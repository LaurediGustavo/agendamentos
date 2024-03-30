package br.com.tcc.chatbot.desativarnotificacoes.passos;

import br.com.tcc.chatbot.desativarnotificacoes.enumerador.DesativarNotificacoesPassosEnum;
import br.com.tcc.chatbot.desativarnotificacoes.interfaces.DesativarNotificacoesPassosInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Paciente;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DesativarNotificacoesPassoDois implements DesativarNotificacoesPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MenuChatBot menuChatBot;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem) && cpfUtilizado(mensagem)) {
            adicionarChatIdNoPaciente(mensagem);
            atualizarMonitor(monitorDeChatBot, StatusDaMensagemChatBotEnum.FINALIZADO);
            List<SendMessage> messages = montarMensagem(message.getChatId(), "Notificações desativadas com sucesso!\n\n");
            messages.add(menuChatBot.getMenu(message.getChatId()));
            return messages;
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido, nao cadastrado ou bloqueado! Por favor informe seu CPF ou entre em contato com a Clinica.");
        }
    }

    private void adicionarChatIdNoPaciente(String mensagem) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findByCpf(mensagem);

        if (pacienteOptional.isPresent()) {
            Paciente paciente = pacienteOptional.get();
            paciente.setChatId(null);

            pacienteRepository.save(paciente);
        }
    }

    private boolean cpfUtilizado(String mensagem) {
        return pacienteRepository
                .findByCpf(Uteis.removerCaracteresNaoNumericos(mensagem)).isPresent();
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot, StatusDaMensagemChatBotEnum status) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(3);
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(status);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    @Override
    public DesativarNotificacoesPassosEnum getPasso() {
        return DesativarNotificacoesPassosEnum.PASSO_DOIS;
    }

}
