package br.com.tcc.chatbot.remarcar.enumerador;

public enum RemarcarPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2),
    PASSO_TRES(3),
    PASSO_QUATRO(4),
    PASSO_CINCO(5),
    PASSO_SEIS(6),
    PASSO_SETE(7);

    private final Integer PASSO;

    RemarcarPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }

}
