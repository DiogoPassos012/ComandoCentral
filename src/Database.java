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
                        "FOREIGN KEY (ocorrencia_id) REFERENCES ocorrencias(id) ON DELETE CASCADE," +
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
            System.out.println("Erro: O driver SQLite não foi encontrado nas Referenced Libraries! " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao inicializar Base de Dados: " + e.getMessage());
        }
    }
}