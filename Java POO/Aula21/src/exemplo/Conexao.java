package exemplo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String url = "jdbc:mysql://localhost:3306/meubanco";
    private static final String usuario = "root";
    private static final String senha = "senai";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conexao estabelecida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro de SQL na conexao: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro na conexao: " + e.getMessage());
        }
        return conn;
    }

    public static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Erro de SQL no encerramento da conexao: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro no encerramento da conexao: " + e.getMessage());
        }
    }
}
