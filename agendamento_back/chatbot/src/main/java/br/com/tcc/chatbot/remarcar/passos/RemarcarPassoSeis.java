package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import br.com.tcc.entity.*;
import br.com.tcc.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.DataUteis;
import uteis.Uteis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RemarcarPassoSeis implements RemarcarPassosInterface {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        RemarcarAgendamentoChatBot remarcarAgendamentoChatBot = getRemarcarAgendamento(message.getChatId());
        if(StringUtils.isNotBlank(mensagem)) {
            LocalTime horario = horarioAtendimento(remarcarAgendamentoChatBot, mensagem);

            if(horario != null) {
                atualizarMonitor(monitorDeChatBot);
                persistirAgendamento(remarcarAgendamentoChatBot, horario);
                return montarMensagem(message.getChatId(), resumoDoAgendamento(remarcarAgendamentoChatBot));
            }
            else {
                return montarMensagem(message.getChatId(), getTextoMensagem(remarcarAgendamentoChatBot));
            }
        }
        else {
            return montarMensagem(message.getChatId(), getTextoMensagem(remarcarAgendamentoChatBot));
        }
    }

    private String resumoDoAgendamento(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot) {
        return "Por favor confirme os dados do agendamento\n\n" +
                "Procedimento: " + getProcedimentos(remarcarAgendamentoChatBot.getConsulta().getProcedimentos()) +
                "\nValor: " + Uteis.formatarMoedaParaReal(remarcarAgendamentoChatBot.getConsulta().getValorTotal()) +
                "\nDia: " + DataUteis.getLocalDateTime_ddMMaaaa(remarcarAgendamentoChatBot.getHorario()) +
                "\nHorário: " + DataUteis.getLocalTimeHHmm(remarcarAgendamentoChatBot.getHorario()) +
                "\n\n" +
                "1. Confirmar\n" +
                "2. Ajustar";
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

    private void persistirAgendamento(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot, LocalTime horario) {
        LocalDate data = remarcarAgendamentoChatBot.getHorario().toLocalDate();
        LocalDateTime dataHora = data.atTime(horario);

        remarcarAgendamentoChatBot.setHorario(dataHora);
        remarcarAgendamentoChatBotRepository.save(remarcarAgendamentoChatBot);
    }

    private LocalTime horarioAtendimento(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot, String opcao) {
        List<LocalTime> horarios = horariosDisponiveis(remarcarAgendamentoChatBot.getHorario().toLocalDate(),
                getIntervalo(getConsulta(remarcarAgendamentoChatBot).getProcedimentos()));

        int opcaoInt;
        try {
            opcaoInt = Integer.parseInt(opcao);

            return horarios.get(opcaoInt - 1);
        }
        catch (Exception e) {
            return null;
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

    private RemarcarAgendamentoChatBot getRemarcarAgendamento(Long chatId) {
        return remarcarAgendamentoChatBotRepository.findByChatId(chatId).get();
    }

    private String getTextoMensagem(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot) {
        List<LocalTime> horarios = horariosDisponiveis(remarcarAgendamentoChatBot.getHorario().toLocalDate(),
                getIntervalo(remarcarAgendamentoChatBot.getConsulta().getProcedimentos()));

        return """
                O horário ficou indisponível.
                
                Horários disponíveis:
                """ +
                formatarHorarios(horarios) +
                """
                Digite apenas o número da opção desejada
                """;
    }

    private String formatarHorarios(List<LocalTime> horarios) {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < horarios.size(); i++) {
            string.append(i + 1)
                    .append(". ")
                    .append(horarios.get(i))
                    .append("\n");
        }

        return string.toString();
    }

    private List<LocalTime> horariosDisponiveis(LocalDate data, int intevalo) {
        List<Doutor> doutorList = doutorRepository
                .findAll();

        List<LocalTime> horarios = gerarHorarios(intevalo);
        List<LocalTime> listaFinal = new ArrayList<LocalTime>();

        for (Doutor doutor : doutorList) {
            for (int i = 0; i < horarios.size() - 1; i++) {
                LocalDateTime dataInicio = data.atTime(horarios.get(i));
                LocalDateTime dataFinal = data.atTime(horarios.get(i + 1));
                dataFinal = dataFinal.minusSeconds(1);

                boolean horarioLivre = false;
                long possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, dataFinal, doutor.getId(), 0L).get();
                horarioLivre = possuiAgendamento == 0;

                possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, dataFinal, doutor.getId(), 0L).get();
                horarioLivre = possuiAgendamento == 0;

                if(horarioLivre) {
                    boolean jaAdicionado = false;
                    for (LocalTime tempo : listaFinal) {
                        if (tempo == horarios.get(i)) {
                            jaAdicionado = true;
                            break;
                        }
                    }

                    if(!jaAdicionado) {
                        listaFinal.add(horarios.get(i));
                    }
                }
            }
        }

        return listaFinal;
    }

    private List<LocalTime> gerarHorarios(int intervalo) {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        List<LocalTime> horarios = new ArrayList<>();

        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
            horarios.add(currentTime);
            currentTime = currentTime.plusMinutes(intervalo);
        }

        return horarios;
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(7);

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
        return RemarcarPassosEnum.PASSO_SEIS;
    }
}
