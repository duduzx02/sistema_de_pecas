package com.oficina.sistemapecas.ui;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.model.Urgencia;
import com.oficina.sistemapecas.model.Usuario;
import com.oficina.sistemapecas.service.PecaService;
import com.oficina.sistemapecas.service.UsuarioService;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NovaPecaForm extends JDialog {

    private static final int CAMPO_COLUNAS = 20;
    private static final Color COR_SALVAR = Color.GREEN.darker();
    private static final Color COR_CANCELAR = Color.RED.darker();

    private final Usuario usuarioSelecionado;
    private final JTextField nomeField = new JTextField(CAMPO_COLUNAS);
    private final JTextField descricaoField = new JTextField(CAMPO_COLUNAS);
    private final JFormattedTextField valorField = criarCampoValor();
    private final JComboBox<Urgencia> urgenciaJComboBox = new JComboBox<>(Urgencia.values());


    private final PecaService pecaService;
    private final MainWindow parent;

    public NovaPecaForm(MainWindow parent, PecaService pecaService, Usuario usuarioSelecionado) {
        super(parent, "Cadastro de Nova Peça", true);
        this.parent = parent;
        this.pecaService = pecaService;
        this.usuarioSelecionado = usuarioSelecionado;


        configurarJanela();
        adicionarComponentes();
        configurarAtalhosTeclado();

    }

    private void configurarJanela(){
        setLayout(new BorderLayout(10, 10));
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void adicionarComponentes(){
        add(criarPainelFormulario(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }


    private JPanel criarPainelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = criarConstraintsPadrao();

        addCampoFormulario(panel, gbc, 0, "Nome:", nomeField);
        addCampoFormulario(panel, gbc, 1, "Descrição:", descricaoField);
        addCampoFormulario(panel, gbc, 2, "Valor (R$):", valorField);
        addCampoFormulario(panel, gbc, 3, "Urgência:", urgenciaJComboBox);

        return panel;
    }

    private GridBagConstraints criarConstraintsPadrao() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.add(criarBotao("Cancelar", this::dispose, COR_CANCELAR));
        panel.add(criarBotao("Salvar", this::salvarPeca, COR_SALVAR));
        return panel;
    }

    private void addCampoFormulario(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private JButton criarBotao(String texto, Runnable acao, Color corFundo) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> acao.run());
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        return botao;
    }

    private JFormattedTextField criarCampoValor() {

        NumberFormat formato = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

        // Congiurar para sempre mostrar 2 casas decimais
        formato.setMaximumFractionDigits(2);
        formato.setMinimumFractionDigits(2);
        formato.setGroupingUsed(false);

        // Usar NumberFormatter para melhor controle
        NumberFormatter formatter = new NumberFormatter(formato);
        formatter.setValueClass(BigDecimal.class);  // Aceitará apenas BigDecimal
        formatter.setAllowsInvalid(false);          // Não permite valores inválidos
        formatter.setMinimum(BigDecimal.ZERO);      // Valor mínimo zero

        JFormattedTextField field = new JFormattedTextField(formato);
        field.setColumns(10);
        field.setValue(BigDecimal.ZERO);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    field.selectAll();
                });
            }
        });

        return field;

    }

    private void configurarAtalhosTeclado() {
        JRootPane rootPane = getRootPane();
        rootPane.setDefaultButton(getBotaoSalvar());

        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        rootPane.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private JButton getBotaoSalvar() {
        JPanel buttonPanel = (JPanel) getContentPane().getComponent(1);
        return (JButton) buttonPanel.getComponent(1);
    }


    private void salvarPeca() {
        if (!validarCampos()) return;

        try {
            Peca peca = new Peca();
            peca.setNome(nomeField.getText().trim());
            peca.setDescricao(descricaoField.getText().trim());
            peca.setValor(new BigDecimal(valorField.getValue().toString()));
            peca.setUrgencia((Urgencia) urgenciaJComboBox.getSelectedItem());
            peca.setUsuario(usuarioSelecionado);

            pecaService.salvar(peca);

            JOptionPane.showMessageDialog(this, "Peça cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            parent.carregarPecas();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar peça: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty()) {
            mostrarErroValidacao("O campo 'Nome' é obrigatório");
            nomeField.requestFocus();
            return false;
        }

        if (((Number) valorField.getValue()).doubleValue() <= 0) {
            mostrarErroValidacao("O valor deve ser maior que zero");
            valorField.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarErroValidacao(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Validação", JOptionPane.WARNING_MESSAGE);
    }
}