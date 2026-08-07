package venda;

import cliente.Cliente;
import produtos.Produto;

import java.util.List;

public interface IVendaService {
    int adicionarVenda(Venda venda); // retorna o ID da venda
    Venda buscarVenda(int id);
    List<Venda> listarVendas();
    void removerVenda(int id);
    List<Venda> listarVendasCliente(int clienteId);
    void adicionarItemVenda(ItemVenda item);
    void finalizarVenda(int vendaId);
    void atualizarTotalVenda(int vendaId, double total);
}
