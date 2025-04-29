package com.oficina.sistemapecas.ui;

import com.oficina.sistemapecas.model.Peca;
import com.oficina.sistemapecas.model.Urgencia;
import com.oficina.sistemapecas.model.Usuario;
import com.oficina.sistemapecas.service.PecaService;
import com.oficina.sistemapecas.service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.List;
import java.math.BigDecimal;

public class NovaPecaForm extends JDialog {

    private final JTextField nomeField = new JTextField(20);
    private final JTextField descricaoField = new JTextField(20);
    private final JFormattedTextField valorField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
    private final JComboBox<Urgencia> urgenciaJComboBox = new JComboBox<>(Urgencia.values());
    private final JComboBox<Usuario> usuarioJComboBox = new JComboBox<>();

    private final PecaService pecaService;
    private final UsuarioService usuarioService;
    private final MainWindow parent;

    Usuario usuarioSelecionado = (Usuario) usuarioJComboBox.getSelectedItem();

    // Construtorr que configura o diálogo
    public NovaPecaForm(MainWindow parent, PecaService pecaService, UsuarioService usuarioService) {
        super(parent, "Cadastro de Nova Peça", true);
        this.parent = parent;
        this.pecaService = pecaService;
        this.usuarioService = usuarioService;

        configureUI();
        carregarUsuario();

    }

    private void configureUI(){
        setLayout(new BorderLayout(10, 10));
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setResizable(false);

        // Configurarção do campo de valor monetário
        valorField.setColumns(10);
        valorField.setValue(BigDecimal.ZERO);

        // Painel de formulário com GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adiconando componentes com alinhamento consistente
        addFormField(formPanel, gbc, 0, "Nome: ", nomeField);
        addFormField(formPanel, gbc, 1, "Descrição:", descricaoField);
        addFormField(formPanel, gbc, 2, "Valor (R$):", valorField);
        addFormField(formPanel, gbc, 3, "Urgência:", urgenciaJComboBox);
        addFormField(formPanel, gbc, 4, "Responsável:", usuarioJComboBox);

        // Painel de botões com flowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10 ,10));
        JButton btnSalvar = createButton("Salvar", this::salvarPeca, Color.GREEN.darker());
        JButton btnCanelcar = createButton("Cancelar", this::dispose, Color.RED.darker());

        buttonPanel.add(btnCanelcar);
        buttonPanel.add(btnSalvar);

        // Adicionando componetes ao diálogo
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Comportamento padrão do Enter/ESC
        configureKeyboardActions();
    }

    // Métodos Auxiliares

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field){
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JButton createButton(String text, Runnable action, Color bgColor){
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void configureKeyboardActions(){
        // Enter salvar, ESQ = Cancelar
        JRootPane rootPane = this.getRootPane();
        rootPane.setDefaultButton((JButton) ((JPanel) getContentPane().getComponent(1)).getComponent(1));

        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        rootPane.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    // Lógica de negocio

    // Método para salvar as peças
    private void salvarPeca() {
        if(!validarCampos()) return;

       try{
           Peca peca = new Peca();
           peca.setNome(nomeField.getText().trim());
           peca.setDescricao(descricaoField.getText().trim());
           peca.setValor(new BigDecimal(valorField.getValue().toString()));
           peca.setUrgencia((Urgencia) urgenciaJComboBox.getSelectedItem());
           peca.setUsuario((Usuario) usuarioJComboBox.getSelectedItem());

           pecaService.salvar(peca);

           JOptionPane.showMessageDialog(this,
                   "Peça cadastrada com sucesso!",
                   "Sucesso",
                   JOptionPane.INFORMATION_MESSAGE);

           parent.carregarPecas();
           dispose();
       } catch (Exception ex){
           JOptionPane.showMessageDialog(this,
                   "Erro ao salva peça: " + ex.getMessage(),
                   "Erro",
                   JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace();
       }

    }

    private boolean validarCampos(){
        // Validação em cascata com mensagens específicas
        if(nomeField.getText().trim().isEmpty()){
            showValidationErro("O campo 'Nome' é obrigatório");
            nomeField.requestFocus();
            return false;
        }

        if(((Number) valorField.getValue()).doubleValue() <= 0){
            showValidationErro("O valor deve ser maior que zero");
            valorField.requestFocus();
            return false;
        }

      /*  if (usuarioJComboBox.getSelectedItem() == null) {
            showValidationErro("Selecione um responsável");
            usuarioJComboBox.requestFocus();
            return false;
        }*/

        return true;

    }

    private void showValidationErro(String message){
        JOptionPane.showMessageDialog(this,
                message,
                "Validação",
                JOptionPane.WARNING_MESSAGE);
    }



    private void carregarUsuario(){
        SwingUtilities.invokeLater(() -> {
            usuarioJComboBox.removeAll();
            List<Usuario> usuarios = usuarioService.listarTodos();
            usuarios.forEach(usuarioJComboBox::addItem);

        });
    }

}
