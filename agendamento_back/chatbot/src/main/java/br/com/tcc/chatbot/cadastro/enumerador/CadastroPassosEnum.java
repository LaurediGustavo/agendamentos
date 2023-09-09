package br.com.tcc.chatbot.cadastro.enumerador;

public enum CadastroPassosEnum {

    PASSO_UM(1),
    PASSO_DOIS(2),
    PASSO_TRES(3),
    PASSO_QUATRO(4);

    private final Integer PASSO;

    CadastroPassosEnum(Integer passo) {
        PASSO = passo;
    }

    public Integer getPASSO() {
        return PASSO;
    }
}
