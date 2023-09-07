package br.com.tcc.chatbot;

import br.com.tcc.chatbot.confirmacao.impl.ConfirmarConsultaMonitorDeMensagensBot;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;

public class BotListener extends TelegramLongPollingBot {

	@Autowired
	private MenuChatBot menuChatBot;

	@Autowired
	private ConfirmarConsultaMonitorDeMensagensBot monitorDeMensagensChatBot;

	@Autowired
	private List<RetornoChatBotInterface> retornoChatBotInterface;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			try {
				long chatId = update.getMessage().getChatId();

				Optional<MonitorDeChatBot> optionalMonitor = monitorDeMensagensChatBot
						.consultarPorChatIdAndStatus(chatId, StatusDaMensagemChatBotEnum.AGUARDANDO);

				SendMessage message = null;
				if(optionalMonitor.isPresent()) {
					MonitorDeChatBot monitor = optionalMonitor.get();

					message = retornoChatBotInterface.stream()
							.filter(p -> p.getTipoChatBot().equals(monitor.getTipoChatBotEnum()))
							.findFirst().get().processarRetorno(update.getMessage(), monitor);

					removeKeyboard(message);
				}
				else {
					message = naoPossuiOperacaoCadastrada(update.getMessage(), update);
				}

				execute(message);
			}
			catch (TelegramApiException e) {
				e.printStackTrace();
			}
        }
	}

	private SendMessage naoPossuiOperacaoCadastrada(Message message, Update update) {
		Optional<RetornoChatBotInterface> botInterfaceOptional = retornoChatBotInterface
				.stream()
				.filter(p -> p.getTipoChatBot().getVALUE().equals(update.getMessage().getText()))
				.findFirst();

		if(botInterfaceOptional.isPresent()) {
			SendMessage sendMessage = botInterfaceOptional.get().processarRetorno(message, null);
			removeKeyboard(sendMessage);
			return sendMessage;
		}
		else {
			return menuChatBot.getMenu(message.getChatId());
		}
	}

    public void removeKeyboard(SendMessage sendMessage) {
		ReplyKeyboardRemove teclado = new ReplyKeyboardRemove();
		teclado.setRemoveKeyboard(true);

		sendMessage.setReplyMarkup(teclado);
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
