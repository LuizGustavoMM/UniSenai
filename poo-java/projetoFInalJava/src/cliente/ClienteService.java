package cliente;

import database.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClienteService implements IClienteService{

    public boolean clienteExistePorCpfOuTelefone(String cpfOuCnpj, String telefone) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE cpf_cnpj = ? OR telefone = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfOuCnpj);
            stmt.setString(2, telefone);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar cliente existente: " + e.getMessage());
        }

        return false;
    }


    @Override
    public int adicionarCliente(Cliente cliente) {
        if(clienteExistePorCpfOuTelefone(cliente.getCpf_cnpj(), cliente.getTelefone())){
            throw new IllegalArgumentException("\"Cliente já cadastrado com esse CPF/CNPJ ou telefone\"");
        }
        String sql = "INSERT INTO cliente (nome, telefone, tipo, cpf_cnpj) VALUES (?,?,?,?)";
        String tipoCliente = "";
        try (Connection connection = Conexao.getConnection(); PreparedStatement statement  = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getTelefone());
            statement.setString(3, cliente.getTipo());
            statement.setString(4, cliente.getCpf_cnpj());

            int rowsInserted = statement.executeUpdate();
            if(rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e){
            System.out.println("Erro ao adicionar cliente: " + e.getMessage());
        }

        return -1;
    }

    @Override
    public void removerCliente(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try(Connection connection = Conexao.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Erro ao remover cliente: " + e.getMessage());
        }

    }

    @Override
    public void editarCliente(int id, String novoNome, String novoTelefone){
        String sql = "UPDATE cliente SET nome = ?, telefone = ? WHERE id = ?";

        try (Connection connection = Conexao.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, novoNome);
            statement.setString(2, novoTelefone);
            statement.setInt(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao editar cliente: " + e.getMessage());
        }
    }

    @Override
    public Cliente mostrarCliente(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try(Connection connection = Conexao.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");
                String tipo = resultSet.getString("tipo");
                String cpfCnpj = resultSet.getString("cpf_cnpj");

                return new Cliente(nome, telefone, tipo, cpfCnpj);
            }
        } catch(SQLException e ){
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Cliente> listarTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try( Connection connection = Conexao.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String telefone = resultSet.getString("telefone");
                String tipo = resultSet.getString("tipo");
                String cpf_cnpj = resultSet.getString("cpf_cnpj");

                clientes.add(new Cliente(nome, telefone, tipo, cpf_cnpj));
            }
        } catch( SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    public int getIdDoCliente(String cpfCnpj) {
        String sql = "SELECT id FROM cliente WHERE cpf_cnpj = ?";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpfCnpj);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao encontrar id do cliente: " + e.getMessage());
        }

        return -1; // retorna -1 caso não encontre ou ocorra erro
    }

}
