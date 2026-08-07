package venda;

import java.util.List;

public interface IItemVendaService {
    void adicionarItem(ItemVenda item);
    List<ItemVenda> listarItensPorVenda(int vendaId);
    double calcularTotalVenda(int vendaId);
}
