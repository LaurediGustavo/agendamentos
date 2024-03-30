package br.com.tcc.chatbot.cadastro.passos;

import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.chatbot.cadastro.interfaces.CadastroPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.PacienteChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CadastroPassoDois implements CadastroPassosInterface {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteChatBotRepository pacienteChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem)) {
            if(cpfNaoUtilizado(mensagem)) {
                atualizarMonitor(monitorDeChatBot, StatusDaMensagemChatBotEnum.AGUARDANDO);
                cadastrarPaciente(message);
                return montarMensagem(message.getChatId(), "Por favor informe seu nome e sobrenome");
            }
            else {
                return montarMensagem(message.getChatId(), "Esse CPF já está em uso. Por favor informe seu CPF");
            }
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido! Por favor informe seu CPF");
        }
    }

    private void cadastrarPaciente(Message message) {
        PacienteChatBot pacienteChatBot = new PacienteChatBot();
        pacienteChatBot.setCpf(Uteis.removerCaracteresNaoNumericos(message.getText()));
        pacienteChatBot.setChatId(message.getChatId());

        this.pacienteChatBotRepository.save(pacienteChatBot);
    }

    private boolean cpfNaoUtilizado(String mensagem) {
        return pacienteRepository
                .findByCpf(Uteis.removerCaracteresNaoNumericos(mensagem)).isEmpty();
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot, StatusDaMensagemChatBotEnum status) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(status);
        monitorDeChatBot.setPasso(3);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    @Override
    public CadastroPassosEnum getPasso() {
        return CadastroPassosEnum.PASSO_DOIS;
    }
}
