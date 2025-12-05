/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Bienvenidos extends JPanel {

    public Bienvenidos () {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false); // Transparente para que se vea la imagen de fondo
        
        // --- 1. Mensaje principal (Título de bienvenida) ---
        JLabel lblMensaje = new JLabel("¡Bienvenido/a! Organiza tus tareas con nosotros ", SwingConstants.CENTER);
        Font welcomeFont = new Font("Segoe UI", Font.BOLD, 24);
        lblMensaje.setFont(welcomeFont);
        lblMensaje.setForeground(UIManager.getColor("ProgressBar.selectionBackground")); 

        // --- 2. Mensaje secundario (Instrucción) ---
        JLabel lblSubtitulo = new JLabel("Crea tu primera tarea usando el botón 'Añadir Tarea' en la parte superior.", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(UIManager.getColor("Label.disabledForeground"));

        // Panel contenedor para los dos Labels
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(150, 0, 0, 0)); 

        contentPanel.add(lblMensaje, BorderLayout.NORTH);
        contentPanel.add(lblSubtitulo, BorderLayout.CENTER);
        
        this.add(contentPanel, BorderLayout.CENTER);
    }
}