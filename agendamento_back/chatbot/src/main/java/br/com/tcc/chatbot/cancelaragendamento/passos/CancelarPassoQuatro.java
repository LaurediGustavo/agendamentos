package br.com.tcc.chatbot.cancelaragendamento.passos;

import br.com.tcc.chatbot.CancelarOperacaoChatBotService;
import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.chatbot.cancelaragendamento.interfaces.CancelarPassosInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.CancelarAgendamentoChatBot;
import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.CancelarAgendamentoChatBotRepository;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.websocket.NotificationWebSocketHandler;
import br.com.tcc.websocket.NotificationWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CancelarPassoQuatro implements CancelarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private CancelarAgendamentoChatBotRepository cancelarAgendamentoChatBotRepository;

    @Autowired
    private CancelarOperacaoChatBotService cancelarOperacaoChatBot;

    @Autowired
    private MenuChatBot menuChatBot;

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Autowired
    private NotificationWebSocketService notificationWebSocketService;

    @Override
    public List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        String opcao = message.getText();

        if(isOpcaoValida(opcao)) {
            if("1".equals(opcao)) {
                cancelarAgendamento(message.getChatId());
                atualizarMonitor(monitorDeChatBot, 5,StatusDaMensagemChatBotEnum.FINALIZADO);

                List<SendMessage> sendMessage = montarMensagem(message.getChatId(), "Cancelamento efetuado com sucesso!\n\n");
                menuChatBot.montarMensagem(sendMessage.get(0), message.getChatId());
                return sendMessage;
            }
            else {
                atualizarMonitor(monitorDeChatBot, 3, StatusDaMensagemChatBotEnum.AGUARDANDO);
                return montarMensagem(message.getChatId(), getTexto(message.getChatId()));
            }
        }
        else {
            montarMensagem(message.getChatId(), "Opção inválida!\nInforme uma opção válida");
        }

        return null;
    }

    private String getTexto(Long chatId) {
        Optional<CancelarAgendamentoChatBot> cancelarAgendamentoChatBot = cancelarAgendamentoChatBotRepository
                .findTopByChatIdOrderByIdDesc(chatId);

        if(cancelarAgendamentoChatBot.isPresent()) {
            CancelarAgendamentoChatBot cancelamento = cancelarAgendamentoChatBot.get();
            return cancelarOperacaoChatBot.consultasDisponiveis(cancelamento.getCpf());
        }

        return "Falha interna! Digite \"CANCELAR\"";
    }

    private void cancelarAgendamento(Long chatId) {
        Optional<CancelarAgendamentoChatBot> cancelarAgendamentoChatBot = cancelarAgendamentoChatBotRepository
                .findTopByChatIdOrderByIdDesc(chatId);

        if(cancelarAgendamentoChatBot.isPresent()) {
            CancelarAgendamentoChatBot cancelamento = cancelarAgendamentoChatBot.get();

            Consulta consulta = consultaRepository.findById(cancelamento.getAgendamentoId()).get();
            cancelarConsulta(consulta);
            notificationWebSocketHandler.notificar(notificationWebSocketService.tratarConsultaParaWebSocket(consulta));
        }
    }

    private void cancelarConsulta(Consulta consulta) {
        consulta.setStatus(StatusConsultaEnum.CANCELADO);
        consultaRepository.save(consulta);
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
    public CancelarPassosEnum getPasso() {
        return CancelarPassosEnum.PASSO_QUATRO;
    }
}
