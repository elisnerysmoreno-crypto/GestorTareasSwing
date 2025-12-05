/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import Modelo.Tarea;
import java.io.Serializable; // Importar
import java.io.*;
public class GestorTareas implements Serializable { // Implementar
    
    private static final long serialVersionUID = 1L;
    private  List<Tarea> listaTareas;
   // private List<Tarea> todasLasTareas = new ArrayList<>();
private static final String ARCHIVO_DATOS = "tareas.dat";
    public GestorTareas() {
        this.listaTareas = new ArrayList<>();
    }

    // --- Métodos CRUD y Filtrado ---
    public static GestorTareas cargarTareas() {
    try (
        FileInputStream fis = new FileInputStream(ARCHIVO_DATOS);
        ObjectInputStream ois = new ObjectInputStream(fis)
    ) {
        GestorTareas gestor = (GestorTareas) ois.readObject();
        
        // 🔑 CORRECCIÓN: Si hay tareas, recalcular el ID más alto.
        if (!gestor.listaTareas.isEmpty()) {
            int maxId = gestor.listaTareas.stream()
                                         .mapToInt(Tarea::getId)
                                         .max()
                                         .orElse(0);
            
            // ¡IMPORTANTE! Asume que Tarea.setContadorId(int) ya existe.
            Modelo.Tarea.setContadorId(maxId + 1); 
        } else {
             // Si la lista está vacía, aseguramos que el contador inicie en 1.
             Modelo.Tarea.setContadorId(1);
        }
        
        System.out.println("Tareas cargadas con éxito.");
        return gestor;
        
    } catch (FileNotFoundException e) {
        // Manejo limpio para la primera ejecución
        System.out.println("Archivo de tareas no encontrado. Creando nuevo gestor.");
        Modelo.Tarea.setContadorId(1); // Asegura que el contador inicie en 1
        return new GestorTareas(); 
        
    } catch (Exception e) {
        System.err.println("Error grave al cargar tareas: " + e.getMessage());
        e.printStackTrace();
        Modelo.Tarea.setContadorId(1); // En caso de error, resetear ID
        return new GestorTareas(); 
    }
}

    /**
     * Guarda la lista actual de tareas en el archivo.
     */
    public void guardarTareas() {
     
    try (
        FileOutputStream fos = new FileOutputStream(ARCHIVO_DATOS);
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
        oos.writeObject(this); // Guardar todo el objeto GestorTareas
        System.out.println("Tareas guardadas exitosamente.");
    } catch (Exception e) {
        System.err.println("Error al guardar tareas: " + e.getMessage());
        e.printStackTrace();
    }
}
    public void añadirTarea(Tarea tarea) {
        this.listaTareas.add(tarea);
    }
    
    public List<Tarea> getTareasPendientes() {
        return listaTareas.stream()
                          .filter(tarea -> !tarea.isCompletada()) // Usamos el Getter
                          .collect(Collectors.toList());
    }
    
 public void actualizarTarea(Tarea tareaModificada) {
    // Buscamos la tarea por su ID
    for (int i = 0; i < listaTareas.size(); i++) {
        
        // CORRECCIÓN: Usar 'listaTareas' en lugar de 'tareas'
        if (listaTareas.get(i).getId() == tareaModificada.getId()) {
            
            // 2. Reemplazar la tarea antigua por la modificada
            // Aunque el objeto 'tareaModificada' ya fue alterado en el diálogo,
            // esta línea asegura que si el gestor fuera a guardar una copia, use el objeto correcto.
            listaTareas.set(i, tareaModificada); 
            
            // Guardar los cambios inmediatamente después de la modificación
            guardarTareas(); 
            break;
        }
    }
 }
 
public List<Tarea> filtrarPorEtiqueta(String etiqueta) {
    // Si la etiqueta es nula o vacía, devolvemos la lista completa.
    if (etiqueta == null || etiqueta.trim().isEmpty() || etiqueta.equalsIgnoreCase("Todas")) {
        // Se recomienda devolver la lista completa (no filtrada por etiqueta)
        return getTodasLasTareas(); 
    }
    
    // Obtenemos una lista filtrada usando Stream API para mayor eficiencia
    final String tagLower = etiqueta.trim().toLowerCase();
    
    return listaTareas.stream()
            // Filtra solo las tareas cuya lista de etiquetas contiene la etiqueta dada (en minúsculas)
            .filter(tarea -> tarea.getEtiquetas().stream().anyMatch(t -> t.toLowerCase().equals(tagLower)))
            .collect(Collectors.toList());
}
    // --- Métodos de Ordenamiento ---

