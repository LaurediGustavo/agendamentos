package br.com.tcc.chatbot.confirmacao.impl;

import java.util.List;

import br.com.tcc.chatbot.confirmacao.interfaces.ConfirmarConsultaInterface;
import br.com.tcc.chatbot.generic.Bot;
import br.com.tcc.entity.Paciente;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component("ConfirmarConsultaBot")
public class ConfirmarConsultaBot extends Bot implements ConfirmarConsultaInterface {
	
	@Override
	public void iniciarConversa(Paciente paciente) throws TelegramApiException {
    	SendMessage message = createYesOrNoKeyboard(paciente.getChatId());
        execute(message);
    }
	
    public SendMessage createYesOrNoKeyboard(Long chatId) {
        // Criação do teclado
        ReplyKeyboardMarkup teclado = new ReplyKeyboardMarkup();
        teclado.setResizeKeyboard(true);  // Redimensiona o teclado

        // Criação das linhas do teclado
        KeyboardRow linhaUm = new KeyboardRow();
        KeyboardRow linhaDois = new KeyboardRow();

        // Criação dos botões de sim e não
        KeyboardButton simBotao = new KeyboardButton("Sim");
        KeyboardButton naoBotao = new KeyboardButton("Não");

        // Adição dos botões às linhas
        linhaUm.add(simBotao);
        linhaDois.add(naoBotao);

        // Adição das linhas ao teclado
        teclado.setKeyboard(List.of(linhaUm, linhaDois));

        // Criação da mensagem com o teclado
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(montarMensagem());
        sendMessage.setReplyMarkup(teclado);

        return sendMessage;
    }
    
    private String montarMensagem() {
    	StringBuilder string = new StringBuilder();
    	string.append("Bom dia, Lauredi Gustavo!\n")
    		  .append("Você possui uma consulta agendada para amanhã às 10h e 20min.\n")
    		  .append("Deseja confirmar ela?");
    	
    	return string.toString();
    }

}
