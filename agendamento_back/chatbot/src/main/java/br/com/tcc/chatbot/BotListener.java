package br.com.tcc.chatbot;

import br.com.tcc.chatbot.confirmacao.impl.ConfirmarConsultaMonitorDeMensagensBot;
import br.com.tcc.chatbot.menu.MenuChatBot;
import br.com.tcc.entity.MonitorDeChatBot;
import br.com.tcc.enumerador.StatusDaMensagemChatBotEnum;
import br.com.tcc.enumerador.TipoChatBotEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BotListener extends TelegramLongPollingBot {

	private final String CANCELAR = "CANCELAR";

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

				List<SendMessage> messagens = new ArrayList<>();
				if(optionalMonitor.isPresent()) {
					MonitorDeChatBot monitor = optionalMonitor.get();
					messagens = chamarOperacao(update.getMessage(), monitor);

					removeKeyboard(messagens);
				}
				else {
					messagens = naoPossuiOperacaoCadastrada(update.getMessage(), update);
				}

				for (SendMessage message : messagens) {
					execute(message);
				}
			}
			catch (TelegramApiException e) {
				e.printStackTrace();
			}
        }
	}

	private List<SendMessage> chamarOperacao(Message message, MonitorDeChatBot monitor) {
		TipoChatBotEnum tipo = getTipoChatBot(message, monitor);

		return retornoChatBotInterface.stream()
				.filter(p -> p.getTipoChatBot().equals(tipo))
				.findFirst().get().processarRetorno(message, monitor);
	}

	private TipoChatBotEnum getTipoChatBot(Message message, MonitorDeChatBot monitor) {
		TipoChatBotEnum tipo = null;

		if(this.CANCELAR.equals(message.getText().toUpperCase())) {
			tipo = TipoChatBotEnum.CANCELAR_OPERACAO;
		}
		else {
			tipo = monitor.getTipoChatBotEnum();
		}

		return tipo;
	}

	private List<SendMessage> naoPossuiOperacaoCadastrada(Message message, Update update) {
		Optional<RetornoChatBotInterface> botInterfaceOptional = retornoChatBotInterface
				.stream()
				.filter(p -> p.getTipoChatBot().getVALUE().equals(update.getMessage().getText()))
				.findFirst();

		List<SendMessage> messages = new ArrayList<>();
		if(botInterfaceOptional.isPresent()) {
			messages = botInterfaceOptional.get().processarRetorno(message, null);
			removeKeyboard(messages);
		}
		else {
			messages.add(menuChatBot.getMenu(message.getChatId()));
		}

		return messages;
	}

    public void removeKeyboard(List<SendMessage> messages) {
		if (messages != null) {
			for (SendMessage message : messages) {
				ReplyKeyboardRemove teclado = new ReplyKeyboardRemove();
				teclado.setRemoveKeyboard(true);

				message.setReplyMarkup(teclado);
			}
		}
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
