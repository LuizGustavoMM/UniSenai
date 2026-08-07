package fiado.registros;

import fiado.clientes.IClienteService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FiadoView extends JFrame {

    private static final String TITULO = "Registro de Fiado";

    private static final int POSICAO_X_INICIAL = 120;
    private static final int POSICAO_Y_INICIAL = 120;

    private static final int LARGURA = 750;
    private static final int ALTURA = 575;

    private static final int COLUNA_SELECAO = 0;
    private static final int COLUNA_ID = 1;
    private static final int COLUNA_DESCRICAO = 2;
    private static final int COLUNA_VALOR = 3;

    private JTextField id;
    private JTextField nomeCliente;
    private JTextField descricao;
    private JFormattedTextField valor;
    private JFormattedTextField desconto;
    private JFormattedTextField totalVenda;

    private JTable table;
    private DefaultTableModel model;

    private JButton btConfirmar;
    private JButton btCancelar;
    private JButton btBuscarCliente;
    private JButton btAdicionarItem;
    private JButton btRemoverItensSelecionados;

    private IFiadoService fiadoService = null;
    private IClienteService clienteService = null;

    private List<ItemVenda> itens;

    /**
     * Cria a janela para inclusão de uma venda
     */
    public FiadoView(IFiadoService fiadoService, IClienteService clienteService) {
        this.fiadoService = fiadoService;
        this.clienteService = clienteService;
        this.itens = new ArrayList<>();

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        setSize(LARGURA, ALTURA);
        setLocation(POSICAO_X_INICIAL, POSICAO_Y_INICIAL);
        setTitle(TITULO);
        getContentPane().setLayout(null);

        JLabel lbId = new JLabel("Cliente ID:");
        lbId.setBounds(31, 40, 72, 17);
        getContentPane().add(lbId);

        id = new JTextField();
        id.setHorizontalAlignment(SwingConstants.CENTER);
        id.setBounds(109, 38, 114, 21);
        getContentPane().add(id);
        id.setColumns(10);

        JLabel lbNome = new JLabel("Nome:");
        lbNome.setBounds(31, 73, 60, 17);
        getContentPane().add(lbNome);

        nomeCliente = new JTextField();
        nomeCliente.setBounds(109, 71, 600, 21);
        getContentPane().add(nomeCliente);
        nomeCliente.setColumns(10);
        nomeCliente.setEditable(false);

        JLabel lbProduto = new JLabel("Produto:");
        lbProduto.setBounds(31, 106, 60, 17);
        getContentPane().add(lbProduto);

        descricao = new JTextField();
        descricao.setBounds(109, 104, 600, 21);
        getContentPane().add(descricao);
        descricao.setColumns(10);

        btConfirmar = new JButton("Registrar Fiado");
        btConfirmar.addActionListener(e -> onClickRegistrarFiado());
        btConfirmar.setBounds(558, 535, 151, 27);
        getContentPane().add(btConfirmar);

        btCancelar = new JButton("Cancelar");
        btCancelar.addActionListener(e -> onClickCancelar());
        btCancelar.setBounds(441, 535, 105, 27);
        getContentPane().add(btCancelar);

        btBuscarCliente = new JButton("Buscar Cliente");
        btBuscarCliente.addActionListener(e -> onClickBuscarCliente());
        btBuscarCliente.setBounds(235, 35, 120, 27);
        getContentPane().add(btBuscarCliente);

        JLabel lbValor = new JLabel("Valor:");
        lbValor.setBounds(31, 139, 60, 17);
        getContentPane().add(lbValor);

        valor = new JFormattedTextField(numberFormat);
        valor.setHorizontalAlignment(SwingConstants.RIGHT);
        valor.setBounds(109, 137, 114, 21);
        getContentPane().add(valor);
        valor.setColumns(10);

        JLabel lbDesconto = new JLabel("Desconto:");
        lbDesconto.setBounds(31, 484, 60, 17);
        getContentPane().add(lbDesconto);

        desconto = new JFormattedTextField(numberFormat);
        desconto.setHorizontalAlignment(SwingConstants.RIGHT);
        desconto.setBounds(109, 479, 114, 21);
        getContentPane().add(desconto);
        desconto.setColumns(10);

        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setBounds(31, 513, 60, 17);
        getContentPane().add(lblTotal);

        totalVenda = new JFormattedTextField(numberFormat);
        totalVenda.setHorizontalAlignment(SwingConstants.RIGHT);
        totalVenda.setBounds(109, 511, 114, 21);
        getContentPane().add(totalVenda);
        totalVenda.setColumns(10);
        totalVenda.setEditable(false);

        btAdicionarItem = new JButton("Adicionar Fiado");
        btAdicionarItem.addActionListener(arg0 -> onClickAdicionarItemVenda());
        btAdicionarItem.setBounds(507, 153, 202, 27);
        getContentPane().add(btAdicionarItem);

        btRemoverItensSelecionados = new JButton("Remover Itens Selecionados");
        btRemoverItensSelecionados.setBounds(507, 466, 202, 27);
        btRemoverItensSelecionados.addActionListener(e -> onClickRemoverItensSelecionados());
        getContentPane().add(btRemoverItensSelecionados);

        // Criação da tabela
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"", "ID", "Nome", "Valor"}, 0) {
            @Serial
            private static final long serialVersionUID = -213504134060145107L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == COLUNA_SELECAO) {
                    return Boolean.class;
                } else if (columnIndex == COLUNA_VALOR) {
                    return Double.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(480, 250));
        table.setFillsViewportHeight(true);

        // Formatação das colunas
        table.getColumnModel().getColumn(COLUNA_SELECAO).setMaxWidth(50);
        table.getColumnModel().getColumn(COLUNA_ID).setCellRenderer(new CenterRenderer());
        table.getColumnModel().getColumn(COLUNA_ID).setMaxWidth(65);
        table.getColumnModel().getColumn(COLUNA_DESCRICAO).setPreferredWidth(10);
        table.getColumnModel().getColumn(COLUNA_VALOR).setCellRenderer(new NumberRenderer());
        table.getColumnModel().getColumn(COLUNA_VALOR).setMaxWidth(95);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Adicionando a JTable em um JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBounds(31, 192, 678, 262);
        getContentPane().add(panel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                // Verifica se a célula clicada é da coluna de seleção
                if (column == COLUNA_SELECAO) {
                    boolean currentValue = (Boolean) table.getValueAt(row, column);
                    // Troca o valor do JCheckBox
                    table.setValueAt(!currentValue, row, column);
                }
            }
        });

    }

    protected void onClickRemoverItensSelecionados() {
        List<Integer> linhasSelecionadas = new ArrayList<>();
        TableModel model = table.getModel();
        for (int linha = 0, qtdLinhas = model.getRowCount(); linha < qtdLinhas; linha++) {
            if ((Boolean) model.getValueAt(linha, COLUNA_SELECAO)) {
                linhasSelecionadas.add(linha);
            }
        }

        Collections.reverse(linhasSelecionadas);
        for (int linha : linhasSelecionadas) {
            ((DefaultTableModel) model).removeRow(linha);
            this.itens.remove(linha);
        }
    }

    /**
     * Prepara o frame para a ação de registrar uma venda
     */
    public void setupRegistrarNovaVenda() {
        // configura os botões de ação
        btConfirmar.setEnabled(false);
        btCancelar.setEnabled(false);
        btBuscarCliente.setEnabled(true);
        btAdicionarItem.setEnabled(false);
        btRemoverItensSelecionados.setEnabled(false);

        // configura o comportamento dos campos
        id.setEditable(true);
        descricao.setEnabled(false);
        valor.setEditable(false);
        desconto.setEditable(false);
    }

    /**
     * Executa as tarefas para efetuar uma pesquisa com base no ID cliente informado
     */
    protected void onClickBuscarCliente() {
        // TODO: Implementar
        System.out.println("==> onClickBuscarCliente");
    }

    /**
     * Executa as tarefas para cancelar a venda
     */
    protected void onClickCancelar() {
        // TODO: Implementar
        System.out.println("==> onClickCancelar");
    }

    /**
     * Executa as tarefas para registrar uma venda
     */
    protected void onClickRegistrarFiado() {
        // TODO: Implementar
        System.out.println("==> onClickRegistrarFiado");
    }

    /**
     * Executa as tarefas para registrar uma venda
     */
    protected void onClickAdicionarItemVenda() {
        // TODO: Criar de fato um registro de venda
        ItemVenda venda = new ItemVenda();
        this.itens.add(venda);

        // TODO: Substituir as duas próximas linhas pela inclusão de fato da venda
        int id = this.model.getRowCount() + 1; // ID sequencial
        double valorDouble = valor.getValue() == null ? 0.0 : ((Number) valor.getValue()).doubleValue();
        this.model.addRow(new Object[]{Boolean.FALSE, id, descricao.getText(), valorDouble}); // Adiciona uma nova linha
    }

    // Classe para renderizar valores numéricos formatados
    static class NumberRenderer extends DefaultTableCellRenderer {
        @Serial
        private static final long serialVersionUID = -3093220843229636461L;

        private final NumberFormat formatter;

        public NumberRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
            formatter = NumberFormat.getInstance();
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            formatter.setGroupingUsed(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int col) {
            if (value instanceof Double) {
                value = formatter.format(value);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
    }

    // Renderer para centralizar texto
    static class CenterRenderer extends DefaultTableCellRenderer {
        @Serial
        private static final long serialVersionUID = -6091171430025834311L;

        public CenterRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
}
