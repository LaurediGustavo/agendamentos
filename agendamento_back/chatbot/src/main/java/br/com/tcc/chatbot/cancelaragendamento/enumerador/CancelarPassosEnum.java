package br.com.tcc.chatbot.cancelaragendamento.enumerador;

public enum CancelarPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2),
    PASSO_TRES(3),
    PASSO_QUATRO(4);

    private final Integer PASSO;

    CancelarPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }

}
