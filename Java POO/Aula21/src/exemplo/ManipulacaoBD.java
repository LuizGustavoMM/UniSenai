package exemplo;

import java.sql.*;

public class ManipulacaoBD {
    private final Connection conn;

    public ManipulacaoBD() {
        conn = Conexao.getConnection();
    }

    public void inserirInseguro(String nome, String telefone) {
        String inserirSQL = "INSERT INTO exemplo (nome, telefone) VALUES ('" + nome + "', '" + telefone + "');";
        try (Statement stmt = conn.createStatement();) {
            stmt.execute(inserirSQL);
        } catch (SQLException e) {
            System.err.println("Nao foi possivel realizar a insercao: " + e.getMessage());
        }

    }

    public void inserirSeguro(String nome, String telefone) {
        String inserirSQL = "INSERT INTO exemplo (nome, telefone) VALUES (?, ?);";
        try (PreparedStatement stmt = conn.prepareStatement(inserirSQL)) {
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Nao foi possivel realizar a insercao: " + e.getMessage());
        }

    }

    public void atualizar(int id, String nome, String telefone) {
        String atualizarSQL = "UPDATE exemplo SET nome = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(atualizarSQL);) {
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setInt(3, id);
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Nao foi possivel realizar a atualizacao: " + e.getMessage());
        }

    }

    public void excluir(int id) {
        String deletarSQL = "DELETE FROM exemplo WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deletarSQL);) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Nao foi possivel realizar a atualizacao: " + e.getMessage());
        }

    }

    public void listar() {
        String listarSQL = "SELECT id, nome, telefone FROM exemplo;";
        try (PreparedStatement stmt = conn.prepareStatement(listarSQL);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("[" + rs.getInt(1) + "]" + " " + rs.getString(2) + " - " + rs.getString(3));
            }
            rs.close();
            rs = null;
        } catch (SQLException e) {
            System.err.println("Nao foi possivel realizar a consulta: " + e.getMessage());
        }
    }

    public void encerrar() {
        Conexao.close(conn);
    }
}
