package fiado.clientes;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

public class ClienteView extends JInternalFrame {

    private static final String TITULO = "Cadastro de Cliente";

    private static final int POSICAO_X_INICIAL = 30;
    private static final int POSICAO_Y_INICIAL = 30;

    private static final int LARGURA = 580;
    private static final int ALTURA = 245;

    private JTextField id;
    private JTextField nome;
    private JFormattedTextField telefone;
    private JFormattedTextField limite;

    private JButton btConfirmar;
    private JButton btCancelar;
    private JButton btNovoCliente;
    private JButton btConsultar;

    private IClienteService service = null;

    /**
     * Cria a janela do CRUD do cliente
     */
    public ClienteView(IClienteService service) {
        this.service = service;

        setClosable(true);
        setIconifiable(true);
        setSize(LARGURA, ALTURA);
        setLocation(POSICAO_X_INICIAL, POSICAO_Y_INICIAL);
        setTitle(TITULO);
        getContentPane().setLayout(null);

        JLabel lbId = new JLabel("ID:");
        lbId.setBounds(31, 40, 60, 17);
        getContentPane().add(lbId);

        id = new JTextField();
        id.setHorizontalAlignment(SwingConstants.CENTER);
        id.setBounds(109, 38, 114, 21);
        getContentPane().add(id);
        id.setColumns(10);

        JLabel lbNome = new JLabel("Nome:");
        lbNome.setBounds(31, 73, 60, 17);
        getContentPane().add(lbNome);

        nome = new JTextField();
        nome.setBounds(109, 71, 430, 21);
        getContentPane().add(nome);
        nome.setColumns(10);

        JLabel lbTelefone = new JLabel("Telefone:");
        lbTelefone.setBounds(31, 106, 60, 17);
        getContentPane().add(lbTelefone);

        MaskFormatter mfTelefone;
        try {
            mfTelefone = new MaskFormatter("(##) #####-####");
            mfTelefone.setPlaceholderCharacter('_'); // Caracter de espaço reservado
            telefone = new JFormattedTextField(mfTelefone);
            telefone.setBounds(109, 104, 132, 21);
            getContentPane().add(telefone);
            telefone.setColumns(10);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JLabel lbLimite = new JLabel("Limite:");
        lbLimite.setBounds(31, 136, 60, 17);
        getContentPane().add(lbLimite);

        NumberFormat nfLimite = NumberFormat.getNumberInstance();
        nfLimite.setMaximumFractionDigits(2);
        nfLimite.setMinimumFractionDigits(2);
        limite = new JFormattedTextField(nfLimite);
        limite.setHorizontalAlignment(SwingConstants.RIGHT);
        limite.setBounds(109, 137, 114, 21);
        getContentPane().add(limite);
        limite.setColumns(10);

        btConfirmar = new JButton("Confirmar");
        btConfirmar.addActionListener(e -> onClickConfirmar());
        btConfirmar.setBounds(434, 169, 105, 27);
        getContentPane().add(btConfirmar);

        btCancelar = new JButton("Cancelar");
        btCancelar.addActionListener(e -> onClickCancelar());
        btCancelar.setBounds(317, 169, 105, 27);
        getContentPane().add(btCancelar);

        btNovoCliente = new JButton("Novo Cliente");
        btNovoCliente.addActionListener(e -> onClickIncluirNovoCliente());
        btNovoCliente.setBounds(400, 35, 139, 27);
        getContentPane().add(btNovoCliente);

        btConsultar = new JButton("Consultar");
        btConsultar.addActionListener(e -> onClickConsultar());
        btConsultar.setBounds(235, 35, 96, 27);
        getContentPane().add(btConsultar);
    }

    /**
     * Prepara o frame para a ação de consultar
     */
    public void setupConsultar() {
        this.clearCampos();

        // configura os botões de ação
        btConfirmar.setEnabled(false);
        btCancelar.setEnabled(false);
        btNovoCliente.setEnabled(true);
        btConsultar.setEnabled(true);

        // configura o comportamento dos campos
        id.setEnabled(true);
        nome.setEnabled(false);
        telefone.setEnabled(false);
        limite.setEnabled(false);
    }

    public void setupInclusao() {
        this.clearCampos();

        // configura os botões de ação
        btConfirmar.setEnabled(true);
        btCancelar.setEnabled(true);
        btNovoCliente.setEnabled(false);
        btConsultar.setEnabled(false);

        // configura o comportamento dos campos
        id.setEnabled(false);
        nome.setEnabled(true);
        telefone.setEnabled(true);
        limite.setEnabled(true);
    }

    public void clearCampos() {
        id.setText("");
        nome.setText("");
        telefone.setText("");
        limite.setText("");
    }

    /**
     * Executa as tarefas para efetuar uma consulta com base no ID informado
     */
    protected void onClickConsultar() {
        if (this.id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Um ID válido deve ser informado para consulta.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            int id = Integer.parseInt(this.id.getText());
            Cliente cliente = this.service.consultarCliente(id);
            if (cliente == null) {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                this.nome.setText(cliente.getNome());
                this.telefone.setText(cliente.getTelefone());
                this.limite.setText(String.format("%.2f", cliente.getLimite()));
            }
        }
    }

    /**
     * Executa as tarefas para preparar a interface para a inclusão de um novo
     * cliente
     */
    protected void onClickIncluirNovoCliente() {
        this.setupInclusao();
    }

    /**
     * Executa as tarefas para cancelar a inclusão de um cliente
     */
    protected void onClickCancelar() {
        this.setupConsultar();
        // TODO: Implementar
        System.out.println("==> onClickCancelar");
    }

    /**
     * Executa as tarefas para confirmar a inclusão de um novo cliente
     */
    protected void onClickConfirmar() {
        // TODO: Implementar validação de campos
        String nome = this.nome.getText();
        String telefone = this.telefone.getText();

        Number valorNumero = (Number) this.limite.getValue();
        double limite = valorNumero.doubleValue();
        Cliente cliente = new Cliente(nome, telefone, limite);

        try {
            this.service.cadastrarCliente(cliente);
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso.", "Operação Realizada", JOptionPane.INFORMATION_MESSAGE);
            this.setupConsultar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

}

