package ui;

import produtos.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaGerenteEditarProduto extends JFrame {

    private JTextField txId, txNome, txPreco, txQuantidade;
    private JButton btnEditar;
    private ProdutoService produtoService;

    public TelaGerenteEditarProduto() {
        super("Editar Produto");
        produtoService = new ProdutoService();

        // Configurações da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new GridLayout(5, 2, 10, 10));

        // Componentes
        JLabel lbId = new JLabel("ID do Produto:");
        JLabel lbNome = new JLabel("Novo Nome:");
        JLabel lbPreco = new JLabel("Novo Preço:");
        JLabel lbQuantidade = new JLabel("Nova Quantidade:");

        txId = new JTextField();
        txNome = new JTextField();
        txPreco = new JTextField();
        txQuantidade = new JTextField();

        btnEditar = new JButton("Salvar Alterações");

        // Adicionar componentes à tela
        add(lbId); add(txId);
        add(lbNome); add(txNome);
        add(lbPreco); add(txPreco);
        add(lbQuantidade); add(txQuantidade);
        add(new JLabel()); add(btnEditar);

        // Ação do botão
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProduto();
            }
        });

        setVisible(true);
    }

    private void editarProduto() {

        try {
            int id = Integer.parseInt(txId.getText());
            String nome = txNome.getText();
            double preco = Double.parseDouble(txPreco.getText());
            int quantidade = Integer.parseInt(txQuantidade.getText());
            if (!produtoService.produtoExistePorId(id)) {
                throw new IllegalArgumentException("Produto com ID " + id + " não existe.");
            }

            // Chamada do service
            produtoService.editarProduto(id, nome, preco, quantidade);

            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            // Limpar campos
            txId.setText("");
            txNome.setText("");
            txPreco.setText("");
            txQuantidade.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preencha os campos corretamente (valores numéricos válidos).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage());
        }
    }
}
