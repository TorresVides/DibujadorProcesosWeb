package co.edu.javeriana.dibujadorprocesos.wiki.model;

/**
 * Elemento del alcance del proyecto.
 *
 * <p>Una sola colección recoge lo que entra y lo que queda fuera: dejar constancia de
 * lo excluido es tan importante como enumerar lo incluido, porque el sistema se
 * confunde con facilidad con un motor BPM de ejecución.
 *
 * @param titulo      nombre del elemento
 * @param descripcion qué significa en la práctica
 * @param incluido    {@code true} si está dentro del alcance
 */
public record ElementoAlcance(
		String titulo,
		String descripcion,
		boolean incluido) {
}
