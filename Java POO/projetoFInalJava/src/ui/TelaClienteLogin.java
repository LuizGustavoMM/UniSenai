package ui;

import cliente.Cliente;
import cliente.ClienteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaClienteLogin extends JFrame {
    private JPanel panel;
    private JTextField txNome;
    private JTextField txTelefone;
    private JComboBox<String> cbTipoDocumento;
    private JTextField txNumeroDocumento;
    private JButton btnEnviar;

    private ClienteService clienteService = new ClienteService();


    public TelaClienteLogin(){
        setTitle("Cadastro / Login de Cliente");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Label e campo nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 20, 100, 25);
        add(lblNome);

        txNome = new JTextField();
        txNome.setBounds(180, 20, 180, 25);
        add(txNome);

        // Label e campo telefone
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setBounds(30, 60, 100, 25);
        add(lblTelefone);

        txTelefone = new JTextField();
        txTelefone.setBounds(180, 60, 180, 25);
        add(txTelefone);

        // Label e ComboBox para tipo de documento
        JLabel lblTipoDocumento = new JLabel("Tipo: F(Pessoa Física) / J(Pessoa Jurídica)");
        lblTipoDocumento.setBounds(30, 100, 250, 25);
        add(lblTipoDocumento);

        String[] opcoes = {"F", "J"};
        cbTipoDocumento = new JComboBox<>(opcoes);
        cbTipoDocumento.setBounds(300, 100, 60, 25);
        add(cbTipoDocumento);

        // Label e campo número do documento
        JLabel lblNumeroDocumento = new JLabel("Número do documento:");
        lblNumeroDocumento.setBounds(30, 140, 150, 25);
        add(lblNumeroDocumento);

        txNumeroDocumento = new JTextField();
        txNumeroDocumento.setBounds(180, 140, 180, 25);
        add(txNumeroDocumento);

        // Botão Enviar
        btnEnviar = new JButton("Enviar");
        btnEnviar.setBounds(180, 190, 100, 30);
        add(btnEnviar);

        // Ação do botão
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });

        setVisible(true);
    }

    private void cadastrarCliente(){
        String nome = txNome.getText();
        String telefone = txTelefone.getText();
        String tipoDocumento = (String) cbTipoDocumento.getSelectedItem();
        String numeroDocumento = txNumeroDocumento.getText();

//        Cliente clienteCadastro = new Cliente(nome, telefone, tipoDocumento, numeroDocumento);
        if (!nome.matches("[a-zA-ZáéíóúãõâêîôûçÁÉÍÓÚÃÕÂÊÎÔÛÇ\\s]+")) {
            JOptionPane.showMessageDialog(this, "Nome inválido. Use apenas letras e espaços.");
            return;
        }

        try{
            if(clienteService.clienteExistePorCpfOuTelefone(numeroDocumento, telefone)){
                int idDoClienteExistente = clienteService.getIdDoCliente(numeroDocumento);
                Cliente clienteJaPossuiConta = new Cliente(idDoClienteExistente,nome, telefone, tipoDocumento, numeroDocumento);
                new TelaClienteCompra(clienteJaPossuiConta);
            } else {
                Cliente novoCliente = new Cliente(nome, telefone, tipoDocumento, numeroDocumento);
                int novoId = clienteService.adicionarCliente(novoCliente);
                novoCliente.setId(novoId);
                if (novoId != -1) {
                    JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso! ID: " + novoId);
                    new TelaClienteCompra(novoCliente);
                    limparCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
                }
            }
            dispose();

        } catch( IllegalArgumentException  ex){
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txNome.setText("");
        txTelefone.setText("");
        txNumeroDocumento.setText("");
        cbTipoDocumento.setSelectedIndex(0);
    }

}
//
//String nome = txNome.getText();
//String telefone = txTelefone.getText();
//String tipoDocumento = (String) cbTipoDocumento.getSelectedItem();
//String numeroDocumento = txNumeroDocumento.getText();
//
//                JOptionPane.showMessageDialog(null,
//                                                      "Nome: " + nome + "\n" +
//                                                      "Telefone: " + telefone + "\n" +
//                                                      "Tipo: " + tipoDocumento + "\n" +
//                                                      "Número do documento: " + numeroDocumento
//);