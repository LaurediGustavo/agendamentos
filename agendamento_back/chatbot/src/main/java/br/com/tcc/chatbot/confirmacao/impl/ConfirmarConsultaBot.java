package br.com.tcc.chatbot.confirmacao.impl;

import java.util.List;

import br.com.tcc.chatbot.confirmacao.interfaces.ConfirmarConsultaInterface;
import br.com.tcc.chatbot.BotListener;
import br.com.tcc.entity.Consulta;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component("ConfirmarConsultaBot")
public class ConfirmarConsultaBot extends BotListener implements ConfirmarConsultaInterface {
	
	@Override
	public void iniciarConversa(Consulta consulta) throws TelegramApiException {
    	SendMessage message = createYesOrNoKeyboard(consulta);
        execute(message);
    }
	
    public SendMessage createYesOrNoKeyboard(Consulta consulta) {
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
        sendMessage.setChatId(consulta.getPaciente().getChatId().toString());
        sendMessage.setText(montarMensagem(consulta));
        sendMessage.setReplyMarkup(teclado);

        return sendMessage;
    }
    
    private String montarMensagem(Consulta consulta) {
    	StringBuilder string = new StringBuilder();
    	string.append("Bom dia, ")
                .append(consulta.getPaciente().getNome())
                .append(" ")
                .append(consulta.getPaciente().getSobrenome())
                .append(". ")
    		    .append("Você possui uma consulta agendada para amanhã às ")
                .append(consulta.getDataHoraInicio().getHour())
                .append(":")
                .append(consulta.getDataHoraFinal().getMinute())
                .append(". ")
    		    .append("Deseja confirmar ela?");
    	
    	return string.toString();
    }

}
