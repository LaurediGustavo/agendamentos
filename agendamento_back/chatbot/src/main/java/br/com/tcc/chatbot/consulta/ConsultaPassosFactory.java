package br.com.tcc.chatbot.consulta;

import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.chatbot.cadastro.interfaces.CadastroPassosInterface;
import br.com.tcc.chatbot.consulta.enumerador.ConsultaPassosEnum;
import br.com.tcc.chatbot.consulta.interfaces.ConsultaPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsultaPassosFactory {

    private Map<ConsultaPassosEnum, ConsultaPassosInterface> impl;

    @Autowired
    public ConsultaPassosFactory(List<ConsultaPassosInterface> passosInterfaces) {
        this.impl = new HashMap<ConsultaPassosEnum, ConsultaPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public ConsultaPassosInterface processar(ConsultaPassosEnum passo) {
        return this.impl.get(passo);
    }

}
