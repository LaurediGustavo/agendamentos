package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.RemarcarOperacaoChatBotService;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.RemarcarAgendamentoChatBot;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.RemarcarAgendamentoChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RemarcarPassoQuatro implements RemarcarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

    @Autowired
    private RemarcarOperacaoChatBotService remarcarOperacaoChatBot;

    @Autowired
    private MenuChatBot menuChatBot;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        String opcao = message.getText();

        if(isOpcaoValida(opcao)) {
            if("1".equals(opcao)) {
                atualizarMonitor(monitorDeChatBot, 5,StatusDaMensagemChatBotEnum.AGUARDANDO);

                return montarMensagem(message.getChatId(), "Por favor informe a nova data da consulta (dd/mm/aaaa)");
            }
            else {
                atualizarMonitor(monitorDeChatBot, 3, StatusDaMensagemChatBotEnum.AGUARDANDO);
                return montarMensagem(message.getChatId(), getTexto(message.getChatId()));
            }
        }
        else {
            return montarMensagem(message.getChatId(), "Opção inválida!\nInforme uma opção válida");
        }
    }

    private String getTexto(Long chatId) {
        Optional<RemarcarAgendamentoChatBot> remarcarAgendamentoChatBot = remarcarAgendamentoChatBotRepository
                .findTopByChatIdOrderByIdDesc(chatId);

        if(remarcarAgendamentoChatBot.isPresent()) {
            RemarcarAgendamentoChatBot remarcar = remarcarAgendamentoChatBot.get();
            return remarcarOperacaoChatBot.consultasDisponiveis(remarcar.getCpf());
        }

        return "Falha interna! Digite \"CANCELAR\"";
    }

    private boolean isOpcaoValida(String opcao) {
        return switch (opcao) {
            case "1", "2" -> true;
            default -> false;
        };
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot, int passo, StatusDaMensagemChatBotEnum status) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(passo);
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(status);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    @Override
    public RemarcarPassosEnum getPasso() {
        return RemarcarPassosEnum.PASSO_QUATRO;
    }
}
