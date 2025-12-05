/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;
import Modelo.Tarea;
import Vistas.MainFrame.EstadoVencimiento; 
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import javax.swing.Icon; // Importación necesaria para manejar iconos

// ===================================================================
// CLASE 1: RoundedPanel (Implementación que soporta la opacidad)
// ===================================================================
class RoundedPanel extends JPanel {
    private int cornerRadius = 10;
    
    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false); 
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // --- Lógica de Opacidad (lee el valor del Client Property) ---
        Object opacityObj = getClientProperty("JComponent.opacity");
        float opacity = (opacityObj instanceof Float) ? (Float)opacityObj : 1.0f;
        
        Color baseColor = getBackground();
        
        // Si la opacidad es menor a 1.0, crea un color con transparencia (Alpha)
        if (opacity < 1.0f) {
            int alpha = (int) (opacity * 255);
            graphics.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha));
        } else {
            graphics.setColor(baseColor);
        }
        // --- Fin Lógica de Opacidad ---

        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}


// ===================================================================
// CLASE 2: TarjetaTarea
// ===================================================================
public class TarjetaTarea extends RoundedPanel {

    private Tarea tareaCapturada;
    private MainFrame parentFrame; 
    
    private JLabel lblPrioridad;
    private JLabel lblFechaLimite;
    private JTextArea txtDescripcion;
    private JCheckBox chkCompletada;
    private JButton btnEliminar;
    private JButton btnEditar;
    
    private JPanel panelAdvertencia;
    private JLabel lblAdvertencia;

    public TarjetaTarea(Tarea tarea, MainFrame parentFrame) {
        super(10); 
        
        this.tareaCapturada = tarea;
        this.parentFrame = parentFrame; 
        
        initComponents();
        configurarListeners(); 
        actualizarTarjeta(); 
    }
    
