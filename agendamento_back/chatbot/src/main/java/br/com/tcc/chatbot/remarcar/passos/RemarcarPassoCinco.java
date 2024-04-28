package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import br.com.tcc.entity.*;
import br.com.tcc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class RemarcarPassoCinco implements RemarcarPassosInterface {

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
    private RemarcarAgendamentoChatBotRepository remarcarAgendamentoChatBotRepository;

    @Autowired
    private ConsultaEstendidaRepository consultaEstendidaRepository;

    @Override
    public List<SendMessage> processarPassosDeRemarcar(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        LocalDate data = getData(mensagem);
        if(data != null && !data.isBefore(LocalDate.now()) ) {
            RemarcarAgendamentoChatBot remarcarAgendamentoChatBot = getRemarcarAgendamento(message.getChatId());
            Consulta consulta = getConsulta(remarcarAgendamentoChatBot);

            List<LocalTime> horarios = horariosDisponiveis(data, getProcedimentos(consulta), remarcarAgendamentoChatBot.getCpf());

            if(!horarios.isEmpty()) {
                atualizarMonitor(monitorDeChatBot);
                persistirRemarcarAgendamento(remarcarAgendamentoChatBot, data);
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

    private Consulta getConsulta(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot) {
        return consultaRepository.findById(remarcarAgendamentoChatBot.getAgendamentoId()).get();
    }

    private void persistirRemarcarAgendamento(RemarcarAgendamentoChatBot remarcarAgendamentoChatBot, LocalDate data) {
        LocalDateTime dataHora = data.atTime(LocalTime.MIDNIGHT);

        remarcarAgendamentoChatBot.setHorario(dataHora);
        remarcarAgendamentoChatBotRepository.save(remarcarAgendamentoChatBot);
    }

    private RemarcarAgendamentoChatBot getRemarcarAgendamento(Long chatId) {
        return remarcarAgendamentoChatBotRepository.findTopByChatIdOrderByIdDesc(chatId).get();
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
        Collections.sort(horarios);

        return """
                Horários disponíveis:\n
                """ +
                formatarHorarios(horarios) +
                """
                \nDigite apenas o número da opção desejada
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

    private List<LocalTime> horariosDisponiveis(LocalDate data, List<Procedimento> procedimentos, String cpf) {
        List<Doutor> doutorList = doutorRepository
                .findAllToProcedureHabilitados(procedimentos.stream().map(Procedimento::getId).toArray(Long[]::new));

        Paciente paciente = getPaciente(cpf);

        int intevalo = getIntervalo(procedimentos);

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

    private int getIntervalo(List<Procedimento> procedimentos) {
        return procedimentos.stream()
                .mapToInt(procedimento -> Integer.parseInt(procedimento.getTempo()))
                .sum();
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

    private List<Procedimento> getProcedimentos(Consulta consulta) {
        List<Procedimento> procedimentos = consulta.getProcedimentos();

        Optional<Consulta> consultaOptional = consultaEstendidaRepository.consultarConsultaDePorPara(consulta.getId());
        if (consultaOptional.isPresent()) {
            procedimentos.addAll(consultaOptional.get().getProcedimentos());
        }

        return procedimentos;
    }

    @Override
    public RemarcarPassosEnum getPasso() {
        return RemarcarPassosEnum.PASSO_CINCO;
    }
}
