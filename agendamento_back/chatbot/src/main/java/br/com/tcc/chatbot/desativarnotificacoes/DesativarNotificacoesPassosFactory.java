package br.com.tcc.chatbot.desativarnotificacoes;

import br.com.tcc.chatbot.desativarnotificacoes.enumerador.DesativarNotificacoesPassosEnum;
import br.com.tcc.chatbot.desativarnotificacoes.interfaces.DesativarNotificacoesPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DesativarNotificacoesPassosFactory {

    private Map<DesativarNotificacoesPassosEnum, DesativarNotificacoesPassosInterface> impl;

    @Autowired
    public DesativarNotificacoesPassosFactory(List<DesativarNotificacoesPassosInterface> passosInterfaces) {
        this.impl = new HashMap<DesativarNotificacoesPassosEnum, DesativarNotificacoesPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public DesativarNotificacoesPassosInterface processar(DesativarNotificacoesPassosEnum passo) {
        return this.impl.get(passo);
    }

}
