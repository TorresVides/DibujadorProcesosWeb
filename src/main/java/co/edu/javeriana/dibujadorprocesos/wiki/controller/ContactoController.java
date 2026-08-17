package co.edu.javeriana.dibujadorprocesos.wiki.controller;

import java.util.List;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.edu.javeriana.dibujadorprocesos.wiki.form.ContactoForm;
import co.edu.javeriana.dibujadorprocesos.wiki.model.OpcionAsunto;
import co.edu.javeriana.dibujadorprocesos.wiki.service.ContactoService;

import jakarta.validation.Valid;

/**
 * Formulario Contáctenos.
 *
 * <p>El envío correcto sigue el patrón Post/Redirect/Get: tras guardar se redirige a la
 * misma página, de modo que recargar el navegador no vuelve a insertar el mensaje. El
 * aviso de éxito viaja como atributo flash.
 *
 * <p>El JavaScript valida antes de enviar, pero aquí se vuelve a validar: la validación
 * del navegador es comodidad para quien escribe, no una garantía para el servidor.
 */
@Controller
@RequestMapping("/contacto")
public class ContactoController {

	private static final String VISTA = "wiki/contacto";

	private final ContactoService contactos;

	public ContactoController(ContactoService contactos) {
		this.contactos = contactos;
	}

	/**
	 * Recorta los espacios de todas las cadenas y convierte en {@code null} las que queden
	 * vacías. Así un nombre formado solo por espacios lo rechaza {@code @NotBlank}, igual
	 * que hace {@code trim()} en el JavaScript.
	 */
	@InitBinder
	void recortarEspacios(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/** Disponible tanto en el GET como al repintar el formulario con errores. */
	@ModelAttribute("asuntos")
	public List<OpcionAsunto> asuntos() {
		return contactos.asuntos();
	}

	@GetMapping
	public String formulario(Model model) {
		prepararVista(model);
		if (!model.containsAttribute("contactoForm")) {
			model.addAttribute("contactoForm", new ContactoForm());
		}
		return VISTA;
	}

	@PostMapping
	public String enviar(@Valid @ModelAttribute("contactoForm") ContactoForm contactoForm,
			BindingResult errores, Model model, RedirectAttributes flash) {

		if (errores.hasErrors()) {
			prepararVista(model);
			return VISTA;
		}

		contactos.registrar(contactoForm);
		// Sin promesas de respuesta: el envío de correo no está implementado.
		flash.addFlashAttribute("mensajeExito",
				"Gracias, " + contactoForm.getNombreCompleto()
						+ ". Hemos recibido correctamente tu mensaje y ha quedado registrado.");
		return "redirect:/contacto";
	}

	private void prepararVista(Model model) {
		model.addAttribute("paginaActiva", "contacto");
		model.addAttribute("tituloPagina", "Contáctenos");
	}

}
