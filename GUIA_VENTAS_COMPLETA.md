# Guía Completa: Sistema de Ventas - Backend y Frontend

## 📋 PARTE 1: PRUEBAS EN POSTMAN

### 1. Crear una Venta (POST)

**URL:** `http://localhost:8080/ventas/crear`

**Método:** POST

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "usuarioEmail": "usuario@email.com",
  "metodoPago": "TARJETA",
  "total": 0,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 50000
    },
    {
      "productoId": 2,
      "cantidad": 1,
      "precioUnitario": 75000
    }
  ]
}
```

**Respuesta Exitosa (200):**
```json
{
  "estado": "OK",
  "mensaje": "Venta creada correctamente",
  "venta": {
    "id": 1,
    "usuarioEmail": "usuario@email.com",
    "fecha": "2025-12-02T14:30:45.123456",
    "total": 175000,
    "estado": "PENDIENTE",
    "metodoPago": "TARJETA",
    "detalles": [
      {
        "id": 1,
        "productoId": 1,
        "productoNombre": "Lentes de Sol",
        "cantidad": 2,
        "precioUnitario": 50000,
        "subtotal": 100000
      },
      {
        "id": 2,
        "productoId": 2,
        "productoNombre": "Lentes de Lectura",
        "cantidad": 1,
        "precioUnitario": 75000,
        "subtotal": 75000
      }
    ]
  }
}
```

---

### 2. Obtener Todas las Ventas (GET)

**URL:** `http://localhost:8080/ventas`

**Método:** GET

**Respuesta:**
```json
[
  {
    "id": 1,
    "usuarioEmail": "usuario@email.com",
    "fecha": "2025-12-02T14:30:45.123456",
    "total": 175000,
    "estado": "PENDIENTE",
    "metodoPago": "TARJETA",
    "detalles": [...]
  }
]
```

---

### 3. Obtener una Venta por ID (GET)

**URL:** `http://localhost:8080/ventas/1`

**Método:** GET

**Respuesta:**
```json
{
  "id": 1,
  "usuarioEmail": "usuario@email.com",
  "fecha": "2025-12-02T14:30:45.123456",
  "total": 175000,
  "estado": "PENDIENTE",
  "metodoPago": "TARJETA",
  "detalles": [...]
}
```

---

### 4. Obtener Ventas de un Usuario (GET)

**URL:** `http://localhost:8080/ventas/usuario/usuario@email.com`

**Método:** GET

**Respuesta:**
```json
[
  {
    "id": 1,
    "usuarioEmail": "usuario@email.com",
    "fecha": "2025-12-02T14:30:45.123456",
    "total": 175000,
    "estado": "PENDIENTE",
    "metodoPago": "TARJETA",
    "detalles": [...]
  }
]
```

---

### 5. Actualizar Estado de una Venta (PUT)

**URL:** `http://localhost:8080/ventas/1/estado`

**Método:** PUT

**Body (JSON):**
```json
{
  "estado": "COMPLETADA"
}
```

**Estados válidos:**
- `PENDIENTE` - Venta pendiente
- `COMPLETADA` - Venta completada
- `CANCELADA` - Venta cancelada

**Respuesta:**
```json
{
  "estado": "OK",
  "mensaje": "Estado actualizado correctamente",
  "venta": {
    "id": 1,
    "usuarioEmail": "usuario@email.com",
    "fecha": "2025-12-02T14:30:45.123456",
    "total": 175000,
    "estado": "COMPLETADA",
    "metodoPago": "TARJETA",
    "detalles": [...]
  }
}
```

---

## 🧪 TESTS RECOMENDADOS EN POSTMAN

### Test Scenario 1: Compra Simple
1. **Crear Usuario** → POST `/usuarios`
2. **Crear Productos** → POST `/productos/crear`
3. **Crear Venta** → POST `/ventas/crear`
4. **Verificar Venta** → GET `/ventas/{id}`

### Test Scenario 2: Compra Múltiple
1. **Crear Venta con varios productos** → POST `/ventas/crear`
2. **Actualizar Estado a COMPLETADA** → PUT `/ventas/{id}/estado`
3. **Listar todas las ventas del usuario** → GET `/ventas/usuario/{email}`

### Test Scenario 3: Validaciones
- Crear venta con usuario inexistente (debe fallar)
- Crear venta con producto inexistente (debe fallar)
- Actualizar estado con formato inválido (debe fallar)

---

## 🔌 PARTE 2: INTEGRACIÓN EN KOTLIN (Android)

### Paso 1: Crear las Data Classes

**Archivo:** `app/src/main/java/com/tu_app/models/VentaModels.kt`

