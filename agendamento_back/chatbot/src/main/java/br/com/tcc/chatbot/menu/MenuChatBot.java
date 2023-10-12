package br.com.tcc.chatbot.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component("MenuChatBot")
public class MenuChatBot {

    public SendMessage getMenu(Long chatId) {
        SendMessage message = criarOpcoesDeResposta();
        montarMensagem(message, chatId);

        return message;
    }

    public void montarMensagem(SendMessage message, Long chatId) {
        String string = """
                Olá! Sou o Odo, o robô de atendimento da clinica odontológida Xpto.
                
                Como eu poderia te ajudar?
                1. Cadastro;
                2. Agendar Consulta;
                3. Remarcar Consulta;
                4. Cancelar Consulta;
                5. Consultar Agendamentos;
                
                Para cancelar qualquer operação digite: CANCELAR
                
                Digite apenas o número da opção desejada""";

        String texto = message.getText() == null? "" : message.getText();
        message.setChatId(chatId.toString());
        message.setText(texto + string);
    }

    private SendMessage criarOpcoesDeResposta() {
        ReplyKeyboardMarkup teclado = new ReplyKeyboardMarkup();
        teclado.setResizeKeyboard(true);

        KeyboardRow linhaUm = new KeyboardRow();
        KeyboardRow linhaDois = new KeyboardRow();

        KeyboardButton btnUm = new KeyboardButton("1");
        KeyboardButton btnDois = new KeyboardButton("2");
        KeyboardButton btnTres = new KeyboardButton("3");
        KeyboardButton btnQuatro = new KeyboardButton("4");
        KeyboardButton btnCinco = new KeyboardButton("5");

        linhaUm.addAll(List.of(btnUm, btnDois, btnTres));
        linhaDois.addAll(List.of(btnQuatro, btnCinco));

        teclado.setKeyboard(List.of(linhaUm, linhaDois));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(teclado);

        return sendMessage;
    }

}
