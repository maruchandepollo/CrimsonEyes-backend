# API de CrimsonEyes - Backend REST

## Descripción General

Este backend proporciona endpoints REST para gestionar el catálogo de productos, carritos de compra, items del carrito y métodos de pago de la aplicación CrimsonEyes.

### Versión de API: v1
### Base URL: `http://localhost:8080/api/v1/`

---

## Estructura de Endpoints

### 1. PRODUCTOS (`/api/v1/productos`)

Gestión del catálogo de productos disponibles.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/productos` | Obtener todos los productos |
| GET | `/api/v1/productos/{id}` | Obtener un producto por ID |
| POST | `/api/v1/productos` | Crear un nuevo producto |
| PUT | `/api/v1/productos` | Actualizar un producto |
| DELETE | `/api/v1/productos/{id}` | Eliminar un producto |

**Modelo Producto:**
```json
{
    "id": 123,
    "nombre": "Lentes de Sol Premium",
    "precio": 45000,
    "descripcion": "Lentes con protección UV 100%",
    "stock": "50",
    "categoria": "Lentes"
}
```

---

### 2. CARRITOS (`/api/v1/carritos`)

Gestión de carritos de compra de usuarios.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/carritos` | Obtener todos los carritos |
| GET | `/api/v1/carritos/{id}` | Obtener carrito por ID |
| GET | `/api/v1/carritos/usuario/{email}` | Obtener carrito por email del usuario |
| POST | `/api/v1/carritos` | Crear un nuevo carrito |

**Modelo Carrito:**
```json
{
    "id": 456,
    "usuarioEmail": "usuario@ejemplo.com",
    "fecha": "2025-11-24",
    "estado": "abierto"
}
```

**Estados del Carrito:**
- `abierto`: Carrito activo con compras pendientes
- `pagado`: Compra completada
- `cancelado`: Carrito anulado

---

### 3. ITEMS DEL CARRITO (`/api/v1/items-carrito`)

Gestión de productos dentro de un carrito específico.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/items-carrito/carrito/{carritoId}` | Obtener items de un carrito |
| POST | `/api/v1/items-carrito` | Agregar item al carrito |
| DELETE | `/api/v1/items-carrito/{id}` | Eliminar item del carrito |

**Modelo ItemCarrito:**
```json
{
    "id": 789,
    "carritoId": 456,
    "productoId": 123,
    "cantidad": 2
}
```

**Comportamiento especial:**
- Al hacer POST con un producto que ya existe en el carrito, se suma la cantidad automáticamente
- Solo se crea un nuevo item si el producto no estaba en el carrito

---

### 4. MÉTODOS DE PAGO (`/api/v1/metodos-pago`)

Gestión de formas de pago disponibles en el sistema.

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/metodos-pago` | Obtener todos los métodos |
| GET | `/api/v1/metodos-pago/{id}` | Obtener método por ID |
| POST | `/api/v1/metodos-pago` | Crear nuevo método de pago |

**Modelo MetodoPago:**
```json
{
    "id": 1,
    "nombre": "Tarjeta de Crédito"
}
```

---

## Respuestas de Éxito

Todos los endpoints retornan respuestas JSON con el siguiente formato:

### Listar (GET sin parámetros)
```json
[
    { /* objeto 1 */ },
    { /* objeto 2 */ }
]
```

### Obtener por ID (GET con parámetro)
```json
{ /* objeto */ }
```

### Crear, Actualizar, Eliminar
```json
{
    "estado": "OK",
    "mensaje": "Descripción de la operación",
    "objeto": { /* objeto creado/actualizado */ }
}
```

---

## Respuestas de Error

Todos los errores retornan:

```json
{
    "estado": "Error",
    "mensaje": "Descripción detallada del error"
}
```

**Errores comunes:**
- `404`: Recurso no encontrado
- `400`: Datos inválidos en el request
- `500`: Error interno del servidor

---

## Generación Automática de IDs

Todos los servicios usan el generador de IDs de `Util.generarID()`:
- Si envías `id: 0` o sin el campo `id`, se genera uno automáticamente
- El ID es único y aleatorio

**Para crear sin enviar ID:**
```json
{
    "nombre": "Nuevo Producto",
    "precio": 25000,
    "descripcion": "Descripción"
}
```

El backend generará un ID automáticamente y lo retornará en la respuesta.

---

## Flujo de Compra Completo

### 1. Obtener Productos
```bash
curl http://localhost:8080/api/v1/productos
```

### 2. Crear Carrito para Usuario
```bash
curl -X POST http://localhost:8080/api/v1/carritos \
  -H "Content-Type: application/json" \
  -d '{
    "id": 0,
    "usuarioEmail": "usuario@ejemplo.com",
    "fecha": "2025-11-24",
    "estado": "abierto"
  }'
```

Respuesta:
```json
{
    "estado": "OK",
    "carrito": {
        "id": 123456,
        "usuarioEmail": "usuario@ejemplo.com",
        "fecha": "2025-11-24",
        "estado": "abierto"
    }
}
```

