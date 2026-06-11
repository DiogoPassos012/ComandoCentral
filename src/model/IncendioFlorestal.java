package model;

import java.util.Objects;

public class IncendioFlorestal extends Incendio {
    private double areaAfetadaHectares;
    private String relevoTerreno; // "Plano", "Acidentado", "Montanhoso"

    // Construtor sem parâmetros
    public IncendioFlorestal() {
        this("Desconhecido", "Nenhuma Viatura", 1, 0, 0.0, "Não especificado");
    }

    // Construtor com parâmetros
    public IncendioFlorestal(String localizacao, String viaturaAtribuida, int nivelAlarme, int tempoEstimadoCombate,
            double areaAfetadaHectares, String relevoTerreno) {
        super(localizacao, viaturaAtribuida, nivelAlarme, tempoEstimadoCombate);
        this.areaAfetadaHectares = areaAfetadaHectares;
        this.relevoTerreno = relevoTerreno;
    }

    // Construtor de cópia
    public IncendioFlorestal(IncendioFlorestal outro) {
        super(outro);
        this.areaAfetadaHectares = outro.areaAfetadaHectares;
        this.relevoTerreno = outro.relevoTerreno;
    }

    // Getters e Setters
    public double getAreaAfetadaHectares() {
        return areaAfetadaHectares;
    }

    public void setAreaAfetadaHectares(double areaAfetadaHectares) {
        this.areaAfetadaHectares = areaAfetadaHectares;
    }

    public String getRelevoTerreno() {
        return relevoTerreno;
    }

    public void setRelevoTerreno(String relevoTerreno) {
        this.relevoTerreno = relevoTerreno;
    }

    @Override
    public void print() {
        super.print();
        System.out.println(
                "      └─> [Especificação Florestal] Área: " + areaAfetadaHectares + " ha | Relevo: " + relevoTerreno);
    }

    @Override
    public String toString() {
        return super.toString() + " -> Florestal";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        IncendioFlorestal outro = (IncendioFlorestal) obj;
        return Double.compare(outro.areaAfetadaHectares, areaAfetadaHectares) == 0 &&
                Objects.equals(relevoTerreno, outro.relevoTerreno);
    }

    @Override
    public Object clone() {
        return new IncendioFlorestal(this);
    }
}