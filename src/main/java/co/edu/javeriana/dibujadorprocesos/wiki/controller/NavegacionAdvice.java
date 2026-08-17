package co.edu.javeriana.dibujadorprocesos.wiki.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import co.edu.javeriana.dibujadorprocesos.wiki.model.Proyecto;
import co.edu.javeriana.dibujadorprocesos.wiki.model.SeccionWiki;
import co.edu.javeriana.dibujadorprocesos.wiki.service.WikiService;

/**
 * Datos presentes en todas las páginas de la Wiki: la ficha del proyecto y las
 * secciones que forman el menú.
 *
 * <p>Al declararlos aquí, el menú y el pie de página se alimentan solos y ningún
 * método de {@link WikiController} tiene que repetir esas dos líneas.
 *
 * <p>El {@code @ControllerAdvice} se limita al paquete de controladores de la Wiki
 * para no inyectar estos atributos en el resto de la aplicación.
 */
@ControllerAdvice(basePackageClasses = WikiController.class)
public class NavegacionAdvice {

	private final WikiService wiki;

	public NavegacionAdvice(WikiService wiki) {
		this.wiki = wiki;
	}

	@ModelAttribute("proyecto")
	public Proyecto proyecto() {
		return wiki.proyecto();
	}

	@ModelAttribute("secciones")
	public List<SeccionWiki> secciones() {
		return wiki.secciones();
	}

}
