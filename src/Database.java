import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
        // O SQLite cria automaticamente este ficheiro "central.db" na raiz do teu
        // projeto
        private static final String URL = "jdbc:sqlite:central.db";

        public static Connection getConnection() throws Exception {
                return DriverManager.getConnection(URL);
        }

        public static void inicializarBD() {
                try {
                        // Força o Java a carregar o driver do SQLite na memória
                        Class.forName("org.sqlite.JDBC");

                        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

                                // 1. Tabela de Ocorrências
                                stmt.execute("CREATE TABLE IF NOT EXISTS ocorrencias (" +
                                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                "tipo TEXT NOT NULL," +
                                                "localizacao TEXT NOT NULL," +
                                                "estado TEXT NOT NULL" +
                                                ");");

                                // 2. Tabela de Viaturas
                                stmt.execute("CREATE TABLE IF NOT EXISTS viaturas (" +
                                                "id TEXT PRIMARY KEY," +
                                                "nome TEXT NOT NULL," +
                                                "tipo TEXT NOT NULL," +
                                                "disponivel INTEGER NOT NULL DEFAULT 1" +
                                                ");");

                                // 3. Tabela Intermédia (Relação Ocorrência <-> Viatura)
                                stmt.execute("CREATE TABLE IF NOT EXISTS ocorrencia_viaturas (" +
                                                "ocorrencia_id INTEGER," +
                                                "viatura_id TEXT," +
                                                "PRIMARY KEY (ocorrencia_id, viatura_id)," +
                                                "FOREIGN KEY (ocorrencia_id) REFERENCES ocorrencias(id) ON DELETE CASCADE,"
                                                +
                                                "FOREIGN KEY (viatura_id) REFERENCES viaturas(id)" +
                                                ");");

                                // 4. Popular a frota inicial corretamente
                                stmt.execute(
                                                "INSERT OR IGNORE INTO viaturas VALUES ('ABSC-01', 'Ambulância de Socorro Tipo B', 'Ambulancia', 1);");
                                stmt.execute(
                                                "INSERT OR IGNORE INTO viaturas VALUES ('ABSC-02', 'Ambulância de Socorro Tipo B', 'Ambulancia', 1);");
                                stmt.execute(
                                                "INSERT OR IGNORE INTO viaturas VALUES ('VUCI-01', 'Veículo Urbano de Combate', 'Incendio', 1);");
                                stmt.execute(
                                                "INSERT OR IGNORE INTO viaturas VALUES ('VFCI-03', 'Veículo Florestal de Combate', 'Incendio', 1);");
                                stmt.execute(
                                                "INSERT OR IGNORE INTO viaturas VALUES ('VSAE-01', 'Veículo de Socorro e Assistência', 'Desencarceramento', 1);");

                                System.out.println("-> SQL Local Inicializado com Sucesso!");
                        }

                } catch (ClassNotFoundException e) {
                        System.out.println("Erro: O driver SQLite não foi encontrado nas Referenced Libraries! "
                                        + e.getMessage());
                } catch (Exception e) {
                        System.out.println("Erro ao inicializar Base de Dados: " + e.getMessage());
                }
        }

        // 1. Carregar as viaturas da Base de Dados para o programa
        public static java.util.ArrayList<model.Viatura> carregarFrota() {
                java.util.ArrayList<model.Viatura> lista = new java.util.ArrayList<>();
                String sql = "SELECT * FROM viaturas";

                try (Connection conn = getConnection();
                                Statement stmt = conn.createStatement();
                                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

                        while (rs.next()) {
                                String id = rs.getString("id");
                                String nome = rs.getString("nome");
                                String tipo = rs.getString("tipo");
                                boolean disponivel = rs.getInt("disponivel") == 1;

                                model.Viatura v = new model.Viatura(id, nome, tipo);
                                v.setDisponivel(disponivel);
                                lista.add(v);
                        }
                } catch (Exception e) {
                        System.out.println("Erro ao carregar frota: " + e.getMessage());
                }
                return lista;
        }

        // 2. Gravar uma nova ocorrência e associar as suas viaturas
        public static void gravarOcorrencia(String tipo, String localizacao, String estado,
                        java.util.ArrayList<String> idsViaturas) {
                String sqlOcorrencia = "INSERT INTO ocorrencias (tipo, localizacao, estado) VALUES (?, ?, ?)";
                String sqlIntermedia = "INSERT INTO ocorrencia_viaturas (ocorrencia_id, viatura_id) VALUES (?, ?)";
                String sqlAtualizarViatura = "UPDATE viaturas SET disponivel = 0 WHERE id = ?";

                try (Connection conn = getConnection()) {
                        conn.setAutoCommit(false); // Ativa transação para gravar tudo junto em segurança

                        // Grava a ocorrência
                        int idOcorrencia = -1;
                        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sqlOcorrencia,
                                        Statement.RETURN_GENERATED_KEYS)) {
                                pstmt.setString(1, tipo);
                                pstmt.setString(2, localizacao);
                                pstmt.setString(3, estado);
                                pstmt.executeUpdate();

                                // Recupera o ID que o SQL gerou automaticamente para esta ocorrência
                                try (java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                                        if (generatedKeys.next()) {
                                                idOcorrencia = generatedKeys.getInt(1);
                                        }
                                }
                        }

                        // Se o ID foi gerado, liga as viaturas a esta ocorrência e mete-as como
                        // indisponíveis
                        if (idOcorrencia != -1) {
                                try (java.sql.PreparedStatement pstmtInter = conn.prepareStatement(sqlIntermedia);
                                                java.sql.PreparedStatement pstmtViatura = conn
                                                                .prepareStatement(sqlAtualizarViatura)) {

                                        for (String idViarura : idsViaturas) {
                                                // Insere na tabela intermédia
                                                pstmtInter.setInt(1, idOcorrencia);
                                                pstmtInter.setString(2, idViarura);
                                                pstmtInter.addBatch();

                                                // Atualiza o estado da viatura para indisponível (0) na BD
                                                pstmtViatura.setString(1, idViarura);
                                                pstmtViatura.addBatch();
                                        }
                                        pstmtInter.executeBatch();
                                        pstmtViatura.executeBatch();
                                }
                        }

                        conn.commit(); // Confirma as alterações na BD
                        System.out.println("-> Ocorrência gravada no SQL com sucesso!");
                } catch (Exception e) {
                        System.out.println("Erro ao gravar ocorrência: " + e.getMessage());
                }
        }

        // 3. Atualizar o estado da ocorrência (Ex: para "No Local" ou "Concluida")
        public static void atualizarEstadoOcorrencia(int id, String novoEstado) {
                String sql = "UPDATE ocorrencias SET estado = ? WHERE id = ?";
                try (Connection conn = getConnection();
                                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, novoEstado);
                        pstmt.setInt(2, id);
                        pstmt.executeUpdate();

                        // Se a ocorrência foi concluída, liberta as viaturas associadas a ela
                        if (novoEstado.equals("Concluida")) {
                                String sqlLibertar = "UPDATE viaturas SET disponivel = 1 WHERE id IN (" +
                                                "SELECT viatura_id FROM ocorrencia_viaturas WHERE ocorrencia_id = ?)";
                                try (java.sql.PreparedStatement pstmtLibertar = conn.prepareStatement(sqlLibertar)) {
                                        pstmtLibertar.setInt(1, id);
                                        pstmtLibertar.executeUpdate();
                                }
                        }
                } catch (Exception e) {
                        System.out.println("Erro ao atualizar estado: " + e.getMessage());
                }
        }

        // 4. Carregar as ocorrências guardadas para o programa arrancar com elas
        public static java.util.ArrayList<model.Ocorrencia> carregarOcorrencias() {
                java.util.ArrayList<model.Ocorrencia> lista = new java.util.ArrayList<>();
                String sql = "SELECT * FROM ocorrencias";

                try (Connection conn = getConnection();
                                Statement stmt = conn.createStatement();
                                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

                        while (rs.next()) {
                                int idReal = rs.getInt("id");
                                String tipo = rs.getString("tipo");
                                String localizacao = rs.getString("localizacao");
                                String estado = rs.getString("estado");

                                // Procurar as viaturas desta ocorrência específica na tabela intermédia
                                java.util.ArrayList<String> viaturasDaOcorrencia = new java.util.ArrayList<>();
                                String sqlViaturas = "SELECT viatura_id FROM ocorrencia_viaturas WHERE ocorrencia_id = ?";

                                try (java.sql.PreparedStatement pstmtV = conn.prepareStatement(sqlViaturas)) {
                                        pstmtV.setInt(1, idReal);
                                        try (java.sql.ResultSet rsV = pstmtV.getGeneratedKeys();
                                                        java.sql.ResultSet rsVReal = pstmtV.executeQuery()) {
                                                while (rsVReal.next()) {
                                                        viaturasDaOcorrencia.add(rsVReal.getString("viatura_id"));
                                                }
                                        }
                                }

                                // Criar o objeto correspondente
                                model.Ocorrencia oc = null;
                                if (tipo.equals("Emergência Médica")) {
                                        oc = new model.EmergenciaMedica(localizacao, viaturasDaOcorrencia, 1,
                                                        "Queixa Geral", 40);
                                } else if (tipo.equals("Acidente de Viação")) {
                                        oc = new model.AcidenteViacao(localizacao, viaturasDaOcorrencia, 2, false,
                                                        "Via Urbana");
                                } else if (tipo.equals("Incêndio Urbano")) {
                                        oc = new model.IncendioUrbano(localizacao, viaturasDaOcorrencia, 1, 30,
                                                        "Habitação", 1);
                                } else {
                                        oc = new model.IncendioFlorestal(localizacao, viaturasDaOcorrencia, 2, 60, 1.0,
                                                        "Plano");
                                }

                                // FORÇAR O ID E ESTADO REAIS DA BASE DE DADOS
                                oc.setEstado(estado);
                                // Como o atributo id na Ocorrencia.java é protected, podemos alterá-lo
                                // diretamente:
                                // (Se houver erro aqui, podes mudar para oc.setId(idReal) caso tenhas esse
                                // método)
                                 oc.setId(idReal);

                                lista.add(oc);
                        }
                } catch (Exception e) {
                        System.out.println("Erro ao carregar ocorrencias: " + e.getMessage());
                }
                return lista;
        }

        // 5. Adicionar uma nova viatura na Base de Dados
        public static boolean adicionarViatura(model.Viatura v) {
                String sql = "INSERT INTO viaturas (id, nome, tipo, disponivel) VALUES (?, ?, ?, ?)";
                try (Connection conn = getConnection();
                                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, v.getId());
                        pstmt.setString(2, v.getNome());
                        pstmt.setString(3, v.getTipo());
                        pstmt.setInt(4, v.isDisponivel() ? 1 : 0);
                        pstmt.executeUpdate();
                        return true;
                } catch (Exception e) {
                        System.out.println("Erro ao adicionar viatura: " + e.getMessage());
                        return false;
                }
        }

        // 6. Eliminar uma viatura da Base de Dados
        public static boolean eliminarViatura(String id) {
                String sql = "DELETE FROM viaturas WHERE id = ?";
                try (Connection conn = getConnection();
                                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, id);
                        pstmt.executeUpdate();
                        return true;
                } catch (Exception e) {
                        System.out.println("Erro ao eliminar viatura: " + e.getMessage());
                        return false;
                }
        }
}
