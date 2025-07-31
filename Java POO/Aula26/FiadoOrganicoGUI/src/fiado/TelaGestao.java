package fiado;

import fiado.clientes.ClienteService;
import fiado.clientes.ClienteView;
import fiado.clientes.IClienteService;
import fiado.relatorios.RelatorioExportavelEmArquivoTexto;
import fiado.relatorios.RelatorioView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class TelaGestao extends JFrame implements ActionListener {

    private static final String TITULO = "Sistema de Gestão de Vendas";
    private static final String VERSAO = "1.0.0";

    private static final int SCREEN_SIZE_WIDTH = 1200;
    private static final int SCREEN_SIZE_HEIGHT = 900;

    private final JDesktopPane desktop;

    // Services
    private IClienteService clienteService = null;

    public TelaGestao() {
        super("::: " + TITULO + "  | v" + VERSAO + " :::");

        // Define o tamanho e a posição da tela
        int[] bounds = calcBounds();
        setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);

        // Set up the GUI.
        desktop = new JDesktopPane(); // a specialized layered pane
        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        // Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        createServices();
    }

    protected int[] calcBounds() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(SCREEN_SIZE_WIDTH, screenSize.width);
        int height = Math.min(SCREEN_SIZE_HEIGHT, screenSize.height);
        int insetWidth = (screenSize.width - width) / 2;
        int insetHeight = (screenSize.height - height) / 2;
        return new int[]{insetWidth, insetHeight, width, height};
    }

    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    protected void createAndShowFrameCliente() {
        var frame = new ClienteView(this.clienteService);
        desktop.add(frame);
        frame.setupConsultar();
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    protected void createAndShowFrameExportarRelatorio(RelatorioExportavelEmArquivoTexto exportador) {
        var frame = new RelatorioView(exportador);
        desktop.add(frame);
        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para criação dos services que serão usados
     */
    protected void createServices() {
        // TODO: Instancie aqui os services que serão usados
        this.clienteService = new ClienteService();
    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;

        // cria a barra de menu.
        menuBar = new JMenuBar();

        // --> CLIENTES
        menu = new JMenu("Clientes");
        menu.setMnemonic(KeyEvent.VK_C);
        menu.getAccessibleContext().setAccessibleDescription("Operações no cadastro de clientes");
        menuBar.add(menu);

        menuItem = new JMenuItem("Cadastro", KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Cadastro de clientes");
        menuItem.addActionListener(e -> createAndShowFrameCliente());
        menu.add(menuItem);

        // define um submenu de Relatórios
        submenu = new JMenu("Relatórios");
        submenu.setMnemonic(KeyEvent.VK_R);
        menu.add(submenu);

        menuItem = new JMenuItem("Lista de Clientes", KeyEvent.VK_C);
        menuItem.getAccessibleContext().setAccessibleDescription("Relatório de todos os clientes em ordem alfabética");
        menuItem.addActionListener(e -> {
            // TODO: Instanciar e passar o exportador correto para o método
            createAndShowFrameExportarRelatorio(null);
        });
        submenu.add(menuItem);
        // <-- CLIENTES


        // --> VENDAS
        menu = new JMenu("Vendas");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext()
                .setAccessibleDescription("Ações pertinentes as vendas (exemplos: inclusão, cancelamento e consulta)");
        menuBar.add(menu);

        // adiciona os itens no menu
        submenu = new JMenu("Relatórios");
        submenu.setMnemonic(KeyEvent.VK_R);
        menu.add(submenu);

        menuItem = new JMenuItem("Vendas do Dia", KeyEvent.VK_V);
        menuItem.getAccessibleContext().setAccessibleDescription("Relatório de vendas do dia");
        menuItem.addActionListener(e -> {
            // TODO: Instanciar e passar o exportador correto para o método
            createAndShowFrameExportarRelatorio(null);
        });
        submenu.add(menuItem);

        menuItem = new JMenuItem("Melhores Clientes", KeyEvent.VK_M);
        menuItem.getAccessibleContext().setAccessibleDescription("Relação dos melhores clientes do estabelecimento");
        menuItem.addActionListener(e -> {
            // TODO: Instanciar e passar o exportador correto para o método
            createAndShowFrameExportarRelatorio(null);
        });
        submenu.add(menuItem);
        // <-- VENDAS

        return menuBar;
    }
}
