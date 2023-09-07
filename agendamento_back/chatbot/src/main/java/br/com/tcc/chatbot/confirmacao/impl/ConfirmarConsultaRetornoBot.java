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
import java.util.Optional;

@Component
public class ConfirmarConsultaRetornoBot extends RetornoChatBotInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public SendMessage processarRetorno(Message message, MonitorDeChatBot monitorDeChatBot) {
        String messageText = message.getText().toUpperCase();
        Optional<Consulta> consultaOptional = consultas(monitorDeChatBot);

        atualizarConsulta(consultaOptional, messageText);
        atualizarMonitor(monitorDeChatBot);
        return null;
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
