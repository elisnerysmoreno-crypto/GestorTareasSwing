/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class DialogoNotas extends JDialog {

    private JTextArea areaNotas;

    public DialogoNotas(java.awt.Frame parent, String notas) {
        super(parent, "Notas de la Tarea", true); // Título y modal
        
        // Configuración básica del diálogo
        setSize(400, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
   // 1. Área de Texto
        areaNotas = new JTextArea(notas);
        areaNotas.setLineWrap(true);
        areaNotas.setWrapStyleWord(true);
        areaNotas.setEditable(false); // ¡Importante! No se debe editar
        areaNotas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        // 2. Scroll Pane
        JScrollPane scrollPane = new JScrollPane(areaNotas);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Botón de Cerrar
        javax.swing.JButton btnCerrar = new javax.swing.JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose()); // Cierra la ventana al hacer clic

        javax.swing.JPanel panelBoton = new javax.swing.JPanel();
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
    }
}
     