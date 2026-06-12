package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

// Classe Abstrata conforme exigido no enunciado
public abstract class Ocorrencia implements Operacional {
    // Atributo de classe (static) para controlo global de contagem
    private static int totalOcorrenciasContador = 0;

    // Atributos protegidos (protected) comuns a qualquer ocorrência
    protected int id;
    protected String localizacao;
    protected String estado; // "Despacho", "No Local", "Concluida"
    protected LocalDateTime horaAlerta;
    protected ArrayList<String> viaturasAtribuidas;

    // Construtor sem parâmetros
    public Ocorrencia() {
        this("Desconhecido", new ArrayList<String>());
    }

    // Construtor com parâmetros - Usa a referência this
    public Ocorrencia(String localizacao, ArrayList<String> viaturasAtribuidas) {
        totalOcorrenciasContador++;
        this.id = totalOcorrenciasContador;
        this.localizacao = localizacao;
        this.viaturasAtribuidas = new ArrayList<String>(viaturasAtribuidas); // Cópia da lista
        this.estado = "Despacho";
        this.horaAlerta = LocalDateTime.now();
    }

    // Construtor de cópia - Exigido pelo professor
    public Ocorrencia(Ocorrencia outra) {
        this.id = outra.id;
        this.localizacao = outra.localizacao;
        this.estado = outra.estado;
        this.horaAlerta = outra.horaAlerta;
        this.viaturasAtribuidas = outra.viaturasAtribuidas;
    }

    // Métodos de acesso (Getters e Setters)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getHoraAlerta() {
        return horaAlerta;
    }

    public void setHoraAlerta(LocalDateTime horaAlerta) {
        this.horaAlerta = horaAlerta;
    }

    public ArrayList<String> getViaturasAtribuidas() {
        return viaturasAtribuidas;
    }

    public void setViaturaAtribuida(ArrayList<String> viaturasAtribuidas) {
        this.viaturasAtribuidas = viaturasAtribuidas;
    }

    public static int getTotalOcorrenciasContador() {
        return totalOcorrenciasContador;
    }

    // Método print() exigido para imprimir os atributos
    public void print() {
        System.out.println(this.toString());
    }

    // Sobreposição (Overriding) dos métodos da classe Object

    @Override
    public String toString() {
        return "ID: " + id + " | Local: " + localizacao + " | Estado: " + estado + " | Viatura: " + viaturasAtribuidas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Ocorrencia outra = (Ocorrencia) obj;
        return id == outra.id &&
                Objects.equals(localizacao, outra.localizacao) &&
                Objects.equals(estado, outra.estado);
    }

    // Método clone abstrato - obriga as subclasses a implementar a sua própria
    // cópia
    @Override
    public abstract Object clone();

    @Override
    public void avancarEstado() {
        switch (estado) {
            case "Despacho":
                estado = "No Local";
                break;
            case "No Local":
                estado = "Concluida";
                break;
            case "Concluida":
                System.out.println("Esta ocorrência já foi concluída.");
                break;
        }
    }
}