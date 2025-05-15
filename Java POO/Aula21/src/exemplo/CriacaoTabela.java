package exemplo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CriacaoTabela {
    public void criar() {
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS exemplo (" +
                    "id int AUTO_INCREMENT PRIMARY KEY , " +
                    "nome VARCHAR(100), " +
                    "telefone VARCHAR(20));");
            System.out.println("Tabela criada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }
}
