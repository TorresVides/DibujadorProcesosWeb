-- Esquema gestionado de forma explícita: spring.jpa.hibernate.ddl-auto sigue en `none`,
-- de modo que Hibernate no crea ni modifica tablas. Este script es la única fuente del
-- esquema y se ejecuta en cada arranque, por lo que debe ser idempotente.
--
-- Por ahora solo contiene la tabla del formulario Contáctenos. Las tablas del dominio
-- (empresas, usuarios, procesos) se definirán junto con su modelo, en otra entrega.

CREATE TABLE IF NOT EXISTS contacto_mensaje (
    id              BIGSERIAL    PRIMARY KEY,
    nombre_completo VARCHAR(120) NOT NULL,
    correo          VARCHAR(180) NOT NULL,
    telefono        VARCHAR(15)  NOT NULL,
    asunto          VARCHAR(60)  NOT NULL,
    mensaje         VARCHAR(400) NOT NULL,
    fecha_creacion  TIMESTAMP    NOT NULL
);
