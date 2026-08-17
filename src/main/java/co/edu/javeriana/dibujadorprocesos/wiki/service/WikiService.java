package co.edu.javeriana.dibujadorprocesos.wiki.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.javeriana.dibujadorprocesos.wiki.model.CapaArquitectura;
import co.edu.javeriana.dibujadorprocesos.wiki.model.ElementoAlcance;
import co.edu.javeriana.dibujadorprocesos.wiki.model.Entrega;
import co.edu.javeriana.dibujadorprocesos.wiki.model.Proyecto;
import co.edu.javeriana.dibujadorprocesos.wiki.model.SeccionWiki;
import co.edu.javeriana.dibujadorprocesos.wiki.model.Tecnologia;

/**
 * Fuente única del contenido de la Wiki.
 *
 * <p>Los datos viven en memoria a propósito. La Wiki documenta el proyecto pero no
 * forma parte de su dominio, de modo que no justifica entidades JPA, tablas propias
 * ni acceso a PostgreSQL. Las colecciones se construyen con {@code List.of}, por lo
 * que son inmutables y pueden compartirse entre peticiones sin copiarlas.
 *
 * <p>Todo el contenido procede de información verificable: el README del proyecto,
 * el {@code pom.xml}, las versiones realmente resueltas en el empaquetado y el
 * historial de commits. No se registran estados, estimaciones, fechas ni porcentajes
 * que no estén definidos oficialmente.
 */
@Service
public class WikiService {

	private static final Proyecto PROYECTO = new Proyecto(
			"Editor y Visor de Procesos Empresariales",
			"Aplicación web para visualizar y editar procesos empresariales en un entorno multiempresa.",
			"Pontificia Universidad Javeriana",
			"Desarrollo Web",
			"2026",
			"https://github.com/TorresVides/DibujadorProcesosWeb");

	private static final List<SeccionWiki> SECCIONES = List.of(
			new SeccionWiki("inicio", "Inicio", "/inicio",
					"Descripción general del sistema y punto de entrada a la documentación."),
			new SeccionWiki("alcance", "Alcance y requisitos", "/alcance",
					"Qué construye el proyecto y qué queda deliberadamente fuera."),
			new SeccionWiki("arquitectura", "Arquitectura y tecnologías", "/arquitectura",
					"Organización por capas y stack técnico verificado del repositorio."),
			new SeccionWiki("historias", "Historias de Usuario", "/historias",
					"Estado del backlog del proyecto y áreas funcionales que deberá cubrir."),
			new SeccionWiki("entregas", "Entregas del proyecto", "/entregas",
					"Calendario oficial de entregas de la asignatura."),
			new SeccionWiki("contacto", "Contáctenos", "/contacto",
					"Formulario para enviarle una consulta al equipo del proyecto."));

	private static final List<ElementoAlcance> ELEMENTOS_ALCANCE = List.of(
			new ElementoAlcance("Visualización de procesos",
					"Representación gráfica de los procesos documentados por cada empresa.", true),
			new ElementoAlcance("Consulta de procesos",
					"Búsqueda y lectura de los procesos registrados en la organización.", true),
			new ElementoAlcance("Creación de procesos",
					"Registro de procesos nuevos dentro de la empresa.", true),
			new ElementoAlcance("Modificación de procesos",
					"Edición de los procesos existentes y de los elementos que los componen.", true),
			new ElementoAlcance("Organización de procesos",
					"Agrupación y ordenamiento del conjunto de procesos de la empresa.", true),
			new ElementoAlcance("Empresas y usuarios",
					"Administración de las organizaciones y de los usuarios que pertenecen a cada una.", true),
			new ElementoAlcance("Aislamiento entre organizaciones",
					"La información de una empresa permanece separada de la del resto.", true),
			new ElementoAlcance("Ejecución automática de procesos",
					"El sistema documenta los procesos, no los ejecuta.", false),
			new ElementoAlcance("Motor BPM",
					"No gestiona instancias de proceso en ejecución.", false),
			new ElementoAlcance("Disparo automático de tareas o flujos",
					"No lanza tareas ni flujos de trabajo por su cuenta.", false));

	private static final List<String> REQUISITOS_TECNICOS = List.of(
			"Aplicación web renderizada en el servidor con Spring MVC y Thymeleaf.",
			"Separación estricta de la información entre organizaciones.",
			"Persistencia sobre PostgreSQL 16.",
			"Ejecución reproducible mediante Docker y Docker Compose.",
			"Construcción con Maven Wrapper, sin instalación global de Maven.",
			"Credenciales suministradas por variables de entorno, nunca versionadas.");

	private static final List<CapaArquitectura> CAPAS = List.of(
			new CapaArquitectura("Controller",
					"Atiende las peticiones HTTP y resuelve las vistas Thymeleaf."),
			new CapaArquitectura("Service",
					"Concentra la lógica de negocio, incluida la separación de información por empresa."),
			new CapaArquitectura("Repository",
					"Acceso a datos mediante Spring Data JPA."),
			new CapaArquitectura("Persistencia",
					"Base de datos PostgreSQL."));

