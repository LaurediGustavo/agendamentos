package br.com.tcc.chatbot.cadastro.passos;

import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.chatbot.cadastro.interfaces.CadastroPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.Paciente;
import br.com.tcc.entity.PacienteChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteChatBotRepository;
import br.com.tcc.repository.PacienteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CadastroPassoQuatro implements CadastroPassosInterface {

    @Autowired
    private PacienteChatBotRepository pacienteChatBotRepository;

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public SendMessage processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(StringUtils.isNotBlank(mensagem)) {
            if(mensagem.length() <= 15) {
                atualizarMonitor(monitorDeChatBot);
                PacienteChatBot paciente = atualizarPaciente(message);
                cadastrarPacienteNoSistema(paciente);
                return montarMensagem(message.getChatId(), "Cadastro realizado com sucesso");
            }
            else {
                return montarMensagem(message.getChatId(), "Por favor informe um número válido");
            }
        }
        else {
            return montarMensagem(message.getChatId(), "Por favor informe o seu telefone");
        }
    }

    private void cadastrarPacienteNoSistema(PacienteChatBot paciente) {
        if(paciente != null) {
            Paciente pacienteSistema = new Paciente();
            pacienteSistema.setNome(paciente.getNome());
            pacienteSistema.setSobrenome(paciente.getSobrenome());
            pacienteSistema.setCpf(paciente.getCpf());
            pacienteSistema.setTelefone(paciente.getTelefone());
            pacienteSistema.setChatId(paciente.getChatId());

            pacienteRepository.save(pacienteSistema);
            pacienteChatBotRepository.delete(paciente);
        }
    }

    private PacienteChatBot atualizarPaciente(Message message) {
        Optional<PacienteChatBot> pacienteChatBotOptional = pacienteChatBotRepository.findByChatId(message.getChatId());

        if(pacienteChatBotOptional.isPresent()) {
            PacienteChatBot paciente = pacienteChatBotOptional.get();
            paciente.setTelefone(message.getText());

            return pacienteChatBotRepository.save(paciente);
        }

        return null;
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setStatusDaMensagemChatBotEnum(StatusDaMensagemChatBotEnum.FINALIZADO);
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
    public CadastroPassosEnum getPasso() {
        return CadastroPassosEnum.PASSO_QUATRO;
    }
}
