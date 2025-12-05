/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import Vistas.MainFrame;
import java.time.LocalDate;
import java.io.Serializable; // Importar
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.time.temporal.ChronoUnit;
public class Tarea implements Serializable { // Implementar

    // ID de serialización recomendado para evitar errores de versión
    private static final long serialVersionUID = 1L;
    private static int contadorId = 1;
    private final int id;
    private String descripcion;
    private Prioridad prioridad;
    private LocalDate fechaLimite;
    private boolean estaCompleta;
    private String notas;
    private MainFrame parentFrame;
    private List<String> etiquetas;

   public Tarea(String descripcion, Prioridad prioridad, LocalDate fechaLimite, String notas) {
    this.id = contadorId++; // Se inicializa aquí
    this.descripcion = descripcion;
    this.prioridad = prioridad;
    this.fechaLimite = fechaLimite;
    this.estaCompleta = false;
    this.notas = notas; // <-- ¡Se asigna el valor recibido!
    this.etiquetas = new ArrayList<>();
}

    // --- Getters requeridos por la Vista y el Controlador ---
    public String getNotas() {
    return notas;
}
    

public void setNotas(String notas) {
    this.notas = notas;
}
    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
}
    public Prioridad getPrioridad() { return prioridad; }
    public void setPrioridad(Modelo.Prioridad prioridad) {
    this.prioridad = prioridad;
}
    public LocalDate getFechaLimite() { 
        return fechaLimite; 
    }
    public void setFechaLimite(java.time.LocalDate fechaLimite) {
    this.fechaLimite = fechaLimite;
}
    public static int getContadorId() { 
    return contadorId; // Asegura que contadorId es estático y privado
} 

// Setter estático para reestablecer el ID (CRÍTICO)
public static void setContadorId(int nuevoContador) { 
    contadorId = nuevoContador; 
}
 
    // Getter para verificar el estado de la tarea (usado por GestorTareas)
 public boolean isCompletada() { 
    return estaCompleta;
}

// *** ¡Añade este Setter para que GestorTareas pueda marcar como false! ***
public void setCompletada(boolean estaCompleta) {
    this.estaCompleta = estaCompleta;
}

// Metodo original se puede mantener, pero es redundante:
public void marcarComoCompleta() {
    this.estaCompleta = true;
}
public List<String> getEtiquetas() {
        // Aseguramos que nunca devolvamos null (aunque debería estar inicializado en el constructor)
        if (etiquetas == null) {
            etiquetas = new ArrayList<>();
        }
        return etiquetas;
    }
    
    // Método para añadir etiquetas si es necesario
    public void añadirEtiqueta(String etiqueta) {
        if (etiquetas == null) {
            etiquetas = new ArrayList<>();
        }
        if (etiqueta != null && !etiqueta.trim().isEmpty() && !etiquetas.contains(etiqueta.trim())) {
            etiquetas.add(etiqueta.trim());
        }
    }
public void setEtiquetas(List<String> etiquetas) {
    this.etiquetas = etiquetas;
}

// Método auxiliar para verificación
public boolean contieneEtiqueta(String tag) {
    return this.etiquetas.contains(tag);
}


// Para evitar errores, puedes implementar los métodos de estado aquí:
public boolean isVencida() {
    // Si no hay fecha límite, no está vencida.
    if (fechaLimite == null) return false;
    return fechaLimite.isBefore(java.time.LocalDate.now());
}

public boolean isPorVencer() {
    // Si no hay fecha límite, no está por vencer.
    if (fechaLimite == null) return false;
    java.time.LocalDate hoy = java.time.LocalDate.now();
    return fechaLimite.isEqual(hoy) || fechaLimite.isEqual(hoy.plusDays(1));
}
}

