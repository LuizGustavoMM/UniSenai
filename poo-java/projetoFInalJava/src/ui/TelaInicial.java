package ui;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;




public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Sistema de Vendas");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnCliente = new JButton("Acessar como Cliente");
        JButton btnGerente = new JButton("Acessar como Gerente");

        btnCliente.addActionListener(e -> {
            new TelaCliente();
            dispose();
        });

        btnGerente.addActionListener(e -> {
            new TelaGerente(); 
            dispose();
        });

        add(btnCliente);
        add(btnGerente);

        setVisible(true);
    }

    public static void main(String[] args) {
        new TelaInicial();
    }
}
