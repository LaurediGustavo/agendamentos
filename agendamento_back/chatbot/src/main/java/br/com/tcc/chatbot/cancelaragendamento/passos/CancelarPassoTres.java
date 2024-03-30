package br.com.tcc.chatbot.cancelaragendamento.passos;

import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.chatbot.cancelaragendamento.interfaces.CancelarPassosInterface;
import br.com.tcc.entity.CancelarAgendamentoChatBot;
import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.repository.CancelarAgendamentoChatBotRepository;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.DataUteis;
import uteis.Uteis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CancelarPassoTres implements CancelarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private CancelarAgendamentoChatBotRepository cancelarAgendamentoChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        Optional<CancelarAgendamentoChatBot> cancelarAgendamentoChatBot = cancelarAgendamentoChatBotRepository
                .findTopByChatIdOrderByIdDesc(message.getChatId());

        if(cancelarAgendamentoChatBot.isPresent()) {
            CancelarAgendamentoChatBot cancelamento = cancelarAgendamentoChatBot.get();

            if(isOpcaoValida(message, cancelamento)) {
                atualizarMonitor(monitorDeChatBot);
                atualizarCancelamento(cancelamento);
                return montarMensagemSucesso(message.getChatId(), cancelarAgendamentoChatBot.get().getConsulta());
            }
            else {
                return montarMensagemErro(message.getChatId(), "Opção informada iválida! Por favor informe uma opção válida.");
            }
        }
        else {
            return montarMensagemErro(message.getChatId(), "Falha interna! \nPor favor digite \"CANCELAR\"");
        }
    }

    private boolean isOpcaoValida(Message message, CancelarAgendamentoChatBot cancelarAgendamentoChatBot) {
        boolean isValid = false;
        String mensagem = message.getText();

        if (Uteis.isValorNumerico(mensagem) && isOpcaoExistente(message, cancelarAgendamentoChatBot)) {
            isValid = true;
        }

        return isValid;
    }

    private boolean isOpcaoExistente(Message message, CancelarAgendamentoChatBot cancelarAgendamentoChatBot) {
        boolean isValid = false;

        List<Consulta> consultaList = consultaRepository
                .findConsultasByStatusAndCpfAndDataHoraInicio(cancelarAgendamentoChatBot.getCpf());
        if(!consultaList.isEmpty()) {
            Consulta consulta = getConsulta(consultaList, message.getText());

            if(consulta != null) {
                cancelarAgendamentoChatBot.setAgendamentoId(consulta.getId());
                cancelarAgendamentoChatBot.setConsulta(consulta);
                isValid = true;
            }
        }

        return isValid;
    }

    private Consulta getConsulta(List<Consulta> consultaList, String text) {
        try {
            return consultaList.get(Integer.parseInt(text) - 1);
        }
        catch (Exception e) {
            return null;
        }
    }

    private void atualizarCancelamento(CancelarAgendamentoChatBot cancelarAgendamentoChatBot) {
        cancelarAgendamentoChatBotRepository.save(cancelarAgendamentoChatBot);
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(4);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private List<SendMessage> montarMensagemErro(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    private List<SendMessage> montarMensagemSucesso(Long chatId, Consulta consulta) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(getMensagem(consulta));

        return new ArrayList<>(List.of(sendMessage));
    }

    public String getMensagem(Consulta consulta) {
        return "Deseja realmente cancelar essa consulta? \n\n" +
                DataUteis.getLocalDateTime_ddMMaaaaHHMM(consulta.getDataHoraInicio()) +
                "\n" +
                "Procedimento: \n" +
                getProcedimentos(consulta.getProcedimentos()) +
                "\n" +
                "Valor: " +
                Uteis.formatarMoedaParaReal(consulta.getValorTotal()) +
                "\n\n" +
                "1. Sim \n" +
                "2. Não";
    }

    private String getProcedimentos(List<Procedimento> procedimentos) {
        return procedimentos.stream()
                .map(procedimento -> new StringBuilder().append("\t\t\t").append(procedimento.getTratamento()).append("\n"))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }

    @Override
    public CancelarPassosEnum getPasso() {
        return CancelarPassosEnum.PASSO_TRES;
    }
}
