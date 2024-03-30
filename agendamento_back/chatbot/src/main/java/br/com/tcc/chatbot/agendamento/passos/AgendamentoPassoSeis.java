package br.com.tcc.chatbot.agendamento.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.*;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class AgendamentoPassoSeis implements AgendamentoPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private AgendamentoChatBotRepository agendamentoChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private MenuChatBot menuChatBot;


    @Override
    public List<SendMessage> processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        try {
            int opcao = Integer.parseInt(mensagem);

            switch (opcao) {
                case 1 -> {
                    atualizarMonitor(monitorDeChatBot, 7, StatusDaMensagemChatBotEnum.FINALIZADO);
                    cadastrarAgendamento(message.getChatId());

                    List<SendMessage> messages = montarMensagem(message.getChatId(), "Agendamento cadastrado com sucesso!\n\n");
                    menuChatBot.montarMensagem(messages.get(0), message.getChatId());
                    return messages;
                }
                case 2 -> {
                    atualizarMonitor(monitorDeChatBot, 3, StatusDaMensagemChatBotEnum.AGUARDANDO);
                    return montarMensagem(message.getChatId(), getTextoMensagem());
                }
                default ->
                {
                    return montarMensagem(message.getChatId(), "Opção inválida. \n Por favor inform:\n1. Confirmar\n 2. Ajustar");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return montarMensagem(message.getChatId(), "Opção inválida. \n Por favor inform:\n1. Confirmar\n 2. Ajustar");
        }

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

    private void cadastrarAgendamento(Long chatId) {
        Optional<AgendamentoChatBot> agendamentoChatBotOptional = agendamentoChatBotRepository.findByChatId(chatId);

        if(agendamentoChatBotOptional.isPresent()) {
            AgendamentoChatBot agendamentoChatBot = agendamentoChatBotOptional.get();
            Optional<Paciente> paciente = pacienteRepository.findByCpf(agendamentoChatBot.getCpf());

            int minutos = Integer.parseInt(agendamentoChatBot.getProcedimento().getTempo());
            int horas = minutos / 60;
            minutos = minutos % 60;

            Consulta consulta = new Consulta();
            consulta.setTempoAproximado(LocalTime.of(horas, minutos));
            consulta.setStatus(StatusConsultaEnum.AGUARDANDO);
            consulta.setValorTotal(agendamentoChatBot.getProcedimento().getValor());
            consulta.setDataHoraInicio(agendamentoChatBot.getHorario());
            consulta.setDataHoraFinal(agendamentoChatBot.getHorario().plusMinutes(minutos).minusMinutes(1));
            consulta.setPaciente(paciente.get());
            consulta.setDoutor(getDoutorDisponivel(agendamentoChatBot));
            consulta.setProcedimentos(getProcedimentos(agendamentoChatBot.getProcedimento()));

            consultaRepository.save(consulta);
            agendamentoChatBotRepository.delete(agendamentoChatBot);
        }
    }

    private List<Procedimento> getProcedimentos(Procedimento procedimento) {
        return Collections.singletonList(procedimento);
    }

    private Doutor getDoutorDisponivel(AgendamentoChatBot agendamentoChatBot) {
        LocalDateTime horarioFinal = agendamentoChatBot.getHorario()
                .plusMinutes(Long.parseLong(agendamentoChatBot.getProcedimento().getTempo()));

        return doutorRepository.findDoutoresDisponiveis(agendamentoChatBot.getHorario(), horarioFinal,
                        new Long[] { agendamentoChatBot.getProcedimento().getId() })
                .get(0);
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot, int passo, StatusDaMensagemChatBotEnum status) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(status);
        monitorDeChatBot.setPasso(passo);

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
        return AgendamentoPassosEnum.PASSO_SEIS;
    }
}
