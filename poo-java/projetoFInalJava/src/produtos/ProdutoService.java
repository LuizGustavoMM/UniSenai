package produtos;

import database.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService implements IProdutoService{
    private List<Produto> produtos;

    public ProdutoService() {
        this.produtos = new ArrayList<>();
    }

    public boolean produtoExistePorNome(String nome) {
        String sql = "SELECT COUNT(*) FROM produto WHERE nome = ?";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Correção: passar o índice do parâmetro
            statement.setString(1, nome);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                // Correção: se for >= 1, o produto já existe
                return count >= 1;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar se produto existe: " + e.getMessage());
        }

        return false;
    }
    @Override
    public int adicionarProduto(Produto produto) {
        if(produtoExistePorNome(produto.getNome())){
            throw new IllegalArgumentException("Esse produto já existe");
        }

        String sql = "INSERT INTO produto (nome, preco, estoque) VALUES (?, ?, ?)";
        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getEstoque());

            int rowsInserted = statement.executeUpdate();

            if(rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
            }
        } catch( SQLException e) {
            System.out.println("Erro ao adicionar produto: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void removerProduto(int id) {
        String sql = "DELETE FROM produto WHERE id = ? ";

        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, id);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Erro ao remover produto: " + e.getMessage());
        }
    }

    @Override
    public void editarProduto(int id, String novoNome, double novoPreco, int novaQuantidade) {
        String sql = "UPDATE produto SET nome = ?, preco = ?, estoque = ? WHERE id = ?";
        if (!produtoExistePorId(id)) {
            System.out.println("Produto com ID " + id + " não existe.");
            return;
        }


        try(Connection connection = Conexao.getConnection(); PreparedStatement statement  = connection.prepareStatement(sql)){

            statement.setString(1, novoNome);
            statement.setDouble(2, novoPreco);
            statement.setInt(3, novaQuantidade);
            statement.setInt(4, id);
            statement.executeUpdate();
        }catch(SQLException e){
            System.out.println("Erro ao editar produto: " + e.getMessage());
        }
    }

    @Override
    public void editarNome(int id, String novoNome){
        String sql = "UPDATE produto SET nome = ? WHERE id = ?";
        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, novoNome);
            statement.setInt(2, id);
            statement.executeUpdate();

        } catch(SQLException e){
            System.out.println("Erro ao editar nome do produto.");
        }
    }

    @Override
    public void editarEstoque(int id, int novaQuantidade) {
        String sql = "UPDATE produto SET estoque = ? WHERE id = ?";

        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, novaQuantidade);
            statement.setInt(2, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao tentar editar estoque de produto em estoque!");
        }
    }

    @Override
    public void editarPreco(int id, double novoPreco){
        String sql = "UPDATE produto SET preco = ? WHERE id = ?";
        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setDouble(1, novoPreco);
            statement.setInt(2, id);
            statement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("Erro ao tentar editar preço do produto!");
        }
    }

    @Override
    public Produto buscarProduto(int id){
        String sql = "SELECT * FROM produto WHERE id = ?";

        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                int produtoId = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                double preco = resultSet.getDouble("preco");
                int estoque = resultSet.getInt("estoque");

                return new Produto(produtoId, nome, preco, estoque);
            }

        } catch(SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Produto> listarTodosProdutos(){
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try(Connection connection = Conexao.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                double preco = resultSet.getDouble("preco");
                int estoque = resultSet.getInt("estoque");

                produtos.add(new Produto(id, nome, preco, estoque));
            }

        } catch( SQLException e) {
            System.out.println("Erro ao listar todos os produtos" + e.getMessage());
        }

        return produtos;
    }

    @Override
    public void reduzirEstoque(int produtoId, int quantidade) {
        String sql = "UPDATE produto SET estoque = estoque - ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProdutoRentavelDTO> listarProdutosMaisRentaveis() {
        List<ProdutoRentavelDTO> produtosRentaveis = new ArrayList<>();

        String sql = """
        SELECT p.id, p.nome, SUM(iv.quantidade * iv.preco_unitario) AS total_vendido
        FROM produto p
        JOIN item_venda iv ON p.id = iv.produto_id
        GROUP BY p.id, p.nome
        ORDER BY total_vendido DESC
    """;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double totalVendido = rs.getDouble("total_vendido");

                produtosRentaveis.add(new ProdutoRentavelDTO(id, nome, totalVendido));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar produtos mais rentáveis: " + e.getMessage());
        }

        return produtosRentaveis;
    }

    public boolean produtoExistePorId(int id) {
        String sql = "SELECT COUNT(*) FROM produto WHERE id = ?";

        try (Connection connection = Conexao.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar se produto existe por ID: " + e.getMessage());
        }

        return false;
    }


}
