package co.edu.javeriana.dibujadorprocesos.wiki.model;

/**
 * Capa de la arquitectura de la aplicación. El orden de la lista que las contiene
 * es el orden del flujo de una petición.
 *
 * @param nombre         nombre de la capa
 * @param responsabilidad de qué se ocupa
 */
public record CapaArquitectura(
		String nombre,
		String responsabilidad) {
}
