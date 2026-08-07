package ui;

import produtos.Produto;
import produtos.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaGerenteConsultarProduto extends JFrame {
    private JTextField campoId;
    private JTextArea areaResultado;
    private final ProdutoService produtoService;

    public TelaGerenteConsultarProduto() {
        setTitle("Consultar Produto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        produtoService = new ProdutoService();

        // Painel superior para input
        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new FlowLayout());

        painelSuperior.add(new JLabel("ID do Produto:"));
        campoId = new JTextField(10);
        painelSuperior.add(campoId);

        JButton botaoConsultar = new JButton("Consultar");
        botaoConsultar.addActionListener(new ConsultarProdutoListener());
        painelSuperior.add(botaoConsultar);

        add(painelSuperior, BorderLayout.NORTH);

        // Área de resultado
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(areaResultado);

        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null); // centraliza na tela
        setVisible(true);
    }

    private class ConsultarProdutoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String textoId = campoId.getText().trim();
            try {
                int id = Integer.parseInt(textoId);
                Produto produto = produtoService.buscarProduto(id);
                if (produto == null) {
                    areaResultado.setText("Produto não encontrado.");
                } else {
                    areaResultado.setText(
                            "ID: " + produto.getId() + "\n" +
                                    "Nome: " + produto.getNome() + "\n" +
                                    "Preço: R$ " + produto.getPreco() + "\n" +
                                    "Estoque: " + produto.getEstoque()
                    );
                }
            } catch (NumberFormatException ex) {
                areaResultado.setText("ID inválido. Digite um número inteiro.");
            }
        }
    }
}
