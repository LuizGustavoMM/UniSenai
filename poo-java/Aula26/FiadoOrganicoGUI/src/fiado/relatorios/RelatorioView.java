package fiado.relatorios;

import javax.swing.*;
import java.io.File;

public class RelatorioView extends JInternalFrame {

    private static final int POSICAO_X_INICIAL = 90;
    private static final int POSICAO_Y_INICIAL = 90;

    private static final int LARGURA = 580;
    private static final int ALTURA = 190;
    private final JButton btExportar;
    private final JButton btCancelar;
    private final JButton btSelecionarDestino;
    private JTextField nomeRelatorio;
    private JTextField destinoCaminhoAbsoluto;
    private File destinoSelecionado = null;
    private RelatorioExportavelEmArquivoTexto exportador = null;

    /**
     * Cria a janela para exportação do relatório
     */
    public RelatorioView(RelatorioExportavelEmArquivoTexto exportador) {
        this.exportador = exportador;

        setClosable(true);
        setIconifiable(true);
        setSize(LARGURA, ALTURA);
        setLocation(POSICAO_X_INICIAL, POSICAO_Y_INICIAL);
        setTitle("Exportador de Relatório");
        getContentPane().setLayout(null);

        JLabel lbId = new JLabel("Relatório:");
        lbId.setBounds(31, 40, 60, 17);
        getContentPane().add(lbId);

        nomeRelatorio = new JTextField();
        nomeRelatorio.setBounds(109, 38, 430, 21);
        getContentPane().add(nomeRelatorio);
        nomeRelatorio.setColumns(10);
        nomeRelatorio.setEditable(false);
        // TODO: Descomentar a linha abaixo quando já existir um exportador criado na TelaGestao
        //nomeRelatorio.setText(exportador.getNomeRelatorio());

        JLabel lbDestino = new JLabel("Destino:");
        lbDestino.setBounds(31, 73, 60, 17);
        getContentPane().add(lbDestino);

        destinoCaminhoAbsoluto = new JTextField();
        destinoCaminhoAbsoluto.setBounds(109, 71, 266, 21);
        getContentPane().add(destinoCaminhoAbsoluto);
        destinoCaminhoAbsoluto.setColumns(10);
        destinoCaminhoAbsoluto.setEditable(false);

        btExportar = new JButton("Exportar");
        btExportar.addActionListener(e -> onClickConfirmar());
        btExportar.setBounds(434, 107, 105, 27);
        getContentPane().add(btExportar);
        btExportar.setEnabled(false);

        btCancelar = new JButton("Cancelar");
        btCancelar.addActionListener(e -> onClickCancelar());
        btCancelar.setBounds(316, 107, 105, 27);
        getContentPane().add(btCancelar);
        btCancelar.setEnabled(true);

        btSelecionarDestino = new JButton("Selecionar Destino");
        btSelecionarDestino.addActionListener(e -> onClickSelecionarDestino());
        btSelecionarDestino.setBounds(387, 68, 152, 27);
        getContentPane().add(btSelecionarDestino);
        btSelecionarDestino.setEnabled(true);
    }

    /**
     * Executa as tarefas para efetuar uma pesquisa com base no ID informado
     */
    protected void onClickSelecionarDestino() {
        // Criação do JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            // Obtendo o arquivo selecionado
            this.destinoSelecionado = fileChooser.getSelectedFile();
            destinoCaminhoAbsoluto.setText(this.destinoSelecionado.getAbsolutePath());
            btExportar.setEnabled(true);
        }
    }

    /**
     * Executa as tarefas para cancelar a geração do relatório
     */
    protected void onClickCancelar() {
        this.dispose();
    }

    /**
     * Executa as tarefas para confirmar a exportação do relatório
     */
    protected void onClickConfirmar() {
        // TODO: Implementar
        System.out.println("==> onClickConfirmar");
    }

}
