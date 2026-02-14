-- Inserción automática de Roles (Usa IGNORE para no tronar si ya existen)
INSERT IGNORE INTO roles (id, name) VALUES (1, 'USER');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'SUPERUSER');

-- Inserción automática de 5 Tipos de Producto (Farmacia Style)
INSERT IGNORE INTO product_types (id, name) VALUES (1, 'ANTIBIOTICOS');
INSERT IGNORE INTO product_types (id, name) VALUES (2, 'ANALGESICOS');
INSERT IGNORE INTO product_types (id, name) VALUES (3, 'CUIDADO_PERSONAL');
INSERT IGNORE INTO product_types (id, name) VALUES (4, 'EQUIPO_MEDICO');
INSERT IGNORE INTO product_types (id, name) VALUES (5, 'SUPLEMENTOS');