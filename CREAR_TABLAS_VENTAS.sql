-- =============================================
-- SCRIPT: Crear Tablas para Sistema de Ventas
-- =============================================

-- Tabla de VENTAS
CREATE TABLE IF NOT EXISTS ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_email VARCHAR(255) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DOUBLE NOT NULL DEFAULT 0,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    metodo_pago VARCHAR(100) NOT NULL,
    FOREIGN KEY (usuario_email) REFERENCES usuarios(email) ON DELETE CASCADE,
    INDEX idx_usuario_email (usuario_email),
    INDEX idx_estado (estado),
    INDEX idx_fecha (fecha)
);

-- Tabla de DETALLES DE VENTAS
CREATE TABLE IF NOT EXISTS detalle_ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DOUBLE NOT NULL DEFAULT 0,
    subtotal DOUBLE NOT NULL DEFAULT 0,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT,
    INDEX idx_venta_id (venta_id),
    INDEX idx_producto_id (producto_id)
);

-- =============================================
-- INSERTS DE PRUEBA
-- =============================================

-- Insertar una venta de ejemplo
-- (Requiere que el usuario y productos existan en la BD)
/*
INSERT INTO ventas (usuario_email, fecha, total, estado, metodo_pago)
VALUES ('usuario@email.com', NOW(), 175000, 'PENDIENTE', 'TARJETA');

-- Obtener el ID de la venta insertada
SET @venta_id = LAST_INSERT_ID();

-- Insertar detalles de la venta
INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal)
VALUES
    (@venta_id, 1, 2, 50000, 100000),
    (@venta_id, 2, 1, 75000, 75000);
*/

-- =============================================
-- CONSULTAS ÚTILES
-- =============================================

-- Ver todas las ventas
-- SELECT * FROM ventas;

-- Ver ventas de un usuario
-- SELECT * FROM ventas WHERE usuario_email = 'usuario@email.com';

-- Ver detalles de una venta específica
-- SELECT dv.*, p.nombre as producto_nombre, p.descripcion
-- FROM detalle_ventas dv
-- JOIN productos p ON dv.producto_id = p.id
-- WHERE dv.venta_id = 1;

-- Ver ventas con total y cantidad de detalles
-- SELECT v.id, v.usuario_email, v.fecha, v.total, v.estado, COUNT(dv.id) as cantidad_items
-- FROM ventas v
-- LEFT JOIN detalle_ventas dv ON v.id = dv.venta_id
-- GROUP BY v.id;

-- Ver total de ventas por usuario
-- SELECT v.usuario_email, COUNT(v.id) as total_compras, SUM(v.total) as monto_total
-- FROM ventas v
-- GROUP BY v.usuario_email;

-- =============================================
-- LIMPIAR DATOS (CUIDADO)
-- =============================================

-- Eliminar todas las ventas y detalles
-- DELETE FROM detalle_ventas;
-- DELETE FROM ventas;

-- Resetear auto_increment
-- ALTER TABLE ventas AUTO_INCREMENT = 1;
-- ALTER TABLE detalle_ventas AUTO_INCREMENT = 1;

