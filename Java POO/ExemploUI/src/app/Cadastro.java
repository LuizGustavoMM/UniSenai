package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cadastro extends JFrame {

    private JPanel painel;
    private JTextField tfNome;
    private JTextField tfAno;
    private JTextField tfValor;

    public Cadastro() {
        setTitle("Cadastro de Antiguidades");
        setLayout(new FlowLayout());

        this.painel = new JPanel();
        this.painel.setLayout(new FlowLayout());
        this.painel.setPreferredSize(new Dimension(500, 500));
        add(this.painel);

        criarTextoFieldNome("Nome da Antiguidade:");
        criarTextoFieldAno("Ano da Antiguidade:");
        criarTextoFieldValor("Valor da Antiguidade:");

        criarBotao("Salvar", new BotaoSalvarHandler());
        criarBotao("Voltar", new BotaoVoltarHandler());

        setSize(new Dimension (500, 500));
        setPreferredSize(new Dimension(500, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void criarBotao(String label, ActionListener listener) {
        JButton botao = new JButton(label);
        botao.addActionListener(listener);
        botao.setPreferredSize(new Dimension (300, 80));
        this.painel.add(botao);
    }

    private void criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setPreferredSize(new Dimension(400, 40));
        this.painel.add(label);
    }

    private void criarTextoFieldNome(String texto) {
        criarLabel(texto);
        this.tfNome = new JTextField();
        this.tfNome.setPreferredSize(new Dimension(400, 40));
        this.painel.add(this.tfNome);
    }

    private void criarTextoFieldAno(String texto) {
        criarLabel(texto);
        this.tfAno = new JTextField();
        this.tfAno.setPreferredSize(new Dimension(400, 40));
        this.painel.add(this.tfNome);
    }

    private void criarTextoFieldValor(String texto) {
        criarLabel(texto);
        this.tfValor = new JTextField();
        this.tfValor.setPreferredSize(new Dimension(400, 40));
        this.painel.add(this.tfNome);
    }

    private void criarAntiguidade() {
        JOptionPane.showMessageDialog(null, "Cadastro " + "realizado com sucesso!");
    }

    private class BotaoSalvarHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            criarAntiguidade();
        }
    }

    private class BotaoVoltarHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
}
