package com.oficina.sistemapecas.ui;


import com.oficina.sistemapecas.model.Usuario;
import com.oficina.sistemapecas.service.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarUsuariosForm extends JDialog {
    private final UsuarioService usuarioService;
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0);
    private final JTable tabela = new JTable(model);

    public GerenciarUsuariosForm(JFrame parent, UsuarioService usuarioService){
        super(parent, "Gerenciar Usuários", true);
        this.usuarioService = usuarioService;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JScrollPane scroll = new JScrollPane(tabela);
        JPanel botoes = new JPanel();

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");



        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnRemover.addActionListener(e -> removerUsuario());

        botoes.add(btnAdicionar);
        botoes.add(btnRemover);

        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        carregarUsuarios();
    }

    private void carregarUsuarios(){
        model.setRowCount(0);
        List<Usuario> usuarios = usuarioService.listarTodos();
        for(Usuario u : usuarios){
            model.addRow(new Object[]{u.getId(), u.getNome()});
        }
    }

    private void adicionarUsuario(){
        String nome = JOptionPane.showInputDialog(this, "Nome do novo usuário: ");
        if(nome != null && !nome.trim().isEmpty()){
            Usuario novo = new Usuario();
            novo.setNome(nome.trim());
            usuarioService.salvar(novo);
            carregarUsuarios();
        }
    }

    private void editarUsuario(){
        int row = tabela.getSelectedRow();
        if(row >= 0){
            Long id = (Long) model.getValueAt(row, 0);
            String nomeAtual = (String) model.getValueAt(row, 1);
            String novoNome = JOptionPane.showInputDialog(this, "Editar nome do usuário", nomeAtual);
            if(novoNome != null && !novoNome.trim().isEmpty()) {
                Usuario usuario = usuarioService.buscarPorId(id);
                usuario.setNome(novoNome.trim());
                usuarioService.salvar(usuario);
                carregarUsuarios();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
        }
    }

    private void removerUsuario(){
        int row = tabela.getSelectedRow();
        if(row >= 0){
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do usuário", "confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    usuarioService.excluir(id);
                    carregarUsuarios();
                    JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                } catch (IllegalStateException ex) {
                    // Aqui exibe a mensagem amigável
                    JOptionPane.showMessageDialog(this,
                            ex.getMessage(),  // Mensagem lançada no service
                            "Erro ao excluir",
                            JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                    // Para qualquer outro erro inesperado
                    JOptionPane.showMessageDialog(this,
                            "Erro inesperado: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
        }
    }
}
