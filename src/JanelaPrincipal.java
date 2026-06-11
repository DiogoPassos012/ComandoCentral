import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.*;

public class JanelaPrincipal extends JFrame {
    private ArrayList<Ocorrencia> listaOcorrencias;
    private ArrayList<Viatura> frotaViaturas; // Lista de todas as viaturas do quartel
    private ArrayList<Viatura> viaturasSelecionadasParaNovaOcorrencia;
    // Componentes Visuais
    private DefaultListModel<String> listModel;
    private JList<String> jListOcorrencias;
    private JTextField txtLocalizacao;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboFreguesia;
    private JComboBox<String> comboTipoLocal;
    private JLabel lblViaturaSelecionada; // Mostra a viatura escolhida
    private Viatura viaturaEscolhida = null;

    public JanelaPrincipal() {
        listaOcorrencias = new ArrayList<>();
        frotaViaturas = new ArrayList<>();
        viaturasSelecionadasParaNovaOcorrencia = new ArrayList<>();
        listModel = new DefaultListModel<>();

        // Popular a frota de viaturas do quartel para teste
        inicializarFrota();

        // Configurações Básicas da Janela
        setTitle("COMANDO CENTRAL - Controlo e Gestão de Ocorrências");
        setSize(900, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. PAINEL SUPERIOR
        JPanel panelTopo = new JPanel();
        panelTopo.setBackground(new Color(24, 34, 54));
        JLabel lblTitulo = new JLabel("SISTEMA DE DESPACHO E GESTÃO DE MEIOS v1.5");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTopo.add(lblTitulo);
        add(panelTopo, BorderLayout.NORTH);

        // 2. PAINEL ESQUERDO (Formulário)
        JPanel panelEsquerdo = new JPanel(new GridLayout(10, 1, 5, 5));
        panelEsquerdo.setBorder(BorderFactory.createTitledBorder("Registar Ocorrência"));
        panelEsquerdo.setPreferredSize(new Dimension(320, 400));

        panelEsquerdo.add(new JLabel(" Tipo de Ocorrência:"));
        comboTipo = new JComboBox<>(
                new String[] { "Emergência Médica", "Acidente de Viação", "Incêndio Urbano", "Incêndio Florestal" });
        panelEsquerdo.add(comboTipo);

        panelEsquerdo.add(new JLabel(" Localização e Tipo de Local:"));
        JPanel panelLocalInput = new JPanel(new BorderLayout(5, 5));
        txtLocalizacao = new JTextField();
        comboTipoLocal = new JComboBox<>(new String[] { "Habitação", "Estabelecimento Público", "Via Pública" });
        panelLocalInput.add(txtLocalizacao, BorderLayout.CENTER);
        panelLocalInput.add(comboTipoLocal, BorderLayout.EAST);
        panelEsquerdo.add(panelLocalInput);

        panelEsquerdo.add(new JLabel(" Freguesia (V. N. Gaia):"));
        comboFreguesia = new JComboBox<>(new String[] {
            "Arcozelo", "Avintes", "Canelas", "Canidelo", 
            "Grijó e Sermonde", "Gulpilhares e Valadares", "Madalena", 
            "Mafamude e Vilar do Paraíso", "Oliveira do Douro", 
            "Pedroso e Seixezelo", "Sandim, Olival, Lever e Crestuma", 
            "Santa Marinha e São Pedro da Afurada", "São Félix da Marinha", 
            "Vilar de Andorinho"
        });
        panelEsquerdo.add(comboFreguesia);

        panelEsquerdo.add(new JLabel(" Viatura para Despacho:"));

        // Sub-painel para o botão de seleção e exibição da viatura
        JPanel panelViaturaSelec = new JPanel(new BorderLayout(5, 5));
        lblViaturaSelecionada = new JLabel("[ Nenhuma Selecionada ]");
        lblViaturaSelecionada.setForeground(Color.RED);
        lblViaturaSelecionada.setFont(new Font("Arial", Font.BOLD, 11));
        JButton btnEscolherViatura = new JButton("Escolher...");
        panelViaturaSelec.add(lblViaturaSelecionada, BorderLayout.CENTER);
        panelViaturaSelec.add(btnEscolherViatura, BorderLayout.EAST);
        panelEsquerdo.add(panelViaturaSelec);

        JButton btnAdicionar = new JButton("Despachar Meio");
        btnAdicionar.setBackground(new Color(34, 139, 34));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 12));
        panelEsquerdo.add(new JLabel("")); // Espaçador
        panelEsquerdo.add(btnAdicionar);
        add(panelEsquerdo, BorderLayout.WEST);

