/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FondoPanel extends JPanel {

    private Image imagenFondo;
    private String rutaImagen; // Para guardar la ruta y poder cargarla

    public FondoPanel(String rutaImagen) {
        this.rutaImagen = rutaImagen;
        cargarImagen();
        setOpaque(false); // Importante para que el panel se pinte a sí mismo
    }

    private void cargarImagen() {
        try {
            // Asegúrate de que la ruta sea relativa al classpath
            // Si la imagen está en src/recursos/fondo.jpg, la ruta es "/recursos/fondo.jpg"
            // La ruta que tu MainFrame usa es "/imagenespers/icono_tarea.jpg"
            // Ajusta esta ruta a donde tengas tu imagen de fondo.
            // Ejemplo: if (rutaImagen == null) this.rutaImagen = "/recursos/fondo_app.jpg";
            
            URL imageUrl = getClass().getResource("/imagenespers/logo_list.PNG");
            if (imageUrl != null) {
                this.imagenFondo = new ImageIcon(imageUrl).getImage();
            } else {
                System.err.println("Advertencia: Imagen de fondo no encontrada en la ruta: " + rutaImagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }
    
    // Método para cambiar la imagen de fondo dinámicamente si es necesario
    public void setImagenFondo(String nuevaRuta) {
        this.rutaImagen = nuevaRuta;
        cargarImagen();
        repaint(); // Vuelve a pintar el panel con la nueva imagen
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // Opción 1: Estirar la imagen para llenar el panel (puede distorsionarse)
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            
            // Opción 2: Centrar la imagen (manteniendo proporciones, el resto del fondo será el color del panel)
            // int x = (getWidth() - imagenFondo.getWidth(this)) / 2;
            // int y = (getHeight() - imagenFondo.getHeight(this)) / 2;
            // g.drawImage(imagenFondo, x, y, this);

            // Opción 3: Repetir la imagen como un patrón (útil para texturas pequeñas)
            // if (imagenFondo.getWidth(this) > 0 && imagenFondo.getHeight(this) > 0) {
            //     for (int x = 0; x < getWidth(); x += imagenFondo.getWidth(this)) {
            //         for (int y = 0; y < getHeight(); y += imagenFondo.getHeight(this)) {
            //             g.drawImage(imagenFondo, x, y, this);
            //         }
            //     }
            // }
        }
    }
}