package model;

public class Viatura {
    private String id; // Ex: "ABSC-01", "VUCI-03"
    private String nome; // Ex: "Ambulância de Socorro", "Veículo Urbano de Combate"
    private String tipo; // "Ambulancia", "Incendio", "Desencarceramento"
    private boolean disponivel;

    public Viatura(String id, String nome, String tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.disponivel = true; // Por padrão, entram em prontidão (livres)
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + (disponivel ? "LIVRE" : "EM SERVIÇO") + ")";
    }
}