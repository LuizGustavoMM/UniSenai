package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaCliente extends JFrame{

    private final JPanel panel;

    public TelaCliente(){
        setTitle("Tela Cliente");
        setLayout(new FlowLayout());

        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
        this.panel.setPreferredSize(new Dimension(500, 300));
        add(this.panel);

        criarBotao("Comprar Sem Login", new BotaoComprarSemLoginHandler());
        criarBotao("Comprar Com Login", new BotaoComprarComLoginHandler());

        setSize(new Dimension(500, 500));
        setPreferredSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void criarBotao(String label, ActionListener listener){
        JButton botao = new JButton(label);
        botao.addActionListener(listener);
        botao.setPreferredSize(new Dimension(300, 80));
        this.panel.add(botao);
    }

    private static class BotaoComprarSemLoginHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaClienteCompra();
        }
    }

    private static class BotaoComprarComLoginHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            new TelaClienteLogin();
        }
    }

    public static void main(String[] args){
        new TelaCliente();
    }
}