    public List<Tarea> ordenarPorPrioridad() {
        List<Tarea> pendientes = getTareasPendientes();
        
        // Ordena de ALTA a BAJA (reversed)
   pendientes.sort(Comparator.comparing(
        Tarea::getPrioridad, // Ordenar por el enum Prioridad
        Comparator.comparingInt(Prioridad::getValorNumerico) // Usando el valor numérico del enum
    ).reversed()); 
    
    return pendientes;
}

    public List<Tarea> ordenarPorFechaLimite() {
        List<Tarea> pendientes = getTareasPendientes();

        // Ordena por fecha: la más cercana primero. Las tareas sin fecha (null) van al final.
        pendientes.sort(Comparator.comparing(
            Tarea::getFechaLimite, 
            Comparator.nullsLast(Comparator.naturalOrder())
        ));

        return pendientes;
    }
    public List<Tarea> getTodasLasTareas() {
    // Devuelve una copia modificable de la lista interna
    return new ArrayList<>(listaTareas); 
}
    
    public List<Tarea> ordenarLista(List<Tarea> lista, String criterio) {
    
    // IMPORTANTE: Crea una copia de la lista de entrada para no modificar la lista original.
    List<Tarea> listaOrdenable = new java.util.ArrayList<>(lista); 

    if ("Fecha Límite".equals(criterio)) {
        // Ordenamiento por Fecha Límite.
        // Maneja los valores 'null' (sin fecha) colocándolos al final.
        java.util.Collections.sort(listaOrdenable, (t1, t2) -> {
            if (t1.getFechaLimite() == null && t2.getFechaLimite() == null) return 0;
            if (t1.getFechaLimite() == null) return 1;    // t1 va al final
            if (t2.getFechaLimite() == null) return -1;   // t2 va al final
            return t1.getFechaLimite().compareTo(t2.getFechaLimite());
        });
    } else { // Criterio "Prioridad" (o cualquier otro por defecto)
        // Ordenamiento por Prioridad (usa el orden definido en el Enum: ALTA, MEDIA, BAJA).
        java.util.Collections.sort(listaOrdenable, (t1, t2) -> t1.getPrioridad().compareTo(t2.getPrioridad()));
    }
    
    return listaOrdenable;
}
public boolean marcarCompletaPorId(int id, boolean estado) {
    for (Tarea tarea : listaTareas) {
        if (tarea.getId() == id) {
            // Usamos el nuevo Setter de la clase Tarea
            tarea.setCompletada(estado); 
            return true; // Éxito
        }
    }
    return false; // Tarea no encontrada
}
    public boolean eliminarTareaPorId(int id) {
    // Usamos el método removeIf para eliminar el elemento que cumpla la condición
    return listaTareas.removeIf(tarea -> tarea.getId() == id);
}
    // Aquí puedes añadir métodos para eliminar, modificar, etc.
    public List<Tarea> filtrarPorDescripcion(String textoBusqueda) {
    if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
        return getTodasLasTareas(); // Devuelve todas las tareas si la búsqueda está vacía
    }
    
    final String textoLower = textoBusqueda.trim().toLowerCase();
    
    return listaTareas.stream()
                      .filter(tarea -> tarea.getDescripcion().toLowerCase().contains(textoLower))
                      .collect(Collectors.toList());
}
    
    public int getPorcentajeCompletado() {
  int totalTareas = listaTareas.size(); 
    
    if (totalTareas == 0) {
        return 0; // Evita división por cero
    }
    
    // 🔑 CORRECCIÓN: Usamos listaTareas para el stream
    long tareasCompletadas = listaTareas.stream() 
        .filter(Tarea::isCompletada)
        .count();
        
    // Calcula y retorna el porcentaje redondeado
    return (int) Math.round(((double) tareasCompletadas / totalTareas) * 100);
}
    
   public void eliminarTarea(Tarea tarea) {
    if (tarea != null) {
        // !!! CORRECCIÓN: Reemplaza 'todasLasTareas' por el nombre real de tu lista !!!
        if (listaTareas.remove(tarea)) { 
            System.out.println("Tarea eliminada con éxito: " + tarea.getDescripcion());
        } else {
            System.out.println("Advertencia: No se encontró la tarea para eliminar.");
        }
    }
}
   public List<Tarea> filtrarListaPorEtiqueta(List<Tarea> lista, String etiqueta) {
    if (etiqueta == null || etiqueta.trim().isEmpty() || etiqueta.equalsIgnoreCase("Todas")) {
        return lista; // Devolvemos la lista original si no hay filtro de etiqueta válido
    }
    
    // Utilizamos Stream para filtrar solo las tareas que contienen la etiqueta
    return lista.stream()
            .filter(tarea -> tarea.getEtiquetas().contains(etiqueta))
            .collect(Collectors.toList());
}
}