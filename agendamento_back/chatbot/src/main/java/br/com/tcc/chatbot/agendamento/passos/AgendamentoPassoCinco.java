package br.com.tcc.chatbot.agendamento.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AgendamentoPassoCinco implements AgendamentoPassosInterface {

    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private DoutorRepository doutorRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private AgendamentoChatBotRepository agendamentoChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeAgendamento(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        AgendamentoChatBot agendamentoChatBot = getAgendamento(message.getChatId());
        if(StringUtils.isNotBlank(mensagem)) {
            LocalTime horario = horarioAtendimento(agendamentoChatBot, mensagem);

            if(horario != null) {
                atualizarMonitor(monitorDeChatBot);
                persistirAgendamento(agendamentoChatBot, horario);
                return montarMensagem(message.getChatId(), resumoDoAgendamento(agendamentoChatBot));
            }
            else {
                return montarMensagem(message.getChatId(), getTextoMensagem(agendamentoChatBot));
            }
        }
        else {
            return montarMensagem(message.getChatId(), getTextoMensagem(agendamentoChatBot));
        }
    }

    private String resumoDoAgendamento(AgendamentoChatBot agendamentoChatBot) {
        return "Por favor confirme os dados do agendamento\n\n" +
                "Procedimento: " + agendamentoChatBot.getProcedimento().getTratamento() +
                "\nValor: " + Uteis.formatarMoedaParaReal(agendamentoChatBot.getProcedimento().getValor()) +
                "\nDia: " + DataUteis.getLocalDateTime_ddMMaaaa(agendamentoChatBot.getHorario()) +
                "\nHorário: " + DataUteis.getLocalTimeHHmm(agendamentoChatBot.getHorario()) +
                "\n\n" +
                "1. Confirmar\n" +
                "2. Ajustar";
    }

    private void persistirAgendamento(AgendamentoChatBot agendamentoChatBot, LocalTime horario) {
        LocalDate data = agendamentoChatBot.getHorario().toLocalDate();
        LocalDateTime dataHora = data.atTime(horario);

        agendamentoChatBot.setHorario(dataHora);
        agendamentoChatBotRepository.save(agendamentoChatBot);
    }

    private LocalTime horarioAtendimento(AgendamentoChatBot agendamentoChatBot, String opcao) {
        List<LocalTime> horarios = horariosDisponiveis(agendamentoChatBot.getHorario().toLocalDate(),
                agendamentoChatBot.getProcedimento(), agendamentoChatBot.getCpf());

        int opcaoInt;
        try {
            Collections.sort(horarios);

            opcaoInt = Integer.parseInt(opcao);

            return horarios.get(opcaoInt - 1);
        }
        catch (Exception e) {
            return null;
        }
    }

    private AgendamentoChatBot getAgendamento(Long chatId) {
        return agendamentoChatBotRepository.findByChatId(chatId).get();
    }

    private String getTextoMensagem(AgendamentoChatBot agendamentoChatBot) {
        List<LocalTime> horarios = horariosDisponiveis(agendamentoChatBot.getHorario().toLocalDate(),
                agendamentoChatBot.getProcedimento(), agendamentoChatBot.getCpf());

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
        Collections.sort(horarios);

        StringBuilder string = new StringBuilder();

        for (int i = 0; i < horarios.size(); i++) {
            string.append(i + 1)
                    .append(". ")
                    .append(horarios.get(i))
                    .append("\n");
        }

        return string.toString();
    }

    private List<LocalTime> horariosDisponiveis(LocalDate data, Procedimento procedimento, String cpf) {
        List<Doutor> doutorList = doutorRepository
                .findAllToProcedureHabilitados(new Long[] { procedimento.getId() });

        Paciente paciente = getPaciente(cpf);

        List<LocalTime> horarios = gerarHorarios(Integer.parseInt(procedimento.getTempo()));
        List<LocalTime> listaFinal = new ArrayList<LocalTime>();

        for (Doutor doutor : doutorList) {
            for (int i = 0; i < horarios.size() - 1; i++) {
                LocalDateTime dataInicio = data.atTime(horarios.get(i));
                LocalDateTime dataFinal = data.atTime(horarios.get(i + 1));

                boolean horarioLivre = false;
                long possuiAgendamento = consultaRepository.consultarPorDataEDoutor(dataInicio, dataFinal, doutor.getId(), 0L).get();
                horarioLivre = possuiAgendamento == 0;

                possuiAgendamento = consultaRepository.consultarPorDataEPaciente(dataInicio, dataFinal, paciente.getId(), 0L).get();
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

    private Paciente getPaciente(String cpf) {
        return pacienteRepository.findByCpf(cpf).get();
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
        monitorDeChatBot.setPasso(6);

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
        return AgendamentoPassosEnum.PASSO_CINCO;
    }
}
