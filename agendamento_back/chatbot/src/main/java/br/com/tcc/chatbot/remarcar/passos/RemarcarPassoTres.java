package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import br.com.tcc.entity.*;
import br.com.tcc.repository.*;
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
public class RemarcarPassoTres implements RemarcarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        Optional<RemarcarAgendamentoChatBot> remarcarAgendamentoChatBot = remarcarAgendamentoChatBotRepository
                .findTopByChatIdOrderByIdDesc(message.getChatId());

        if(remarcarAgendamentoChatBot.isPresent()) {
            RemarcarAgendamentoChatBot remarcar = remarcarAgendamentoChatBot.get();

            if(isOpcaoValida(message, remarcar)) {
                atualizarMonitor(monitorDeChatBot);
                atualizarCancelamento(remarcar);
                return montarMensagemSucesso(message.getChatId(), remarcarAgendamentoChatBot.get().getConsulta());
            }
            else {
                return montarMensagemErro(message.getChatId(), "Opção informada inválida! Por favor informa uma opção válida.");
            }
        }
        else {
            return montarMensagemErro(message.getChatId(), "Falha interna! \nPor favor digite \"CANCELAR\"");
        }
    }

    private boolean isOpcaoValida(Message message, RemarcarAgendamentoChatBot remarcar) {
        boolean isValid = false;
        String mensagem = message.getText();

        if (Uteis.isValorNumerico(mensagem) && isOpcaoExistente(message, remarcar)) {
            isValid = true;
        }

        return isValid;
    }

    private boolean isOpcaoExistente(Message message, RemarcarAgendamentoChatBot remarcarAgendamentoChatBot) {
        boolean isValid = false;

        List<Consulta> consultaList = consultaRepository
                .findConsultasByStatusAndCpfAndDataHoraInicio(remarcarAgendamentoChatBot.getCpf());
        if(!consultaList.isEmpty()) {
            Consulta consulta = getConsulta(consultaList, message.getText());

            if(consulta != null) {
                remarcarAgendamentoChatBot.setAgendamentoId(consulta.getId());
                remarcarAgendamentoChatBot.setConsulta(consulta);
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

    private void atualizarCancelamento(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot) {
        remarcarAgendamentoChatBotRepository.save(remarcarAgendamentoChatBot);
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
        return "Deseja realmente remarcar essa consulta? \n\n" +
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
    public RemarcarPassosEnum getPasso() {
        return RemarcarPassosEnum.PASSO_TRES;
    }
}
