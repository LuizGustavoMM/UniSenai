package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import produtos.Produto;
import produtos.ProdutoService;


public class TelaGerenteCadastrarProduto extends JFrame{
    private JPanel panel;
    private JTextField txNomeProduto;
    private  JTextField txPrecoProduto;
    private JTextField txEstoqueProduto;

    ProdutoService produtoService = new ProdutoService();

    public TelaGerenteCadastrarProduto(){
        super("Cadastro de produto");

        setLayout(new GridLayout(5, 2, 10, 10));

        // Campos
        add(new JLabel("Nome:"));
        txNomeProduto = new JTextField();
        add(txNomeProduto);

        add(new JLabel("Preço:"));
        txPrecoProduto = new JTextField();
        add(txPrecoProduto);

        add(new JLabel("Estoque:"));
        txEstoqueProduto = new JTextField();
        add(txEstoqueProduto);

        // Botão
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new BotaoCadastrar());
        add(btnCadastrar);

        // Espaço vazio para alinhar
        add(new JLabel());

        setSize(400, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class BotaoCadastrar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nome = txNomeProduto.getText().trim();
            String precoStr = txPrecoProduto.getText().trim();
            String estoqueStr = txEstoqueProduto.getText().trim();

            if (nome.isEmpty() || precoStr.isEmpty() || estoqueStr.isEmpty()) {
                JOptionPane.showMessageDialog(TelaGerenteCadastrarProduto.this, "Todos os campos devem ser preenchidos.");
                return;
            }

            try {
                double preco = Double.parseDouble(precoStr);
                int estoque = Integer.parseInt(estoqueStr);

                Produto produto = new Produto(nome, preco, estoque);

                produtoService.adicionarProduto(produto);

                JOptionPane.showMessageDialog(TelaGerenteCadastrarProduto.this, "Produto cadastrado com sucesso!");
                txNomeProduto.setText("");
                txPrecoProduto.setText("");
                txEstoqueProduto.setText("");
//                dispose(); // fecha a tela após cadastro

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(TelaGerenteCadastrarProduto.this, "Preço deve ser um número decimal e estoque um número inteiro.");
            }
        }
    }
}
