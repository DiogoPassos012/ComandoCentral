import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Inicializa a Interface Gráfica de forma segura
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JanelaPrincipal janela = new JanelaPrincipal();
                janela.setVisible(true);
            }
        });
    }
}