package co.edu.javeriana.dibujadorprocesos.wiki.model;

/**
 * Opción del desplegable «Asunto» del formulario de contacto.
 *
 * <p>El catálogo lo sirve el servidor y la vista lo recorre con {@code th:each}: el
 * JavaScript de validación solo comprueba que se haya elegido una opción, de modo que
 * la lista no se duplica en el navegador.
 *
 * @param valor    valor enviado en el formulario
 * @param etiqueta texto visible en el desplegable
 */
public record OpcionAsunto(
		String valor,
		String etiqueta) {
}
