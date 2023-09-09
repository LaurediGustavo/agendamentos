package br.com.tcc.chatbot.agendamento;

import br.com.tcc.chatbot.agendamento.enumerador.AgendamentoPassosEnum;
import br.com.tcc.chatbot.agendamento.interfaces.AgendamentoPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgendamentoPassosFactory {

    private Map<AgendamentoPassosEnum, AgendamentoPassosInterface> impl;

    @Autowired
    public AgendamentoPassosFactory(List<AgendamentoPassosInterface> passosInterfaces) {
        this.impl = new HashMap<AgendamentoPassosEnum, AgendamentoPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public AgendamentoPassosInterface processar(AgendamentoPassosEnum passo) {
        return this.impl.get(passo);
    }

}
