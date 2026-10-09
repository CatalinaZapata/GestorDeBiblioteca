package org.example.app;

import org.example.view.VentanaAutenticacion;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorada) {}
        SwingUtilities.invokeLater(() -> new VentanaAutenticacion().setVisible(true));

    }
}