/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelo.Tarea;
import Modelo.Prioridad;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser; 
import javax.swing.Timer;

public class FormularioTareaDialog extends JDialog {

    private Tarea tareaResultado;
    private boolean edicionExitosa = false;
    private Tarea tareaOriginal;
    
    // Componentes UI
    private JTextArea txtDescripcion;
    private JTextField txtTitulo;
    private JDateChooser dateChooser;
    
    private JToggleButton btnAlta;
    private JToggleButton btnMedia;
    private JToggleButton btnBaja;
    private ButtonGroup grupoPrioridad;
    
    private JButton btnGuardar;
    private JButton btnCancelar;

    // --- VARIABLES PARA EL EFECTO DE DESVANECIMIENTO ---
    private Timer fadeTimer;
    private float targetOpacity;
    private float currentOpacity;
    private final int FADE_STEPS = 15;
    private final int FADE_DELAY = 25; 

    // --- CONSTRUCTORES (CORREGIDOS) ---

    public FormularioTareaDialog(java.awt.Frame parent, boolean modal) {
        // 1. LLAMADA A SUPER() DEBE SER LA PRIMERA
        super(parent, modal); 
        this.tareaOriginal = null;
        
        // 2. CONFIGURACIÓN CRÍTICA PARA EL FADE
        setUndecorated(true);
        
        initComponents("Crear Nueva Tarea");
        setLocationRelativeTo(parent);
    }

    public FormularioTareaDialog(java.awt.Frame parent, boolean modal, Tarea tarea) {
        // 1. LLAMADA A SUPER() DEBE SER LA PRIMERA
        super(parent, modal); 
        this.tareaOriginal = tarea;
        
        // 2. CONFIGURACIÓN CRÍTICA PARA EL FADE
        setUndecorated(true); 
        
        initComponents("Editar Tarea Existente");
        cargarDatosTarea(tarea);
        setLocationRelativeTo(parent);
    }
    
    // NOTA: El método inicializarDialogo ha sido eliminado.

    // --- INICIALIZACIÓN DE COMPONENTES ---

    private void initComponents(String titulo) {
        setTitle(titulo);
        setMinimumSize(new Dimension(500, 400));
        setLayout(new BorderLayout(15, 15));
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        contentPanel.setBackground(UIManager.getColor("Panel.background"));

        JPanel panelDatos = new JPanel(new java.awt.GridLayout(3, 1, 10, 10));
        panelDatos.setOpaque(false);
        
        txtTitulo = new JTextField(tareaOriginal != null ? tareaOriginal.getDescripcion() : "");
        txtTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTitulo.putClientProperty("JTextField.placeholderText", "Título de la Tarea...");

        dateChooser = new JDateChooser();
        dateChooser.setLocale(java.util.Locale.getDefault());
        
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFecha.setOpaque(false);
        panelFecha.add(new JLabel(new FlatSVGIcon("com/formdev/flatlaf/icons/calendar.svg")));
        panelFecha.add(new JLabel("Fecha Límite:"));
        panelFecha.add(dateChooser);
        
        panelDatos.add(txtTitulo);
        panelDatos.add(panelFecha);
        
        JPanel panelPrioridad = crearPanelPrioridad();
        panelDatos.add(panelPrioridad);

        txtDescripcion = new JTextArea();
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.putClientProperty("JTextArea.placeholderText", "Detalles o notas adicionales...");
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.setPreferredSize(new Dimension(450, 150));
        
        JPanel panelBotones = crearPanelBotones();
        
        contentPanel.add(panelDatos, BorderLayout.NORTH);
        contentPanel.add(scrollDescripcion, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        pack();
    }
    
    // --- MÉTODOS AUXILIARES ---

    private JPanel crearPanelPrioridad() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        panel.add(new JLabel(new FlatSVGIcon("com/formdev/flatlaf/icons/flag.svg")));
        panel.add(new JLabel("Prioridad:"));
        
        ButtonGroup grupoPrioridad = new ButtonGroup();
        
        btnAlta = new JToggleButton("ALTA");
        btnAlta.putClientProperty("JButton.buttonType", "roundRect");
        btnAlta.setForeground(new Color(255, 87, 87));
        grupoPrioridad.add(btnAlta);
        panel.add(btnAlta);
        
        btnMedia = new JToggleButton("MEDIA");
        btnMedia.putClientProperty("JButton.buttonType", "roundRect");
        btnMedia.setForeground(new Color(255, 165, 0));
        grupoPrioridad.add(btnMedia);
        panel.add(btnMedia);
        
        btnBaja = new JToggleButton("BAJA");
        btnBaja.putClientProperty("JButton.buttonType", "roundRect");
        btnBaja.setForeground(new Color(0, 123, 255));
        grupoPrioridad.add(btnBaja);
        panel.add(btnBaja);
        
        btnMedia.setSelected(true);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setOpaque(false);

        btnCancelar = new JButton("Cancelar", new FlatSVGIcon("com/formdev/flatlaf/icons/close.svg"));
        btnCancelar.addActionListener(e -> desvanecerYCerrar()); 
        panel.add(btnCancelar);

        btnGuardar = new JButton("Guardar", new FlatSVGIcon("com/formdev/flatlaf/icons/menu-saveall.svg"));
        btnGuardar.putClientProperty("JButton.defaultButton", true);
        btnGuardar.addActionListener(e -> validarYGuardar());
        panel.add(btnGuardar);
        
        return panel;
    }