```kotlin
package com.tu_app.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

// DTO para crear una venta
data class CreateVentaRequest(
    @SerializedName("usuarioEmail")
    val usuarioEmail: String,
    @SerializedName("metodoPago")
    val metodoPago: String,
    @SerializedName("total")
    val total: Double,
    @SerializedName("detalles")
    val detalles: List<DetalleVentaRequest>
)

// DTO para los detalles de venta en la solicitud
data class DetalleVentaRequest(
    @SerializedName("productoId")
    val productoId: Int,
    @SerializedName("cantidad")
    val cantidad: Int,
    @SerializedName("precioUnitario")
    val precioUnitario: Double
)

// Response de la venta
data class VentaResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("usuarioEmail")
    val usuarioEmail: String,
    @SerializedName("fecha")
    val fecha: String,
    @SerializedName("total")
    val total: Double,
    @SerializedName("estado")
    val estado: String,
    @SerializedName("metodoPago")
    val metodoPago: String,
    @SerializedName("detalles")
    val detalles: List<DetalleVentaResponse>
)

// Response de detalles de venta
data class DetalleVentaResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("productoId")
    val productoId: Int,
    @SerializedName("productoNombre")
    val productoNombre: String,
    @SerializedName("cantidad")
    val cantidad: Int,
    @SerializedName("precioUnitario")
    val precioUnitario: Double,
    @SerializedName("subtotal")
    val subtotal: Double
)

// Response genérico
data class ApiResponse<T>(
    @SerializedName("estado")
    val estado: String,
    @SerializedName("mensaje")
    val mensaje: String?,
    @SerializedName("venta")
    val venta: T?
)
```

---

### Paso 2: Crear la Interfaz de API

**Archivo:** `app/src/main/java/com/tu_app/api/ApiService.kt`

```kotlin
package com.tu_app.api

import com.tu_app.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Crear una venta
    @POST("ventas/crear")
    suspend fun crearVenta(@Body request: CreateVentaRequest): Response<ApiResponse<VentaResponse>>
    
    // Obtener todas las ventas
    @GET("ventas")
    suspend fun obtenerVentas(): Response<List<VentaResponse>>
    
    // Obtener venta por ID
    @GET("ventas/{id}")
    suspend fun obtenerVenta(@Path("id") id: Int): Response<VentaResponse>
    
    // Obtener ventas de un usuario
    @GET("ventas/usuario/{email}")
    suspend fun obtenerVentasUsuario(@Path("email") email: String): Response<List<VentaResponse>>
    
    // Actualizar estado de venta
    @PUT("ventas/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") id: Int,
        @Body estado: Map<String, String>
    ): Response<ApiResponse<VentaResponse>>
}
```

---

### Paso 3: Crear el Repositorio

**Archivo:** `app/src/main/java/com/tu_app/repository/VentaRepository.kt`

```kotlin
package com.tu_app.repository

import com.tu_app.api.ApiService
import com.tu_app.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VentaRepository(private val apiService: ApiService) {
    
    suspend fun crearVenta(
        usuarioEmail: String,
        metodoPago: String,
        detalles: List<DetalleVentaRequest>
    ): Result<VentaResponse> = withContext(Dispatchers.IO) {
        try {
            // Calcular total
            val total = detalles.sumOf { it.cantidad * it.precioUnitario }
            
            val request = CreateVentaRequest(
                usuarioEmail = usuarioEmail,
                metodoPago = metodoPago,
                total = total,
                detalles = detalles
            )
            
            val response = apiService.crearVenta(request)
            
            if (response.isSuccessful && response.body()?.venta != null) {
                Result.success(response.body()!!.venta!!)
            } else {
                Result.failure(Exception("Error al crear venta: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun obtenerVentasUsuario(email: String): Result<List<VentaResponse>> = 
        withContext(Dispatchers.IO) {
        try {
            val response = apiService.obtenerVentasUsuario(email)
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener ventas: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun actualizarEstado(
        ventaId: Int,
        nuevoEstado: String
    ): Result<VentaResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.actualizarEstado(ventaId, mapOf("estado" to nuevoEstado))
            
            if (response.isSuccessful && response.body()?.venta != null) {
                Result.success(response.body()!!.venta!!)
            } else {
                Result.failure(Exception("Error al actualizar estado: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

---

### Paso 4: Crear el ViewModel

**Archivo:** `app/src/main/java/com/tu_app/viewmodel/VentaViewModel.kt`

```kotlin
package com.tu_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tu_app.models.DetalleVentaRequest
import com.tu_app.models.VentaResponse
import com.tu_app.repository.VentaRepository
import kotlinx.coroutines.launch

class VentaViewModel(private val repository: VentaRepository) : ViewModel() {
    
    private val _ventaCreada = MutableLiveData<VentaResponse?>()
    val ventaCreada: LiveData<VentaResponse?> get() = _ventaCreada
    
    private val _ventasUsuario = MutableLiveData<List<VentaResponse>>()
    val ventasUsuario: LiveData<List<VentaResponse>> get() = _ventasUsuario
    
