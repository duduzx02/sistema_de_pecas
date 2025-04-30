package com.oficina.sistemapecas.ui.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class UrgenciaRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && !isSelected) {
            switch (value.toString().toLowerCase()) {
                case "vermelho": comp.setBackground(Color.RED); break;
                case "laranja": comp.setBackground(Color.ORANGE); break;
                case "verde": comp.setBackground(Color.GREEN); break;
                case "azul": comp.setBackground(Color.CYAN); break;
                default: comp.setBackground(Color.WHITE);
            }
        } else if (!isSelected) {
            comp.setBackground(Color.WHITE);
        }

        return comp;
    }
}
