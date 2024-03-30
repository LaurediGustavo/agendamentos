package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
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
public class RemarcarPassoSete implements RemarcarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

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
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        try {
            int opcao = Integer.parseInt(mensagem);

            switch (opcao) {
                case 1 -> {
                    atualizarMonitor(monitorDeChatBot, 8, StatusDaMensagemChatBotEnum.FINALIZADO);
                    cadastrarAgendamento(message.getChatId());

                    List<SendMessage> messages = montarMensagem(message.getChatId(), "Agendamento remarcado com sucesso!\n\n");
                    menuChatBot.montarMensagem(messages.get(0), message.getChatId());
                    return messages;
                }
                case 2 -> {
                    atualizarMonitor(monitorDeChatBot, 6, StatusDaMensagemChatBotEnum.AGUARDANDO);
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
        Optional<RemarcarAgendamentoChatBot> remarcarAgendamentoChatBot = remarcarAgendamentoChatBotRepository.findTopByChatIdOrderByIdDesc(chatId);

        if(remarcarAgendamentoChatBot.isPresent()) {
            RemarcarAgendamentoChatBot remarcar = remarcarAgendamentoChatBot.get();
            Optional<Paciente> paciente = pacienteRepository.findByCpf(remarcar.getCpf());
            Consulta consultaAnterior = getConsulta(remarcar);

            int minutos = getIntervalo(consultaAnterior.getProcedimentos());
            int horas = minutos / 60;
            minutos = minutos % 60;

            Consulta consulta = new Consulta();
            consulta.setTempoAproximado(LocalTime.of(horas, minutos));
            consulta.setStatus(StatusConsultaEnum.AGUARDANDO);
            consulta.setValorTotal(consultaAnterior.getValorTotal());
            consulta.setDataHoraInicio(remarcar.getHorario());
            consulta.setDataHoraFinal(remarcar.getHorario().plusMinutes(minutos).minusMinutes(1));
            consulta.setPaciente(paciente.get());
            consulta.setDoutor(getDoutorDisponivel(remarcar, minutos));
            consulta.setProcedimentos(consultaAnterior.getProcedimentos());

            consultaRepository.save(consulta);

            consultaAnterior.setConsultaRemarcadaPara(consulta);
            consultaAnterior.setStatus(StatusConsultaEnum.REMARCADO);
            consultaRepository.save(consultaAnterior);

            remarcarAgendamentoChatBotRepository.delete(remarcar);
        }
    }

    private int getIntervalo(List<Procedimento> procedimentos) {
        return procedimentos.stream()
                .mapToInt(procedimento -> Integer.parseInt(procedimento.getTempo()))
                .sum();
    }

    private Consulta getConsulta(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot) {
        Consulta consulta = consultaRepository.findById(remarcarAgendamentoChatBot.getAgendamentoId()).get();
        remarcarAgendamentoChatBot.setConsulta(consulta);
        return consulta;
    }

    private Doutor getDoutorDisponivel(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot, int minutos) {
        LocalDateTime horarioFinal = remarcarAgendamentoChatBot.getHorario()
                .plusMinutes(minutos);

        Long[] procedimentoIds = remarcarAgendamentoChatBot.getConsulta().getProcedimentos()
                .stream()
                .map(Procedimento::getId)
                .toArray(Long[]::new);

        return doutorRepository.findDoutoresDisponiveis(remarcarAgendamentoChatBot.getHorario(), horarioFinal, procedimentoIds)
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
    public RemarcarPassosEnum getPasso() {
        return RemarcarPassosEnum.PASSO_SETE;
    }
}
