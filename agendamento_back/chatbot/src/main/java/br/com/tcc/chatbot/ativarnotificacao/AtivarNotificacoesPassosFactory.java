package br.com.tcc.chatbot.ativarnotificacao;

import br.com.tcc.chatbot.ativarnotificacao.enumerador.AtivarNotificacoesPassosEnum;
import br.com.tcc.chatbot.ativarnotificacao.interfaces.AtivarNotificacoesPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AtivarNotificacoesPassosFactory {

    private Map<AtivarNotificacoesPassosEnum, AtivarNotificacoesPassosInterface> impl;

    @Autowired
    public AtivarNotificacoesPassosFactory(List<AtivarNotificacoesPassosInterface> passosInterfaces) {
        this.impl = new HashMap<AtivarNotificacoesPassosEnum, AtivarNotificacoesPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public AtivarNotificacoesPassosInterface processar(AtivarNotificacoesPassosEnum passo) {
        return this.impl.get(passo);
    }

}
