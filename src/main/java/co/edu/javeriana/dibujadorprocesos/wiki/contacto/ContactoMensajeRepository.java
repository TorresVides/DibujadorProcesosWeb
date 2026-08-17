package co.edu.javeriana.dibujadorprocesos.wiki.contacto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acceso a los mensajes del formulario Contáctenos.
 *
 * <p>Spring Data genera la implementación; de momento basta con el {@code save} que
 * hereda de {@link JpaRepository}, así que no se declaran métodos propios.
 */
@Repository
public interface ContactoMensajeRepository extends JpaRepository<ContactoMensaje, Long> {
}
