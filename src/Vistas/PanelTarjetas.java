/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;
import Modelo.Tarea;
import Vistas.WrapLayout; // <--- ¡NUEVA IMPORTACIÓN!
import java.awt.Dimension;
// import java.awt.FlowLayout; // <--- Ya no es necesario si usamos Vistas.WrapLayout
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.Box; // Necesario para Box.createVerticalGlue()


public class PanelTarjetas extends JPanel {

    private List<Tarea> tareasMostradas;
    private MainFrame parentFrame;  
    
    // **NUEVO:** Panel que contendrá las tarjetas
    private JPanel flowPanel; 

    public PanelTarjetas(Modelo.GestorTareas gestorTareas, MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        
        // --- 1. CONFIGURACIÓN DEL PANEL PRINCIPAL (PanelTarjetas) ---
        // Se mantiene BoxLayout Y_AXIS para que el flowPanel quede arriba.
        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS)); 
        this.setOpaque(false);
        this.tareasMostradas = gestorTareas.getTodasLasTareas();
        
        // --- 2. CONFIGURACIÓN DEL FLOW PANEL (El subpanel que envuelve) ---
        flowPanel = new JPanel();
        
        // ¡CAMBIO CLAVE! USAR WRAPLAYOUT PARA EL ENVOLVIMIENTO INTELIGENTE
        flowPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 15, 15)); // <--- ¡AQUÍ VA EL CAMBIO!
        
        flowPanel.setOpaque(false); 
        
        // --- 3. AÑADIR EL FLOW PANEL AL PANEL PRINCIPAL ---
        this.add(flowPanel); 
        
        // Añadir un "pegamento" vertical al final para que el flowPanel se quede arriba.
        this.add(Box.createVerticalGlue()); // Usamos Box.createVerticalGlue()
    }
    
    public void setTareasMostradas(List<Tarea> tareas) { 
        this.tareasMostradas = tareas;
    }
    
    //Lógi de Renderizado
    public void cargarTarjetas() {
    flowPanel.removeAll();
    for (Tarea tarea : tareasMostradas) {
        TarjetaTarea tarjeta = new TarjetaTarea(tarea, this.parentFrame);  
        flowPanel.add(tarjeta);
    }
    flowPanel.revalidate();
    flowPanel.repaint();
   
    javax.swing.SwingUtilities.invokeLater(() -> {
       
        if (flowPanel.getParent() != null) {
            flowPanel.getParent().revalidate();
            flowPanel.getParent().repaint();
        }
        
        if (parentFrame != null) {
            parentFrame.getContentPane().revalidate();
            parentFrame.repaint();
        }
    });
}
}