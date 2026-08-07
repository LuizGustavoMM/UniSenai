package venda;

import java.time.LocalDateTime;
import java.util.List;

public class Venda {
    private int id; //gerarIdAleatorio();
    private int clienteId;
    private LocalDateTime data;
    private double total;
    private List<ItemVenda> itens;

    public Venda(int id, Integer clienteId, LocalDateTime data, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.data = data;
        this.total = total;
    }

    public Venda(int id, Integer clienteId, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.total = total;
        this.data = LocalDateTime.now();
    }


    public Venda(Integer clienteId, double total) {
        this.clienteId = clienteId;
        this.total = total;
        this.data = LocalDateTime.now();
    }

    public Venda(int id, Integer clienteId, LocalDateTime data, double total, List<ItemVenda> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.data = data;
        this.total = total;
        this.itens = itens;
    }

    // Getters e Setters
    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public LocalDateTime getData() { return data; }
    public double getTotal() { return total; }
    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setId(int id) { this.id = id; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public void setData(LocalDateTime data) { this.data = data; }
    public void setTotal(double total) { this.total = total; }
    public void setItens(List<ItemVenda> itens){
        this.itens = itens;
    }
//    private int gerarIdAleatorio() {
//        return 100000 + (int)(Math.random() * 900000); // Gera número entre 100000 e 999999
//    }

    @Override
    public String toString() {

        return data + "\n" +
                id + "\n" +
                ", clienteId=" + clienteId +
                ", data=" + data +
                ", total=" + total;
    }
}