	private static final List<Tecnologia> TECNOLOGIAS = List.of(
			new Tecnologia("Java", "21", "Backend y servidor",
					"Lenguaje y plataforma de ejecución."),
			new Tecnologia("Spring Boot", "4.1.0", "Backend y servidor",
					"Arranque y autoconfiguración de la aplicación."),
			new Tecnologia("Spring MVC", "7.0.8", "Backend y servidor",
					"Controladores, rutas y resolución de vistas."),
			new Tecnologia("Hibernate Validator", "9.1.0", "Backend y servidor",
					"Implementación de Bean Validation para los datos de entrada."),
			new Tecnologia("Spring Boot Actuator", "4.1.0", "Backend y servidor",
					"Endpoint de salud que alimenta el healthcheck del contenedor."),
			new Tecnologia("Tomcat", "11.0.22", "Backend y servidor",
					"Servidor de aplicaciones embebido en el ejecutable."),

			new Tecnologia("Thymeleaf", "3.1.5", "Vistas",
					"Motor de plantillas renderizadas en el servidor."),

			new Tecnologia("Spring Data JPA", "4.1.0", "Persistencia",
					"Repositorios y acceso a datos."),
			new Tecnologia("Hibernate ORM", "7.4.1", "Persistencia",
					"Implementación de JPA."),
			new Tecnologia("PostgreSQL", "16", "Persistencia",
					"Motor de base de datos relacional."),
			new Tecnologia("Driver JDBC de PostgreSQL", "42.7.11", "Persistencia",
					"Conector entre la aplicación y la base de datos."),

			new Tecnologia("Maven", "3.9.16", "Construcción",
					"Compilación, pruebas y gestión de dependencias."),
			new Tecnologia("Maven Wrapper", "3.3.4", "Construcción",
					"Ejecuta Maven sin necesidad de instalarlo globalmente."),

			new Tecnologia("Docker", "", "Infraestructura",
					"Empaquetado de la aplicación en una imagen reproducible."),
			new Tecnologia("Docker Compose", "", "Infraestructura",
					"Orquestación conjunta de la aplicación y la base de datos."),

			new Tecnologia("Git", "", "Control de versiones",
					"Control de versiones del código."),
			new Tecnologia("GitHub", "", "Control de versiones",
					"Alojamiento del repositorio y trabajo por ramas."));

	private static final List<Entrega> ENTREGAS = List.of(
			new Entrega("Primera entrega",
					"Aplicación web con Spring Boot, Thymeleaf y JPA",
					LocalDate.of(2026, 9, 14), 15,
					"Aplicación web renderizada en el servidor, con persistencia sobre PostgreSQL.",
					List.of("Spring Boot y Spring MVC",
							"Vistas con Thymeleaf",
							"Persistencia con Spring Data JPA",
							"Ejecución con Docker y Docker Compose")),
			new Entrega("Segunda entrega",
					"API REST y frontend con Angular",
					LocalDate.of(2026, 10, 21), 25,
					"Separación entre backend y frontend: la aplicación expone una API y la consume un cliente Angular.",
					List.of("API REST",
							"Angular y RxJS",
							"Documentación de la API")),
			new Entrega("Entrega final",
					"Seguridad, pruebas y despliegue",
					LocalDate.of(2026, 11, 25), 20,
					"Cierre del proyecto: control de acceso, estrategia de pruebas y puesta en producción.",
					List.of("Spring Security",
							"Pruebas de integración y E2E",
							"Despliegue de la solución")));

	public Proyecto proyecto() {
		return PROYECTO;
	}

	public List<SeccionWiki> secciones() {
		return SECCIONES;
	}

	public List<ElementoAlcance> elementosAlcance() {
		return ELEMENTOS_ALCANCE;
	}

	public List<String> requisitosTecnicos() {
		return REQUISITOS_TECNICOS;
	}

	public List<CapaArquitectura> capas() {
		return CAPAS;
	}

	/**
	 * Agrupa las tecnologías por categoría respetando el orden de declaración, tanto el
	 * de las categorías como el de las tecnologías dentro de cada una. La vista recorre
	 * el mapa con un {@code th:each} anidado.
	 */
	public Map<String, List<Tecnologia>> tecnologiasPorCategoria() {
		Map<String, List<Tecnologia>> agrupadas = new LinkedHashMap<>();
		for (Tecnologia tecnologia : TECNOLOGIAS) {
			agrupadas.computeIfAbsent(tecnologia.categoria(), categoria -> new ArrayList<>())
					.add(tecnologia);
		}
		return agrupadas;
	}

	/**
	 * Áreas funcionales confirmadas del alcance. La página de Historias de Usuario las
	 * muestra como insumo del backlog pendiente; no son historias de usuario.
	 */
	public List<ElementoAlcance> areasFuncionalesConfirmadas() {
		return ELEMENTOS_ALCANCE.stream().filter(ElementoAlcance::incluido).toList();
	}

	public List<Entrega> entregas() {
		return ENTREGAS;
	}

}
