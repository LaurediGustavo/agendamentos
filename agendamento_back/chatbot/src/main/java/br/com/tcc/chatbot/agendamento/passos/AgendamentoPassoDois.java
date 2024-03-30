package br.com.tcc.chatbot.agendamento.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.entity.AgendamentoChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.repository.AgendamentoChatBotRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.repository.ProcedimentoRepository;
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
public class AgendamentoPassoDois implements AgendamentoPassosInterface {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private AgendamentoChatBotRepository agendamentoChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem) && cpfUtilizado(mensagem)) {
            atualizarMonitor(monitorDeChatBot);
            persistirAgendamento(message);
            return montarMensagem(message.getChatId(), getTextoMensagem());
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido, nao cadastrado ou bloqueado! Por favor informe seu CPF ou entre em contato com a Clínica.");
        }
    }

    private void persistirAgendamento(Message message) {
        Optional<AgendamentoChatBot> agendamentoChatBotOptional = agendamentoChatBotRepository.findByChatId(message.getChatId());

        AgendamentoChatBot agendamentoChatBot = null;
        agendamentoChatBot = agendamentoChatBotOptional.orElseGet(AgendamentoChatBot::new);

        agendamentoChatBot.setCpf(Uteis.removerCaracteresNaoNumericos(message.getText()));
        agendamentoChatBot.setChatId(message.getChatId());
        agendamentoChatBotRepository.save(agendamentoChatBot);
    }

    private String getTextoMensagem() {
        StringBuilder procedimentos = procedimentoRepository
                .findAllHabilitados()
                .stream()
                .map(procedimento ->
                        new StringBuilder()
                                .append(procedimento.getId())
                                .append(". ")
                                .append(procedimento.getTratamento())
                                .append("\n\t\t\t\t\t- Valor: " + Uteis.formatarMoedaParaReal(procedimento.getValor()))
                                .append("\n"))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                );

        return """
                Por favor informe o código do procedimento:
                """
                + procedimentos.toString();
    }

    private boolean cpfUtilizado(String mensagem) {
        return pacienteRepository
                .findByCpf(Uteis.removerCaracteresNaoNumericos(mensagem)).isPresent();
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
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
    public AgendamentoPassosEnum getPasso() {
        return AgendamentoPassosEnum.PASSO_DOIS;
    }
}
