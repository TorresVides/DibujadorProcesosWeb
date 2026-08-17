package co.edu.javeriana.dibujadorprocesos.wiki.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos del formulario Contáctenos.
 *
 * <p>Las restricciones replican una a una las reglas que valida
 * {@code static/js/contacto.js}. El navegador es quien da la respuesta inmediata, pero
 * la validación del servidor es la que decide: sin ella bastaría con desactivar el
 * JavaScript para grabar cualquier cosa.
 *
 * <p>Es una clase con setters, no un {@code record}, porque el data binding de Spring
 * MVC y {@code th:field} necesitan escribir sobre las propiedades.
 *
 * <p>El controlador recorta los espacios de todas las cadenas antes de validar, de modo
 * que un valor formado solo por espacios llega aquí como {@code null} y lo rechaza
 * {@code @NotBlank}.
 */
public class ContactoForm {

	@NotBlank(message = "El nombre completo es obligatorio.")
	@Size(min = 3, max = 120, message = "El nombre completo debe tener entre 3 y 120 caracteres.")
	private String nombreCompleto;

	@NotBlank(message = "El correo electrónico es obligatorio.")
	@Size(max = 180, message = "El correo electrónico no puede superar los 180 caracteres.")
	@Email(message = "El correo electrónico no tiene un formato válido.")
	@Pattern(regexp = "^[^\\s@]+@[^\\s@.]+(\\.[^\\s@.]+)+$",
			message = "El correo debe contener @ y al menos un punto después de la @.")
	private String correo;

	@NotBlank(message = "El teléfono es obligatorio.")
	@Pattern(regexp = "^\\d{7,15}$",
			message = "El teléfono debe contener solo números, entre 7 y 15 dígitos.")
	private String telefono;

	@NotBlank(message = "Selecciona un motivo de contacto.")
	@Size(max = 60, message = "El asunto no es válido.")
	private String asunto;

	@NotBlank(message = "El mensaje es obligatorio.")
	@Size(min = 20, max = 400, message = "El mensaje debe tener entre 20 y 400 caracteres.")
	private String mensaje;

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
