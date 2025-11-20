-- Script para crear la base de datos de la cooperativa de taxis
-- Ejecutar este script en MySQL antes de levantar la aplicación

CREATE DATABASE IF NOT EXISTS cooperative_taxi_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE cooperative_taxi_db;

-- La aplicación creará las tablas automáticamente con JPA
-- gracias a la configuración: spring.jpa.hibernate.ddl-auto=update

