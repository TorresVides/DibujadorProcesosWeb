package co.edu.javeriana.dibujadorprocesos.wiki.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.javeriana.dibujadorprocesos.wiki.model.OpcionAsunto;
import co.edu.javeriana.dibujadorprocesos.wiki.service.ContactoService;
import co.edu.javeriana.dibujadorprocesos.wiki.service.WikiService;

/**
 * Pruebas de la capa web del formulario Contáctenos.
 *
 * <p>{@link ContactoService} se sustituye por un doble: aquí se comprueba el
 * comportamiento del controlador —qué vista devuelve, si llama o no al servicio, si
 * redirige— sin tocar PostgreSQL. {@link WikiService} sí es el real porque lo necesita
 * {@link NavegacionAdvice} para pintar el menú.
 */
@WebMvcTest(ContactoController.class)
@Import(WikiService.class)
class ContactoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ContactoService contactoService;

	@BeforeEach
	void prepararAsuntos() {
		given(contactoService.asuntos()).willReturn(List.of(
				new OpcionAsunto("soporte", "Soporte tecnico"),
				new OpcionAsunto("error", "Reporte de un error")));
	}

	@Test
	void elGetDevuelveElFormularioVacio() throws Exception {
		mockMvc.perform(get("/contacto"))
				.andExpect(status().isOk())
				.andExpect(view().name("wiki/contacto"))
				.andExpect(model().attributeExists("contactoForm", "asuntos"))
				.andExpect(content().string(containsString("id=\"formContacto\"")))
				.andExpect(content().string(containsString("novalidate")))
				.andExpect(content().string(containsString("id=\"nombreCompleto\"")))
				.andExpect(content().string(containsString("id=\"correo\"")))
				.andExpect(content().string(containsString("id=\"telefono\"")))
				.andExpect(content().string(containsString("id=\"asunto\"")))
				.andExpect(content().string(containsString("id=\"mensaje\"")));
	}

	/**
	 * Las opciones se generan desde el modelo, no están escritas en la plantilla: el
	 * doble devuelve dos y en el HTML aparecen esas dos, más la opción inicial vacía.
	 */
	@Test
	void lasOpcionesDeAsuntoLleganDesdeElModelo() throws Exception {
		mockMvc.perform(get("/contacto"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("asuntos", hasSize(2)))
				.andExpect(content().string(containsString("value=\"soporte\"")))
				.andExpect(content().string(containsString("Soporte tecnico")))
				.andExpect(content().string(containsString("value=\"error\"")))
				.andExpect(content().string(containsString("Reporte de un error")))
				.andExpect(content().string(containsString("<option value=\"\"")));
	}

	@Test
	void elPostValidoGuardaYRedirige() throws Exception {
		mockMvc.perform(post("/contacto")
						.param("nombreCompleto", "Ana Maria Torres")
						.param("correo", "ana.torres@javeriana.edu.co")
						.param("telefono", "3001234567")
						.param("asunto", "soporte")
						.param("mensaje", "Quisiera saber como se documenta un proceso nuevo."))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/contacto"))
				.andExpect(flash().attributeExists("mensajeExito"));

		verify(contactoService, times(1)).registrar(any());
	}

	@ParameterizedTest(name = "{0}")
	@CsvSource(delimiter = '|', value = {
			"nombre vacio            |                  | a@b.com  | 3001234567       | soporte | Mensaje suficientemente largo para pasar.",
			"nombre solo espacios    |    '   '         | a@b.com  | 3001234567       | soporte | Mensaje suficientemente largo para pasar.",
			"nombre de dos letras    | Ab               | a@b.com  | 3001234567       | soporte | Mensaje suficientemente largo para pasar.",
			"correo sin arroba       | Ana Maria Torres | anab.com | 3001234567       | soporte | Mensaje suficientemente largo para pasar.",
			"correo sin punto final  | Ana Maria Torres | ana@bcom | 3001234567       | soporte | Mensaje suficientemente largo para pasar.",
			"telefono con letras     | Ana Maria Torres | a@b.com  | 30012ABC45       | soporte | Mensaje suficientemente largo para pasar.",
			"telefono de 6 digitos   | Ana Maria Torres | a@b.com  | 300123           | soporte | Mensaje suficientemente largo para pasar.",
			"telefono de 16 digitos  | Ana Maria Torres | a@b.com  | 3001234567890123 | soporte | Mensaje suficientemente largo para pasar.",
			"asunto sin seleccionar  | Ana Maria Torres | a@b.com  | 3001234567       |         | Mensaje suficientemente largo para pasar.",
			"mensaje de 19 caracteres| Ana Maria Torres | a@b.com  | 3001234567       | soporte | 1234567890123456789"
	})
	void elPostInvalidoNoGuardaYRepintaElFormulario(String caso, String nombre, String correo,
			String telefono, String asunto, String mensaje) throws Exception {

		mockMvc.perform(post("/contacto")
						.param("nombreCompleto", nombre == null ? "" : nombre)
						.param("correo", correo == null ? "" : correo)
						.param("telefono", telefono == null ? "" : telefono)
						.param("asunto", asunto == null ? "" : asunto)
						.param("mensaje", mensaje == null ? "" : mensaje))
				.andExpect(status().isOk())
				.andExpect(view().name("wiki/contacto"))
				.andExpect(model().hasErrors());

		verify(contactoService, never()).registrar(any());
	}

	/** Tras un error el usuario no debe perder lo que ya había escrito. */
	@Test
	void elPostInvalidoConservaLosValoresIngresadosYMuestraElError() throws Exception {
		mockMvc.perform(post("/contacto")
						.param("nombreCompleto", "Ana Maria Torres")
						.param("correo", "ana.torres@javeriana.edu.co")
						.param("telefono", "300")
						.param("asunto", "soporte")
						.param("mensaje", "Quisiera saber como se documenta un proceso nuevo."))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("contactoForm", "telefono"))
				.andExpect(content().string(containsString("Ana Maria Torres")))
				.andExpect(content().string(containsString("ana.torres@javeriana.edu.co")))
				// th:errors deja el mensaje del servidor en el mismo hueco que usa el JavaScript.
				.andExpect(content().string(containsString("id=\"error-telefono\"")))
				.andExpect(content().string(containsString("entre 7 y 15")));
	}

	/**
	 * El envío de correo no está implementado. Ninguna pantalla debe prometer una
	 * respuesta: es la clase de texto que reaparece con facilidad al reescribir copy.
	 */
	@Test
	void ningunTextoPrometeUnaRespuestaPosterior() throws Exception {
		mockMvc.perform(get("/contacto"))
				.andExpect(status().isOk())
				.andExpect(content().string(not(containsString("responderemos"))))
				.andExpect(content().string(not(containsString("te contactaremos"))))
				.andExpect(content().string(not(containsString("recibirás"))));
	}

}
