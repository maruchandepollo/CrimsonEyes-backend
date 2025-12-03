# 📋 Guía Completa de Pruebas en Postman - CrimsonEyes

## 🚀 Flujo Completo de Trabajo

```
1. REGISTER (Crea usuario + carrito automático)
   ↓
2. LOGIN (Valida credenciales)
   ↓
3. OBTENER CARRITO (Obtiene el carrito del usuario)
   ↓
4. AGREGAR PRODUCTOS AL CARRITO (Agregar items)
   ↓
5. VER ITEMS DEL CARRITO (Lista todos los items)
   ↓
6. ACTUALIZAR CANTIDAD (Modificar cantidades)
   ↓
7. FINALIZAR COMPRA (Pagar y vaciar carrito)
```

---

## 📝 ENDPOINTS Y PRUEBAS

### 🔐 1. REGISTER - Crear Usuario Automático con Carrito

**URL:** `POST http://localhost:8080/usuario/register`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "juan@example.com",
  "password": "123456",
  "nombre": "Juan Pérez",
  "rut": ""
}
```

**Respuesta Esperada:**
```json
{
  "estado": "OK",
  "mensaje": "Usuario creado correctamente!",
  "usuarioDTO": {
    "email": "juan@example.com",
    "password": "*************",
    "nombre": "Juan Pérez",
    "rut": "TEMP-juan-example-com"
  }
}
```

✅ **NOTA:** El carrito se crea automáticamente aquí

---

### 🔓 2. LOGIN - Validar Credenciales

**URL:** `POST http://localhost:8080/usuario/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "email": "juan@example.com",
  "password": "123456"
}
```

**Respuesta Esperada:**
```json
{
  "email": "juan@example.com",
  "password": "*************",
  "nombre": "Juan Pérez",
  "rut": "TEMP-juan-example-com"
}
```

---

### 🛒 3. OBTENER CARRITO DEL USUARIO

**URL:** `GET http://localhost:8080/carritos/usuario/{email}`

**Ejemplo:**
```
GET http://localhost:8080/carritos/usuario/juan@example.com
```

**Respuesta Esperada:**
```json
{
  "estado": "OK",
  "carrito": {
    "id": 123456,
    "usuario": {
      "email": "juan@example.com",
      "password": "123456",
      "persona": {
        "rut": "TEMP-juan-example-com",
        "nombre": "Juan Pérez"
      }
    },
    "fecha": "2024-11-29 10:30:45",
    "estado": "activo",
    "items": []
  }
}
```

⚠️ **NOTA:** Guarda el `id` del carrito para los próximos pasos

---

### 📦 4. AGREGAR PRODUCTO AL CARRITO

**URL:** `POST http://localhost:8080/items-carrito/agregar/{carritoId}`

**Ejemplo:**
```
POST http://localhost:8080/items-carrito/agregar/123456
```

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "id": 0,
  "cantidad": 2,
  "producto": {
    "id": 1,
    "nombre": "Receta Sustentable",
    "precio": 5000
  }
}
```

**Respuesta Esperada:**
```json
{
  "estado": "OK",
  "item": {
    "id": 456789,
    "cantidad": 2,
    "carrito": {
      "id": 123456
    },
    "producto": {
      "id": 1,
      "nombre": "Receta Sustentable",
      "precio": 5000
    }
  }
}
```

---

### 📊 5. VER ITEMS DEL CARRITO

**URL:** `GET http://localhost:8080/items-carrito/carrito/{carritoId}`

**Ejemplo:**
```
GET http://localhost:8080/items-carrito/carrito/123456
```

**Respuesta Esperada:**
```json
[
  {
    "id": 456789,
    "cantidad": 2,
    "carrito": {
      "id": 123456
    },
    "producto": {
      "id": 1,
      "nombre": "Receta Sustentable",
      "precio": 5000
    }
  },
  {
    "id": 456790,
    "cantidad": 1,
    "carrito": {
      "id": 123456
    },
    "producto": {
      "id": 2,
      "nombre": "Bolsas Ecológicas",
      "precio": 3000
    }
  }
]
```

---

### ✏️ 6. ACTUALIZAR CANTIDAD DE UN ITEM

**URL:** `PUT http://localhost:8080/items-carrito/actualizar-cantidad/{itemId}/{nuevaCantidad}`

**Ejemplo - Cambiar cantidad a 5:**
```
PUT http://localhost:8080/items-carrito/actualizar-cantidad/456789/5
```

**Respuesta Esperada:**
```json
{
  "estado": "OK",
  "item": {
    "id": 456789,
    "cantidad": 5,
    "carrito": {
      "id": 123456
    },
    "producto": {
      "id": 1,
      "nombre": "Receta Sustentable",
      "precio": 5000
    }
  }
}
```

⚠️ **NOTA:** Si la cantidad es 0 o negativa, el item se elimina automáticamente

---

### 🗑️ 7. ELIMINAR UN ITEM DEL CARRITO

**URL:** `DELETE http://localhost:8080/items-carrito/eliminar/{itemId}`

**Ejemplo:**
```
DELETE http://localhost:8080/items-carrito/eliminar/456789
```

