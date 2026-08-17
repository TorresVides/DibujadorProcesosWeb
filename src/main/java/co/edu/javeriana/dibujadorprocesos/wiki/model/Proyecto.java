package co.edu.javeriana.dibujadorprocesos.wiki.model;

/**
 * Datos globales del proyecto, presentes en todas las páginas de la Wiki.
 *
 * @param nombre      nombre del sistema documentado
 * @param resumen     descripción en una frase
 * @param institucion institución en la que se desarrolla
 * @param asignatura  asignatura en cuyo marco se desarrolla
 * @param anio        año del curso
 * @param repositorio URL del repositorio en GitHub
 */
public record Proyecto(
		String nombre,
		String resumen,
		String institucion,
		String asignatura,
		String anio,
		String repositorio) {
}
