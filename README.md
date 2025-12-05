 ⚙️ Gestor de Tareas Personal (Java Swing)

Este proyecto es una aplicación de escritorio diseñada para la gestión personal de tareas, utilizando Java Swing para la interfaz gráfica de usuario (GUI) y el tema moderno FlatLaf.

La aplicación permite la clasificación de tareas por prioridad (ALTA, MEDIA, BAJA), establecer fechas límite, y aplicar filtros avanzados (por fecha límite, prioridad o texto de búsqueda).

 🔗 Repositorio del Proyecto

| Elemento | Enlace |
| :--- | :--- |
| **Repositorio Git** | https://github.com/elisnerymoreno-crypto/GestorTareasSwing |
| **Autor** | [Elisnery Moreno] |

---

🛠️ Requisitos de Instalación y Configuración

Para compilar y ejecutar el proyecto, debe contar con las siguientes dependencias:

1.  Java Development Kit (JDK):Versión 8 (1.8)
    Importante: La compatibilidad del proyecto está establecida para JDK 1.8. Usar versiones más recientes (como JDK 21) puede causar problemas con las librerías antiguas.
2.  Entorno de Desarrollo Integrado (IDE):NetBeans IDE .

📝 Instrucciones Paso a Paso para NetBeans

1.  Clonar el Proyecto: Descargue el repositorio usando Git o descargue el ZIP directamente desde la página principal de GitHub.
2.  Abrir el Proyecto Inicie NetBeans y abra la carpeta del proyecto (`GestorTareasSwing`).
3.  Verificar Plataforma: Clic derecho sobre el proyecto → Propiedades → Libraries (Librerías).
4.  Asegúrese de que la Java Platform esté configurada en JDK 1.8 (Default).

📦 Dependencias (Librerías JAR)

El proyecto utiliza librerías externas que deben estar vinculadas. Estas se encuentran en la carpeta `/lib` del repositorio:

* `flatlaf-core-3.4.2.jar`
* `flatlaf-extras-3.4.2.jar`
* `calendar-1.4.jar`
* `jxl-2.6.jar`
  

🚀 Generación de la Aplicación (App)

Para crear el archivo ejecutable (`.jar`) de la aplicación:

1.  En NetBeans, vaya al menú Run (Ejecutar) o Buil (Construir).
2.  Seleccione la opción "Clean and Build Project" (Limpiar y Construir Proyecto).
3.  El archivo ejecutable (`GestorTareasSwing.jar`) se generará en la carpeta `dist` del proyecto (ya subida al repositorio).

Ejecución Directa

Puede ejecutar la aplicación terminada desde la terminal con el siguiente comando:

```bash
java -jar dist/GestorTareasSwing.jar
