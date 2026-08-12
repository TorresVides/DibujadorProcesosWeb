# Build stage
# Maven 3.9.16: misma versión que el wrapper del proyecto.
FROM maven:3.9.16-eclipse-temurin-21 AS build

WORKDIR /build

# El pom se copia primero para que la capa de dependencias no se invalide al cambiar el código.
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src

# Las pruebas se ejecutan fuera de la imagen, con ./mvnw test.
RUN mvn -B -q clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S dibujador && adduser -S dibujador -G dibujador

WORKDIR /app

COPY --from=build --chown=dibujador:dibujador /build/target/*.jar app.jar

USER dibujador

EXPOSE 8080
ENV SERVER_PORT=8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
