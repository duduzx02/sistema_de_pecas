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

import java.util.List;

@Component
public class MainWindow extends JFrame {

    // Services (Injetados via Spring)
    private final PecaService pecaService;
    private final UsuarioService usuarioService;

    // Componetes UI
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JComboBox<Usuario> cbUsuarios = new JComboBox<>();

    // Constantes
    private static final String[] COLUNAS_TABELA = {
            "ID", "Nome", "Descrição", "Valor", "Urgência", "Responsável"
    };
    private static final Color COR_FUNDO = new Color(240, 240, 240);
    private static final Color COR_BOTAO = new Color(0,120, 215);


    @Autowired
    public MainWindow(PecaService pecaService, UsuarioService usuarioService){

        this.pecaService = pecaService;
        this.usuarioService = usuarioService;


        // Colunas da tabela
        String[] colunas = {"ID", "Nome", "Descrição", "Valor", "Urgência"};
        tableModel = new DefaultTableModel(COLUNAS_TABELA, 0);
        tabela = criarTabela();
        JScrollPane scrollPane = new JScrollPane(tabela);

        tabela.setDefaultEditor(Object.class, null);
        JButton btnNovaPeca = new JButton("Nova Peça");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnNovaPeca);
        painelBotoes.add(btnAtualizar);

        // Layout
        add(criarPainelFiltros(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnNovaPeca.addActionListener(e -> {
            Usuario usuario = (Usuario) cbUsuarios.getSelectedItem();
          /*  if(usuario == null){
                JOptionPane.showMessageDialog(this, "Selecione um usuário primeiro!");
                return;
            }*/

            NovaPecaForm form = new NovaPecaForm(this, pecaService, usuarioService);
            form.setVisible(true);
        });

        btnAtualizar.addActionListener(e -> carregarPecas());

        // Detectando a tecla Delete
        tabela.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE){
                    excluirPeca();
                }
            }
        });

        //Primeira carga
        configurarJanela();
        carregarPecas();
        carregarUsuarios();





    }

    private void configurarJanela(){
        setTitle("Sistema de gestão de peças - Oficina mecânica");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000,550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);

        setLayout(new BorderLayout(15, 15));

        tabela.getColumnModel().getColumn(4).setCellRenderer(new UrgenciaRenderer());
    }

    private JPanel criarPainelFiltros(){
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painel.setBorder(BorderFactory.createTitledBorder("Filtrar"));

        // Componentes
        JLabel IblUsuarios = new JLabel("Responsável: ");
        cbUsuarios.setPreferredSize(new Dimension(200, 25));

        // adiciona ao painel
        painel.add(IblUsuarios);
        painel.add(cbUsuarios);

        return painel;
    }

    private JTable criarTabela(){

        // Cria a tabela
        JTable tabela = new JTable(tableModel);

        // Configuração de edição
        tabela.setDefaultEditor(Object.class, null); // Impede edição

        // Configurações visuais
        tabela.setGridColor(Color.black);
        tabela.setRowHeight(30);
        tabela.setIntercellSpacing(new Dimension(0 ,5));

        // Funcionalidades
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabela;
    }


    // Método para exluir uma peça
    private void excluirPeca(){
        int row = tabela.getSelectedRow();
        if(row >= 0){
            Long id = (Long) tabela.getValueAt(row, 0); // Assumindo que o ID está na primeira coluna
            int confirm =  JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta peça?", "Excluir " +
                    "Peça", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                pecaService.excluir(id);
                carregarPecas(); // atualiza a lista
                JOptionPane.showMessageDialog(this, "Peça excluída com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma peça para exluir");
        }
    }

    public void carregarPecas(){
        tableModel.setRowCount(0); // limpa a tabela
        List<Peca> pecas = pecaService.listarTodas();
        for (Peca p : pecas){
            tableModel.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getDescricao(), p.getValor(), p.getUrgencia()
            });
        }
    }

    private void carregarUsuarios(){
        List<Usuario> usuarios = usuarioService.listarTodos();
        for(Usuario usuario : usuarios){
            cbUsuarios.addItem(usuario);
        }
    }

    public void exibir(){
        this.setVisible(true);
    }

}
