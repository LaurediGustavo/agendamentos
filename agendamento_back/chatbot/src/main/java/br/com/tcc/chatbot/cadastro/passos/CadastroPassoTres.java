package br.com.tcc.chatbot.cadastro.passos;

import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.chatbot.cadastro.interfaces.CadastroPassosInterface;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.entity.PacienteChatBot;
import br.com.tcc.repository.MonitorDeChatBotRepository;
import br.com.tcc.repository.PacienteChatBotRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CadastroPassoTres implements CadastroPassosInterface {

    @Autowired
    private MonitorDeChatBotRepository monitorDeChatBotRepository;

    @Autowired
    private PacienteChatBotRepository pacienteChatBotRepository;

    @Override
    public List<SendMessage> processarPassosDeCadastro(MonitorDeChatBot monitorDeChatBot, Message message) {
        String mensagem = message.getText();

        if(StringUtils.isNotBlank(mensagem)) {
            atualizarMonitor(monitorDeChatBot);
            atualizarPaciente(message);
            return montarMensagem(message.getChatId(), "Por favor informe o seu telefone");
        }
        else {
            return montarMensagem(message.getChatId(), "Por favor informe o seu nome e sobrenome");
        }
    }

    private void atualizarPaciente(Message message) {
        Optional<PacienteChatBot> pacienteChatBotOptional = pacienteChatBotRepository.findByChatId(message.getChatId());

        if(pacienteChatBotOptional.isPresent()) {
            String[] nomes = message.getText().split(" ");

            PacienteChatBot paciente = pacienteChatBotOptional.get();
            paciente.setNome(nomes[0]);
            paciente.setSobrenome(nomes.length > 1? nomes[1] : "");

            pacienteChatBotRepository.save(paciente);
        }
    }

    private void atualizarMonitor(MonitorDeChatBot monitorDeChatBot) {
        monitorDeChatBot.setDataDaMensagem(LocalDateTime.now());
        monitorDeChatBot.setPasso(4);

        monitorDeChatBotRepository.save(monitorDeChatBot);
    }

    private List<SendMessage> montarMensagem(Long chatId, String mensagem) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(mensagem);

        return new ArrayList<>(List.of(sendMessage));
    }

    @Override
    public CadastroPassosEnum getPasso() {
        return CadastroPassosEnum.PASSO_TRES;
    }
}
