package ui;

import produtos.ProdutoRentavelDTO;
import produtos.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TelaGerenteRelatorioProdutosMaisRentaveis extends JFrame {

    public TelaGerenteRelatorioProdutosMaisRentaveis() {
        setTitle("Relatório - Produtos Mais Rentáveis");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Nome do Produto", "Total Vendido (R$)"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        ProdutoService produtoService = new ProdutoService();
        List<ProdutoRentavelDTO> produtos = produtoService.listarProdutosMaisRentaveis();

        for (ProdutoRentavelDTO produto : produtos) {
            model.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    String.format("R$ %.2f", produto.getTotalVendido())
            });
        }

        JButton btnGerarTxt = new JButton("Gerar TXT");
        btnGerarTxt.addActionListener(e -> gerarArquivoTxt(produtos));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnGerarTxt);

        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void gerarArquivoTxt(List<ProdutoRentavelDTO> produtos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("relatorio_produtos_mais_rentaveis.txt"))) {
            for (ProdutoRentavelDTO produto : produtos) {
                writer.write("ID: " + produto.getId() + "\n");
                writer.write("Nome: " + produto.getNome() + "\n");
                writer.write(String.format("Total Vendido: R$ %.2f\n", produto.getTotalVendido()));
                writer.write("--------------------------------------------------\n");
            }
            JOptionPane.showMessageDialog(this, "Arquivo gerado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar o arquivo: " + e.getMessage());
        }
    }
}
