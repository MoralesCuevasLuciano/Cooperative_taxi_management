-- Script para verificar y corregir la estructura de la tabla models
-- Si la tabla existe con estructura incorrecta, elimínala primero

-- Eliminar la tabla si existe (CUIDADO: Esto borrará todos los datos)
DROP TABLE IF EXISTS models;

-- La tabla se recreará automáticamente cuando reinicies la aplicación
-- con ddl-auto=update

