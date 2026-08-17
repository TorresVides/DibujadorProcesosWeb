package co.edu.javeriana.dibujadorprocesos.wiki.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.javeriana.dibujadorprocesos.wiki.service.WikiService;

/**
 * Pruebas de la capa web de la Wiki.
 *
 * <p>{@code @WebMvcTest} levanta solo Spring MVC: no configura el DataSource, así que
 * estas pruebas no necesitan PostgreSQL en ejecución. Se importa el {@link WikiService}
 * real —no un doble— porque no tiene dependencias y así se comprueba que los datos del
 * servicio llegan realmente hasta el HTML renderizado.
 *
 * <p>En Spring Boot 4 la anotación vive en {@code org.springframework.boot.webmvc.test
 * .autoconfigure}, no en el paquete que usaba Spring Boot 3.
 */
@WebMvcTest(WikiController.class)
@Import(WikiService.class)
class WikiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@ParameterizedTest(name = "GET {0} -> {1}")
	@CsvSource({
			"/,             wiki/inicio",
			"/inicio,       wiki/inicio",
			"/alcance,      wiki/alcance",
			"/arquitectura, wiki/arquitectura",
			"/historias,    wiki/historias",
			"/entregas,     wiki/entregas"
	})
	void cadaRutaDevuelveSuVista(String ruta, String vista) throws Exception {
		mockMvc.perform(get(ruta))
				.andExpect(status().isOk())
				.andExpect(view().name(vista))
				.andExpect(model().attributeExists("proyecto", "secciones", "paginaActiva"));
	}

	@Test
	void laNavegacionSeRenderizaConLasSeccionesDelServicio() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Alcance y requisitos")))
				.andExpect(content().string(containsString("Historias de Usuario")))
				.andExpect(content().string(containsString("Entregas del proyecto")))
				.andExpect(content().string(containsString("href=\"/contacto\"")));
	}

	@Test
	void arquitecturaRenderizaLasTecnologiasDelServicio() throws Exception {
		mockMvc.perform(get("/arquitectura"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("capas", "tecnologiasPorCategoria"))
				.andExpect(content().string(containsString("Thymeleaf")))
				.andExpect(content().string(containsString("3.1.5")))
				.andExpect(content().string(containsString("PostgreSQL")));
	}

	/**
	 * El backlog oficial todavía no está publicado. Esta prueba fija esa decisión: la
	 * página no debe volver a mostrar historias redactadas por el equipo.
	 */
	@Test
	void historiasNoPublicaHistoriasSinRespaldoOficial() throws Exception {
		mockMvc.perform(get("/historias"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("areasFuncionales"))
				.andExpect(model().attributeDoesNotExist("historias"))
				.andExpect(content().string(containsString("backlog")))
				.andExpect(content().string(not(containsString("HU-0"))))
				.andExpect(content().string(not(containsString("Como usuario de una empresa"))));
	}

	@Test
	void alcanceSeparaLoIncluidoDeLoExcluido() throws Exception {
		mockMvc.perform(get("/alcance"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Dentro del alcance")))
				.andExpect(content().string(containsString("Fuera del alcance")))
				.andExpect(content().string(containsString("Motor BPM")));
	}

	/** Fechas y pesos oficiales de la asignatura, sin estados de avance inventados. */
	@Test
	void entregasMuestraElCalendarioOficial() throws Exception {
		mockMvc.perform(get("/entregas"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("entregas"))
				.andExpect(content().string(containsString("14/09/2026")))
				.andExpect(content().string(containsString("21/10/2026")))
				.andExpect(content().string(containsString("25/11/2026")))
				.andExpect(content().string(containsString("15% de la nota")))
				.andExpect(content().string(not(containsString("Completada"))));
	}

}
