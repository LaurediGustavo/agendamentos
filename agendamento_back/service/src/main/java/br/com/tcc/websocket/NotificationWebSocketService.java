package br.com.tcc.websocket;

import br.com.tcc.dto.ConsultaWebSocketDTO;
import br.com.tcc.entity.Consulta;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    public ConsultaWebSocketDTO tratarConsultaParaWebSocket(Consulta consulta) {
        return new ConsultaWebSocketDTO(consulta.getId(),
                                        consulta.getPaciente().getNome(),
                                        consulta.getDataHoraInicio(),
                                        consulta.getDataHoraFinal(),
                                        consulta.getStatus().name());
    }

}
