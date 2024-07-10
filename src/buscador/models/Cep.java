package buscador.models;

public class Cep {
    private String cep, logradouro, complemento, unidade, bairro, localidade;
    public Cep (ViaCEP searchResult) {
        this.cep = searchResult.cep();
        this.logradouro = searchResult.logradouro();
        this.complemento = searchResult.complemento();
        this.unidade = searchResult.unidade();
        this.bairro = searchResult.bairro();
        this.localidade = searchResult.localidade();
    }

    @Override
    public String toString() {
        return "\nCEP: "+cep+"\nLogradouro: "+logradouro+"\nComplemento: "+complemento+"\nUnidade: "+unidade+
                "\nBairro: "+bairro+"\nLocalidade: "+localidade+"\n";
    }
}
