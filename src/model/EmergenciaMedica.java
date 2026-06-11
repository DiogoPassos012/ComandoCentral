package model;

import java.util.Objects;

public class EmergenciaMedica extends Ocorrencia {
    private int numVitimas;
    private String queixaPrincipal;
    private int idadePaciente;

    // Construtor sem parâmetros - Usa o método this()
    public EmergenciaMedica() {
        this("Desconhecido", "Nenhuma Viatura", 0, "Não especificada", 0);
    }

    // Construtor com parâmetros - Usa a referência super para a classe mãe
    public EmergenciaMedica(String localizacao, String viaturaAtribuida, int numVitimas, String queixaPrincipal,
            int idadePaciente) {
        super(localizacao, viaturaAtribuida);
        this.numVitimas = numVitimas;
        this.queixaPrincipal = queixaPrincipal;
        this.idadePaciente = idadePaciente;
    }

    // Construtor de cópia - Usa o super para copiar a parte da classe mãe
    public EmergenciaMedica(EmergenciaMedica outra) {
        super(outra);
        this.numVitimas = outra.numVitimas;
        this.queixaPrincipal = outra.queixaPrincipal;
        this.idadePaciente = outra.idadePaciente;
    }

    // Getters e Setters
    public int getNumVitimas() {
        return numVitimas;
    }

    public void setNumVitimas(int numVitimas) {
        this.numVitimas = numVitimas;
    }

    public String getQueixaPrincipal() {
        return queixaPrincipal;
    }

    public void setQueixaPrincipal(String queixaPrincipal) {
        this.queixaPrincipal = queixaPrincipal;
    }

    public int getIdadePaciente() {
        return idadePaciente;
    }

    public void setIdadePaciente(int idadePaciente) {
        this.idadePaciente = idadePaciente;
    }

    // Sobreposição do método print() usando super
    @Override
    public void print() {
        super.print();
        System.out.println("   └─> [Emergência Médica] Vítimas: " + numVitimas + " | Queixa: " + queixaPrincipal
                + " | Idade: " + idadePaciente);
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Médica";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        EmergenciaMedica outra = (EmergenciaMedica) obj;
        return numVitimas == outra.numVitimas &&
                idadePaciente == outra.idadePaciente &&
                Objects.equals(queixaPrincipal, outra.queixaPrincipal);
    }

    @Override
    public Object clone() {
        return new EmergenciaMedica(this);
    }
}