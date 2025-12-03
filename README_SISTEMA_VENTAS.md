# 🛍️ SISTEMA COMPLETO DE VENTAS - CrimsonEyes

## 📊 ¿QUÉ SE HA IMPLEMENTADO?

### ✅ Backend (Java Spring Boot)

#### 1. **Entidades (Models)**
- **Venta.java** - Entidad principal de ventas
  - Relación con Usuario (ManyToOne)
  - Relación con DetalleVenta (OneToMany)
  - Campos: id, usuario, fecha, total, estado, metodoPago
  
- **DetalleVenta.java** - Detalles de cada venta
  - Relación con Venta (ManyToOne)
  - Relación con Producto (ManyToOne)
  - Campos: id, venta, producto, cantidad, precioUnitario, subtotal

#### 2. **DTOs (Data Transfer Objects)**
- **VentaDTO.java** - Para transferencia de datos de Venta
- **DetalleVentaDTO.java** - Para transferencia de datos de DetalleVenta

#### 3. **Repositorios**
- **VentaRepository.java** - Acceso a datos de Ventas
  - Métodos: findAll(), findById(), findByUsuarioEmail()
  
- **DetalleVentaRepository.java** - Acceso a datos de DetalleVenta
  - Métodos: findAll(), findById(), findByVentaId()

#### 4. **Servicios**
- **VentaService.java** - Lógica de negocio para Ventas
  - listar() - Obtener todas las ventas
  - obtenerPorId(int id) - Obtener venta específica
  - obtenerPorUsuario(String email) - Obtener ventas de un usuario
  - crearVenta(VentaDTO) - Crear nueva venta
  - actualizarEstado(int id, String estado) - Cambiar estado

#### 5. **Controladores**
- **VentaController.java** - Endpoints REST
  - GET /ventas - Listar todas
  - GET /ventas/{id} - Obtener por ID
  - GET /ventas/usuario/{email} - Obtener por usuario
  - POST /ventas/crear - Crear nueva venta
  - PUT /ventas/{id}/estado - Actualizar estado

---

## 📋 ARCHIVOS CREADOS

### En Backend:
```
src/main/java/com/Ecostyle/CrimsonEyes/
├── model/
│   ├── Venta.java                    ✨ NUEVO
│   └── DetalleVenta.java             ✨ NUEVO
├── dto/
│   ├── VentaDTO.java                 ✨ NUEVO
│   └── DetalleVentaDTO.java          ✨ NUEVO
├── repository/
│   ├── VentaRepository.java          ✨ NUEVO
│   └── DetalleVentaRepository.java   ✨ NUEVO
├── service/
│   └── VentaService.java             ✨ NUEVO
└── controller/
    └── VentaController.java          ✨ NUEVO

📄 GUIA_VENTAS_COMPLETA.md
📄 CrimsonEyes_Ventas_API.postman_collection.json
📄 CREAR_TABLAS_VENTAS.sql
📄 PASOS_EXACTOS_VENTAS.md
📄 README_SISTEMA_VENTAS.md           (Este archivo)
```

---

## 🔄 FLUJO DE LA APLICACIÓN

### 1. Usuario realiza una compra en Kotlin
```
Usuario hace click en "Comprar"
    ↓
Kotlin recopila datos del carrito
    ↓
POST /ventas/crear
    ↓
VentaController recibe la solicitud
    ↓
VentaService procesa y valida
    ↓
Se crea Venta + DetalleVenta en BD
    ↓
Se retorna respuesta con ID y detalles
    ↓
Kotlin muestra confirmación
```

### 2. Usuario consulta su historial
```
Usuario abre "Mis Compras"
    ↓
GET /ventas/usuario/{email}
    ↓
VentaService obtiene de BD
    ↓
Se retorna lista de ventas
    ↓
Kotlin muestra el historial
```

---

## 💾 ESTRUCTURA DE BASE DE DATOS

### Tabla: ventas
```sql
id              INT (PK, AUTO_INCREMENT)
usuario_email   VARCHAR(255) (FK)
fecha           DATETIME
total           DOUBLE
estado          VARCHAR(50)
metodo_pago     VARCHAR(100)
```

### Tabla: detalle_ventas
```sql
id                  INT (PK, AUTO_INCREMENT)
venta_id            INT (FK → ventas.id)
producto_id         INT (FK → productos.id)
cantidad            INT
precio_unitario     DOUBLE
subtotal            DOUBLE
```

---

## 🧪 PRUEBAS RECOMENDADAS

### Test 1: Crear una Venta
```
POST http://localhost:8080/ventas/crear

Body:
{
  "usuarioEmail": "usuario@email.com",
  "metodoPago": "TARJETA",
  "total": 0,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 50000
    }
  ]
}

Response:
{
  "estado": "OK",
  "mensaje": "Venta creada correctamente",
  "venta": { ... }
}
```

### Test 2: Obtener Ventas del Usuario
```
GET http://localhost:8080/ventas/usuario/usuario@email.com

Response:
[ { venta1 }, { venta2 }, ... ]
```

### Test 3: Actualizar Estado
```
PUT http://localhost:8080/ventas/1/estado

Body:
{
  "estado": "COMPLETADA"
}

Response:
{
  "estado": "OK",
  "mensaje": "Estado actualizado correctamente",
  "venta": { ... }
}
```

