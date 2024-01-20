package br.com.tcc.chatbot.remarcar;

import br.com.tcc.chatbot.remarcar.enumerador.RemarcarPassosEnum;
import br.com.tcc.chatbot.remarcar.interfaces.RemarcarPassosInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RemarcarPassosFactory {

    private Map<RemarcarPassosEnum, RemarcarPassosInterface> impl;

    @Autowired
    public RemarcarPassosFactory(List<RemarcarPassosInterface> passosInterfaces) {
        this.impl = new HashMap<RemarcarPassosEnum, RemarcarPassosInterface>();

        passosInterfaces.forEach(passo -> {
            impl.put(passo.getPasso(), passo);
        });
    }

    public RemarcarPassosInterface processar(RemarcarPassosEnum passo) {
        return this.impl.get(passo);
    }

}
