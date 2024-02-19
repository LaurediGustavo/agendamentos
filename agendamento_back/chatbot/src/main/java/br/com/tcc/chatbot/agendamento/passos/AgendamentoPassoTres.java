package br.com.tcc.chatbot.agendamento.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.entity.AgendamentoChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.repository.AgendamentoChatBotRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.ProcedimentoRepository;
import org.apache.commons.lang3.StringUtils;
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
public class AgendamentoPassoTres implements AgendamentoPassosInterface {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private AgendamentoChatBotRepository agendamentoChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        Optional<Procedimento> procedimento = getProcedimento(mensagem);
        if(procedimento.isPresent()) {
            atualizarMonitor(monitorDeChatBot);
            persistirAgendamento(procedimento.get(), message.getChatId());
            return montarMensagem(message.getChatId(), "Por favor informe a data da consulta (dd/mm/aaaa)");
        }
        else {
            return montarMensagem(message.getChatId(), getTextoMensagemErro());
        }
    }

    private void persistirAgendamento(Procedimento procedimento, Long chatId) {
        Optional<AgendamentoChatBot> agendamentoChatBotOptional = agendamentoChatBotRepository.findByChatId(chatId);
        AgendamentoChatBot agendamentoChatBot = agendamentoChatBotOptional.get();

        agendamentoChatBot.setProcedimento(procedimento);
        agendamentoChatBotRepository.save(agendamentoChatBot);
    }

    private String getTextoMensagemErro() {
        StringBuilder procedimentos = procedimentoRepository
                .findAllHabilitados()
                .stream()
                .map(procedimento ->
                        new StringBuilder()
                                .append(procedimento.getId())
                                .append(". ")
                                .append(procedimento.getTratamento())
                                .append(" - Valor: " + Uteis.formatarMoedaParaReal(procedimento.getValor()))
                                .append("\n"))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                );

        return """
                Código inválido!
                Por favor informe o código do procedimento:
                """
                + procedimentos.toString();
    }

    private Optional<Procedimento> getProcedimento(String codigo) {
        return procedimentoRepository.findByIdHabilitado(Long.parseLong(StringUtils.isBlank(codigo) || !Uteis.isValorNumerico(codigo)? (String) "0" : codigo));
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(4);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    @Override
    public AgendamentoPassosEnum getPasso() {
        return AgendamentoPassosEnum.PASSO_TRES;
    }
}
