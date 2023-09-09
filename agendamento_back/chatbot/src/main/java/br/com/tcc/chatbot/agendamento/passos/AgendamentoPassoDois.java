package br.com.tcc.chatbot.agendamento.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import br.com.tcc.repository.ProcedimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;

@Component
public class AgendamentoPassoDois implements AgendamentoPassosInterface {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Override
    public SendMessage processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem) && cpfUtilizado(mensagem)) {
            atualizarMonitor(monitorDeChatBot);
            return montarMensagem(message.getChatId(), getTextoMensagem());
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido ou nao cadastrado! Por favor informe seu CPF");
        }
    }

    private String getTextoMensagem() {
        StringBuilder procedimentos = procedimentoRepository
                .findAll()
                .stream()
                .map(procedimento ->
                        new StringBuilder()
                                .append(procedimento.getId())
                                .append(". ")
                                .append(procedimento.getTratamento())
                                .append("\n - Valor: " + Uteis.formatarMoedaParaReal(procedimento.getValor()))
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
                .findByCpf(mensagem).isPresent();
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(3);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private SendMessage montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return sendMessage;
    }

    @Override
    public AgendamentoPassosEnum getPasso() {
        return AgendamentoPassosEnum.PASSO_DOIS;
    }
}
