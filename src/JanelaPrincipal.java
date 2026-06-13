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

    // Detalhes Dinâmicos - Emergência Médica
    private JSpinner spinMedVitimas;
    private JTextField txtMedQueixa;
    private JSpinner spinMedIdade;

    // Detalhes Dinâmicos - Acidente de Viação
    private JSpinner spinAciVeiculos;
    private JComboBox<String> comboAciEncarcerados;
    private JComboBox<String> comboAciVia;

    // Detalhes Dinâmicos - Incêndio Urbano
    private JComboBox<Integer> comboUrbAlarme;
    private JTextField txtUrbTempo;
    private JComboBox<String> comboUrbEdificio;
    private JSpinner spinUrbPisos;

    // Detalhes Dinâmicos - Incêndio Florestal
    private JComboBox<Integer> comboFloAlarme;
    private JTextField txtFloTempo;
    private JTextField txtFloArea;
    private JComboBox<String> comboFloRelevo;

    public JanelaPrincipal() {
        listaOcorrencias = Database.carregarOcorrencias();
        frotaViaturas = new ArrayList<>();
        viaturasSelecionadasParaNovaOcorrencia = new ArrayList<>();
        listModel = new DefaultListModel<>();

        // Popular a frota de viaturas do quartel para teste
        inicializarFrota();

        // Configurações Básicas da Janela
        setTitle("COMANDO CENTRAL - Controlo e Gestão de Ocorrências");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. PAINEL SUPERIOR
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBackground(new Color(24, 34, 54));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel lblTitulo = new JLabel("Sistema de Controlo e Gestão de Ocorrências");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTopo.add(lblTitulo, BorderLayout.WEST);

        JButton btnGerirViaturas = new JButton("Gerir Viaturas");
        btnGerirViaturas.setBackground(new Color(41, 128, 185));
        btnGerirViaturas.setForeground(Color.WHITE);
        btnGerirViaturas.setFont(new Font("Arial", Font.BOLD, 12));
        btnGerirViaturas.setFocusPainted(false);
        btnGerirViaturas.addActionListener(e -> abrirJanelaGerirViaturas());
        panelTopo.add(btnGerirViaturas, BorderLayout.EAST);

        add(panelTopo, BorderLayout.NORTH);

        // 2. PAINEL ESQUERDO (Formulário)
        JPanel panelEsquerdo = new JPanel(new BorderLayout(5, 5));
        panelEsquerdo.setBorder(BorderFactory.createTitledBorder("Registar Ocorrência"));
        panelEsquerdo.setPreferredSize(new Dimension(320, 500));

        // Painel para os Campos Comuns
        JPanel panelComum = new JPanel(new GridLayout(8, 1, 3, 3));

        panelComum.add(new JLabel(" Tipo de Ocorrência:"));
        comboTipo = new JComboBox<>(
                new String[] { "Emergência Médica", "Acidente de Viação", "Incêndio Urbano", "Incêndio Florestal" });
        panelComum.add(comboTipo);

        panelComum.add(new JLabel(" Localização e Tipo de Local:"));
        JPanel panelLocalInput = new JPanel(new BorderLayout(5, 5));
        txtLocalizacao = new JTextField();
        comboTipoLocal = new JComboBox<>(new String[] { "Habitação", "Estabelecimento Público", "Via Pública" });
        panelLocalInput.add(txtLocalizacao, BorderLayout.CENTER);
        panelLocalInput.add(comboTipoLocal, BorderLayout.EAST);
        panelComum.add(panelLocalInput);

        panelComum.add(new JLabel(" Freguesia (V. N. Gaia):"));
        comboFreguesia = new JComboBox<>(new String[] {
                "Arcozelo", "Avintes", "Canelas", "Canidelo",
                "Grijó e Sermonde", "Gulpilhares e Valadares", "Madalena",
                "Mafamude e Vilar do Paraíso", "Oliveira do Douro",
                "Pedroso e Seixezelo", "Sandim, Olival, Lever e Crestuma",
                "Santa Marinha e São Pedro da Afurada", "São Félix da Marinha",
                "Vilar de Andorinho"
        });
        panelComum.add(comboFreguesia);

        panelComum.add(new JLabel(" Viatura para Despacho:"));

        // Sub-painel para o botão de seleção e exibição da viatura
        JPanel panelViaturaSelec = new JPanel(new BorderLayout(5, 5));
        lblViaturaSelecionada = new JLabel("[ Nenhuma Selecionada ]");
        lblViaturaSelecionada.setForeground(Color.RED);
        lblViaturaSelecionada.setFont(new Font("Arial", Font.BOLD, 11));
        JButton btnEscolherViatura = new JButton("Escolher...");
        panelViaturaSelec.add(lblViaturaSelecionada, BorderLayout.CENTER);
        panelViaturaSelec.add(btnEscolherViatura, BorderLayout.EAST);
        panelComum.add(panelViaturaSelec);

        panelEsquerdo.add(panelComum, BorderLayout.NORTH);

        // Painel para os Detalhes Dinâmicos (CardLayout)
        CardLayout cardLayout = new CardLayout();
        JPanel panelDinamico = new JPanel(cardLayout);

        // Card 1: Emergência Médica
        JPanel cardMedica = new JPanel(new GridLayout(3, 2, 5, 5));
        cardMedica.setBorder(BorderFactory.createTitledBorder("Detalhes Médicos"));
        spinMedVitimas = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        txtMedQueixa = new JTextField();
        spinMedIdade = new JSpinner(new SpinnerNumberModel(40, 0, 120, 1));
        cardMedica.add(new JLabel("Nº de Vítimas:"));
        cardMedica.add(spinMedVitimas);
        cardMedica.add(new JLabel("Queixa Principal:"));
        cardMedica.add(txtMedQueixa);
        cardMedica.add(new JLabel("Idade do Paciente:"));
        cardMedica.add(spinMedIdade);

        // Card 2: Acidente de Viação
        JPanel cardAcidente = new JPanel(new GridLayout(3, 2, 5, 5));
        cardAcidente.setBorder(BorderFactory.createTitledBorder("Detalhes do Acidente"));
        spinAciVeiculos = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));
        comboAciEncarcerados = new JComboBox<>(new String[] { "Não", "Sim" });
        comboAciVia = new JComboBox<>(new String[] { "Autoestrada", "Via Urbana", "Estrada Nacional", "Outra" });
        cardAcidente.add(new JLabel("Veículos Envolvidos:"));
        cardAcidente.add(spinAciVeiculos);
        cardAcidente.add(new JLabel("Há Encarcerados?"));
        cardAcidente.add(comboAciEncarcerados);
        cardAcidente.add(new JLabel("Tipo de Via:"));
        cardAcidente.add(comboAciVia);

        // Card 3: Incêndio Urbano
        JPanel cardUrbano = new JPanel(new GridLayout(4, 2, 3, 3));
        cardUrbano.setBorder(BorderFactory.createTitledBorder("Incêndio Urbano"));
        comboUrbAlarme = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
        txtUrbTempo = new JTextField("30");
        comboUrbEdificio = new JComboBox<>(new String[] { "Habitação", "Comercial", "Industrial", "Outro" });
        spinUrbPisos = new JSpinner(new SpinnerNumberModel(1, 1, 150, 1));
        cardUrbano.add(new JLabel("Nível Alarme:"));
        cardUrbano.add(comboUrbAlarme);
        cardUrbano.add(new JLabel("Tempo Est. (min):"));
        cardUrbano.add(txtUrbTempo);
        cardUrbano.add(new JLabel("Tipo Edifício:"));
        cardUrbano.add(comboUrbEdificio);
        cardUrbano.add(new JLabel("Pisos Afetados:"));
        cardUrbano.add(spinUrbPisos);

        // Card 4: Incêndio Florestal
        JPanel cardFlorestal = new JPanel(new GridLayout(4, 2, 3, 3));
        cardFlorestal.setBorder(BorderFactory.createTitledBorder("Incêndio Florestal"));
        comboFloAlarme = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5 });
        txtFloTempo = new JTextField("60");
        txtFloArea = new JTextField("1.0");
        comboFloRelevo = new JComboBox<>(new String[] { "Plano", "Acidentado", "Montanhoso" });
        cardFlorestal.add(new JLabel("Nível Alarme:"));
        cardFlorestal.add(comboFloAlarme);
        cardFlorestal.add(new JLabel("Tempo Est. (min):"));
        cardFlorestal.add(txtFloTempo);
        cardFlorestal.add(new JLabel("Área Afetada (ha):"));
        cardFlorestal.add(txtFloArea);
        cardFlorestal.add(new JLabel("Relevo Terreno:"));
        cardFlorestal.add(comboFloRelevo);

        panelDinamico.add(cardMedica, "Emergência Médica");
        panelDinamico.add(cardAcidente, "Acidente de Viação");
        panelDinamico.add(cardUrbano, "Incêndio Urbano");
        panelDinamico.add(cardFlorestal, "Incêndio Florestal");

        panelEsquerdo.add(panelDinamico, BorderLayout.CENTER);

        // Painel do Botão e Ação
        JPanel panelAcao = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnAdicionar = new JButton("Despachar Meio");
        btnAdicionar.setBackground(new Color(34, 139, 34));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 12));
        panelAcao.add(new JLabel("")); // Espaçador
        panelAcao.add(btnAdicionar);
        panelEsquerdo.add(panelAcao, BorderLayout.SOUTH);
        add(panelEsquerdo, BorderLayout.WEST);

        // Listener para alternar os cards
        comboTipo.addActionListener(e -> {
            String selecionado = (String) comboTipo.getSelectedItem();
            cardLayout.show(panelDinamico, selecionado);
        });

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

            // Concatenar localidade, tipo de local e freguesia para guardar como
            // localização da ocorrência
            String tipoLocal = (String) comboTipoLocal.getSelectedItem();
            String freguesia = (String) comboFreguesia.getSelectedItem();
            String localizacaoCompleta = local + " [" + tipoLocal + "] (" + freguesia + ")";

            // 3. Criar a ocorrência correta passando o ArrayList (idsViaturas) e os dados
            // dinâmicos do formulário
            if (tipo.equals("Emergência Médica")) {
                int numVitimas = (Integer) spinMedVitimas.getValue();
                String queixa = txtMedQueixa.getText();
                int idade = (Integer) spinMedIdade.getValue();
                nova = new EmergenciaMedica(localizacaoCompleta, idsViaturas, numVitimas, queixa, idade);
            } else if (tipo.equals("Acidente de Viação")) {
                int numVeiculos = (Integer) spinAciVeiculos.getValue();
                boolean enc = comboAciEncarcerados.getSelectedItem().equals("Sim");
                String via = (String) comboAciVia.getSelectedItem();
                nova = new AcidenteViacao(localizacaoCompleta, idsViaturas, numVeiculos, enc, via);
            } else if (tipo.equals("Incêndio Urbano")) {
                int alarme = (Integer) comboUrbAlarme.getSelectedItem();
                int tempo = 30;
                try {
                    tempo = Integer.parseInt(txtUrbTempo.getText().trim());
                } catch (NumberFormatException ex) {
                }
                String ed = (String) comboUrbEdificio.getSelectedItem();
                int pisos = (Integer) spinUrbPisos.getValue();
                nova = new IncendioUrbano(localizacaoCompleta, idsViaturas, alarme, tempo, ed, pisos);
            } else if (tipo.equals("Incêndio Florestal")) {
                int alarme = (Integer) comboFloAlarme.getSelectedItem();
                int tempo = 60;
                try {
                    tempo = Integer.parseInt(txtFloTempo.getText().trim());
                } catch (NumberFormatException ex) {
                }
                double area = 1.0;
                try {
                    area = Double.parseDouble(txtFloArea.getText().trim());
                } catch (NumberFormatException ex) {
                }
                String rel = (String) comboFloRelevo.getSelectedItem();
                nova = new IncendioFlorestal(localizacaoCompleta, idsViaturas, alarme, tempo, area, rel);
            }

            // 4. Se correu tudo bem, adiciona à lista e limpa o ecrã
            if (nova != null) {
                Database.gravarOcorrencia(tipo, local, "Despacho", idsViaturas);
                listaOcorrencias.add(nova);
                atualizarListaVisual();

                // Limpar o formulário para uma nova inserção
                txtLocalizacao.setText("");
                comboFreguesia.setSelectedIndex(0);
                comboTipoLocal.setSelectedIndex(0);

                // Limpar campos de detalhes
                spinMedVitimas.setValue(1);
                txtMedQueixa.setText("");
                spinMedIdade.setValue(40);

                spinAciVeiculos.setValue(2);
                comboAciEncarcerados.setSelectedIndex(0);
                comboAciVia.setSelectedIndex(0);

                comboUrbAlarme.setSelectedIndex(0);
                txtUrbTempo.setText("30");
                comboUrbEdificio.setSelectedIndex(0);
                spinUrbPisos.setValue(1);

                comboFloAlarme.setSelectedIndex(0);
                txtFloTempo.setText("60");
                txtFloArea.setText("1.0");
                comboFloRelevo.setSelectedIndex(0);

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
                    oc.getEstado());

            if (estadoSelecionado != null) {
                String estadoAntigo = oc.getEstado();
                oc.setEstado(estadoSelecionado);
                Database.atualizarEstadoOcorrencia(oc.getId(), estadoSelecionado);

                // Se mudou para "Concluida" e antes não estava "Concluida", liberta as viaturas
                if (estadoSelecionado.equals("Concluida") && !estadoAntigo.equals("Concluida")) {
                    for (String idViatura : oc.getViaturasAtribuidas()) {
                        libertarViatura(idViatura);
                    }
                }
                atualizarListaVisual();
            }
        });

        atualizarListaVisual();
    }

    // Método para abrir a janela flutuante com abas (Opção B)
    private void abrirJanelaSelecaoViatura() {
        JDialog dialog = new JDialog(this, "Selecionar Viaturas Disponíveis", true);
        dialog.setSize(480, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Dica simples para o utilizador
        JLabel lblDica = new JLabel(" Segure a tecla CTRL (ou Shift) para selecionar múltiplas viaturas em cada aba.",
                JLabel.CENTER);
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
                    if (i > 0)
                        sb.append(", ");
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

    // Método para abrir o gestor de viaturas (Adicionar/Remover)
    private void abrirJanelaGerirViaturas() {
        // Limpar seleções ativas na janela principal para evitar inconsistências
        viaturasSelecionadasParaNovaOcorrencia.clear();
        lblViaturaSelecionada.setText("[ Nenhuma Selecionada ]");
        lblViaturaSelecionada.setForeground(Color.RED);

        JDialog dialog = new JDialog(this, "Gerir Frota de Viaturas", true);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        // Painel para a listagem das viaturas
        JPanel panelList = new JPanel(new BorderLayout(5, 5));
        panelList.setBorder(BorderFactory.createTitledBorder("Frota Atual"));

        DefaultListModel<Viatura> modelFrota = new DefaultListModel<>();
        for (Viatura v : frotaViaturas) {
            modelFrota.addElement(v);
        }
        JList<Viatura> listFrota = new JList<>(modelFrota);
        listFrota.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panelList.add(new JScrollPane(listFrota), BorderLayout.CENTER);

        JButton btnEliminar = new JButton("Remover Viatura Selecionada");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
        panelList.add(btnEliminar, BorderLayout.SOUTH);

        dialog.add(panelList, BorderLayout.CENTER);

        // Painel para adicionar novas viaturas
        JPanel panelAdicionar = new JPanel(new GridBagLayout());
        panelAdicionar.setBorder(BorderFactory.createTitledBorder("Adicionar Nova Viatura"));
        panelAdicionar.setPreferredSize(new Dimension(250, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // ID / Matrícula
        gbc.gridy = 0;
        panelAdicionar.add(new JLabel("Matrícula:"), gbc);
        gbc.gridy = 1;
        JTextField txtId = new JTextField();
        panelAdicionar.add(txtId, gbc);

        // Nome
        gbc.gridy = 2;
        panelAdicionar.add(new JLabel("Designação /Nr de Cauda:"), gbc);
        gbc.gridy = 3;
        JTextField txtNome = new JTextField();
        panelAdicionar.add(txtNome, gbc);

        // Tipo
        gbc.gridy = 4;
        panelAdicionar.add(new JLabel("Tipo de Viatura:"), gbc);
        gbc.gridy = 5;
        JComboBox<String> comboTipoViatura = new JComboBox<>(new String[] {
                "Ambulancia", "Incendio", "Desencarceramento"
        });
        panelAdicionar.add(comboTipoViatura, gbc);

        // Botão Adicionar
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 5, 10);
        JButton btnAddViatura = new JButton("Adicionar Viatura");
        btnAddViatura.setBackground(new Color(40, 167, 69));
        btnAddViatura.setForeground(Color.WHITE);
        btnAddViatura.setFont(new Font("Arial", Font.BOLD, 12));
        panelAdicionar.add(btnAddViatura, gbc);

        dialog.add(panelAdicionar, BorderLayout.EAST);

        // Ações do Botão Adicionar
        btnAddViatura.addActionListener(e -> {
            String id = txtId.getText().trim().toUpperCase();
            String nome = txtNome.getText().trim();
            String tipo = (String) comboTipoViatura.getSelectedItem();

            if (id.isEmpty() || nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, preencha todos os campos!", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verificar se ID já existe
            for (Viatura v : frotaViaturas) {
                if (v.getId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(dialog, "Já existe uma viatura com esse ID/Matrícula!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Viatura nova = new Viatura(id, nome, tipo);
            if (Database.adicionarViatura(nova)) {
                frotaViaturas.add(nova);
                modelFrota.addElement(nova);
                txtId.setText("");
                txtNome.setText("");
                JOptionPane.showMessageDialog(dialog, "Viatura adicionada com sucesso!", "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Erro ao gravar viatura na base de dados.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ações do Botão Remover
        btnEliminar.addActionListener(e -> {
            Viatura selecionada = listFrota.getSelectedValue();
            if (selecionada == null) {
                JOptionPane.showMessageDialog(dialog, "Selecione uma viatura para remover!", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Impedir remoção se estiver em serviço
            if (!selecionada.isDisponivel()) {
                JOptionPane.showMessageDialog(dialog, "Não é possível remover uma viatura em serviço!", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Tem a certeza que deseja remover a viatura " + selecionada.getId() + "?",
                    "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (Database.eliminarViatura(selecionada.getId())) {
                    frotaViaturas.remove(selecionada);
                    modelFrota.removeElement(selecionada);
                    JOptionPane.showMessageDialog(dialog, "Viatura removida com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao remover viatura da base de dados.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setVisible(true);
    }

    private void inicializarFrota() {
        // Carrega a frota diretamente do SQL Local
        frotaViaturas = Database.carregarFrota();
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
                    detalhes);
            listModel.addElement(formatado);
        }
    }

    private String getTipoOcorrenciaFormatado(Ocorrencia oc) {
        if (oc instanceof EmergenciaMedica)
            return "Emergência Médica";
        if (oc instanceof AcidenteViacao)
            return "Acidente de Viação";
        if (oc instanceof IncendioUrbano)
            return "Incêndio Urbano";
        if (oc instanceof IncendioFlorestal)
            return "Incêndio Florestal";
        return "Ocorrência Geral";
    }

    private String getDetalhesFormatados(Ocorrencia oc) {
        if (oc instanceof EmergenciaMedica) {
            EmergenciaMedica m = (EmergenciaMedica) oc;
            return "Vítimas: " + m.getNumVitimas() + " | Queixa: " + m.getQueixaPrincipal() + " | Idade: "
                    + m.getIdadePaciente();
        }
        if (oc instanceof AcidenteViacao) {
            AcidenteViacao a = (AcidenteViacao) oc;
            return "Veículos: " + a.getNumVeiculos() + " | Encarcerados: "
                    + (a.isPresencaEncarcerados() ? "Sim" : "Não") + " | Via: " + a.getTipoVia();
        }
        if (oc instanceof IncendioUrbano) {
            IncendioUrbano u = (IncendioUrbano) oc;
            return "Nível Alarme: " + u.getNivelAlarme() + " | Edifício: " + u.getTipoEdificio() + " | Pisos: "
                    + u.getNumPisosAfetados();
        }
        if (oc instanceof IncendioFlorestal) {
            IncendioFlorestal f = (IncendioFlorestal) oc;
            return "Nível Alarme: " + f.getNivelAlarme() + " | Área: " + f.getAreaAfetadaHectares() + " ha | Terreno: "
                    + f.getRelevoTerreno();
        }
        return "";
    }

    private String getEstadoCor(String estado) {
        if ("Despacho".equals(estado))
            return "#FF8C00"; // DarkOrange
        if ("No Local".equals(estado))
            return "#008B8B"; // DarkCyan
        if ("Concluida".equals(estado))
            return "#228B22"; // ForestGreen
        return "#000000";
    }
}