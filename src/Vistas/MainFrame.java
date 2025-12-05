
package Vistas;
import com.formdev.flatlaf.FlatDarculaLaf;
import Modelo.GestorTareas;
import Modelo.Prioridad;
import Modelo.Tarea;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class MainFrame extends javax.swing.JFrame {

    //  Estados de Vencimiento 
    public enum EstadoVencimiento {
        VENCIDA,      
        POR_VENCER,   
        NORMAL        
    }

    //Modelos y Datos
    private GestorTareas gestorTareas;

    //Variables de UI específicas 
    private Vistas.PanelTarjetas panelTarjetas;
    private Vistas.Bienvenidos welcomePanel;
    
    // Declaraciones de componentes para uso manual
    private JButton btnAnadirTarea;
    private JButton btnFiltroHoy;
    private JCheckBox chkMostrarCompletadas;
    private JComboBox<String> cmbOrdenamiento;
    private Vistas.FondoPanel jPanel1; 
    private JPanel jPanel2; 
    private JScrollPane jScrollPane1; 
    private JTextField jTextField1; 
    private JLabel lblTituloProyectos;
    private JPanel panelBarraLateral;
    private JPanel panelProgreso; 
    private JProgressBar progressBar;
    
    
    private JButton btnFiltroTodas; 
    private JButton btnFiltroImportantes;
   
    
    public MainFrame() {
        
       initComponents();
    
    this.gestorTareas = Modelo.GestorTareas.cargarTareas();
    
    panelTarjetas = new Vistas.PanelTarjetas(gestorTareas, this);
    welcomePanel = new Vistas.Bienvenidos();
    
    if (cmbOrdenamiento != null) {
        cmbOrdenamiento.removeAllItems();
        cmbOrdenamiento.addItem("Prioridad");
        cmbOrdenamiento.addItem("Fecha Límite");
        cmbOrdenamiento.setSelectedItem("Prioridad");
    }
    
    if (jScrollPane1 != null) {
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
    }

    // Configuración de Layouts
    if (jPanel1 != null) {
        jPanel1.setLayout(new java.awt.BorderLayout());
    }
    if (jPanel2 != null) {
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
    }

    //Configuración Lógica (Layouts internos y Listeners - Barra Lateral y Controles Superiores)
    configurarBarraLateral();  
    configurarControles();  

    // Aplicación del BorderLayout 
    if (jPanel1 != null) {
        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH); 
        jPanel1.add(panelBarraLateral, java.awt.BorderLayout.WEST);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER); 
        
        JPanel southPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        if (progressBar != null) { 
            southPanel.add(progressBar);
         
            southPanel.setOpaque(false); 
            jPanel1.add(southPanel, java.awt.BorderLayout.SOUTH);
        }
    }

    if (this.gestorTareas.getTodasLasTareas().isEmpty()) {
        cargarDatosIniciales();
    }

    if(jTextField1 != null) {
        jTextField1.putClientProperty("FiltroActivo", "Todas");
    }
    
    
    this.setSize(800, 600); 
    this.setMinimumSize(new Dimension(600, 400));
    this.pack(); 
    this.setLocationRelativeTo(null);
 
    try {
        URL urlIcono = getClass().getResource("/imagenespers/icono_tarea.jpg");
        if (urlIcono != null) {
            Image icono = Toolkit.getDefaultToolkit().getImage(urlIcono);
            this.setIconImage(icono);
        }
    } catch (Exception e) {
        System.err.println("No se pudo cargar el icono: " + e.getMessage());
    }

    javax.swing.SwingUtilities.invokeLater(() -> {
      
        refrescarVentana(false); 
    });

    this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            gestorTareas.guardarTareas();
        }
    });
}

    //logi de iniciacion 

    private void cargarDatosIniciales() {
        LocalDate hoy = LocalDate.now();
        
        Tarea t1 = new Tarea("Diseñar Interfaz", Prioridad.ALTA, hoy.plusDays(1), "Estudiar layouts y FlatLaf."); 
        gestorTareas.añadirTarea(t1);

        Tarea t2 = new Tarea("Implementar Tarea Vencida", Prioridad.MEDIA, hoy.minusDays(7), "Esta debe aparecer como vencida."); 
        gestorTareas.añadirTarea(t2);

        Tarea t3 = new Tarea("Revisar Código", Prioridad.BAJA, hoy.plusDays(15), "Asegurar que constructores y eventos funcionan.");
        gestorTareas.añadirTarea(t3);
        
        gestorTareas.guardarTareas();
    }

    private void configurarBarraLateral() {
        if (panelBarraLateral == null || btnFiltroTodas == null || btnFiltroHoy == null || btnFiltroImportantes == null) return;
        
        panelBarraLateral.setLayout(new javax.swing.BoxLayout(panelBarraLateral, javax.swing.BoxLayout.Y_AXIS));  
        panelBarraLateral.setBorder(new EmptyBorder(15, 10, 15, 10));  
        panelBarraLateral.setPreferredSize(new Dimension(180, 400));
        
        // Estilo del título de proyectos
        lblTituloProyectos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloProyectos.setBorder(new EmptyBorder(0, 0, 10, 0));
        lblTituloProyectos.setAlignmentX(Component.LEFT_ALIGNMENT);

        //estilo  para los botones de filtro 
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension maxButtonSize = new Dimension(Integer.MAX_VALUE, 35);
        Color colorBase = UIManager.getColor("Panel.background"); 
        
        JButton[] buttons = {btnFiltroTodas, btnFiltroHoy, btnFiltroImportantes};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(maxButtonSize);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(5, 5, 5, 5));
            btn.setBackground(colorBase); 
            btn.setOpaque(true);
            btn.setBorderPainted(false); 
        }

        //componentes a la barra latera
        panelBarraLateral.add(lblTituloProyectos);
        
        panelBarraLateral.add(btnFiltroTodas); 
        panelBarraLateral.add(btnFiltroHoy); 
        panelBarraLateral.add(btnFiltroImportantes);
        panelBarraLateral.add(Box.createRigidArea(new Dimension(0, 20))); 
        
        java.awt.event.ActionListener filtroListener = e -> {
            JButton source = (JButton) e.getSource();
            String filtro = source.getText().equals("Todas") ? "Todas" : 
                                    source.getText().equals("Hoy") ? "Hoy" : "Importantes";
            
            jTextField1.setText(""); 
            jTextField1.putClientProperty("FiltroActivo", filtro);
            
            actualizarEstiloFiltros(filtro); 
            
            refrescarVentana(chkMostrarCompletadas.isSelected());
        };
        
        btnFiltroTodas.addActionListener(filtroListener);
        btnFiltroHoy.addActionListener(filtroListener);
        btnFiltroImportantes.addActionListener(filtroListener);
    }


    private void configurarControles() {
        if (cmbOrdenamiento == null || jTextField1 == null || chkMostrarCompletadas == null || btnAnadirTarea == null) return;
    
        //  Declarar y Asignar la fuente
        Font controlsFont = new Font("Segoe UI", Font.PLAIN, 14);
    
        btnAnadirTarea.setFont(controlsFont);
        chkMostrarCompletadas.setFont(controlsFont);
        cmbOrdenamiento.setFont(controlsFont);
        jTextField1.setFont(controlsFont);

        btnAnadirTarea.putClientProperty("JButton.buttonType", "roundRect"); 
        btnAnadirTarea.putClientProperty("JButton.defaultButton", true);
        btnAnadirTarea.setPreferredSize(new Dimension(130, 30)); 
        
        // Ajustar tamaño del campo de texto
        jTextField1.setPreferredSize(new Dimension(150, 28));
     
        cmbOrdenamiento.addActionListener(e -> refrescarVentana(chkMostrarCompletadas.isSelected()));

        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void handleUpdate() {
                jTextField1.putClientProperty("FiltroActivo", "TextoBusqueda"); 
                actualizarEstiloFiltros("TextoBusqueda");
                refrescarVentana(chkMostrarCompletadas.isSelected()); 
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handleUpdate(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handleUpdate(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handleUpdate(); }
        });
    }
    
    private void actualizarEstiloFiltros(String filtroActivo) {
        if (btnFiltroTodas == null || btnFiltroHoy == null || btnFiltroImportantes == null) return;

        // Colores
        Color colorInactivo = UIManager.getColor("Panel.background"); 
        Color colorActivo = UIManager.getColor("Button.default.focusColor"); 
        Color colorTextoActivo = UIManager.getColor("Button.default.foreground"); 

        // Resetear todos los botones
        JButton[] buttons = {btnFiltroTodas, btnFiltroHoy, btnFiltroImportantes};
        for (JButton btn : buttons) {
            btn.setBackground(colorInactivo);
            btn.setForeground(UIManager.getColor("Label.foreground")); 
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        }

        // Aplicar estilo al botón activo
        JButton botonSeleccionado = null;
        
        if ("Todas".equals(filtroActivo)) {
            botonSeleccionado = btnFiltroTodas;
        } else if ("Hoy".equals(filtroActivo)) {
            botonSeleccionado = btnFiltroHoy;
        } else if ("Importantes".equals(filtroActivo)) {
            botonSeleccionado = btnFiltroImportantes;
        }
        
        if (botonSeleccionado != null) {
            botonSeleccionado.setBackground(colorActivo);
            botonSeleccionado.setForeground(colorTextoActivo); 
            botonSeleccionado.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        }
    }


    public static EstadoVencimiento obtenerEstadoVencimiento(Tarea tarea) {
        if (tarea.getFechaLimite() == null) {
            return EstadoVencimiento.NORMAL;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate limite = tarea.getFechaLimite();

        if (limite.isBefore(hoy)) {
            return EstadoVencimiento.VENCIDA;
        } else if (limite.isEqual(hoy) || limite.isEqual(hoy.plusDays(1))) {
            return EstadoVencimiento.POR_VENCER;
        } else {
            return EstadoVencimiento.NORMAL;
        }
    }


    public void mostrarFormularioEdicion(Tarea tarea) {
        FormularioTareaDialog dialog = new FormularioTareaDialog(this, true, tarea);
        
        dialog.setOpacity(0.0f); 
        dialog.aparecerSuavemente(); 
        dialog.setVisible(true); 

        if (dialog.haGuardadoExitoso()) {
            gestorTareas.guardarTareas(); 
            refrescarVentana(chkMostrarCompletadas.isSelected());
        }
    }

    public void refrescarVentana(boolean incluirCompletadas) {
       if (jTextField1 == null || cmbOrdenamiento == null || jScrollPane1 == null || welcomePanel == null || panelTarjetas == null) {
        System.err.println("Advertencia: Componentes críticos de la UI son NULL al intentar refrescar.");
        return; 
    }

    String textoBusqueda = jTextField1.getText(); 
    List<Tarea> tareasFiltradas = gestorTareas.filtrarPorDescripcion(textoBusqueda); 

    String filtroActivo = (String) jTextField1.getClientProperty("FiltroActivo");

    if (filtroActivo != null && textoBusqueda.isEmpty()) { 
        
        actualizarEstiloFiltros(filtroActivo);

        if (filtroActivo.equals("Hoy")) {
            LocalDate hoy = LocalDate.now();
            // Filtro Hoy: Tareas no completadas con fecha límite hoy.
            tareasFiltradas = tareasFiltradas.stream()
                .filter(t -> t.getFechaLimite() != null && t.getFechaLimite().equals(hoy) && !t.isCompletada())
                .collect(Collectors.toList());
        
        } else if (filtroActivo.equals("Importantes")) {
            // Filtro Importantes: Tareas con Prioridad ALTA.
            tareasFiltradas = tareasFiltradas.stream()
                .filter(t -> t.getPrioridad() == Prioridad.ALTA)
                .collect(Collectors.toList());
        }

    } else if (!textoBusqueda.isEmpty()) {
        // Si hay texto de búsqueda, desactivamos visualmente los filtros de la barra lateral
        actualizarEstiloFiltros("TextoBusqueda"); 
    }

   
    if (!incluirCompletadas && !("Hoy").equals(filtroActivo)) {
        tareasFiltradas = tareasFiltradas.stream()
            .filter(t -> !t.isCompletada())
            .collect(Collectors.toList());
    }

    //Ordenamiento
    String orden = (String) cmbOrdenamiento.getSelectedItem();
    if (orden != null) {
        tareasFiltradas = gestorTareas.ordenarLista(tareasFiltradas, orden);
    }
    
    //Tarjetas o Bienvenida
    if (tareasFiltradas.isEmpty() && textoBusqueda.isEmpty()) {
        if (jScrollPane1.getViewport().getView() != welcomePanel) {
            jScrollPane1.setViewportView(welcomePanel);
        }
    } else {
        if (jScrollPane1.getViewport().getView() != panelTarjetas) {
            jScrollPane1.setViewportView(panelTarjetas);
        }
        // Solo cargar las tarjetas si estamos en el PanelTarjetas
        panelTarjetas.setTareasMostradas(tareasFiltradas);
        panelTarjetas.cargarTarjetas();
    }
    
    //Actualización de UI
    actualizarBarraProgreso();
    desplazarAlInicio();

    }

    private void actualizarBarraProgreso() {
        if (progressBar == null) return;
        int totalTareas = gestorTareas.getTodasLasTareas().size();
        if (totalTareas == 0) {
            progressBar.setValue(0);
            progressBar.setString("0% Completado");
            progressBar.setStringPainted(true);
            progressBar.setForeground(UIManager.getColor("ProgressBar.foreground")); 
            return;
        }

        int porcentaje = gestorTareas.getPorcentajeCompletado();
        progressBar.setValue(porcentaje);
        progressBar.setString(porcentaje + "% Completado");
        progressBar.setStringPainted(true);

        // Colores 
        if (porcentaje == 100) progressBar.setForeground(new Color(60, 179, 113)); 
        else if (porcentaje >= 75) progressBar.setForeground(new Color(255, 165, 0)); 
        else if (porcentaje >= 50) progressBar.setForeground(new Color(100, 149, 237)); 
        else progressBar.setForeground(new Color(220, 20, 60)); 
    }

    private void abrirFormularioNuevaTarea() {
        FormularioTareaDialog dialogo = new FormularioTareaDialog(this, true);
        
        dialogo.setOpacity(0.0f); 
        dialogo.aparecerSuavemente(); 
        dialogo.setVisible(true); 

        if (dialogo.haGuardadoExitoso()) {
            Tarea nuevaTarea = dialogo.getTareaResultado();
            if (nuevaTarea != null) {
                gestorTareas.añadirTarea(nuevaTarea);
                gestorTareas.guardarTareas(); 
                refrescarVentana(chkMostrarCompletadas.isSelected());
            }
        }
    }
    
    public Modelo.GestorTareas getGestorTareas() {
        return gestorTareas;
    }

    public JCheckBox getChkMostrarCompletadas() {
        return chkMostrarCompletadas;
    }
    
    private void chkMostrarCompletadasActionPerformed(java.awt.event.ActionEvent evt) {  
        refrescarVentana(chkMostrarCompletadas.isSelected()); 
    }
    
    public void mostrarAlertaAnimada(String mensaje, Icon icono, Color colorFondo) {
    
    SwingUtilities.invokeLater(() -> {
        AlertaAnimadaPopup popup = new AlertaAnimadaPopup(this, mensaje, icono, colorFondo);
        popup.mostrarYAnimar();
    });
}

public Icon getIconoFeliz() {
   
    try {
        Image img = new ImageIcon(getClass().getResource("/imagenespers/snoopy_feliz.png")).getImage();
        
        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH); 
        return new ImageIcon(scaledImg);
    } catch (Exception e) {
        System.err.println("Error cargando icono Duo Feliz: " + e.getMessage());
        return new ImageIcon(); 
    }
}

