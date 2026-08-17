package co.edu.javeriana.dibujadorprocesos.wiki.contacto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Mensaje recibido a través del formulario Contáctenos.
 *
 * <p>Única entidad JPA del proyecto por ahora. El modelo de dominio —empresas, usuarios
 * y procesos— se define en una entrega posterior y no se anticipa aquí.
 *
 * <p>La tabla la crea {@code src/main/resources/schema.sql}, no Hibernate:
 * {@code spring.jpa.hibernate.ddl-auto} sigue en {@code none} para conservar el control
 * explícito del esquema. La estrategia {@code IDENTITY} se corresponde con la columna
 * {@code BIGSERIAL} declarada en ese script.
 */
@Entity
@Table(name = "contacto_mensaje")
public class ContactoMensaje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre_completo", nullable = false, length = 120)
	private String nombreCompleto;

	@Column(nullable = false, length = 180)
	private String correo;

	@Column(nullable = false, length = 15)
	private String telefono;

	@Column(nullable = false, length = 60)
	private String asunto;

	@Column(nullable = false, length = 400)
	private String mensaje;

	@Column(name = "fecha_creacion", nullable = false)
	private LocalDateTime fechaCreacion;

	/** Requerido por JPA. */
	protected ContactoMensaje() {
	}

	public ContactoMensaje(String nombreCompleto, String correo, String telefono,
			String asunto, String mensaje, LocalDateTime fechaCreacion) {
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.telefono = telefono;
		this.asunto = asunto;
		this.mensaje = mensaje;
		this.fechaCreacion = fechaCreacion;
	}

	public Long getId() {
		return id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getCorreo() {
		return correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

}
