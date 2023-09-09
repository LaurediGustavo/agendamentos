package br.com.tcc.chatbot.agendamento.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.entity.AgendamentoChatBot;
import br.com.tcc.entity.Doutor;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Procedimento;
import br.com.tcc.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AgendamentoPassoQuatro implements AgendamentoPassosInterface {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendamentoChatBotRepository agendamentoChatBotRepository;

    @Override
    public SendMessage processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        LocalDate data = getData(mensagem);
        if(data != null && !data.isBefore(LocalDate.now()) ) {
            AgendamentoChatBot agendamentoChatBot = getAgendamento(message.getChatId());
            List<LocalTime> horarios = horariosDisponiveis(data, Integer.parseInt(agendamentoChatBot.getProcedimento().getTempo()));

            if(!horarios.isEmpty()) {
                atualizarMonitor(monitorDeChatBot);
                persistirAgendamento(agendamentoChatBot, data);
                return montarMensagem(message.getChatId(), getTextoMensagem(horarios));
            }
            else {
                return montarMensagem(message.getChatId(), "Nenhum horário disponível.\n Por favor informe outra data");
            }
        }
        else {
            return montarMensagem(message.getChatId(), "Por favor informe uma data válida (dd/mm/aaaa)");
        }
    }

    private void persistirAgendamento(AgendamentoChatBot agendamentoChatBot, LocalDate data) {
        LocalDateTime dataHora = data.atTime(LocalTime.MIDNIGHT);

        agendamentoChatBot.setHorario(dataHora);
        agendamentoChatBotRepository.save(agendamentoChatBot);
    }

    private AgendamentoChatBot getAgendamento(Long chatId) {
        return agendamentoChatBotRepository.findByChatId(chatId).get();
    }

    private LocalDate getData(String data) {
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(data, formato);
        }
        catch (Exception e) {
            return null;
        }
    }

    private String getTextoMensagem(List<LocalTime> horarios) {
        return """
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
                long possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, doutor.getId()).get();
                horarioLivre = possuiAgendamento == 0;

                possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataFinal, doutor.getId()).get();
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
        monitorDeChatBot.setPasso(5);

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
        return AgendamentoPassosEnum.PASSO_QUATRO;
    }
}
