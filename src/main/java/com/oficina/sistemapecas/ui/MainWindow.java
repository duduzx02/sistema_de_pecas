package com.oficina.sistemapecas.ui;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.service.PecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@Component
public class MainWindow extends JFrame {

    private final PecaService pecaService;
    private final JTable tabela;
    private final DefaultTableModel tableModel;

    @Autowired
    public MainWindow(PecaService pecaService){

        this.pecaService = pecaService;
        setTitle("Sistema de peças da oficina");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Colunas da tabela
        String[] colunas = {"ID", "Nome", "Descrição", "Valor", "Urgência"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        tabela.setDefaultEditor(Object.class, null);
        JButton btnNovaPeca = new JButton("Nova Peça");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnNovaPeca);
        painelBotoes.add(btnAtualizar);

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnNovaPeca.addActionListener(e -> {
            NovaPecaForm form = new NovaPecaForm(this, pecaService);
            form.setVisible(true);
        });

        btnAtualizar.addActionListener(e -> carregarPecas());

        //Primeira carga
        carregarPecas();

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

    public void exibir(){
        this.setVisible(true);
    }

}
