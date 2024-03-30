package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.RemarcarOperacaoChatBotService;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.RemarcarAgendamentoChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RemarcarPassoDois implements RemarcarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

    @Autowired
    private RemarcarOperacaoChatBotService remarcarOperacaoChatBot;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MenuChatBot menuChatBot;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem) && cpfUtilizado(mensagem)) {
            if(isExisteConsulta(mensagem)) {
                cadastrarRemarcar(message);
                atualizarMonitor(monitorDeChatBot, StatusDaMensagemChatBotEnum.AGUARDANDO);
                return montarMensagem(message.getChatId(), remarcarOperacaoChatBot.consultasDisponiveis(Uteis.removerCaracteresNaoNumericos(mensagem)));
            }
            else {
                atualizarMonitor(monitorDeChatBot, StatusDaMensagemChatBotEnum.FINALIZADO);
                List<SendMessage> messages = montarMensagem(message.getChatId(), "Você não possui consultas agendadas!\n\n");
                messages.add(menuChatBot.getMenu(message.getChatId()));
                return messages;
            }
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido, nao cadastrado ou bloqueado! Por favor informe seu CPF ou entre em contato com a Clínica.");
        }
    }

    private boolean isExisteConsulta(String cpf) {
        return consultaRepository.hasConsultasByStatusAndCpfAndDataHoraInicio(Uteis.removerCaracteresNaoNumericos(cpf)) > 0L;
    }

    private void cadastrarRemarcar(Message mensagem) {
        RemarcarAgendamentoChatBot remarcarAgendamentoChatBot = new RemarcarAgendamentoChatBot();
        remarcarAgendamentoChatBot.setCpf(Uteis.removerCaracteresNaoNumericos(mensagem.getText()));
        remarcarAgendamentoChatBot.setChatId(mensagem.getChatId());

        remarcarAgendamentoChatBotRepository.save(remarcarAgendamentoChatBot);
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
    public RemarcarPassosEnum getPasso() {
        return RemarcarPassosEnum.PASSO_DOIS;
    }
}
