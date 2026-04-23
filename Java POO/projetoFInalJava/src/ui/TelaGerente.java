package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TelaGerente extends JFrame {
    private final JPanel panel;

    public TelaGerente(){
        setTitle("Tela Gerente");
        setLayout(new FlowLayout());

        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
        this.panel.setPreferredSize(new Dimension(500, 500));
        add(this.panel);

        criarBotao("Cadastrar Produto", new BotaoCadastrarProduto());
        criarBotao("Consultar Produto", new BotaoConsultarProduto());
        criarBotao("Editar Produto", new BotaoEditarProduto());
        criarBotao("Relatório Vendas Hoje", new BotaoRelatorioVendasHoje());
        criarBotao("Relatório Clientes mais Rentáveis", new BotaoClientesMaisRentaveis());
        criarBotao("Relatório Produtos mais Rentáveis", new BotaoProdutosMaisRentaveis());

        setSize(new Dimension(500, 700));
        setPreferredSize(new Dimension(500, 700));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void criarBotao(String label, ActionListener listener){
        JButton botao = new JButton(label);
        botao.addActionListener(listener);
        botao.setPreferredSize(new Dimension(300,80));
        this.panel.add(botao);
    }

    private static class BotaoCadastrarProduto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaGerenteCadastrarProduto();
        }
    }
    private static class BotaoConsultarProduto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaGerenteConsultarProduto();
        }
    }
    private static class BotaoEditarProduto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaGerenteEditarProduto();
        }
    }
    private static class BotaoRelatorioVendasHoje implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaGerenteRelatorioVendasHoje();
        }
    }
    private static class BotaoClientesMaisRentaveis implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaGerenteRelatorioClientesMaisRentaveis();
        }
    }
    private static class BotaoProdutosMaisRentaveis implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaGerenteRelatorioProdutosMaisRentaveis();
        }
    }

    public static void main(String[] args){
        new TelaGerente();
    }

}
