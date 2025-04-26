package com.oficina.sistemapecas.ui;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JFrame {

    public MainWindow(){
        super("Sistema de peças da Oficina");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Bem-vindo ao Sistema de Gestão de peças!", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    public void exibir(){
        this.setVisible(true);
    }
}
