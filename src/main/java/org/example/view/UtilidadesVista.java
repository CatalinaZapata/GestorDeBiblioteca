package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.function.Function;

public class UtilidadesVista {
    private UtilidadesVista() {}

    public static void info(Component padre, String mensaje){
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component padre, Exception e){
        String texto;
        if(e instanceof SQLIntegrityConstraintViolationException){
            texto = "No se pudo completar la operación: el valor ya existe (RUT, correo o ISBN duplicado)" + "o el registro tiene datos asociados (por ejemplo, préstamos).";
        } else if(e instanceof IllegalArgumentException){
            texto = e.getMessage();
        }  else {
            texto = "Error: " + e.getMessage();
        }
        JOptionPane.showMessageDialog(padre, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component padre, String pregunta){
        return JOptionPane.showConfirmDialog(padre, pregunta, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @SuppressWarnings("unchecked")
    public static <T> DefaultListCellRenderer textoDe(Function<T,String> funcion){
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice, boolean seleccionado, boolean foco){
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, foco);
                if (valor != null) {
                    setText(funcion.apply((T) valor));
                }
                return this;
            }
        };
    }

    public static void resaltarAtrasados(JTable tabla, int columnaEstado){
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent (JTable t, Object valor,boolean seleccionada, boolean foco, int fila, int columna) {
                Component c = super.getTableCellRendererComponent(t, valor, seleccionada, foco, fila, columna);
                if (!seleccionada) {
                    boolean atrasado = "ATRASADO".equals(t.getValueAt(fila, columnaEstado));
                    c.setForeground(atrasado ? Color.RED : t.getForeground());
                }
                return c;
            }
        });
    }

}