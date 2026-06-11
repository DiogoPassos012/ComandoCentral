import java.util.ArrayList;
import model.*;

public class App {
    public static void main(String[] args) {
        System.out.println("=== COMANDO CENTRAL - TESTE DE SELEÇÃO DINÂMICA DE MÉTODOS ===");

        // 1. Criar uma coleção polimórfica (ArrayList que aceita qualquer tipo de
        // Ocorrência)
        ArrayList<Ocorrencia> listaOcorrencias = new ArrayList<>();

        // 2. Criar instâncias de cada uma das subclasses usando os construtores com
        // parâmetros
        EmergenciaMedica med = new EmergenciaMedica("Rua da Escola, Gaia", "ABSC-01", 1, "Dor Torácica", 54);
        AcidenteViacao aci = new AcidenteViacao("Ponte do Freixo", "VSAE-02", 2, false, "Autoestrada A1");
        IncendioUrbano urb = new IncendioUrbano("Av. da República", "VUCI-01", 2, 30, "Apartamento", 3);
        IncendioFlorestal flo = new IncendioFlorestal("Monte da Virgem", "VFCI-05", 4, 120, 2.5, "Montanhoso");

        // 3. Adicionar todas as sub-classes à mesma lista da super-classe
        // (Polimorfismo)
        listaOcorrencias.add(med);
        listaOcorrencias.add(aci);
        listaOcorrencias.add(urb);
        listaOcorrencias.add(flo);

        // Testar o método da interface antes de imprimir
        System.out.println(">>> A alterar o estado da primeira ocorrência (Emergência Médica)...");
        listaOcorrencias.get(0).avancarEstado(); // Passa de "Despacho" para "No Local"

        // Percorrer a lista e disparar o método print()
        for (Ocorrencia oc : listaOcorrencias) {
            oc.print();
            System.out.println("----------------------------------------------------------------------");
        }
    }
}