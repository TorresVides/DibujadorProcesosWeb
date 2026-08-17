package co.edu.javeriana.dibujadorprocesos.wiki.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Entrega académica del proyecto, según el calendario oficial de la asignatura.
 *
 * <p>Nombre, fecha y peso son datos oficiales. No se registra estado de avance: la
 * asignatura no define uno y no procede inventarlo.
 *
 * @param codigo      identificador corto de la entrega
 * @param nombre      título oficial de la entrega
 * @param fecha       fecha de entrega
 * @param peso        porcentaje que aporta a la nota
 * @param descripcion en qué consiste
 * @param alcance     trabajo que incluye
 */
public record Entrega(
		String codigo,
		String nombre,
		LocalDate fecha,
		int peso,
		String descripcion,
		List<String> alcance) {
}
