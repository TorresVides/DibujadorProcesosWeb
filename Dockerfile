# ===========================================================================
# DibujadorProcesosWeb - imagen de la aplicacion
#
# Build multi-stage: la primera etapa compila con Maven y JDK completos; la
# segunda contiene solo el JRE y el .jar, de modo que ni el codigo fuente ni
# Maven ni el repositorio ~/.m2 llegan a la imagen final.
# ===========================================================================

# ---------------------------------------------------------------------------
# ETAPA 1 - BUILD
#
# Se fija `maven:3.9.16-eclipse-temurin-21` para usar EXACTAMENTE la misma
# version de Maven (3.9.16) que declara .mvn/wrapper/maven-wrapper.properties
# y el mismo Java 21 del proyecto. Asi la compilacion dentro de Docker es
# equivalente a la que hace `./mvnw` en local y en CI.
#
# No se invoca `./mvnw` dentro del contenedor: esta imagen ya trae Maven
# 3.9.16 instalado, y hacerlo obligaria a descargar una segunda copia de
# Maven en cada build sin ganar nada.
# ---------------------------------------------------------------------------
FROM maven:3.9.16-eclipse-temurin-21 AS build

WORKDIR /build

# Se copia primero unicamente el pom.xml y se resuelven las dependencias.
# Esta capa solo se invalida cuando cambia el pom.xml, por lo que editar
# codigo fuente no obliga a volver a descargar todas las dependencias.
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

# Ahora si el codigo fuente, que cambia con mucha mas frecuencia.
COPY src ./src

# Se omiten las pruebas a proposito: se ejecutan antes, fuera de la imagen,
# con `./mvnw test`. Repetirlas aqui alargaria cada build y ademas exigiria
# infraestructura de base de datos dentro del contenedor de build.
RUN mvn -B -q clean package -DskipTests


# ---------------------------------------------------------------------------
# ETAPA 2 - RUNTIME
#
# Solo JRE sobre Alpine: imagen final pequena y con menos superficie de
# ataque que una con JDK completo.
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runtime

# Usuario sin privilegios: la aplicacion NO se ejecuta como root.
RUN addgroup -S dibujador && adduser -S dibujador -G dibujador

WORKDIR /app

# Se copia unicamente el artefacto empaquetado desde la etapa de build.
# El comodin *.jar toma el jar ejecutable y deja fuera el *.jar.original que
# genera spring-boot-maven-plugin.
COPY --from=build --chown=dibujador:dibujador /build/target/*.jar app.jar

USER dibujador

EXPOSE 8080

# Puerto por defecto dentro del contenedor; docker-compose puede sobrescribirlo.
ENV SERVER_PORT=8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
