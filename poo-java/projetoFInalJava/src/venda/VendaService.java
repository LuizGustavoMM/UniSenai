package venda;

import cliente.Cliente;
import cliente.ClienteGastoDTO;
import produtos.Produto;
import produtos.ProdutoService;
import database.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaService {

    private ProdutoService produtoService = new ProdutoService();

//    public boolean efetuarVenda(Cliente cliente, List<ItemVenda> itens) {
//        String sqlVenda = "INSERT INTO venda (cliente_id, total) VALUES (?, ?)";
//        String sqlItem = "INSERT INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
//        String sqlUpdateEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id = ?";
//
//        try (Connection connection = Conexao.getConnection()) {
//            connection.setAutoCommit(false);
//
//            double total = itens.stream()
//                    .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
//                    .sum();
//            if(cliente != null){
//                if(cliente.getTipo().equals("F")){
//                    total *= 0.95;
//                } else {
//                    total *= 0.90;
//                }
//            }
//
//            int vendaId;
//            try (PreparedStatement stmtVenda = connection.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
//                if (cliente != null) {
//                    stmtVenda.setInt(1, cliente.getId());
//                } else {
//                    stmtVenda.setNull(1, Types.INTEGER);
//                }
//
//                stmtVenda.setDouble(2, total);
//                stmtVenda.executeUpdate();
//
//                ResultSet rs = stmtVenda.getGeneratedKeys();
//                if (rs.next()) {
//                    vendaId = rs.getInt(1);
//                } else {
//                    connection.rollback();
//                    throw new SQLException("Falha ao obter ID da venda.");
//                }
//            }
//
////            for (ItemVenda item : itens) {
////                Produto produto = produtoService.buscarProduto(item.getProdutoId());
////                if (produto == null ) {
////                    connection.rollback();
////                    System.out.println("Estoque insuficiente ou produto não encontrado: " + item.getProdutoId());
////                    return false;
////                }
////
////                try (PreparedStatement stmtItem = connection.prepareStatement(sqlItem)) {
////                    stmtItem.setInt(1, vendaId);
////                    stmtItem.setInt(2, item.getProdutoId());
////                    stmtItem.setInt(3, item.getQuantidade());
////                    stmtItem.setDouble(4, item.getPrecoUnitario());
////                    stmtItem.executeUpdate();
////                }
////
////                try (PreparedStatement stmtEstoque = connection.prepareStatement(sqlUpdateEstoque)) {
////                    stmtEstoque.setInt(1, item.getQuantidade());
////                    stmtEstoque.setInt(2, item.getProdutoId());
////                    stmtEstoque.executeUpdate();
////                }
////            }
//            for (ItemVenda item : itens) {
//                Produto produto = produtoService.buscarProduto(item.getProdutoId());
//                if (produto == null) {
//                    connection.rollback();
//                    System.out.println("Produto não encontrado: " + item.getProdutoId());
//                    return false;
//                }
//
//                // Insere item de venda
//                try (PreparedStatement stmtItem = connection.prepareStatement(sqlItem)) {
//                    stmtItem.setInt(1, vendaId);
//                    stmtItem.setInt(2, item.getProdutoId());
//                    stmtItem.setInt(3, item.getQuantidade());
//                    stmtItem.setDouble(4, item.getPrecoUnitario());
//                    stmtItem.executeUpdate();
//                }
//
//                // Atualiza estoque (pode ficar negativo)
//                try (PreparedStatement stmtEstoque = connection.prepareStatement(sqlUpdateEstoque)) {
//                    stmtEstoque.setInt(1, item.getQuantidade());
//                    stmtEstoque.setInt(2, item.getProdutoId());
//                    stmtEstoque.executeUpdate();
//                }
//            }
//
//
//            connection.commit();
//            System.out.println("Venda efetuada com sucesso!");
//            return true;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Erro ao efetuar venda: " + e.getMessage());
//            return false;
//        }
//    }
    public boolean efetuarVenda(Cliente cliente, List<ItemVenda> itens) {
        String sqlVenda = "INSERT INTO venda (cliente_id, total) VALUES (?, ?)";
        String sqlItem = "INSERT INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmtVenda = null;
        PreparedStatement stmtItem = null;

        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); // Início da transação

            // Inserir a venda
            stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            if (cliente != null) {
                stmtVenda.setInt(1, cliente.getId());
            } else {
                stmtVenda.setNull(1, java.sql.Types.INTEGER);
            }

            double total = 0.0;
            for (ItemVenda item : itens) {
                total += item.getPrecoUnitario() * item.getQuantidade();
            }
            if(cliente != null){
                if(cliente.getTipo() == "F"){
                    total *= 0.95;
                }
                if(cliente.getTipo() == "J"){
                    total *= 0.90;
                }
            }

            stmtVenda.setDouble(2, total);
            stmtVenda.executeUpdate();

            // Obter o ID da venda gerado
            ResultSet rs = stmtVenda.getGeneratedKeys();
            int vendaId = -1;
            if (rs.next()) {
                vendaId = rs.getInt(1);
            }

            // Inserir os itens da venda
            stmtItem = conn.prepareStatement(sqlItem);
            for (ItemVenda item : itens) {
                stmtItem.setInt(1, vendaId);
                stmtItem.setInt(2, item.getProdutoId());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, item.getPrecoUnitario());
                stmtItem.addBatch();

                // Reduz estoque
                produtoService.reduzirEstoque(item.getProdutoId(), item.getQuantidade());
            }

            stmtItem.executeBatch();

            conn.commit(); // Sucesso
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Reverter transação em caso de erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (stmtVenda != null) stmtVenda.close();
                if (stmtItem != null) stmtItem.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Venda> listarVendasHoje() {
        List<Venda> vendas = new ArrayList<>();

        String sqlVendas = "SELECT * FROM venda WHERE DATE(data_hora) = CURRENT_DATE";
        String sqlItens = "SELECT * FROM item_venda WHERE venda_id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtVendas = conn.prepareStatement(sqlVendas)) {

            ResultSet rsVendas = stmtVendas.executeQuery();

            while (rsVendas.next()) {
                int vendaId = rsVendas.getInt("id");
                int clienteId = rsVendas.getInt("cliente_id");
                double total = rsVendas.getDouble("total");

                List<ItemVenda> itens = new ArrayList<>();
                try (PreparedStatement stmtItens = conn.prepareStatement(sqlItens)) {
                    stmtItens.setInt(1, vendaId);
                    ResultSet rsItens = stmtItens.executeQuery();

                    while (rsItens.next()) {
                        int produtoId = rsItens.getInt("produto_id");
                        int quantidade = rsItens.getInt("quantidade");
                        double precoUnitario = rsItens.getDouble("preco_unitario");

                        itens.add(new ItemVenda(produtoId, quantidade, precoUnitario));
                    }
                }

                Venda venda = new Venda(vendaId, clienteId, total);
                venda.setItens(itens);
                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas de hoje: " + e.getMessage());
        }

        return vendas;
    }

    public List<ClienteGastoDTO> listarClientesMaisRentaveis() {
        String sql = """
        SELECT c.id, c.nome, SUM(v.total) AS total_gasto
        FROM cliente c
        JOIN venda v ON c.id = v.cliente_id
        GROUP BY c.id, c.nome
        ORDER BY total_gasto DESC
    """;

        List<ClienteGastoDTO> clientes = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double totalGasto = rs.getDouble("total_gasto");
                clientes.add(new ClienteGastoDTO(id, nome, totalGasto));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar clientes mais rentáveis: " + e.getMessage());
        }

        return clientes;
    }



    public List<Venda> listarVendasCliente(int clienteId) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda WHERE cliente_id = ?";
        String sqlItem = "SELECT * FROM item_venda WHERE venda_id = ?";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, clienteId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double total = resultSet.getDouble("total");

                List<ItemVenda> itens = new ArrayList<>();

                try (PreparedStatement statement1 = connection.prepareStatement(sqlItem)) {
                    statement1.setInt(1, id);
                    ResultSet resultSet1 = statement1.executeQuery();

                    while (resultSet1.next()) {
                        int produtoId = resultSet1.getInt("produto_id");
                        int quantidade = resultSet1.getInt("quantidade");
                        double precoUnitario = resultSet1.getDouble("preco_unitario");

                        ItemVenda item = new ItemVenda(produtoId, quantidade, precoUnitario);
                        itens.add(item);
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao ler itens da venda: " + e.getMessage());
                }

                Venda venda = new Venda(id, clienteId, total);
                venda.setItens(itens); // <-- Aqui é necessário que a classe Venda tenha esse método
                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas de um determinado cliente: " + e.getMessage());
        }

        return vendas;
    }


}
