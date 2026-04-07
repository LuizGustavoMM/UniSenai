package ui;

import venda.Venda;
import venda.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TelaGerenteRelatorioVendasHoje extends JFrame {

    private JTable tabela;
    private VendaService vendaService = new VendaService();

    public TelaGerenteRelatorioVendasHoje() {
        setTitle("Relatório de Vendas - Hoje");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabela = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnGerarTxt = new JButton("Gerar TXT");
        btnGerarTxt.addActionListener(e -> gerarArquivoTxt());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnGerarTxt);

        add(painelBotoes, BorderLayout.SOUTH);

        carregarDados();
        setVisible(true);
    }

    private void carregarDados() {
        List<Venda> vendasHoje = vendaService.listarVendasHoje();

        String[] colunas = {"ID Venda", "Cliente ID", "Total", "Itens"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);

        for (Venda venda : vendasHoje) {
            StringBuilder itens = new StringBuilder();
            venda.getItens().forEach(item -> {
                itens.append("Produto ID: ").append(item.getProdutoId())
                        .append(", Qtd: ").append(item.getQuantidade())
                        .append(", Preço: ").append(item.getPrecoUnitario())
                        .append(" | ");
            });

            modelo.addRow(new Object[]{
                    venda.getId(),
                    venda.getClienteId(),
                    venda.getTotal(),
                    itens.toString()
            });
        }

        tabela.setModel(modelo);
    }

    private void gerarArquivoTxt() {
        List<Venda> vendasHoje = vendaService.listarVendasHoje();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("relatorio_vendas_hoje.txt"))) {
            for (Venda venda : vendasHoje) {
                writer.write("Venda ID: " + venda.getId() + "\n");
                writer.write("Cliente ID: " + venda.getClienteId() + "\n");
                writer.write("Total: " + venda.getTotal() + "\n");
                writer.write("Itens:\n");
                for (var item : venda.getItens()) {
                    writer.write("  - Produto ID: " + item.getProdutoId() +
                            ", Quantidade: " + item.getQuantidade() +
                            ", Preço Unitário: " + item.getPrecoUnitario() + "\n");
                }
                writer.write("--------------------------------------------------\n");
            }

            JOptionPane.showMessageDialog(this, "Arquivo gerado com sucesso!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar o arquivo: " + e.getMessage());
        }
    }
}
