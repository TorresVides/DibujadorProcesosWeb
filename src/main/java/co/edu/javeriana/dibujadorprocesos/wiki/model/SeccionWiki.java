package co.edu.javeriana.dibujadorprocesos.wiki.model;

/**
 * Sección de la Wiki. Alimenta a la vez el menú de navegación, el mapa del sitio
 * del pie de página y las tarjetas de acceso de la página de inicio.
 *
 * @param clave       identificador estable; se compara con {@code paginaActiva} para
 *                    resaltar el enlace en curso
 * @param titulo      etiqueta visible
 * @param ruta        ruta HTTP de la sección
 * @param descripcion resumen de una línea sobre su contenido
 */
public record SeccionWiki(
		String clave,
		String titulo,
		String ruta,
		String descripcion) {
}
