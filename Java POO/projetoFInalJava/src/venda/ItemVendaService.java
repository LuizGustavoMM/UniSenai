package venda;

import database.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaService implements IItemVendaService{

    @Override
    public void adicionarItem(ItemVenda item){
        String sql = "INSERT INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, item.getVendaId());
            statement.setInt(2, item.getProdutoId());
            statement.setInt(3, item.getQuantidade());
            statement.setDouble(4, item.getPrecoUnitario());

            statement.executeUpdate();

        }catch (SQLException e) {
            System.out.println("Erro ao adicionar item na venda: " + e.getMessage());
        }
    }

    @Override
    public List<ItemVenda> listarItensPorVenda(int vendaId){
        List< ItemVenda> itens = new ArrayList<>();
//        String sql = "SELECT * FROM item_venda WHERE id = ?";
        String sql = "SELECT * FROM item_venda WHERE venda_id = ?";

        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, vendaId);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                int produtoId = resultSet.getInt("produto_id");
                int quantidade = resultSet.getInt("quantidade");
                double precoUnitario = resultSet.getDouble("preco_unitario");

                ItemVenda item = new ItemVenda(produtoId, quantidade, precoUnitario);
                // Se a classe ItemVenda tiver o campo vendaId, use setter ou construtor que aceite vendaId também
                item.setVendaId(vendaId); // se existir
                itens.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar itens da venda: " + e.getMessage());
        }

        return itens;
    }

    @Override
    public double calcularTotalVenda(int vendaId) {
        double total = 0.0;
        String sql = "SELECT quantidade, preco_unitario FROM item_venda WHERE venda_id = ?";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, vendaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int quantidade = resultSet.getInt("quantidade");
                double preco = resultSet.getDouble("preco_unitario");
                total += quantidade * preco;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular total da venda: " + e.getMessage());
        }

        return total;
    }
}