        // 3. PAINEL CENTRAL (Lista)
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Ocorrências Ativas na Central"));

        jListOcorrencias = new JList<>(listModel);
        jListOcorrencias.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(jListOcorrencias);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        JButton btnAvancar = new JButton("Alterar Estado da Ocorrência");
        btnAvancar.setBackground(new Color(220, 53, 69));
        btnAvancar.setForeground(Color.WHITE);
        btnAvancar.setFont(new Font("Arial", Font.BOLD, 12));
        panelCentral.add(btnAvancar, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        // --- AÇÕES DOS BOTÕES ---

        // Ação: Abrir a Janela de Seleção de Viaturas por Abas (Opção B)
        btnEscolherViatura.addActionListener(e -> abrirJanelaSelecaoViatura());

        // Ação: Criar ocorrência e ocupar viatura
        btnAdicionar.addActionListener(e -> {
            String local = txtLocalizacao.getText();

            // 1. Verificação de segurança: garante que há local e viaturas escolhidas
            if (local.isEmpty() || viaturasSelecionadasParaNovaOcorrencia.isEmpty()) {
                JOptionPane.showMessageDialog(JanelaPrincipal.this,
                        "Insira a localização e selecione pelo menos uma viatura!", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String tipo = (String) comboTipo.getSelectedItem();
            Ocorrencia nova = null;

            // 2. Extrair os IDs das viaturas selecionadas para um ArrayList<String>
            ArrayList<String> idsViaturas = new ArrayList<>();
            for (Viatura v : viaturasSelecionadasParaNovaOcorrencia) {
                idsViaturas.add(v.getId());
                v.setDisponivel(false); // Tranca a viatura na frota global
            }

            // Concatenar localidade, tipo de local e freguesia para guardar como localização da ocorrência
            String tipoLocal = (String) comboTipoLocal.getSelectedItem();
            String freguesia = (String) comboFreguesia.getSelectedItem();
            String localizacaoCompleta = local + " [" + tipoLocal + "] (" + freguesia + ")";

            // 3. Criar a ocorrência correta passando o ArrayList (idsViaturas)
            if (tipo.equals("Emergência Médica")) {
                nova = new EmergenciaMedica(localizacaoCompleta, idsViaturas, 1, "Queixa Geral", 40);
            } else if (tipo.equals("Acidente de Viação")) {
                nova = new AcidenteViacao(localizacaoCompleta, idsViaturas, 2, false, "Via Urbana");
            } else if (tipo.equals("Incêndio Urbano")) {
                nova = new IncendioUrbano(localizacaoCompleta, idsViaturas, 1, 30, "Habitação", 1);
            } else if (tipo.equals("Incêndio Florestal")) {
                nova = new IncendioFlorestal(localizacaoCompleta, idsViaturas, 2, 60, 1.0, "Plano");
            }

            // 4. Se correu tudo bem, adiciona à lista e limpa o ecrã
            if (nova != null) {
                listaOcorrencias.add(nova);
                atualizarListaVisual();

                // Limpar o formulário para uma nova inserção
                txtLocalizacao.setText("");
                comboFreguesia.setSelectedIndex(0);
                comboTipoLocal.setSelectedIndex(0);
                viaturasSelecionadasParaNovaOcorrencia.clear();
                lblViaturaSelecionada.setText("[ Nenhuma Selecionada ]");
                lblViaturaSelecionada.setForeground(Color.RED);
            }
        });

        // Ação: Escolher um estado específico para a ocorrência
        btnAvancar.addActionListener(e -> {
            int index = jListOcorrencias.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(JanelaPrincipal.this, "Selecione uma ocorrência primeiro!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Ocorrencia oc = listaOcorrencias.get(index);
            
            String[] estados = { "Despacho", "No Local", "Concluida" };
            String estadoSelecionado = (String) JOptionPane.showInputDialog(
                    JanelaPrincipal.this,
                    "Selecione o novo estado da ocorrência:",
                    "Alterar Estado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    estados,
                    oc.getEstado()
            );

            if (estadoSelecionado != null) {
                String estadoAntigo = oc.getEstado();
                oc.setEstado(estadoSelecionado);

                // Se mudou para "Concluida" e antes não estava "Concluida", liberta as viaturas
                if (estadoSelecionado.equals("Concluida") && !estadoAntigo.equals("Concluida")) {
                    for (String idViatura : oc.getViaturasAtribuidas()) {
                        libertarViatura(idViatura);
                    }
                }
                
                atualizarListaVisual();
            }
        });
    }

    // Método para abrir a janela flutuante com abas (Opção B)
    private void abrirJanelaSelecaoViatura() {
        JDialog dialog = new JDialog(this, "Selecionar Viaturas Disponíveis", true);
        dialog.setSize(480, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Dica simples para o utilizador
        JLabel lblDica = new JLabel(" Segure a tecla CTRL (ou Shift) para selecionar múltiplas viaturas em cada aba.", JLabel.CENTER);
        lblDica.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDica.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dialog.add(lblDica, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Modelos de lista para cada aba
        DefaultListModel<Viatura> modelAmbulancias = new DefaultListModel<>();
        DefaultListModel<Viatura> modelIncendio = new DefaultListModel<>();
        DefaultListModel<Viatura> modelDesenca = new DefaultListModel<>();

        // Separar as viaturas LIVRES pelas abas correspondentes
        for (Viatura v : frotaViaturas) {
            if (v.isDisponivel()) {
                if (v.getTipo().equals("Ambulancia"))
                    modelAmbulancias.addElement(v);
                else if (v.getTipo().equals("Incendio"))
                    modelIncendio.addElement(v);
                else if (v.getTipo().equals("Desencarceramento"))
                    modelDesenca.addElement(v);
            }
        }

        JList<Viatura> listAmb = new JList<>(modelAmbulancias);
        JList<Viatura> listInc = new JList<>(modelIncendio);
        JList<Viatura> listDes = new JList<>(modelDesenca);

        // Ativar seleção múltipla em todas as abas
        listAmb.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listInc.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listDes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        tabbedPane.addTab("Ambulâncias", new JScrollPane(listAmb));
        tabbedPane.addTab("Combate a Incêndios", new JScrollPane(listInc));
        tabbedPane.addTab("Desencarceramento", new JScrollPane(listDes));

        dialog.add(tabbedPane, BorderLayout.CENTER);

        JButton btnConfirmar = new JButton("Confirmar Seleção");
        dialog.add(btnConfirmar, BorderLayout.SOUTH);

        btnConfirmar.addActionListener(e -> {
            viaturasSelecionadasParaNovaOcorrencia.clear();

            // Recolher os elementos selecionados de todas as listas (abas)
            for (Viatura v : listAmb.getSelectedValuesList()) {
                viaturasSelecionadasParaNovaOcorrencia.add(v);
            }
            for (Viatura v : listInc.getSelectedValuesList()) {
                viaturasSelecionadasParaNovaOcorrencia.add(v);
            }
            for (Viatura v : listDes.getSelectedValuesList()) {
                viaturasSelecionadasParaNovaOcorrencia.add(v);
            }

            if (!viaturasSelecionadasParaNovaOcorrencia.isEmpty()) {
                // Criar um texto bonito com todas as viaturas selecionadas
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < viaturasSelecionadasParaNovaOcorrencia.size(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(viaturasSelecionadasParaNovaOcorrencia.get(i).getId());
                }
                lblViaturaSelecionada.setText(sb.toString());
                lblViaturaSelecionada.setForeground(new Color(34, 139, 34));
                dialog.dispose(); // Fecha o diálogo
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecione pelo menos uma viatura da lista!", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void inicializarFrota() {
        // Ambulâncias
        frotaViaturas.add(new Viatura("ABSC-01", "Ambulância de Socorro Tipo B", "Ambulancia"));
        frotaViaturas.add(new Viatura("ABSC-02", "Ambulância de Socorro Tipo B", "Ambulancia"));
        // Incêndio
        frotaViaturas.add(new Viatura("VUCI-01", "Veículo Urbano de Combate", "Incendio"));
        frotaViaturas.add(new Viatura("VFCI-03", "Veículo Florestal de Combate", "Incendio"));
        // Desencarceramento
        frotaViaturas.add(new Viatura("VSAE-01", "Veículo de Socorro e Assistência", "Desencarceramento"));
    }

    private void libertarViatura(String idViatura) {
        for (Viatura v : frotaViaturas) {
            if (v.getId().equals(idViatura)) {
                v.setDisponivel(true); // Fica livre outra vez
                break;
            }
        }
    }

    private void atualizarListaVisual() {
        listModel.clear();
        for (Ocorrencia oc : listaOcorrencias) {
            String tipoOcorrencia = getTipoOcorrenciaFormatado(oc);
            String detalhes = getDetalhesFormatados(oc);
            String horaFormatada = oc.getHoraAlerta().toString();
            // Tenta obter apenas a parte HH:mm:ss se a string for longa
            if (horaFormatada.length() >= 19) {
                horaFormatada = horaFormatada.substring(11, 19);
            }

            String formatado = String.format(
                "<html><body style='width: 450px; padding: 4px;'>"
                + "<b style='color:#182236;'>ID %d - %s</b> <small style='color:#666666;'>(%s)</small><br>"
                + "<b>Local:</b> %s<br>"
                + "<b>Viaturas:</b> %s &nbsp;|&nbsp; <b>Estado:</b> <font color='%s'><b>%s</b></font><br>"
                + "<small style='color:#555555;'>%s</small>"
                + "</body></html>",
                oc.getId(),
                tipoOcorrencia,
                horaFormatada,
                oc.getLocalizacao(),
                oc.getViaturasAtribuidas().isEmpty() ? "Nenhuma" : String.join(", ", oc.getViaturasAtribuidas()),
                getEstadoCor(oc.getEstado()),
                oc.getEstado(),
                detalhes
            );
            listModel.addElement(formatado);
        }
    }

    private String getTipoOcorrenciaFormatado(Ocorrencia oc) {
        if (oc instanceof EmergenciaMedica) return "Emergência Médica";
        if (oc instanceof AcidenteViacao) return "Acidente de Viação";
        if (oc instanceof IncendioUrbano) return "Incêndio Urbano";
        if (oc instanceof IncendioFlorestal) return "Incêndio Florestal";
        return "Ocorrência Geral";
    }

    private String getDetalhesFormatados(Ocorrencia oc) {
        if (oc instanceof EmergenciaMedica) {
            EmergenciaMedica m = (EmergenciaMedica) oc;
            return "Vítimas: " + m.getNumVitimas() + " | Queixa: " + m.getQueixaPrincipal() + " | Idade: " + m.getIdadePaciente();
        }
        if (oc instanceof AcidenteViacao) {
            AcidenteViacao a = (AcidenteViacao) oc;
            return "Veículos: " + a.getNumVeiculos() + " | Encarcerados: " + (a.isPresencaEncarcerados() ? "Sim" : "Não") + " | Via: " + a.getTipoVia();
        }
        if (oc instanceof IncendioUrbano) {
            IncendioUrbano u = (IncendioUrbano) oc;
            return "Nível Alarme: " + u.getNivelAlarme() + " | Edifício: " + u.getTipoEdificio() + " | Pisos: " + u.getNumPisosAfetados();
        }
        if (oc instanceof IncendioFlorestal) {
            IncendioFlorestal f = (IncendioFlorestal) oc;
            return "Nível Alarme: " + f.getNivelAlarme() + " | Área: " + f.getAreaAfetadaHectares() + " ha | Terreno: " + f.getRelevoTerreno();
        }
        return "";
    }

    private String getEstadoCor(String estado) {
        if ("Despacho".equals(estado)) return "#FF8C00"; // DarkOrange
        if ("No Local".equals(estado)) return "#008B8B"; // DarkCyan
        if ("Concluida".equals(estado)) return "#228B22"; // ForestGreen
        return "#000000";
    }
}