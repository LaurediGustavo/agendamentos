package br.com.tcc.chatbot.consulta.passos;

import br.com.tcc.chatbot.consulta.enumerador.ConsultaPassosEnum;
import br.com.tcc.chatbot.consulta.interfaces.ConsultaPassosInterface;
import br.com.tcc.entity.Consulta;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
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
import java.util.List;
import java.util.Optional;

@Component
public class ConsultaPassoDois implements ConsultaPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Override
    public SendMessage processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem) && cpfUtilizado(mensagem)) {
            atualizarMonitor(monitorDeChatBot);
            return montarMensagem(message.getChatId(), getTextoMensagem(mensagem));
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido ou nao cadastrado! Por favor informe seu CPF");
        }
    }

    private String getTextoMensagem(String cpf) {
        StringBuilder consultas = new StringBuilder();
        consultas.append("Lista de consultas agendadas: \n\n");

        List<Consulta> consultaList = consultaRepository
                .findConsultasByStatusAndCpfAndDataHoraInicio(cpf);

        if(!consultaList.isEmpty()) {
            consultas.append(consultaList.stream()
                                .map(con -> new StringBuilder()
                                        .append(DataUteis.getLocalDateTime_ddMMaaaaHHMM(con.getDataHoraInicio()))
                                        .append("\n")
                                        .append("Procedimento: \n")
                                        .append(getProcedimentos(con.getProcedimentos()))
                                        .append("\n")
                                        .append("Valor: " + Uteis.formatarMoedaParaReal(con.getValorTotal()))
                                        .append("\n\n"))
                                .collect(
                                        StringBuilder::new,
                                        StringBuilder::append,
                                        StringBuilder::append
                                ).toString()
            );
        }
        else {
            consultas.append("Você não possui consultas agendadas");
        }

        return consultas.toString();
    }

    private String getProcedimentos(List<Procedimento> procedimentos) {
        return procedimentos.stream()
                .map(procedimento -> new StringBuilder()
                        .append("\t\t\t" + procedimento.getTratamento() + "\n"))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }

    private boolean cpfUtilizado(String mensagem) {
        return pacienteRepository
                .findByCpf(mensagem).isPresent();
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.FINALIZADO);
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
    public ConsultaPassosEnum getPasso() {
        return ConsultaPassosEnum.PASSO_DOIS;
    }
}
