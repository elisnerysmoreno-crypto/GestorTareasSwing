
package Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatClientProperties; 

public class AlertaAnimadaPopup extends JDialog {

    private JLabel lblPersonaje;
    private Timer animTimer;
    private int animStep = 0;
    private final int MAX_STEPS = 20; 
    private final int DURATION_MS = 500; // Duración total de la animación de entrada/salida
    private final int PAUSE_MS = 1500; // Tiempo que permanece visible en su estado final

    public AlertaAnimadaPopup(Frame owner, String mensajeAlerta, Icon iconoPersonaje, Color backgroundColor) {
    super(owner, false); 
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); 
        setLayout(new GridBagLayout());

        // Contenedor principal de la alerta
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(true);
        contentPanel.setBackground(backgroundColor); // Color de fondo del globo o burbuja
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20;"); // Esquinas redondeadas

        // Etiqueta del mensaje
        JTextArea txtMensaje = new JTextArea(mensajeAlerta);
        txtMensaje.setEditable(false);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setLineWrap(true);
        txtMensaje.setOpaque(false);
        txtMensaje.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtMensaje.setForeground(Color.WHITE);
        txtMensaje.setPreferredSize(new Dimension(300, 50)); 

        // Etiqueta del personaje
        lblPersonaje = new JLabel(iconoPersonaje);
        lblPersonaje.setHorizontalAlignment(JLabel.CENTER);

        contentPanel.add(lblPersonaje, BorderLayout.WEST); // Personaje a la izquierda
        contentPanel.add(txtMensaje, BorderLayout.CENTER); // Mensaje al centro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(contentPanel, gbc);

        pack(); // Ajusta el tamaño del diálogo al contenido
        setLocationRelativeTo(owner); // Centrar respecto al MainFrame
    }

    public void mostrarYAnimar() {
        
        this.setOpacity(0f);
        this.setSize(0, 0); 

        setVisible(true); 

        animTimer = new Timer(DURATION_MS / MAX_STEPS, new ActionListener() {
            private int currentStep = 0;
            private int phase = 0; 

            @Override
            public void actionPerformed(ActionEvent e) {
                if (phase == 0) { 
                    currentStep++;
                    float progress = (float) currentStep / MAX_STEPS;

                    setOpacity(progress);
                    int initialWidth = 0; 
                    int initialHeight = 0;
                    int targetWidth = AlertaAnimadaPopup.this.getPreferredSize().width;
                    int targetHeight = AlertaAnimadaPopup.this.getPreferredSize().height;

                    int currentWidth = (int) (initialWidth + (targetWidth - initialWidth) * progress);
                    int currentHeight = (int) (initialHeight + (targetHeight - initialHeight) * progress);

                    // Para que crezca desde el centro
                    int x = (getOwner().getWidth() - currentWidth) / 2;
                    int y = (getOwner().getHeight() - currentHeight) / 2;
                    
                    setBounds(x, y, currentWidth, currentHeight);
                    
                    if (currentStep >= MAX_STEPS) {
                        currentStep = 0;
                        phase = 1;
                        ((Timer) e.getSource()).setDelay(PAUSE_MS); 
                    }
                } else if (phase == 1) { 
                    currentStep++;
                    if (currentStep >= 1) { 
                        currentStep = 0;
                        phase = 2; 
                        ((Timer) e.getSource()).setDelay(DURATION_MS / MAX_STEPS);
                    }
                } else if (phase == 2) { 
                    currentStep++;
                    float progress = 1.0f - ((float) currentStep / MAX_STEPS); 

                    
                    setOpacity(progress);
                   
                    int targetWidth = AlertaAnimadaPopup.this.getPreferredSize().width;
                    int targetHeight = AlertaAnimadaPopup.this.getPreferredSize().height;

                    int currentWidth = (int) (targetWidth * progress);
                    int currentHeight = (int) (targetHeight * progress);
                    
                    // Para que se encoja hacia el centro
                    int x = (getOwner().getWidth() - currentWidth) / 2;
                    int y = (getOwner().getHeight() - currentHeight) / 2;

                    setBounds(x, y, currentWidth, currentHeight);

                    if (currentStep >= MAX_STEPS) {
                        animTimer.stop();
                        dispose(); 
                    }
                }
            }
        });
        animTimer.start();
    }
}