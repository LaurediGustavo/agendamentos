package br.com.tcc.chatbot.cancelaragendamento;

import br.com.tcc.chatbot.cancelaragendamento.enumerador.CancelarPassosEnum;
import br.com.tcc.chatbot.cancelaragendamento.interfaces.CancelarPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CancelarPassosFactory {

    private Map<CancelarPassosEnum, CancelarPassosInterface> impl;

    @Autowired
    public CancelarPassosFactory(List<CancelarPassosInterface> passosInterfaces) {
        this.impl = new HashMap<CancelarPassosEnum, CancelarPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public CancelarPassosInterface processar(CancelarPassosEnum passo) {
        return this.impl.get(passo);
    }

}
