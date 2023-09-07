package br.com.tcc.chatbot.cadastro;

import br.com.tcc.chatbot.cadastro.enumerador.CadastroPassosEnum;
import br.com.tcc.chatbot.cadastro.interfaces.CadastroPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CadastroPassosFactory {

    private Map<CadastroPassosEnum, CadastroPassosInterface> impl;

    @Autowired
    public CadastroPassosFactory(List<CadastroPassosInterface> passosInterfaces) {
        this.impl = new HashMap<CadastroPassosEnum, CadastroPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public CadastroPassosInterface processar(CadastroPassosEnum passo) {
        return this.impl.get(passo);
    }

}
