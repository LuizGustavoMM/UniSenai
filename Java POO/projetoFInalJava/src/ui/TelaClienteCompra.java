package ui;

import cliente.Cliente;
import produtos.Produto;
import produtos.ProdutoService;
//import venda.Venda;
import venda.VendaService;
import venda.ItemVenda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TelaClienteCompra extends JFrame {
    private Cliente cliente;
    private ProdutoService produtoService = new ProdutoService();
    private List<ItemVenda> carrinho = new ArrayList<>();
    private VendaService vendaService = new VendaService();

    private JTextField txIdProduto;
    private JTextField txQuantidade;
    private JTextArea taCarrinho;

    int idVenda = gerarIdAleatorio();
    double total = 0.0;


    public TelaClienteCompra(Cliente clienteLogado) {
        this.cliente = clienteLogado;
        inicializar();
    }

    public TelaClienteCompra() {
        inicializar();
    }


    private void inicializar() {
        setTitle("Compra de Produtos");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblIdProduto = new JLabel("ID do Produto:");
        lblIdProduto.setBounds(30, 20, 100, 25);
        add(lblIdProduto);

        txIdProduto = new JTextField();
        txIdProduto.setBounds(150, 20, 150, 25);
        add(txIdProduto);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        lblQuantidade.setBounds(30, 60, 100, 25);
        add(lblQuantidade);

        txQuantidade = new JTextField();
        txQuantidade.setBounds(150, 60, 150, 25);
        add(txQuantidade);

        JButton btnAdicionar = new JButton("Adicionar ao Carrinho");
        btnAdicionar.setBounds(30, 100, 270, 30);
        add(btnAdicionar);

        taCarrinho = new JTextArea();
        taCarrinho.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taCarrinho);
        scrollPane.setBounds(30, 150, 520, 300);
        add(scrollPane);

        JButton btnEfetuarVenda = new JButton("Efetuar Venda");
        btnEfetuarVenda.setBounds(30, 470, 270, 30);
        add(btnEfetuarVenda);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarAoCarrinho();
            }
        });

        btnEfetuarVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                efetuarVenda();
            }
        });

        setVisible(true);
    }

    private void adicionarAoCarrinho() {
        try {
            System.out.println("Valor do txIdProduto: '" + txIdProduto.getText() + "'");

            int idProduto = Integer.parseInt(txIdProduto.getText());
            int quantidade = Integer.parseInt(txQuantidade.getText().trim());

            if (idProduto <= 0) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um ID válido para o produto (maior que zero).");
                return;
            }

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Por favor, insira uma quantidade válida (maior que zero).");
                return;
            }
            Produto produto = produtoService.buscarProduto(idProduto);
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado para o ID informado.");
                return;
            }

            ItemVenda itemVenda = new ItemVenda(idProduto, quantidade, produto.getPreco());
            carrinho.add(itemVenda);
            total += produto.getPreco() * quantidade;
            if(cliente != null){
                if(cliente.getTipo() == "F"){
                    total *= 0.95;
                }
                if (cliente.getTipo() == "J") {
                    total *= 0.90;
                }
            }
            System.out.println(carrinho);

            txIdProduto.setText("");
            txQuantidade.setText("");

            taCarrinho.append("Produto: " + produto.getNome() + ", Quantidade: " + quantidade + ", Preço Total: R$" + (produto.getPreco() * quantidade) + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do produto e quantidade devem ser números válidos.");
        }
    }


    private void efetuarVenda() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho vazio.");
            return;
        }
//

//        for (Produto p : carrinho) {
//            total += p.getPreco() * p.getEstoque(); // p.getEstoque() aqui é usado como quantidade
//            produtoService.reduzirEstoque(p.getId(), p.getEstoque());
//        }
//        List<ItemVenda> listVenda = new ArrayList<>();

//        for(ItemVenda produto: carrinho){
//
//        }
        if (cliente != null) {
            System.out.println(carrinho);
            vendaService.efetuarVenda(cliente, carrinho);
        } else {
            vendaService.efetuarVenda(null, carrinho);
        }

        JOptionPane.showMessageDialog(this, "Venda realizada com sucesso. Total: R$" + total);

        carrinho.clear();
        total = 0;
        taCarrinho.setText("");

        dispose();
    }

    private int gerarIdAleatorio() {
        return 100000 + (int)(Math.random() * 900000); // Gera número entre 100000 e 999999
    }
}
