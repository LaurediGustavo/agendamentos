package br.com.tcc.chatbot.cancelaragendamento.passos;

import br.com.tcc.chatbot.CancelarOperacaoChatBotService;
import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.chatbot.cancelaragendamento.interfaces.CancelarPassosInterface;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.CancelarAgendamentoChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusConsultaEnum;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.CancelarAgendamentoChatBotRepository;
import br.com.tcc.repository.ConsultaRepository;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uteis.Uteis;

import java.time.LocalDateTime;

@Component
public class CancelarPassoDois implements CancelarPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CancelarAgendamentoChatBotRepository cancelarAgendamentoChatBotRepository;

    @Autowired
    private CancelarOperacaoChatBotService cancelarOperacaoChatBot;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MenuChatBot menuChatBot;

    @Override
    public SendMessage processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(Uteis.cpfValido(mensagem) && cpfUtilizado(mensagem)) {
            if(isExisteConsulta(mensagem)) {
                cadastrarCancelamento(message);
                atualizarMonitor(monitorDeChatBot, StatusDaMensagemChatBotEnum.AGUARDANDO);
                return montarMensagem(message.getChatId(), cancelarOperacaoChatBot.consultasDisponiveis(mensagem));
            }
            else {
                atualizarMonitor(monitorDeChatBot, StatusDaMensagemChatBotEnum.FINALIZADO);
                SendMessage sendMessage = montarMensagem(message.getChatId(), "Você não possui consultas agendadas!\n\n");
                menuChatBot.montarMensagem(sendMessage, message.getChatId());
                return sendMessage;
            }
        }
        else {
            return montarMensagem(message.getChatId(), "CPF inválido ou nao cadastrado! Por favor informe seu CPF");
        }
    }

    private boolean isExisteConsulta(String cpf) {
        return consultaRepository.hasConsultasByStatusAndCpfAndDataHoraInicio(cpf) > 0L;
    }

    private void cadastrarCancelamento(Message mensagem) {
        CancelarAgendamentoChatBot cancelarAgendamentoChatBot = new CancelarAgendamentoChatBot();
        cancelarAgendamentoChatBot.setCpf(mensagem.getText());
        cancelarAgendamentoChatBot.setChatId(mensagem.getChatId());

        cancelarAgendamentoChatBotRepository.save(cancelarAgendamentoChatBot);
    }

    private boolean cpfUtilizado(String mensagem) {
        return pacienteRepository
                .findByCpf(mensagem).isPresent();
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot, StatusDaMensagemChatBotEnum status) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(3);
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(status);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private SendMessage montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return sendMessage;
    }

    @Override
    public CancelarPassosEnum getPasso() {
        return CancelarPassosEnum.PASSO_DOIS;
    }
}
