package br.com.tcc.chatbot.generic;

import br.com.tcc.chatbot.confirmacao.impl.MonitorDeMensagensChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

public class Bot extends TelegramLongPollingBot {

	@Autowired
	private MonitorDeMensagensChatBot monitorDeMensagensChatBot;

	@Autowired
	private List<RetornoChatBotInterface> retornoChatBotInterface;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			try {
				long chatId = update.getMessage().getChatId();

				Optional<MonitorDeChatBot> optionalMonitor = monitorDeMensagensChatBot
						.consultarPorChatIdAndStatus(chatId, StatusDaMensagemChatBotEnum.AGUARDANDO);

				if(optionalMonitor.isPresent()) {
					MonitorDeChatBot monitor = optionalMonitor.get();

					retornoChatBotInterface.stream()
							.filter(p -> p.getTipoChatBot().equals(monitor.getTipoChatBotEnum()))
							.forEach(retorno -> retorno.processarRetorno(update.getMessage(), monitor));
				}

				execute(removeKeyboard(update.getMessage()));
			}
			catch (TelegramApiException e) {
				e.printStackTrace();
			}
        }
	}

    public SendMessage removeKeyboard(Message message) {
		ReplyKeyboardRemove teclado = new ReplyKeyboardRemove();
		teclado.setRemoveKeyboard(true);

		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setText("Obrigado pelo retorno!");
		sendMessage.setReplyMarkup(teclado);

        return sendMessage;
    }

	@Override
	public String getBotUsername() {
		return "ConfirmarConsultaBot";
	}

	@Override
	public String getBotToken() {
		return "6059019238:AAG2CaB4IvBx8dRd9_d2VItsn6LA6CYH8K8";
	}

}
