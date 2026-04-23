package ui;

import cliente.ClienteGastoDTO;
import venda.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TelaGerenteRelatorioClientesMaisRentaveis extends JFrame {

    public TelaGerenteRelatorioClientesMaisRentaveis() {
        setTitle("Relatório - Clientes Mais Rentáveis");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Nome", "Total Gasto"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        VendaService vendaService = new VendaService();
        List<ClienteGastoDTO> clientes = vendaService.listarClientesMaisRentaveis();

        for (ClienteGastoDTO cliente : clientes) {
            model.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNome(),
                    String.format("R$ %.2f", cliente.getTotalGasto())
            });
        }

        JButton btnGerarTxt = new JButton("Gerar TXT");
        btnGerarTxt.addActionListener(e -> gerarArquivoTxt(clientes));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.add(btnGerarTxt);

        add(painelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void gerarArquivoTxt(List<ClienteGastoDTO> clientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("relatorio_clientes_mais_rentaveis.txt"))) {
            for (ClienteGastoDTO cliente : clientes) {
                writer.write("ID: " + cliente.getId() + "\n");
                writer.write("Nome: " + cliente.getNome() + "\n");
                writer.write(String.format("Total Gasto: R$ %.2f\n", cliente.getTotalGasto()));
                writer.write("--------------------------------------------------\n");
            }
            JOptionPane.showMessageDialog(this, "Arquivo gerado com sucesso!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar o arquivo: " + e.getMessage());
        }
    }
}
