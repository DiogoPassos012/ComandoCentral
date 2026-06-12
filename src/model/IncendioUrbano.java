package model;

import java.util.ArrayList;
import java.util.Objects;

public class IncendioUrbano extends Incendio {
    private String tipoEdificio; // "Habitação", "Comercial", "Industrial"
    private int numPisosAfetados;

    // Construtor sem parâmetros
    public IncendioUrbano() {
        this("Desconhecido", new ArrayList<String>(), 1, 0, "Não especificado", 1);
    }

    // Construtor com parâmetros - Usa o super para passar os dados para a classe
    // Incendio
    public IncendioUrbano(String localizacao, ArrayList<String> viaturasAtribuidas, int nivelAlarme,
            int tempoEstimadoCombate, String tipoEdificio, int numPisosAfetados) {
        super(localizacao, viaturasAtribuidas, nivelAlarme, tempoEstimadoCombate);
        this.tipoEdificio = tipoEdificio;
        this.numPisosAfetados = numPisosAfetados;
    }

    // Construtor de cópia - Usa o super para copiar a parte do Incendio
    public IncendioUrbano(IncendioUrbano outro) {
        super(outro);
        this.tipoEdificio = outro.tipoEdificio;
        this.numPisosAfetados = outro.numPisosAfetados;
    }

    // Getters e Setters
    public String getTipoEdificio() {
        return tipoEdificio;
    }

    public void setTipoEdificio(String tipoEdificio) {
        this.tipoEdificio = tipoEdificio;
    }

    public int getNumPisosAfetados() {
        return numPisosAfetados;
    }

    public void setNumPisosAfetados(int numPisosAfetados) {
        this.numPisosAfetados = numPisosAfetados;
    }

    // Sobreposição do método print() combinando super.print() com dados específicos
    @Override
    public void print() {
        super.print();
        System.out.println("      └─> [Especificação Urbano] Edifício: " + tipoEdificio + " | Pisos Afetados: "
                + numPisosAfetados);
    }

    @Override
    public String toString() {
        return super.toString() + " -> Urbano";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        IncendioUrbano outro = (IncendioUrbano) obj;
        return numPisosAfetados == outro.numPisosAfetados &&
                Objects.equals(tipoEdificio, outro.tipoEdificio);
    }

    @Override
    public Object clone() {
        return new IncendioUrbano(this);
    }
}