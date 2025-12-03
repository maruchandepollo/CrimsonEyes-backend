# ⚡ GUÍA RÁPIDA: Sistema de Ventas - Pasos Exactos

## 🔧 PASO 1: PREPARAR LA BASE DE DATOS

1. Abre tu cliente MySQL (MySQL Workbench, HeidiSQL, etc.)
2. Copia y ejecuta el archivo: `CREAR_TABLAS_VENTAS.sql`
3. Verifica que se crearon las tablas:
   ```sql
   SHOW TABLES;
   ```

---

## 🚀 PASO 2: INICIAR EL BACKEND

1. Abre la terminal en la carpeta del backend
2. Ejecuta:
   ```bash
   ./mvnw.cmd spring-boot:run
   ```
3. Espera a que veas: `Started CrimsonEyesApplication`

---

## 📬 PASO 3: PROBAR EN POSTMAN

### 3.1 Importar la Colección
1. Abre Postman
2. Haz click en "Import"
3. Selecciona: `CrimsonEyes_Ventas_API.postman_collection.json`
4. ¡Listo! Ya tienes los endpoints disponibles

### 3.2 Test: Crear una Venta
1. Abre la solicitud: `1. Crear Venta`
2. **IMPORTANTE:** Cambia `usuario@email.com` por un email que exista en tu BD
3. Verifica que los productos (ID 1, ID 2) existan en tu BD
4. Haz click en "Send"
5. Deberías recibir la venta creada con ID

### 3.3 Test: Obtener Ventas del Usuario
1. Abre: `4. Obtener Ventas por Usuario`
2. Cambia la URL: `usuario@email.com` por el email usado en el test anterior
3. Haz click en "Send"
4. Verás la lista de ventas creadas

### 3.4 Test: Actualizar Estado
1. Abre: `5. Actualizar Estado de Venta`
2. Cambia el ID: reemplaza `1` con el ID de la venta creada
3. Haz click en "Send"
4. El estado cambió a "COMPLETADA"

---

## 💻 PASO 4: INTEGRAR EN KOTLIN

### 4.1 Copiar las Data Classes
1. En Android Studio, crea la carpeta: `app/src/main/java/com/tu_app/models/`
2. Crea el archivo: `VentaModels.kt`
3. Copia el contenido de la sección "Data Classes" de `GUIA_VENTAS_COMPLETA.md`

### 4.2 Crear la Interfaz API
1. Crea: `app/src/main/java/com/tu_app/api/ApiService.kt`
2. Copia el contenido de la sección "Interfaz de API"

### 4.3 Crear el Repositorio
1. Crea: `app/src/main/java/com/tu_app/repository/VentaRepository.kt`
2. Copia el contenido de la sección "Repositorio"

### 4.4 Crear el ViewModel
1. Crea: `app/src/main/java/com/tu_app/viewmodel/VentaViewModel.kt`
2. Copia el contenido de la sección "ViewModel"

### 4.5 Crear la Factory
1. Crea: `app/src/main/java/com/tu_app/viewmodel/VentaViewModelFactory.kt`
2. Copia el contenido de la sección "Factory"

### 4.6 Usar en tu Fragment/Activity
```kotlin
// En tu CheckoutFragment, cuando el usuario hace click en "Comprar":

private fun realizarCompra() {
    val usuarioEmail = "usuario@email.com" // Obtener del usuario autenticado
    val metodoPago = "TARJETA" // Obtener de la selección del usuario
    
    val detalles = listOf(
        DetalleVentaRequest(
            productoId = 1,
            cantidad = 2,
            precioUnitario = 50000.0
        ),
        DetalleVentaRequest(
            productoId = 2,
            cantidad = 1,
            precioUnitario = 75000.0
        )
    )
    
    ventaViewModel.crearVenta(usuarioEmail, metodoPago, detalles)
}
```

---

