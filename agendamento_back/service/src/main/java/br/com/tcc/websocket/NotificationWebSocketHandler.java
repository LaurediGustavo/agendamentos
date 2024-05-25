package br.com.tcc.websocket;

import br.com.tcc.dto.ConsultaWebSocketDTO;
import br.com.tcc.enumerador.StatusConsultaEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    private static Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        notificar(new ConsultaWebSocketDTO(1000L, "teste", LocalDateTime.now(), LocalDateTime.now(), StatusConsultaEnum.AGUARDANDO.name()));
    }

    public void notificar(ConsultaWebSocketDTO consulta) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonMessage = objectMapper.writeValueAsString(consulta);

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
