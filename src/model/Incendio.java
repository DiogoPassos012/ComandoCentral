package model;

import java.util.ArrayList;

public class Incendio extends Ocorrencia {
    protected int nivelAlarme; // 1 a 5
    protected int tempoEstimadoCombate; // em minutos

    // Construtor sem parâmetros
    public Incendio() {
        this("Desconhecido", new ArrayList<String>(), 1, 0);
    }

    // Construtor com parâmetros
    public Incendio(String localizacao, ArrayList<String> viaturasAtribuidas, int nivelAlarme,
            int tempoEstimadoCombate) {
        super(localizacao, viaturasAtribuidas);
        this.nivelAlarme = nivelAlarme;
        this.tempoEstimadoCombate = tempoEstimadoCombate;
    }

    // Construtor de cópia
    public Incendio(Incendio outro) {
        super(outro);
        this.nivelAlarme = outro.nivelAlarme;
        this.tempoEstimadoCombate = outro.tempoEstimadoCombate;
    }

    // Getters e Setters
    public int getNivelAlarme() {
        return nivelAlarme;
    }

    public void setNivelAlarme(int nivelAlarme) {
        this.nivelAlarme = nivelAlarme;
    }

    public int getTempoEstimadoCombate() {
        return tempoEstimadoCombate;
    }

    public void setTempoEstimadoCombate(int tempoEstimadoCombate) {
        this.tempoEstimadoCombate = tempoEstimadoCombate;
    }

    @Override
    public void print() {
        super.print();
        System.out.println("   └─> [Incêndio Geral] Alarme Nível: " + nivelAlarme + " | Tempo Est.: "
                + tempoEstimadoCombate + " min");
    }

    @Override
    public String toString() {
        return super.toString() + " | Tipo: Incêndio";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        Incendio outro = (Incendio) obj;
        return nivelAlarme == outro.nivelAlarme && tempoEstimadoCombate == outro.tempoEstimadoCombate;
    }

    @Override
    public Object clone() {
        return new Incendio(this);
    }
}