package model;

import java.util.ArrayList;
import java.util.Objects;

public class AcidenteViacao extends Ocorrencia {
    private int numVeiculos;
    private boolean presencaEncarcerados;
    private String tipoVia;

    // Construtor sem parâmetros
    public AcidenteViacao() {
        this("Desconhecido", new ArrayList<String>(), 0, false, "Urbana");
    }

    // Construtor com parâmetros
    public AcidenteViacao(String localizacao, ArrayList<String> viaturasAtribuidas, int numVeiculos,
            boolean presencaEncarcerados, String tipoVia) {
        super(localizacao, viaturasAtribuidas);
        this.numVeiculos = numVeiculos;
        this.presencaEncarcerados = presencaEncarcerados;
        this.tipoVia = tipoVia;
    }

    // Construtor de cópia
    public AcidenteViacao(AcidenteViacao outro) {
        super(outro);
        this.numVeiculos = outro.numVeiculos;
        this.presencaEncarcerados = outro.presencaEncarcerados;
        this.tipoVia = outro.tipoVia;
    }

    // Getters e Setters
    public int getNumVeiculos() {
        return numVeiculos;
    }

    public void setNumVeiculos(int numVeiculos) {
        this.numVeiculos = numVeiculos;
    }

    public boolean isPresencaEncarcerados() {
        return presencaEncarcerados;
    }

    public void setPresencaEncarcerados(boolean presencaEncarcerados) {
        this.presencaEncarcerados = presencaEncarcerados;
    }

    public String getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(String tipoVia) {
        this.tipoVia = tipoVia;
    }

    @Override
    public void print() {
        super.print();
        System.out.println("   └─> [Acidente Viação] Veículos: " + numVeiculos + " | Encarcerados: "
                + (presencaEncarcerados ? "Sim" : "Não") + " | Via: " + tipoVia);
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Acidente";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        AcidenteViacao outro = (AcidenteViacao) obj;
        return numVeiculos == outro.numVeiculos &&
                presencaEncarcerados == outro.presencaEncarcerados &&
                Objects.equals(tipoVia, outro.tipoVia);
    }

    @Override
    public Object clone() {
        return new AcidenteViacao(this);
    }
}