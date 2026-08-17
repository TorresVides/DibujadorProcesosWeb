package co.edu.javeriana.dibujadorprocesos.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import co.edu.javeriana.dibujadorprocesos.wiki.service.WikiService;

/**
 * Páginas de contenido de la Wiki.
 *
 * <p>Cada método prepara los datos de su página y devuelve el nombre de la vista; no
 * construye HTML ni contiene lógica de negocio. Los datos comunes a todas las páginas
 * (proyecto y secciones) los aporta {@link NavegacionAdvice}.
 *
 * <p>El atributo {@code paginaActiva} le permite al fragment de navegación resaltar el
 * enlace en curso. Se pasa por el modelo porque Thymeleaf 3.1 retiró los objetos de
 * expresión {@code #request} y {@code #session}, con los que antes se leía la URI.
 */
@Controller
public class WikiController {

	private final WikiService wiki;

	public WikiController(WikiService wiki) {
		this.wiki = wiki;
	}

	@GetMapping({ "/", "/inicio" })
	public String inicio(Model model) {
		model.addAttribute("paginaActiva", "inicio");
		model.addAttribute("tituloPagina", "Inicio");
		return "wiki/inicio";
	}

	@GetMapping("/alcance")
	public String alcance(Model model) {
		model.addAttribute("paginaActiva", "alcance");
		model.addAttribute("tituloPagina", "Alcance y requisitos");
		model.addAttribute("elementosAlcance", wiki.elementosAlcance());
		model.addAttribute("requisitosTecnicos", wiki.requisitosTecnicos());
		return "wiki/alcance";
	}

	@GetMapping("/arquitectura")
	public String arquitectura(Model model) {
		model.addAttribute("paginaActiva", "arquitectura");
		model.addAttribute("tituloPagina", "Arquitectura y tecnologías");
		model.addAttribute("capas", wiki.capas());
		model.addAttribute("tecnologiasPorCategoria", wiki.tecnologiasPorCategoria());
		return "wiki/arquitectura";
	}

	@GetMapping("/historias")
	public String historias(Model model) {
		model.addAttribute("paginaActiva", "historias");
		model.addAttribute("tituloPagina", "Historias de Usuario");
		model.addAttribute("areasFuncionales", wiki.areasFuncionalesConfirmadas());
		return "wiki/historias";
	}

	@GetMapping("/entregas")
	public String entregas(Model model) {
		model.addAttribute("paginaActiva", "entregas");
		model.addAttribute("tituloPagina", "Entregas del proyecto");
		model.addAttribute("entregas", wiki.entregas());
		return "wiki/entregas";
	}

}
