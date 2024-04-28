package br.com.tcc.chatbot.remarcar.passos;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import br.com.tcc.dto.AgendamentoDto;
import br.com.tcc.entity.*;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDate;
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

    @Autowired
    private ConsultaEstendidaRepository consultaEstendidaRepository;

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
                    return montarMensagem(message.getChatId(), getTextoMensagem(message.getChatId()));
                }
                default ->
                {
                    return montarMensagem(message.getChatId(), "Opção inválida. \n Por favor informe:\n1. Confirmar\n 2. Ajustar");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return montarMensagem(message.getChatId(), "Opção inválida. \n Por favor informe:\n1. Confirmar\n 2. Ajustar");
        }

    }

    private String getTextoMensagem(Long chatId) {
        Optional<RemarcarAgendamentoChatBot> remarcarAgendamentoChatBot = remarcarAgendamentoChatBotRepository.findTopByChatIdOrderByIdDesc(chatId);

        List<LocalTime> horarios = horariosDisponiveis(remarcarAgendamentoChatBot.get().getHorario().toLocalDate(),
                getProcedimentos(getConsulta(remarcarAgendamentoChatBot.get())), remarcarAgendamentoChatBot.get().getCpf());

        return """
            Horários disponíveis:
            """ +
            formatarHorarios(horarios) +
            """
            Digite apenas o número da opção desejada
            """;
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

    private Paciente getPaciente(String cpf) {
        return pacienteRepository.findByCpf(cpf).get();
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

    private void cadastrarAgendamento(Long chatId) {
        Optional<RemarcarAgendamentoChatBot> remarcarAgendamentoChatBot = remarcarAgendamentoChatBotRepository.findTopByChatIdOrderByIdDesc(chatId);

        if(remarcarAgendamentoChatBot.isPresent()) {
            RemarcarAgendamentoChatBot remarcar = remarcarAgendamentoChatBot.get();
            Optional<Paciente> paciente = pacienteRepository.findByCpf(remarcar.getCpf());
            Consulta consultaAnterior = getConsulta(remarcar);

            int minutos = getIntervalo(getProcedimentos(consultaAnterior));
            int horas = minutos / 60;
            minutos = minutos % 60;

            List<Procedimento> procedimentosAnterior = new ArrayList<>();
            procedimentosAnterior.addAll(consultaAnterior.getProcedimentos());

            Consulta consulta = new Consulta();
            consulta.setTempoAproximado(LocalTime.of(horas, minutos));
            consulta.setStatus(StatusConsultaEnum.AGUARDANDO);
            consulta.setValorTotal(consultaAnterior.getValorTotal());
            consulta.setDataHoraInicio(remarcar.getHorario());
            consulta.setDataHoraFinal(remarcar.getHorario().plusHours(horas).plusMinutes(minutos).minusMinutes(1));
            consulta.setPaciente(paciente.get());
            consulta.setDoutor(getDoutorDisponivel(remarcar, minutos));
            consulta.setProcedimentos(procedimentosAnterior);

            consultaRepository.save(consulta);
            remarcarAgendamentoChatBotRepository.delete(remarcar);

            consultaAnterior.setConsultaRemarcadaPara(consulta);
            consultaAnterior.setStatus(StatusConsultaEnum.REMARCADO);
            consultaAnterior.setProcedimentos(procedimentosAnterior);
            consultaRepository.save(consultaAnterior);

            adicionarConsultaEstendida(consulta, consultaAnterior);
        }
    }

    private void adicionarConsultaEstendida(Consulta consulta, Consulta consultaAnterior) {
        Optional<Consulta> consultaOptional = consultaEstendidaRepository.consultarConsultaDePorPara(consultaAnterior.getId());

        if (consultaOptional.isPresent()) {
            ConsultaEstendida consultaEstendida = new ConsultaEstendida();
            consultaEstendida.setConsultaEstendidaDe(consultaOptional.get());
            consultaEstendida.setConsultaEstendidaPara(consulta);

            this.consultaEstendidaRepository.save(consultaEstendida);
        }
    }

    private List<Procedimento> getProcedimentos(Consulta consulta) {
        List<Procedimento> procedimentos = new ArrayList<>();
        procedimentos.addAll(consulta.getProcedimentos());

        Optional<Consulta> consultaOptional = consultaEstendidaRepository.consultarConsultaDePorPara(consulta.getId());
        if (consultaOptional.isPresent()) {
            procedimentos.addAll(consultaOptional.get().getProcedimentos());
        }

        return procedimentos;
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

        Long[] procedimentoIds = getProcedimentos(remarcarAgendamentoChatBot.getConsulta()).stream()
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