    // El método initComponents() se mantiene igual.
    private void initComponents() {
        // --- 1. Configuración del Panel de la Tarjeta ---
        setLayout(new BorderLayout(0, 5)); 
        
        setPreferredSize(new Dimension(250, 180)); 
        setMinimumSize(new Dimension(250, 180));
        setMaximumSize(new Dimension(250, 180));

        setBorder(new EmptyBorder(5, 10, 5, 10)); 

        // --- 2. Panel de Advertencia ---
        panelAdvertencia = new JPanel(new BorderLayout());
        panelAdvertencia.setOpaque(true);
        panelAdvertencia.setPreferredSize(new Dimension(230, 20));
        panelAdvertencia.setVisible(false);
        
        lblAdvertencia = new JLabel();
        lblAdvertencia.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblAdvertencia.setForeground(Color.WHITE);
        lblAdvertencia.setHorizontalAlignment(JLabel.CENTER);
        
        panelAdvertencia.add(lblAdvertencia, BorderLayout.CENTER);
        
        panelAdvertencia.putClientProperty(FlatClientProperties.STYLE, "arc: 8;"); 
        
        // --- 3. Contenedor de Contenido ---
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BorderLayout(10, 10)); 
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(5, 0, 5, 0)); 

        // Panel Superior (Prioridad y Fecha)
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        lblPrioridad = new JLabel();
        lblPrioridad.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblFechaLimite = new JLabel();
        lblFechaLimite.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFechaLimite.setForeground(UIManager.getColor("Label.disabledForeground"));

        panelSuperior.add(lblPrioridad, BorderLayout.WEST);
        panelSuperior.add(lblFechaLimite, BorderLayout.EAST);

        // Área de Descripción (Cuerpo de la Nota)
        String descripcion = (tareaCapturada != null) ? tareaCapturada.getDescripcion() : "Tarea sin descripción";
        txtDescripcion = new JTextArea(descripcion);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.putClientProperty(FlatClientProperties.STYLE, "font: bold;"); 
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setBorder(null);

        // Panel Inferior (Checkbox y Botones)
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);

        chkCompletada = new JCheckBox("Completada");
        chkCompletada.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkCompletada.setFocusPainted(false);

        JPanel panelBotones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 3, 0));
        panelBotones.setOpaque(false);

        btnEditar = new JButton("✏️");
        btnEditar.setFocusPainted(false);
        btnEditar.setPreferredSize(new Dimension(30,28));

        btnEliminar = new JButton("🗑️");
        btnEliminar.setFocusPainted(false);
        btnEliminar.setPreferredSize(new Dimension(30,28));

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panelInferior.add(chkCompletada, BorderLayout.WEST);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        // --- 4. Añadir Componentes al Contenido y al Panel Principal ---
        panelContenido.add(panelSuperior, BorderLayout.NORTH);
        panelContenido.add(txtDescripcion, BorderLayout.CENTER);
        panelContenido.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelAdvertencia, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);
    }
    
    // El método configurarListeners() se mantiene igual.
    private void configurarListeners() {
        if (tareaCapturada == null) return;
        
        Modelo.GestorTareas gestor = parentFrame.getGestorTareas();

        // Listener del Checkbox (Completar Tarea)
        chkCompletada.addActionListener(e -> {
            if (gestor == null) return;
            
            tareaCapturada.setCompletada(chkCompletada.isSelected());
            gestor.guardarTareas(); 
            
            parentFrame.refrescarVentana(
                parentFrame.getChkMostrarCompletadas().isSelected()
            );
        });

        // Listener del Botón Eliminar
        btnEliminar.addActionListener(e -> {
            if (gestor == null) return;
            
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar esta tarea?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                gestor.eliminarTarea(tareaCapturada); 
                gestor.guardarTareas();
                
                parentFrame.refrescarVentana(
                    parentFrame.getChkMostrarCompletadas().isSelected()
                );
            }
        });
        
        // Listener del Botón Editar
        btnEditar.addActionListener(e -> {
            parentFrame.mostrarFormularioEdicion(tareaCapturada);
        });
    }

    // ===================================================================
    // MÉTODO ACTUALIZADO PARA DISPARAR LA ALERTA ANIMADA
    // ===================================================================
    private void actualizarTarjeta() {
       if (tareaCapturada == null) return;

        // 1. Asignar valores de los campos
        txtDescripcion.setText(tareaCapturada.getDescripcion());
        chkCompletada.setSelected(tareaCapturada.isCompletada());
        lblPrioridad.setText(tareaCapturada.getPrioridad().toString());

        String fechaTexto;
        if (tareaCapturada.getFechaLimite() != null) {
            fechaTexto = "Limite: " + tareaCapturada.getFechaLimite().toString();
        } else {
            fechaTexto = "Sin Fecha";
        }
        lblFechaLimite.setText(fechaTexto);

        // --- 2. LÓGICA DE COLORES, ESTADO y ADVERTENCIAS ---
        Color colorFondo;
        Color colorTexto = UIManager.getColor("TextComponent.foreground");
        
        EstadoVencimiento estado = MainFrame.obtenerEstadoVencimiento(tareaCapturada);
        
        // NUEVAS VARIABLES PARA LA ALERTA POPUP
        boolean necesitaAlertaAnimada = false;
        String mensajeAlerta = "";
        Icon iconoAlerta = null;
        Color colorAlerta = null;
        
        // Resetear advertencia y opacidad por defecto
        panelAdvertencia.setVisible(false); 
        this.putClientProperty("JComponent.opacity", 1.0f); 

        // =======================================================
        // A) PRIORIDAD ABSOLUTA: Tarea VENCIDA
        // =======================================================
        if (estado == EstadoVencimiento.VENCIDA) {
            colorFondo = new Color(139, 0, 0); 
            colorTexto = Color.WHITE; 
            lblPrioridad.setText("⛔ URGENTE");
            
            lblAdvertencia.setText("🚨 ACTIVIDAD VENCIDA 🚨");
            panelAdvertencia.setBackground(new Color(255, 69, 0)); 
            panelAdvertencia.setVisible(true);

            // CONFIGURACIÓN DE LA ALERTA ANIMADA
            necesitaAlertaAnimada = true;
            mensajeAlerta = "¡¡ALERTA!! Tarea VENCIDA: " + tareaCapturada.getDescripcion();
            
            // Llama a un método en MainFrame para obtener el icono que tú definiste
            iconoAlerta = parentFrame.getIconoAsustado(); 
            colorAlerta = new Color(255, 69, 0).darker(); 

        // =======================================================
        // B) PRIORIDAD ALTA: Por Vencer (Si no está vencida)
        // =======================================================
        } else if (estado == EstadoVencimiento.POR_VENCER) {
            colorFondo = new Color(74, 60, 30); 
            
            lblAdvertencia.setText("🔔 ACTIVADA POR VENCER - ¡Vamos Apresurarnos! 🚀");
            panelAdvertencia.setBackground(new Color(255, 140, 0)); 
            panelAdvertencia.setVisible(true);

            // CONFIGURACIÓN DE LA ALERTA ANIMADA
            necesitaAlertaAnimada = true;
            mensajeAlerta = "¡¡ATENCIÓN!! Tarea Por Vencer: " + tareaCapturada.getDescripcion();
            
            // Llama a un método en MainFrame para obtener el icono que tú definiste
            iconoAlerta = parentFrame.getIconoFeliz(); 
            colorAlerta = new Color(255, 140, 0).darker(); 

        // =======================================================
        // C) LÓGICA ESTÁNDAR: (Ni vencida ni por vencer)
        // =======================================================
        } else {
            switch (tareaCapturada.getPrioridad()) {
                case ALTA:
                    colorFondo = new Color(74, 30, 30); 
                    break;
                case MEDIA:
                    colorFondo = new Color(74, 60, 30); 
                    break;
                default: // Caso BAJA o SIN PRIORIDAD
                    colorFondo = new Color(30, 74, 30); 
                    break;
            }
        }

        // =======================================================
        // D) ESTADO COMPLETADO: Estilos de desactivación (Override)
        // =======================================================
        if (tareaCapturada.isCompletada()) {
            colorFondo = UIManager.getColor("TextArea.background").darker();
            colorTexto = UIManager.getColor("Label.disabledForeground");
            
            txtDescripcion.setEnabled(false);
            chkCompletada.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(true);
            
            panelAdvertencia.setVisible(false); 
            necesitaAlertaAnimada = false;

        } else {
            txtDescripcion.setEnabled(true);
            chkCompletada.setEnabled(true);
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }

        // --- 3. Aplicar estilos finales ---
        this.setBackground(colorFondo);
        
        txtDescripcion.setForeground(colorTexto);
        lblPrioridad.setForeground(colorTexto);
        lblFechaLimite.setForeground(colorTexto);
        chkCompletada.setForeground(colorTexto);
        
        // --- 4. Disparar la Alerta Animada si es necesario ---
        // Solo la disparamos si el MainFrame existe y la bandera está activa.
        if (necesitaAlertaAnimada && parentFrame != null) {
            parentFrame.mostrarAlertaAnimada(mensajeAlerta, iconoAlerta, colorAlerta);
        }
        
        // Si no se usa la animación, se asegura que la opacidad esté en 1.0
        this.putClientProperty("JComponent.opacity", 1.0f);
        
        repaint(); 
    }
    
    // NOTA: El método animarAdvertencia() (fade-in o pulse) ya no es necesario
    // si usas el AlertaAnimadaPopup, por lo que lo hemos omitido aquí.
}
