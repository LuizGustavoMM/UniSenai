package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicial extends JFrame {

    private JPanel painel;

    public TelaInicial() {
        setTitle("Exemplo de Tela");
        setLayout(new FlowLayout());

        this.painel = new JPanel();
        this.painel.setLayout(new FlowLayout());
        this.painel.setPreferredSize(new Dimension(500, 200));
        add(this.painel);

        criarBotao("Cadastrar", new BotaoCadastrarHandler());
        criarBotao("Sair", new BotaoSairHandler());

        setSize(new Dimension(500, 300));
        setPreferredSize (new Dimension(500, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void criarBotao(String label, ActionListener listener) {
        JButton botao = new JButton(label);
        botao.addActionListener(listener);
        botao.setPreferredSize(new Dimension (300, 80));
        this.painel.add(botao);
    }

    private static class BotaoCadastrarHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Cadastro();
        }
    }

    private static class BotaoSairHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
