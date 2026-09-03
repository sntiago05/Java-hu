-- =========================================================
-- HU5: Script de creación de la base de datos y tablas.
-- Motor de referencia: MySQL 8+ (compatible con mysql-connector-j
-- configurado en el pom.xml y en ConexionBD.java).
-- =========================================================

CREATE DATABASE IF NOT EXISTS talent_hub_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE talent_hub_db;

CREATE TABLE IF NOT EXISTS empleados (
    id_empleado      INT AUTO_INCREMENT PRIMARY KEY,
    nivel_acceso     TINYINT      NOT NULL,
    anio_ingreso     SMALLINT     NOT NULL,
    numero_documento BIGINT       NOT NULL,
    puntaje_test     FLOAT        NOT NULL,
    salario_base     DOUBLE       NOT NULL,
    tipo_contrato    CHAR(1)      NOT NULL,
    es_activo        BOOLEAN      NOT NULL DEFAULT TRUE,
    nombre           VARCHAR(150) NOT NULL,
    edad             INT          NOT NULL,
    id_sede          INT          NOT NULL,
    bono_mensual     DOUBLE       NOT NULL DEFAULT 0
);

-- Calificaciones trimestrales de cada empleado (0.0 a 5.0), usadas por
-- la consulta SELECT compleja que alimenta el record DesempeñoReport
-- (TASK 4 de la HU5).
CREATE TABLE IF NOT EXISTS calificaciones (
    id_calificacion  INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado      INT NOT NULL,
    trimestre        TINYINT NOT NULL,
    calificacion     DECIMAL(3, 2) NOT NULL,
    CONSTRAINT fk_calificaciones_empleado
        FOREIGN KEY (id_empleado) REFERENCES empleados (id_empleado)
        ON DELETE CASCADE
);
