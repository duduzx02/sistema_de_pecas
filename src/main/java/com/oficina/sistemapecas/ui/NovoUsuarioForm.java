package com.oficina.sistemapecas.ui;

import com.oficina.sistemapecas.model.Usuario;
import com.oficina.sistemapecas.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

public class NovoUsuarioForm extends JDialog {

    private final JTextField txtNome = new JTextField(20);
    private final UsuarioService usuarioService;
    private final MainWindow mainWindow;

    public NovoUsuarioForm(MainWindow mainWindow, UsuarioService usuarioService){
        super(mainWindow, "Cadastrar Novo Usuário", true);
        this.usuarioService = usuarioService;
        this.mainWindow = mainWindow;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10 , 10);

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome do Usuário: "), gbc);

        gbc.gridx = 1;
        add(txtNome, gbc);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnSalvar, gbc);

        btnSalvar.addActionListener(e -> salvarUsuario());

        pack();
        setLocationRelativeTo(mainWindow);
    }


    private void salvarUsuario(){
        String nome = txtNome.getText().trim();
        if(nome.isEmpty()){
            JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.");
            return;
        }

        Usuario novo = new Usuario();
        novo.setNome(nome);
        usuarioService.salvar(novo);

        JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");

        mainWindow.atualizarListarUsuarios();
        dispose();
    }
}
