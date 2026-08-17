package co.edu.javeriana.dibujadorprocesos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Comprueba que el contexto de Spring se construye correctamente.
 *
 * <p>La prueba es hermética: las propiedades de abajo evitan que Hikari e Hibernate
 * abran una conexión, de modo que no hace falta un PostgreSQL en ejecución. Por lo
 * mismo, no verifica conectividad real con la base de datos.
 *
 * <p>{@code spring.sql.init.mode=never} desactiva aquí la ejecución de {@code schema.sql}.
 * La aplicación lo tiene en {@code always} para crear la tabla de los mensajes de
 * contacto, pero ejecutar el script exigiría una conexión real y rompería el aislamiento
 * de esta prueba.
 */
@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:postgresql://localhost:5433/dibujador_procesos",
		"spring.datasource.username=dibujador_app",
		"spring.datasource.password=",
		"spring.datasource.hikari.initialization-fail-timeout=-1",
		"spring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=false",
		"spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
		"spring.sql.init.mode=never"
})
class DibujadorProcesosApplicationTests {

	@Test
	void contextLoads() {
	}

}