## ✅ ENDPOINTS DISPONIBLES

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/ventas/crear` | Crear una nueva venta |
| GET | `/ventas` | Obtener todas las ventas |
| GET | `/ventas/{id}` | Obtener venta por ID |
| GET | `/ventas/usuario/{email}` | Obtener ventas de un usuario |
| PUT | `/ventas/{id}/estado` | Actualizar estado de venta |

---

## 🎯 FLUJO COMPLETO DEL USUARIO

```
1. Usuario selecciona productos y los agrega al carrito
   ↓
2. Usuario va al carrito y hace click en "Proceder al Pago"
   ↓
3. Pantalla de Checkout muestra:
   - Lista de productos
   - Cantidad
   - Precio
   - Total
   - Opción de método de pago
   ↓
4. Usuario selecciona método de pago y hace click en "Comprar"
   ↓
5. La app llama a: POST /ventas/crear
   ↓
6. Backend:
   - Valida el usuario
   - Valida los productos
   - Crea la venta (estado: PENDIENTE)
   - Crea los detalles de venta
   - Calcula el total
   ↓
7. App recibe respuesta con ID de venta
   ↓
8. Pantalla de confirmación muestra:
   - ID de compra
   - Resumen de productos
   - Total
   - Estado: PENDIENTE
   ↓
9. Usuario puede:
   - Ver su historial de compras: GET /ventas/usuario/{email}
   - Ver detalles de una compra: GET /ventas/{id}
```

---

## ⚠️ ERRORES COMUNES Y SOLUCIONES

| Error | Causa | Solución |
|-------|-------|----------|
| 400 Bad Request | Usuario no existe | Verifica que el email exista en usuarios |
| 400 Bad Request | Producto no existe | Verifica que el producto_id exista en productos |
| 400 Bad Request | Datos incompletos | Revisa que todos los campos requeridos estén presentes |
| 500 Internal Server Error | Error en el servidor | Revisa los logs del backend |
| No conecta | Backend no está corriendo | Ejecuta `./mvnw.cmd spring-boot:run` |
| Connection refused | Puerto ocupado | Cambia el puerto en `application.properties` |

---

## 🗄️ ESTRUCTURA DE LA BD

```
usuarios
  ├── email (PK)
  ├── password
  ├── persona_rut (FK)

ventas
  ├── id (PK)
  ├── usuario_email (FK → usuarios.email)
  ├── fecha
  ├── total
  ├── estado
  └── metodo_pago

detalle_ventas
  ├── id (PK)
  ├── venta_id (FK → ventas.id)
  ├── producto_id (FK → productos.id)
  ├── cantidad
  ├── precio_unitario
  └── subtotal

productos
  ├── id (PK)
  ├── nombre
  ├── precio
  ├── descripcion
  ├── stock
  └── categoria
```

---

## 📱 INTEGRACIÓN CON EL CARRITO

Si tienes un Carrito en tu BD, la lógica sería:

1. Usuario agrega productos a: `ItemCarrito`
2. En checkout, lees los items del carrito del usuario
3. Creas una `Venta` con esos mismos detalles
4. (Opcional) Luego puedes limpiar el carrito del usuario

```kotlin
// Pseudo-código
fun realizarCompra() {
    // 1. Obtener items del carrito del usuario
    val itemsCarrito = carritoRepository.obtenerItems(usuarioEmail)
    
    // 2. Convertir items del carrito a detalles de venta
    val detalles = itemsCarrito.map { item ->
        DetalleVentaRequest(
            productoId = item.producto.id,
            cantidad = item.cantidad,
            precioUnitario = item.producto.precio.toDouble()
        )
    }
    
    // 3. Crear la venta
    ventaViewModel.crearVenta(usuarioEmail, metodoPago, detalles)
    
    // 4. (Opcional) Limpiar el carrito
    // carritoRepository.limpiar(usuarioEmail)
}
```

---

## 🎉 ¡LISTO!

Ya tienes todo configurado. Ahora puedes:
- ✅ Crear ventas desde Postman
- ✅ Integrar en tu app Kotlin
- ✅ Los usuarios pueden comprar productos
- ✅ Ver su historial de compras
- ✅ El backend registra todo correctamente

¿Preguntas? Revisa `GUIA_VENTAS_COMPLETA.md` para más detalles.

