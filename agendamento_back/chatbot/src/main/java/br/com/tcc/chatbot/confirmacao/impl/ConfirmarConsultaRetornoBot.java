package br.com.tcc.chatbot.confirmacao.impl;

import br.com.tcc.chatbot.RetornoChatBotInterface;
import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ConfirmarConsultaRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<SendMessage> processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        String messageText = message.getText().toUpperCase();

        if ("sim".equals(messageText.toLowerCase()) || "não".equals(messageText.toLowerCase())) {
            Optional<Consulta> consultaOptional = consultas(monitorDeChatBot);

            atualizarConsulta(consultaOptional, messageText);
            atualizarMonitor(monitorDeChatBot);

            return montarMensagem(message.getChatId(), "sim".equals(messageText.toLowerCase())? "Consulta confirmada com sucesso!" : "Consulta cancelada com sucesso!");
        }
        else {
            return montarMensagem(message.getChatId(), "Opção inválida! Informe Sim ou Não.");
        }
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.FINALIZADO);
        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    void atualizarConsulta(Optional<Consulta> consultaOptional, String messageText) {
        if(consultaOptional.isPresent()) {
            Consulta consulta = consultaOptional.get();

            if ("SIM".equals(messageText)) {
                consulta.setStatus(StatusConsultaEnum.CONFIRMADO);
            }
            else {
                consulta.setStatus(StatusConsultaEnum.CANCELADO);
            }

            consultaRepository.save(consulta);
        }
    }

    Optional<Consulta> consultas(MonitorDeChatBot monitorDeChatBot) {
        LocalDateTime data = monitorDeChatBot.getDataDaMensagem().plusDays(1);

        return consultaRepository
                .consultarAgendamentoPendente(StatusConsultaEnum.ENVIADO,
                        monitorDeChatBot.getChatId(),
                        data.getYear(),
                        data.getMonthValue(),
                        data.getDayOfMonth());
    }

    @Override
    public TipoChatBotEnum getTipoChatBot() {
        return TipoChatBotEnum.CONFIRMACAO;
    }
}
