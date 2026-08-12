# Editor y Visor de Procesos Empresariales

## Descripción

Este proyecto consiste en una aplicación web para la visualización y edición de procesos empresariales en un entorno multiempresa. Cada organización administra sus propios usuarios y sus propios procesos, de modo que la información de una empresa permanece aislada de la del resto.

El sistema permite consultar, crear, modificar y organizar procesos. Está orientado a representarlos y editarlos, no a ejecutarlos: no actúa como motor BPM ni gestiona instancias de proceso en ejecución.

## Objetivo

Ofrecer una herramienta web que permita a varias empresas documentar y mantener sus procesos de forma centralizada, con una interfaz común para consultarlos y editarlos, y con separación estricta de la información entre organizaciones.

## Equipo del Proyecto

| Nombre        | Rol           | GitHub                                       |
| ------------- | ------------- | -------------------------------------------- |
| David Orjuela | Por definir   | [@Kerosene21](https://github.com/Kerosene21) |
| Por completar | Por completar | Por completar                                |
| Por completar | Por completar | Por completar                                |
| Por completar | Por completar | Por completar                                |

## Tecnologías Utilizadas

**Backend y servidor**

- Java 21
- Spring Boot 4.1.0
- Spring MVC
- Spring Validation
- Spring Boot Actuator

**Vistas**

- Thymeleaf

**Persistencia**

- Spring Data JPA
- PostgreSQL 16

**Build**

- Maven
- Maven Wrapper

**Infraestructura**

- Docker
- Docker Compose

**Control de versiones**

- Git
- GitHub

### Evolución prevista

En entregas posteriores el proyecto evolucionará con la incorporación de API REST, Angular, RxJS, Swagger/OpenAPI y Spring Security, además de ampliar la estrategia de pruebas con pruebas de integración y E2E y completar el despliegue de la solución.

## Arquitectura

La aplicación es un proyecto Spring Boot estructurado bajo el patrón MVC, con separación por capas:

```text
Controller → Service → Repository → Persistencia
```

- **Controller:** atiende las peticiones HTTP y resuelve las vistas Thymeleaf.
- **Service:** concentra la lógica de negocio, incluida la separación de información por empresa.
- **Repository:** acceso a datos mediante Spring Data JPA.
- **Persistencia:** base de datos PostgreSQL.

Esta es la arquitectura prevista para el código. El repositorio contiene por ahora la clase de arranque, la configuración de la aplicación y la infraestructura de ejecución; las capas anteriores se implementarán junto con el modelo de dominio.

## Estructura del Proyecto

```text
DibujadorProcesosWeb/
├── .github/
│   └── workflows/                  # marcadores de workflows futuros, aún inactivos
│       ├── Sonar.yml.disabled
│       ├── docker-ci.yml.disabled
│       └── docker-publish.yml.disabled
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── conf/                           # configuración adicional, aún sin contenido
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── co/edu/javeriana/dibujadorprocesos/
│   │   │       └── DibujadorProcesosApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── co/edu/javeriana/dibujadorprocesos/
│               └── DibujadorProcesosApplicationTests.java
├── .dockerignore
├── .env.example
├── .gitattributes
├── .gitignore
├── Dockerfile
├── LICENSE
├── README.md
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Requisitos

- Git
- Docker Desktop, o bien Docker Engine con Docker Compose
- Java 21, únicamente si se ejecuta la aplicación de forma local fuera de Docker

No se requiere una instalación global de Maven: el repositorio incluye Maven Wrapper.

## Configuración

Clonar el repositorio:

```bash
git clone https://github.com/TorresVides/DibujadorProcesosWeb.git
```

Antes de ejecutar, copiar `.env.example` a `.env` y ajustar los valores, en particular `POSTGRES_PASSWORD`:

```bash
cp .env.example .env
```

En Windows con PowerShell:

```bash
Copy-Item .env.example .env
```

El archivo `.env` es local y no se versiona. Sus variables las consumen tanto Docker Compose como Spring Boot, que lo importa desde `application.properties`. `.env.example` solo contiene valores de ejemplo y ningún dato sensible real.

## Ejecución

### Ejecución con Docker

Construir e iniciar la aplicación y la base de datos:

```bash
docker compose up --build
```

Consultar el estado de los servicios:

```bash
docker compose ps
```

Detener y eliminar los contenedores:

```bash
docker compose down
```

Una vez iniciados los servicios:

- Aplicación: `http://localhost:8080`
- Estado de la aplicación: `http://localhost:8080/actuator/health`
- PostgreSQL: publicado en el host en el puerto `5433`

Añadir `-v` a `docker compose down` elimina también el volumen de datos de PostgreSQL.

### Ejecución local con Maven

Requiere una instancia de PostgreSQL accesible con las variables de conexión definidas en `.env`. Puede levantarse únicamente la base de datos:

```bash
docker compose up -d db
```

Después, iniciar la aplicación con el Maven Wrapper.

En Git Bash, Linux o macOS:

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

## Base de Datos

- Motor: PostgreSQL 16 (imagen `postgres:16-alpine`).
- El nombre de la base de datos, el usuario y la contraseña se definen por variables de entorno (`POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD`).
- Los datos persisten en un volumen de Docker, por lo que sobreviven al reinicio de los contenedores.
- Puerto en el host: `5433`. Puerto interno de PostgreSQL: `5432`.

Todavía no se ha implementado el modelo de dominio, por lo que no se han definido tablas propias del dominio de la aplicación.

## Documentación

La documentación ampliada del proyecto se publica en la Wiki del repositorio:

[Wiki del proyecto](https://github.com/TorresVides/DibujadorProcesosWeb/wiki)

## Contexto Académico

Proyecto académico desarrollado en el marco de la asignatura Desarrollo Web.

- Institución: Pontificia Universidad Javeriana
- Año: 2026
