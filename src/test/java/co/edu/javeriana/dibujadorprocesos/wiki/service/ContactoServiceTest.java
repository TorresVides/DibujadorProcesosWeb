package co.edu.javeriana.dibujadorprocesos.wiki.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.javeriana.dibujadorprocesos.wiki.contacto.ContactoMensaje;
import co.edu.javeriana.dibujadorprocesos.wiki.contacto.ContactoMensajeRepository;
import co.edu.javeriana.dibujadorprocesos.wiki.form.ContactoForm;

/**
 * Prueba unitaria del servicio: comprueba la traducción de formulario a entidad sin
 * levantar el contexto de Spring ni tocar la base de datos.
 */
@ExtendWith(MockitoExtension.class)
class ContactoServiceTest {

	@Mock
	private ContactoMensajeRepository repositorio;

	@InjectMocks
	private ContactoService contactoService;

	@Captor
	private ArgumentCaptor<ContactoMensaje> capturado;

	@Test
	void elFormularioSeConvierteEnEntidadAntesDeGuardarse() {
		ContactoForm formulario = new ContactoForm();
		formulario.setNombreCompleto("Ana Maria Torres");
		formulario.setCorreo("ana.torres@javeriana.edu.co");
		formulario.setTelefono("3001234567");
		formulario.setAsunto("soporte");
		formulario.setMensaje("Quisiera saber como se documenta un proceso nuevo.");

		LocalDateTime antes = LocalDateTime.now();
		given(repositorio.save(any(ContactoMensaje.class))).willAnswer(llamada -> llamada.getArgument(0));

		contactoService.registrar(formulario);

		verify(repositorio).save(capturado.capture());
		ContactoMensaje guardado = capturado.getValue();

		assertThat(guardado.getNombreCompleto()).isEqualTo("Ana Maria Torres");
		assertThat(guardado.getCorreo()).isEqualTo("ana.torres@javeriana.edu.co");
		assertThat(guardado.getTelefono()).isEqualTo("3001234567");
		assertThat(guardado.getAsunto()).isEqualTo("soporte");
		assertThat(guardado.getMensaje()).isEqualTo("Quisiera saber como se documenta un proceso nuevo.");
		// La marca de tiempo la pone el servidor, no llega del formulario.
		assertThat(guardado.getFechaCreacion()).isNotNull().isAfterOrEqualTo(antes);
	}

	@Test
	void elCatalogoDeAsuntosNoEstaVacioYNoTraeOpcionVacia() {
		assertThat(contactoService.asuntos()).isNotEmpty();
		assertThat(contactoService.asuntos())
				.noneMatch(opcion -> opcion.valor() == null || opcion.valor().isBlank());
	}

}