### 3. Agregar Producto al Carrito
```bash
curl -X POST http://localhost:8080/api/v1/items-carrito \
  -H "Content-Type: application/json" \
  -d '{
    "id": 0,
    "carritoId": 123456,
    "productoId": 789,
    "cantidad": 2
  }'
```

### 4. Ver Items del Carrito
```bash
curl http://localhost:8080/api/v1/items-carrito/carrito/123456
```

### 5. Obtener Métodos de Pago (para checkout)
```bash
curl http://localhost:8080/api/v1/metodos-pago
```

### 6. Eliminar Item
```bash
curl -X DELETE http://localhost:8080/api/v1/items-carrito/987654
```

---

## Configuración

### application.properties

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/crimson_eyes
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### RetrofitProvider (Frontend)

```kotlin
val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("http://192.168.1.94:8080/") // Cambiar a tu IP local
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
```

---

## Compilar y Ejecutar el Backend

```bash
cd backend

# Compilar sin ejecutar tests
./mvnw -DskipTests package

# Ejecutar la aplicación
./mvnw spring-boot:run
```

---

## Compilar y Ejecutar Frontend (Kotlin/Android)

```bash
cd CrimsonEyes

# Build debug
./gradlew build

# Instalar en emulador
./gradlew installDebug

# O abrir en Android Studio y dar Run
```

---

## Arquitectura Backend

```
backend/
├── src/main/java/com/Ecostyle/CrimsonEyes/
│   ├── model/
│   │   ├── Producto.java
│   │   ├── Carrito.java
│   │   ├── ItemCarrito.java
│   │   ├── MetodoPago.java
│   │   ├── Usuario.java
│   │   └── Persona.java
│   ├── repository/
│   │   ├── ProductoRepository.java
│   │   ├── CarritoRepository.java
│   │   ├── ItemCarritoRepository.java
│   │   ├── MetodoPagoRepository.java
│   │   ├── UsuarioRepository.java
│   │   └── PersonaRepository.java
│   ├── service/
│   │   ├── ProductoService.java
│   │   ├── CarritoService.java
│   │   ├── ItemCarritoService.java
│   │   ├── MetodoPagoService.java
│   │   ├── UsuarioService.java
│   │   └── RecetaService.java
│   ├── controller/
│   │   ├── ProductoController.java
│   │   ├── CarritoController.java
│   │   ├── ItemCarritoController.java
│   │   ├── MetodoPagoController.java
│   │   └── RecetaController.java
│   ├── util/
│   │   └── Util.java
│   └── CrimsonEyesApplication.java
└── pom.xml
```

---

## Arquitectura Frontend (Kotlin)

```
app/src/main/java/com/example/crimsoneyes/
├── model/
│   ├── Producto.kt
│   ├── Carrito.kt
│   ├── ItemCarrito.kt
│   ├── MetodoPago.kt
│   ├── Usuario.kt
│   └── LoginRequest.kt
├── network/
│   ├── RetrofitProvider.kt
│   ├── ApiRepository.kt
│   └── api/
│       ├── ProductoApiService.kt
│       ├── CarritoApiService.kt
│       ├── ItemCarritoApiService.kt
│       └── MetodoPagoApiService.kt
├── view/
│   ├── ProductoScreen.kt
│   ├── CarritoScreen.kt
│   ├── LoginScreen.kt
│   └── ProfileScreen.kt
├── controller/
│   ├── ProductoViewModel.kt
│   ├── CarritoViewModel.kt
│   ├── LoginViewModel.kt
│   └── ViewModelFactory.kt
├── navigation/
│   ├── Screen.kt
│   └── AppNavigation.kt
└── MainActivity.kt
```

---

## Notas Importantes

1. **IDs Automáticos**: Los servicios generan IDs únicos si no los proporcionas (envía `0` o no incluyas el campo)

2. **Relaciones**: 
   - Un usuario tiene 1 carrito activo
   - Un carrito tiene muchos items
   - Un item contiene 1 producto

3. **Stock**: El campo `stock` en Producto es String (se puede actualizar manualmente desde admin)

4. **Seguridad**: Estos endpoints están sin autenticación. Para producción, implementar JWT o OAuth2

5. **CORS**: Asegúrate de habilitar CORS en el backend si el frontend está en otro servidor

---

## Próximas Mejoras

- [ ] Autenticación JWT
- [ ] Endpoint POST `/api/v1/pedidos` para completar compras
- [ ] Endpoint para actualizar stock automáticamente
- [ ] Validación de datos más robusta
- [ ] Tests unitarios
- [ ] Documentación Swagger/OpenAPI

---

## Soporte

Para preguntas sobre los endpoints, revisa la documentación en:
- `API_DOCUMENTATION.kt` (Frontend) - Ejemplos de uso en Kotlin
- `README.md` (Este archivo)
- Comentarios en los controladores Java

