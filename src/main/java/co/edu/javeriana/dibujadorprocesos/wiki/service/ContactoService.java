package co.edu.javeriana.dibujadorprocesos.wiki.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.javeriana.dibujadorprocesos.wiki.contacto.ContactoMensaje;
import co.edu.javeriana.dibujadorprocesos.wiki.contacto.ContactoMensajeRepository;
import co.edu.javeriana.dibujadorprocesos.wiki.form.ContactoForm;
import co.edu.javeriana.dibujadorprocesos.wiki.model.OpcionAsunto;

/**
 * Motivos de contacto disponibles y registro de los mensajes recibidos.
 *
 * <p>Es la única pieza del proyecto que escribe en PostgreSQL. El controlador no conoce
 * ni la entidad ni el repositorio: le entrega el formulario ya validado y aquí se
 * traduce a {@link ContactoMensaje}.
 */
@Service
public class ContactoService {

	private static final List<OpcionAsunto> ASUNTOS = List.of(
			new OpcionAsunto("soporte", "Soporte técnico"),
			new OpcionAsunto("funcionalidad", "Sugerencia de una funcionalidad"),
			new OpcionAsunto("error", "Reporte de un error"),
			new OpcionAsunto("documentacion", "Consulta sobre la documentación"),
			new OpcionAsunto("academico", "Consulta académica"),
			new OpcionAsunto("otro", "Otro"));

	private final ContactoMensajeRepository repositorio;

	public ContactoService(ContactoMensajeRepository repositorio) {
		this.repositorio = repositorio;
	}

	/**
	 * Catálogo del desplegable «Asunto». La vista lo recorre con {@code th:each}, de modo
	 * que las opciones se declaran una sola vez y aquí.
	 */
	public List<OpcionAsunto> asuntos() {
		return ASUNTOS;
	}

	/**
	 * Convierte el formulario en entidad y lo persiste. La marca de tiempo la pone el
	 * servidor: no es un dato que deba llegar del navegador.
	 */
	@Transactional
	public ContactoMensaje registrar(ContactoForm formulario) {
		ContactoMensaje mensaje = new ContactoMensaje(
				formulario.getNombreCompleto(),
				formulario.getCorreo(),
				formulario.getTelefono(),
				formulario.getAsunto(),
				formulario.getMensaje(),
				LocalDateTime.now());
		return repositorio.save(mensaje);
	}

}
