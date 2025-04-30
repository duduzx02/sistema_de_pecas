package com.oficina.sistemapecas.ui;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.model.Usuario;
import com.oficina.sistemapecas.service.PecaService;
import com.oficina.sistemapecas.service.UsuarioService;
import com.oficina.sistemapecas.ui.renderer.UrgenciaRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Component
public class MainWindow extends JFrame {

    private final PecaService pecaService;
    private final UsuarioService usuarioService;

    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JComboBox<Usuario> cbUsuarios = new JComboBox<>();

    private static final String[] COLUNAS_TABELA = {
            "ID", "Nome", "Descrição", "Valor", "Urgência", "Responsável"
    };

    private static final Color COR_FUNDO = new Color(240, 240, 240);
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @Autowired
    public MainWindow(PecaService pecaService, UsuarioService usuarioService) {
        this.pecaService = pecaService;
        this.usuarioService = usuarioService;

        tableModel = new DefaultTableModel(COLUNAS_TABELA, 0);
        tabela = criarTabela();

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        configurarJanela();
        add(criarPainelFiltros(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        configurarEventosTeclado();
        carregarUsuarios();
        carregarPecas();

        // Atualiza a lista ao trocar o usuário selecionado
        cbUsuarios.addActionListener(e -> carregarPecas());
    }

    private void configurarJanela() {
        setTitle("Sistema de Gestão de Peças - Oficina Mecânica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(15, 15));
        tabela.getColumnModel().getColumn(4).setCellRenderer(new UrgenciaRenderer());
    }

    private JTable criarTabela() {
        JTable tabela = new JTable(tableModel);
        tabela.setDefaultEditor(Object.class, null);
        tabela.setGridColor(Color.BLACK);
        tabela.setRowHeight(30);
        tabela.setIntercellSpacing(new Dimension(0, 5));
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabela;
    }

    private JPanel criarPainelFiltros() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Filtrar"));
        JLabel lblUsuarios = new JLabel("Responsável:");
        cbUsuarios.setPreferredSize(new Dimension(200, 25));
        painel.add(lblUsuarios);
        painel.add(cbUsuarios);
        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        painel.add(criarBotao("Nova Peça", this::abrirFormularioNovaPeca));
        painel.add(criarBotao("Atualizar Lista", this::carregarPecas));
        painel.add(criarBotao("Novo Usuário", this::abrirFormularioNovoUsuario));
        return painel;
    }

    private JButton criarBotao(String texto, Runnable acao) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> acao.run());
        return botao;
    }

    private void configurarEventosTeclado() {
        tabela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    excluirPeca();
                }
            }
        });
    }

    private void abrirFormularioNovaPeca() {
        Usuario usuario = (Usuario) cbUsuarios.getSelectedItem();
        if (usuario == null || usuario.getId() == null) {
            mostrarMensagem("Selecione um usuário válido primeiro!");
            return;
        }

        NovaPecaForm form = new NovaPecaForm(this, pecaService, usuarioService);
        form.setVisible(true);
    }

    private void abrirFormularioNovoUsuario() {
        NovoUsuarioForm form = new NovoUsuarioForm(this, usuarioService);
        form.setVisible(true);
    }

    private void excluirPeca() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) tabela.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir esta peça?",
                    "Excluir Peça",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                pecaService.excluir(id);
                carregarPecas();
                mostrarMensagem("Peça excluída com sucesso!");
            }
        } else {
            mostrarMensagem("Selecione uma peça para excluir.");
        }
    }

    public void carregarPecas() {
        tableModel.setRowCount(0);
        Usuario usuarioSelecionado = (Usuario) cbUsuarios.getSelectedItem();

        List<Peca> pecas;
        if (usuarioSelecionado == null || usuarioSelecionado.getId() == null) {
            // Se for "Todos" ou nenhum usuário, traz todas as peças
            pecas = pecaService.listarTodas();
        } else {
            // Se um usuário específico estiver selecionado, filtra
            pecas = pecaService.listarPorUsuario(usuarioSelecionado.getId());
        }

        for (Peca p : pecas) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getDescricao(),
                    formatarValor(p.getValor()),
                    p.getUrgencia(),
                    p.getUsuario() != null ? p.getUsuario().getNome() : "-"
            });
        }
    }

    private void carregarUsuarios() {
        cbUsuarios.removeAllItems();


        List<Usuario> usuarios = usuarioService.listarTodos();
        for (Usuario usuario : usuarios) {
            cbUsuarios.addItem(usuario);
        }
    }

    public void atualizarListarUsuarios() {
        carregarUsuarios();
    }

    private void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    private String formatarValor(BigDecimal valor) {
        return FORMATO_MOEDA.format(valor);
    }

    public void exibir() {
        this.setVisible(true);
    }
}
