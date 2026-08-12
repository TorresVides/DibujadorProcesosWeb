package co.edu.javeriana.dibujadorprocesos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Verifica que el contexto de Spring se construye correctamente: que la
 * autoconfiguracion, Spring MVC, Thymeleaf, JPA, Validation y Actuator quedan
 * bien cableados entre si.
 *
 * <p>Esta prueba es deliberadamente HERMETICA: no necesita un servidor
 * PostgreSQL en ejecucion. Las propiedades declaradas abajo evitan que Hikari
 * abra el pool y que Hibernate consulte los metadatos JDBC durante el arranque
 * del contexto. Gracias a eso {@code ./mvnw test} funciona en cualquier
 * maquina y en CI sin levantar infraestructura previa.
 *
 * <p><strong>Importante:</strong> por ese mismo motivo esta prueba NO
 * demuestra conectividad real contra PostgreSQL. Esa comprobacion se hace por
 * otras vias: la aplicacion ejecutandose en Docker Compose
 * ({@code /actuator/health} y {@code pg_stat_activity}) y, mas adelante,
 * pruebas de integracion dedicadas.
 *
 * <p>No se declara contrasena alguna: al no abrirse ninguna conexion, basta
 * con un valor vacio y asi no queda ninguna credencial escrita en el
 * repositorio.
 */
@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:postgresql://localhost:5433/dibujador_procesos",
		"spring.datasource.username=dibujador_app",
		"spring.datasource.password=",
		"spring.datasource.hikari.initialization-fail-timeout=-1",
		"spring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=false",
		"spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect"
})
class DibujadorProcesosApplicationTests {

	@Test
	void contextLoads() {
	}

}