---

## 🚀 INTEGRACIÓN CON KOTLIN

### Pasos para integrar:

1. **Copiar Data Classes**
   - VentaModels.kt (contiene todos los DTOs)

2. **Crear Interfaz API**
   - ApiService.kt (con todos los endpoints)

3. **Crear Repositorio**
   - VentaRepository.kt (lógica de conexión)

4. **Crear ViewModel**
   - VentaViewModel.kt (lógica de UI)
   - VentaViewModelFactory.kt (factory)

5. **Usar en Fragment/Activity**
   - Llamar a ventaViewModel.crearVenta()
   - Observar ventaViewModel.ventaCreada
   - Observar ventaViewModel.error

### Ejemplo en Fragment:
```kotlin
private fun realizarCompra() {
    val detalles = listOf(
        DetalleVentaRequest(
            productoId = 1,
            cantidad = 2,
            precioUnitario = 50000.0
        )
    )
    
    ventaViewModel.crearVenta("usuario@email.com", "TARJETA", detalles)
}

ventaViewModel.ventaCreada.observe(viewLifecycleOwner) { venta ->
    Toast.makeText(requireContext(), "Compra realizada: ${venta.id}", Toast.LENGTH_LONG).show()
}
```

---

## ✨ VALIDACIONES IMPLEMENTADAS

### En el Backend:
- ✅ Verifica que el usuario exista
- ✅ Verifica que los productos existan
- ✅ Valida cantidades positivas
- ✅ Calcula automáticamente subtotales y total
- ✅ Manejo de errores con mensajes claros

### En el Frontend (Recomendado):
- ✅ Validar que haya al menos 1 producto en carrito
- ✅ Validar que el método de pago esté seleccionado
- ✅ Mostrar indicador de carga mientras se procesa
- ✅ Mostrar mensajes de éxito/error

---

## 📱 ESTADOS DE VENTA

| Estado | Descripción | Cambios posibles |
|--------|-------------|------------------|
| PENDIENTE | Venta creada, no procesada | → COMPLETADA, CANCELADA |
| COMPLETADA | Venta confirmada y pagada | → CANCELADA |
| CANCELADA | Venta cancelada por usuario o sistema | Ninguno |

---

## 🔐 CONSIDERACIONES DE SEGURIDAD

1. **Autenticación**: Asegurar que el usuario esté autenticado antes de crear venta
2. **Email del Usuario**: Validar que coincida con el usuario autenticado
3. **Validación de Datos**: Todos los datos se validan en el servidor
4. **Relaciones**: Usar FK para mantener integridad referencial
5. **Transacciones**: Usar @Transactional para operaciones complejas (si es necesario)

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Problema: 400 Bad Request al crear venta
**Solución**: Verifica que el usuario y productos existan en la BD

### Problema: No aparecen las ventas
**Solución**: Verifica el email del usuario exactamente

### Problema: Error 500 en el servidor
**Solución**: Revisa los logs: `./mvnw.cmd spring-boot:run`

### Problema: Conexión rechazada
**Solución**: Asegurate que el backend esté corriendo en el puerto 8080

---

## 📚 ARCHIVOS DE REFERENCIA

1. **GUIA_VENTAS_COMPLETA.md** - Guía completa con todos los detalles
2. **PASOS_EXACTOS_VENTAS.md** - Pasos exactos para implementar
3. **CrimsonEyes_Ventas_API.postman_collection.json** - Colección de Postman
4. **CREAR_TABLAS_VENTAS.sql** - Script SQL para la BD

---

## 🎯 PRÓXIMOS PASOS RECOMENDADOS

### Fase 1 (Completado ✅)
- ✅ Crear entidades Venta y DetalleVenta
- ✅ Crear DTOs
- ✅ Crear Repositorios
- ✅ Crear Servicios
- ✅ Crear Controladores

### Fase 2 (Por hacer)
- ⏳ Configurar autenticación JWT
- ⏳ Agregar validaciones adicionales
- ⏳ Implementar paginación en listados
- ⏳ Agregar filtros por fecha/estado

### Fase 3 (Por hacer)
- ⏳ Reportes de ventas
- ⏳ Descuentos y cupones
- ⏳ Devoluciones/cambios
- ⏳ Historial de cambios de estado

---

## 📞 NOTAS IMPORTANTES

1. **Base de Datos**: Ejecuta `CREAR_TABLAS_VENTAS.sql` antes de usar
2. **Usuarios**: Los usuarios deben existir previamente en la BD
3. **Productos**: Los productos deben existir previamente
4. **Email**: Usa el email exacto del usuario registrado
5. **Pruebas**: Usa Postman para pruebas antes de integrar con Kotlin

---

## 🎉 ¡LISTO PARA USAR!

Ya tienes un sistema completo de ventas funcional:
- ✅ Backend con APIs REST
- ✅ Documentación completa
- ✅ Ejemplos en Postman
- ✅ Código Kotlin listo para integrar

¿Necesitas ayuda con algo más? Consulta los archivos de documentación.

**Versión**: 1.0  
**Última actualización**: 2025-12-02  
**Estado**: ✅ COMPLETADO

