package produtos;

import java.util.List;

public interface IProdutoService {
    int adicionarProduto(Produto produto);
    void removerProduto(int id);

    void editarProduto(int id, String novoNome, double preco, int novaQuantidade);

    void editarNome(int id, String novoNome);
    void editarPreco(int id, double preco);
    void editarEstoque(int id, int novaQuantidade);

    void reduzirEstoque(int produtoId, int quantidade);

    Produto buscarProduto(int id);
    List<Produto> listarTodosProdutos();

}