    // --- LÓGICA DE DATOS Y VALIDACIÓN ---

    private void cargarDatosTarea(Tarea tarea) {
        if (tarea == null) return;
        
        txtTitulo.setText(tarea.getDescripcion());
        txtDescripcion.setText(tarea.getNotas() != null ? tarea.getNotas() : ""); 
        
        if (tarea.getFechaLimite() != null) {
            Date date = Date.from(tarea.getFechaLimite().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(date);
        }
        
        switch (tarea.getPrioridad()) {
            case ALTA:
                btnAlta.setSelected(true);
                break;
            case MEDIA:
                btnMedia.setSelected(true);
                break;
            case BAJA:
                btnBaja.setSelected(true);
                break;
            default:
                 btnMedia.setSelected(true);
        }
    }
    
    private void validarYGuardar() {
        String descripcion = txtTitulo.getText().trim();
        LocalDate fechaLimite = null;
        Prioridad prioridad = getPrioridadSeleccionada();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título de la tarea no puede estar vacío.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            txtTitulo.requestFocus();
            return;
        }
        
        if (dateChooser.getDate() != null) {
            fechaLimite = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        
        String notas = txtDescripcion.getText().trim();

        if (tareaOriginal == null) {
            tareaResultado = new Tarea(descripcion, prioridad, fechaLimite, notas);
        } else {
            tareaOriginal.setDescripcion(descripcion);
            tareaOriginal.setPrioridad(prioridad);
            tareaOriginal.setFechaLimite(fechaLimite);
            tareaOriginal.setNotas(notas);
            tareaResultado = tareaOriginal;
        }

        this.edicionExitosa = true;
        
        desvanecerYCerrar(); 
    }
    
    private Prioridad getPrioridadSeleccionada() {
        if (btnAlta.isSelected()) return Prioridad.ALTA;
        if (btnMedia.isSelected()) return Prioridad.MEDIA;
        if (btnBaja.isSelected()) return Prioridad.BAJA;
        return Prioridad.MEDIA;
    }

    // --- MÉTODOS DE ANIMACIÓN EN ESPAÑOL ---

    public void aparecerSuavemente() { 
        currentOpacity = 0.0f;
    targetOpacity = 1.0f;
    
    fadeTimer = new Timer(FADE_DELAY, e -> {
        currentOpacity += (targetOpacity / FADE_STEPS);
        if (currentOpacity >= targetOpacity) {
            currentOpacity = targetOpacity;
            fadeTimer.stop();
        }
        setOpacity(currentOpacity);
    });
    fadeTimer.start();
}
    

    private void desvanecerYCerrar() { 
        if (getOpacity() <= 0.0f) {
            super.dispose();
            return;
        }
        
        currentOpacity = getOpacity();
        targetOpacity = 0.0f;
        
        fadeTimer = new Timer(FADE_DELAY, e -> {
            currentOpacity -= (1.0f / FADE_STEPS);
            if (currentOpacity <= targetOpacity) {
                currentOpacity = targetOpacity;
                fadeTimer.stop();
                super.dispose(); 
            }
            setOpacity(currentOpacity);
        });
        fadeTimer.start();
    }

  

    // --- GETTERS PÚBLICOS ---
    
    public Tarea getTareaResultado() {
        return tareaResultado;
    }

    public boolean haGuardadoExitoso() {
        return edicionExitosa;
    }
    
    public Tarea getTareaOriginal() {
        return tareaOriginal;
    }
}