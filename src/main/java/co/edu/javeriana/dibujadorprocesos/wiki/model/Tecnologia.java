package co.edu.javeriana.dibujadorprocesos.wiki.model;

/**
 * Tecnología que forma parte del stack del proyecto.
 *
 * @param nombre    nombre de la tecnología
 * @param version   versión en uso; cadena vacía cuando no aplica fijar una (Git, Docker),
 *                  caso que la vista resuelve con {@code th:if}
 * @param categoria agrupación funcional; determina el bloque en el que se muestra
 * @param proposito para qué se usa dentro del proyecto
 */
public record Tecnologia(
		String nombre,
		String version,
		String categoria,
		String proposito) {
}
