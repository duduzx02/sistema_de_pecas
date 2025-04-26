package com.oficina.sistemapecas.ui;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.model.Urgencia;
import com.oficina.sistemapecas.service.PecaService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class NovaPecaForm extends JDialog {

    private final JTextField nomeField = new JTextField(20);
    private final JTextField descricaoField = new JTextField(20);
    private final JTextField valorField = new JTextField(10);

    private final JComboBox<Urgencia> urgenciaJComboBox = new JComboBox<>(Urgencia.values());

    private final PecaService pecaService;
    private final MainWindow parent;

    // Construtorr que configura o diálogo
    public NovaPecaForm(MainWindow parent, PecaService pecaService) {
        super(parent, "Nova Peça", true);
        this.parent = parent;
        this.pecaService = pecaService;

        setLayout(new GridLayout(5, 2, 5, 5));
        setSize(400, 250);
        setLocationRelativeTo(parent);

        // Adição de componentes
        add(new JLabel("Nome: "));
        add(nomeField);

        add(new JLabel("Descrição: "));
        add(descricaoField);

        add(new JLabel("Valor: "));
        add(valorField);

        add(new JLabel("Urgência: "));
        add(urgenciaJComboBox);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarPeca());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        add(btnSalvar);
        add(btnCancelar);
    }

    // Método para salvar as peças
    private void salvarPeca() {
        try {
            String nome = nomeField.getText().trim();
            String descricao = descricaoField.getText().trim();
            BigDecimal valor = new BigDecimal(valorField.getText().trim());
            Urgencia urgencia = (Urgencia) urgenciaJComboBox.getSelectedItem();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome não pode estar em branco.");

                return;
            }

            Peca peca = new Peca();
            peca.setNome(nome);
            peca.setDescricao(descricao);
            peca.setValor(valor);
            peca.setUrgencia(urgencia);

            pecaService.salvar(peca);
            JOptionPane.showMessageDialog(this, "Peça salva com sucesso!");
            parent.carregarPecas();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar peça" + ex.getMessage());
        }
    }

}