    private val _estado = MutableLiveData<String>()
    val estado: LiveData<String> get() = _estado
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    
    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> get() = _cargando
    
    fun crearVenta(
        usuarioEmail: String,
        metodoPago: String,
        detalles: List<DetalleVentaRequest>
    ) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            
            val resultado = repository.crearVenta(usuarioEmail, metodoPago, detalles)
            
            resultado.onSuccess { venta ->
                _ventaCreada.value = venta
                _estado.value = "Venta creada exitosamente"
                _cargando.value = false
            }
            
            resultado.onFailure { exception ->
                _error.value = exception.message
                _cargando.value = false
            }
        }
    }
    
    fun obtenerVentasUsuario(email: String) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            
            val resultado = repository.obtenerVentasUsuario(email)
            
            resultado.onSuccess { ventas ->
                _ventasUsuario.value = ventas
                _cargando.value = false
            }
            
            resultado.onFailure { exception ->
                _error.value = exception.message
                _cargando.value = false
            }
        }
    }
    
    fun actualizarEstado(ventaId: Int, nuevoEstado: String) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            
            val resultado = repository.actualizarEstado(ventaId, nuevoEstado)
            
            resultado.onSuccess { venta ->
                _estado.value = "Estado actualizado a: $nuevoEstado"
                _cargando.value = false
            }
            
            resultado.onFailure { exception ->
                _error.value = exception.message
                _cargando.value = false
            }
        }
    }
}
```

---

### Paso 5: Implementar en la UI (Fragment/Activity)

**Archivo:** `app/src/main/java/com/tu_app/ui/CheckoutFragment.kt`

```kotlin
package com.tu_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tu_app.databinding.FragmentCheckoutBinding
import com.tu_app.models.DetalleVentaRequest
import com.tu_app.viewmodel.VentaViewModel
import com.tu_app.viewmodel.VentaViewModelFactory

class CheckoutFragment : Fragment() {
    
    private lateinit var binding: FragmentCheckoutBinding
    private val ventaViewModel: VentaViewModel by viewModels {
        VentaViewModelFactory(repository) // Necesitas pasar tu repository
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Observar el resultado de la venta
        ventaViewModel.ventaCreada.observe(viewLifecycleOwner) { venta ->
            if (venta != null) {
                Toast.makeText(
                    requireContext(),
                    "¡Compra realizada! ID: ${venta.id}",
                    Toast.LENGTH_LONG
                ).show()
                // Navegar a la pantalla de confirmación
                navegarAConfirmacion(venta)
            }
        }
        
        // Observar errores
        ventaViewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        }
        
        // Observar estado de carga
        ventaViewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.btnComprar.isEnabled = !cargando
        }
        
        // Configurar botón de compra
        binding.btnComprar.setOnClickListener {
            realizarCompra()
        }
    }
    
    private fun realizarCompra() {
        val usuarioEmail = "usuario@email.com" // Obtener del SharedPreferences o ViewModel de usuario
        val metodoPago = "TARJETA" // O lo que seleccione el usuario
        
        // Crear detalles de venta desde el carrito
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
    
    private fun navegarAConfirmacion(venta: VentaResponse) {
        // Implementar navegación a la pantalla de confirmación
        // navController.navigate(R.id.action_checkout_to_confirmacion)
    }
}
```

---

### Paso 6: Factory para el ViewModel

**Archivo:** `app/src/main/java/com/tu_app/viewmodel/VentaViewModelFactory.kt`

```kotlin
package com.tu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tu_app.repository.VentaRepository

class VentaViewModelFactory(private val repository: VentaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VentaViewModel(repository) as T
    }
}
```

---

## 📱 FLUJO DE USO EN LA APP

### Desde el Carrito:
1. Usuario agrega productos al carrito
2. Usuario hace click en "Proceder al Pago"
3. Pantalla de Checkout muestra:
   - Lista de productos
   - Cantidad de cada uno
   - Precio unitario
   - Total
   - Opción de método de pago
4. Usuario hace click en "Comprar"
5. La app crea la venta en el backend
6. Pantalla de confirmación muestra:
   - ID de la venta
   - Estado: PENDIENTE
   - Detalles de la compra
   - Opción para ver historial de compras

### Pantalla de Historial:
1. Usuario ve lista de sus compras
2. Puede ver detalles de cada compra
3. Puede ver estado de cada venta

---

## ⚠️ CONSIDERACIONES IMPORTANTES

1. **Autenticación:** Asegúrate de obtener el email del usuario autenticado
2. **Validaciones:** 
   - Verificar que haya al menos 1 producto en la venta
   - Verificar que las cantidades sean positivas
   - Verificar que los precios sean válidos
3. **Manejo de Errores:** Mostrar mensajes claros al usuario
4. **Estado de Carga:** Desactivar botones mientras se procesa
5. **Base de Datos:** Hacer backup antes de usar en producción