public Icon getIconoAsustado() {
    try {
        Image img = new ImageIcon(getClass().getResource("/imagenespers/snoopy_asustado.png")).getImage();
        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH); 
        return new ImageIcon(scaledImg);
    } catch (Exception e) {
        System.err.println("Error cargando icono Duo Asustado: " + e.getMessage());
        return new ImageIcon();
    }
}
    public static void main(String args[]) {
        try {
                
                 FlatDarculaLaf.setup();
                 UIManager.put("Button.arc", 10); 
                 UIManager.put("Component.arc", 10); 
                 UIManager.put("ProgressBar.arc", 10); 
                 UIManager.put("TextComponent.arc", 10); 
            
            } catch (Exception ex) {
                System.err.println("Error FlatLaf: " + ex);
            }
        
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
    
    private void desplazarAlInicio() {
        
        if (jScrollPane1 != null && jScrollPane1.getViewport() != null) {
            jScrollPane1.getViewport().setViewPosition(new java.awt.Point(0, 0));
        }
    }
   
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new Vistas.FondoPanel("/imagenespers/archivo_im.jpg"); 
        
        jPanel2 = new JPanel();
        jPanel2.setOpaque(false); 

        jTextField1 = new JTextField(15);
        btnAnadirTarea = new JButton("Añadir Tarea");
        chkMostrarCompletadas = new JCheckBox("Mostrar Completadas");
        cmbOrdenamiento = new JComboBox<>();
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setOpaque(false); 
        jScrollPane1.getViewport().setOpaque(false); 

        panelBarraLateral = new JPanel();
        panelBarraLateral.setOpaque(false); 
        
        btnFiltroTodas = new JButton("Todas"); 
        btnFiltroHoy = new JButton("Hoy");
        btnFiltroImportantes = new JButton("Importantes");

        lblTituloProyectos = new JLabel("Proyectos");
        panelProgreso = new JPanel();
        panelProgreso.setOpaque(false); 
        
        progressBar = new JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        panelBarraLateral.setPreferredSize(new Dimension(200, 400));
       
        jPanel2.add(jTextField1); 
        jPanel2.add(btnAnadirTarea);
        jPanel2.add(chkMostrarCompletadas);
        jPanel2.add(cmbOrdenamiento);
      
        panelProgreso.add(progressBar);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        chkMostrarCompletadas.addActionListener(this::chkMostrarCompletadasActionPerformed); 
        btnAnadirTarea.addActionListener(e -> abrirFormularioNuevaTarea());

        pack();
    }
}