**Respuesta Esperada:**
```json
{
  "estado": "OK",
  "mensaje": "Item eliminado"
}
```

---

### 🚀 8. FINALIZAR COMPRA (Pagar)

**URL:** `POST http://localhost:8080/carritos/finalizar-compra/{carritoId}`

**Ejemplo:**
```
POST http://localhost:8080/carritos/finalizar-compra/123456
```

**Respuesta Esperada:**
```json
{
  "estado": "OK",
  "mensaje": "Compra finalizada correctamente",
  "carrito": {
    "id": 123456,
    "fecha": "2024-11-29 10:30:45",
    "estado": "pagado",
    "usuario": {
      "email": "juan@example.com"
    }
  }
}
```

✅ **NOTA:** 
- El estado del carrito cambia a "pagado"
- Todos los items del carrito se eliminan
- Listo para una nueva compra

---

## 🔄 CASO DE USO COMPLETO - Paso a Paso

### Paso 1: Registrarse
```
POST http://localhost:8080/usuario/register
{
  "email": "maria@example.com",
  "password": "pass123",
  "nombre": "María García",
  "rut": ""
}
```
**Guardar:** `email = maria@example.com` y el `id` del carrito que viene en la respuesta

---

### Paso 2: Obtener el Carrito
```
GET http://localhost:8080/carritos/usuario/maria@example.com
```
**Guardar:** `carritoId = [id obtenido]`

---

### Paso 3: Agregar 2 Productos al Carrito

**Producto 1:**
```
POST http://localhost:8080/items-carrito/agregar/[carritoId]
{
  "cantidad": 2,
  "producto": {
    "id": 1,
    "nombre": "Receta de Té Verde",
    "precio": 5000
  }
}
```

**Producto 2:**
```
POST http://localhost:8080/items-carrito/agregar/[carritoId]
{
  "cantidad": 1,
  "producto": {
    "id": 2,
    "nombre": "Empaque Reutilizable",
    "precio": 2000
  }
}
```

---

### Paso 4: Ver el Carrito
```
GET http://localhost:8080/items-carrito/carrito/[carritoId]
```

---

### Paso 5: Actualizar una Cantidad
```
PUT http://localhost:8080/items-carrito/actualizar-cantidad/[itemId]/3
```

---

### Paso 6: Finalizar Compra (Pagar)
```
POST http://localhost:8080/carritos/finalizar-compra/[carritoId]
```

---

## 🔍 VARIABLES DE ENTORNO EN POSTMAN (Recomendado)

Para facilitar las pruebas, crea variables en Postman:

1. En Postman, ve a **Environments**
2. Crea un nuevo environment: **CrimsonEyes-Dev**
3. Agrega estas variables:

```
Base URL: http://localhost:8080
userEmail: juan@example.com
userPassword: 123456
carritoId: [obtén del paso 3]
itemId: [obtén del paso 4]
```

Luego en tus requests, usa:
- `{{Base URL}}/usuario/register`
- `{{Base URL}}/carritos/usuario/{{userEmail}}`
- `{{Base URL}}/items-carrito/agregar/{{carritoId}}`

---

## ❌ ERRORES COMUNES Y SOLUCIONES

| Error | Causa | Solución |
|-------|-------|----------|
| `"Carrito no encontrado"` | No se creó el carrito | Asegúrate de haber registrado primero |
| `"Producto faltante"` | No enviaste el objeto producto | Agrega `"producto": { "id": X }` en el body |
| `"Item no encontrado"` | El itemId no existe | Verifica que el itemId sea correcto |
| `"Usuario no encontrado"` | El email no está registrado | Registra un nuevo usuario primero |

---

## 📱 Endpoints Resumen

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/usuario/register` | Registrar usuario (crea carrito) |
| POST | `/usuario/login` | Login |
| GET | `/carritos/usuario/{email}` | Obtener carrito del usuario ⭐ |
| GET | `/carritos/{id}` | Obtener carrito por ID |
| GET | `/items-carrito/carrito/{carritoId}` | Ver items del carrito |
| POST | `/items-carrito/agregar/{carritoId}` | Agregar item al carrito |
| PUT | `/items-carrito/actualizar-cantidad/{itemId}/{cantidad}` | Actualizar cantidad |
| DELETE | `/items-carrito/eliminar/{id}` | Eliminar item |
| DELETE | `/items-carrito/vaciar-carrito/{carritoId}` | Vaciar todo el carrito |
| POST | `/carritos/finalizar-compra/{id}` | Finalizar compra (pagar) |

---

## ✅ CHECKLIST DE PRUEBAS

- [ ] Register crea usuario y carrito automáticamente
- [ ] Login retorna datos correctos
- [ ] Obtener carrito funciona correctamente
- [ ] Agregar primer producto funciona
- [ ] Agregar segundo producto funciona
- [ ] Ver items del carrito muestra los 2 productos
- [ ] Actualizar cantidad funciona
- [ ] Eliminar item funciona
- [ ] Finalizar compra cambia estado a "pagado"
- [ ] Después de pagar, carrito está vacío

¡Listo para empezar! 🎉

