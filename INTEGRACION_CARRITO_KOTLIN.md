# 🛒 EJEMPLO PRÁCTICO: Integración Carrito → Venta en Kotlin

## 📝 Supuesto: Tienes un Carrito y quieres hacer una Compra

### Estructura de Datos Existente (Carrito)

```kotlin
// Lo que ya tienes
data class ItemCarrito(
    val id: Int,
    val producto: Producto,
    val cantidad: Int,
    val carritoId: Int
)

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val stock: Int,
    val categoria: String
)
```

---

## 🔌 PASO A PASO: Implementar Compra desde Carrito

### 1️⃣ Agregar al `application/build.gradle`

```gradle
dependencies {
    // Ya deberías tener estas:
    implementation 'com.squareup.retrofit2:retrofit:2.11.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.11.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    
    // Agregar si no tienes:
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
}
```

---

### 2️⃣ Crear los Modelos de Venta

**Archivo**: `app/src/main/java/com/tu_app/models/VentaModels.kt`

```kotlin
package com.tu_app.models

import com.google.gson.annotations.SerializedName

// Para CREAR una venta
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

// Detalle de venta para enviar
data class DetalleVentaRequest(
    @SerializedName("productoId")
    val productoId: Int,
    
    @SerializedName("cantidad")
    val cantidad: Int,
    
    @SerializedName("precioUnitario")
    val precioUnitario: Double
)

// Respuesta del servidor
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

// Detalle en respuesta
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

// Response genérico del API
data class ApiResponse<T>(
    @SerializedName("estado")
    val estado: String,

    @SerializedName("mensaje")
    val mensaje: String? = null,

    @SerializedName("venta")
    val venta: T? = null
)
```

---

### 3️⃣ Crear la Interfaz API

**Archivo**: `app/src/main/java/com/tu_app/api/ApiService.kt`

```kotlin
package com.tu_app.api

import com.tu_app.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("ventas/crear")
    suspend fun crearVenta(
        @Body request: CreateVentaRequest
    ): Response<ApiResponse<VentaResponse>>
    
    @GET("ventas/usuario/{email}")
    suspend fun obtenerVentasUsuario(
        @Path("email") email: String
    ): Response<List<VentaResponse>>
    
    @GET("ventas/{id}")
    suspend fun obtenerVenta(
        @Path("id") id: Int
    ): Response<VentaResponse>
}
```

---

### 4️⃣ Actualizar el Repositorio Existente

**Archivo**: `app/src/main/java/com/tu_app/repository/CarritoRepository.kt`

Agrega estos métodos al repositorio de carrito existente:

```kotlin
// En la clase CarritoRepository, agregar:

    suspend fun procesarCompra(
        email: String,
        metodoPago: String
    ): Result<VentaResponse> = withContext(Dispatchers.IO) {
        try {
            // 1. Obtener items del carrito del usuario
            val itemsCarrito = apiService.obtenerCarritoDelUsuario(email)
            
            if (itemsCarrito.isEmpty()) {
                return@withContext Result.failure(
                    Exception("El carrito está vacío")
                )
            }
            
            // 2. Convertir items del carrito a detalles de venta
            val detalles = itemsCarrito.map { item ->
                DetalleVentaRequest(
                    productoId = item.producto.id,
                    cantidad = item.cantidad,
                    precioUnitario = item.producto.precio.toDouble()
                )
            }
            
            // 3. Calcular total
            val total = detalles.sumOf { 
                it.cantidad * it.precioUnitario 
            }
            
            // 4. Crear request de venta
            val ventaRequest = CreateVentaRequest(
                usuarioEmail = email,
                metodoPago = metodoPago,
                total = total,
                detalles = detalles
            )
            
            // 5. Enviar al servidor
            val response = apiService.crearVenta(ventaRequest)
            
            if (response.isSuccessful && response.body()?.venta != null) {
                // 6. Si es exitoso, limpiar el carrito
                limpiarCarrito(email)
                
                Result.success(response.body()!!.venta!!)
            } else {
                Result.failure(
                    Exception("Error al crear venta: ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun limpiarCarrito(email: String) {
        // Limpiar los items del carrito después de la compra
        apiService.limpiarCarrito(email)
    }
```

---

### 5️⃣ Crear ViewModel para el Checkout

**Archivo**: `app/src/main/java/com/tu_app/viewmodel/CheckoutViewModel.kt`

```kotlin
package com.tu_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tu_app.models.DetalleVentaRequest
import com.tu_app.models.VentaResponse
import com.tu_app.repository.CarritoRepository
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val carritoRepository: CarritoRepository,
    private val usuarioEmail: String
) : ViewModel() {
    
    // Estados
    private val _compraExitosa = MutableLiveData<VentaResponse?>()
    val compraExitosa: LiveData<VentaResponse?> get() = _compraExitosa
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    
    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> get() = _cargando
    
    private val _total = MutableLiveData<Double>()
    val total: LiveData<Double> get() = _total
    
    // Procesar la compra
    fun procesarCompra(metodoPago: String) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            
            val resultado = carritoRepository.procesarCompra(
                email = usuarioEmail,
                metodoPago = metodoPago
            )
            
            resultado.onSuccess { venta ->
                _compraExitosa.value = venta
                _total.value = venta.total
                _cargando.value = false
            }
            
            resultado.onFailure { exception ->
                _error.value = exception.message ?: "Error desconocido"
                _cargando.value = false
            }
        }
    }
}
```

---

### 6️⃣ Factory del ViewModel

**Archivo**: `app/src/main/java/com/tu_app/viewmodel/CheckoutViewModelFactory.kt`

```kotlin
package com.tu_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tu_app.repository.CarritoRepository

class CheckoutViewModelFactory(
    private val repository: CarritoRepository,
    private val usuarioEmail: String
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CheckoutViewModel(repository, usuarioEmail) as T
    }
}
```

---

### 7️⃣ Fragment de Checkout

**Archivo**: `app/src/main/java/com/tu_app/ui/CheckoutFragment.kt`

```kotlin
package com.tu_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tu_app.databinding.FragmentCheckoutBinding
import com.tu_app.models.ItemCarrito
import com.tu_app.repository.CarritoRepository
import com.tu_app.viewmodel.CheckoutViewModel
import com.tu_app.viewmodel.CheckoutViewModelFactory

class CheckoutFragment : Fragment() {
    
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var checkoutViewModel: CheckoutViewModel
    private var metodoPagoSeleccionado: String = ""
    private var usuarioEmail: String = ""
    private var items: List<ItemCarrito> = emptyList()
    
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
        
        // Obtener datos del Bundle o SharedPreferences
        usuarioEmail = obtenerEmailUsuario() // Implementar según tu lógica
        items = obtenerItemsDelCarrito()      // Implementar según tu lógica
        
        // Inicializar ViewModel
        val repository = CarritoRepository(/* tu apiService */)
        val factory = CheckoutViewModelFactory(repository, usuarioEmail)
        checkoutViewModel = viewModels(factory = factory)
        
        // Mostrar resumen del carrito
        mostrarResumenCarrito()
        
        // Configurar observadores
        setupObservadores()
        
        // Configurar botones
        configurarBotones()
    }
    
    private fun mostrarResumenCarrito() {
        // Mostrar lista de items
        binding.recyclerViewItems.adapter = ItemCarritoAdapter(items)
        
        // Calcular y mostrar total
        val total = items.sumOf { 
            it.cantidad * it.producto.precio 
        }
        binding.textViewTotal.text = "Total: $$total"
    }
    
    private fun setupObservadores() {
        // Compra exitosa
        checkoutViewModel.compraExitosa.observe(viewLifecycleOwner) { venta ->
            if (venta != null) {
                Toast.makeText(
                    requireContext(),
                    "¡Compra realizada! ID: ${venta.id}",
                    Toast.LENGTH_LONG
                ).show()
                
                // Navegar a pantalla de confirmación
                val bundle = Bundle().apply {
                    putInt("ventaId", venta.id)
                    putDouble("total", venta.total)
                }
                findNavController().navigate(
                    R.id.action_checkout_to_confirmacion,
                    bundle
                )
            }
        }
        
        // Errores
        checkoutViewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Error: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        
        // Cargando
        checkoutViewModel.cargando.observe(viewLifecycleOwner) { cargando ->
            binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
            binding.btnComprar.isEnabled = !cargando
        }
    }
    
    private fun configurarBotones() {
        // Selector de método de pago
        binding.radioGroupMetodoPago.setOnCheckedChangeListener { _, checkedId ->
            metodoPagoSeleccionado = when (checkedId) {
                R.id.radioBtnTarjeta -> "TARJETA"
                R.id.radioBtnTransferencia -> "TRANSFERENCIA"
                else -> ""
            }
        }
        
        // Botón de compra
        binding.btnComprar.setOnClickListener {
            if (metodoPagoSeleccionado.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona un método de pago",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            
            checkoutViewModel.procesarCompra(metodoPagoSeleccionado)
        }
        
        // Botón de cancelar
        binding.btnCancelar.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    
    private fun obtenerEmailUsuario(): String {
        // Obtener del SharedPreferences o de tu sistema de autenticación
        val sharedPref = requireContext().getSharedPreferences("usuario", 0)
        return sharedPref.getString("email", "") ?: ""
    }
    
    private fun obtenerItemsDelCarrito(): List<ItemCarrito> {
        // Obtener los items del carrito del ViewModel de carrito
        // Aquí es solo ejemplo, adapta según tu implementación
        return emptyList()
    }
}
```

---

## 📱 Layout XML para Checkout

**Archivo**: `app/src/main/res/layout/fragment_checkout.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">
    
    <!-- Título -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Resumen de Compra"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp" />
    
    <!-- Lista de Items -->
    <RecyclerView
        android:id="@+id/recyclerViewItems"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_marginBottom="16dp" />
    
    <!-- Total -->
    <TextView
        android:id="@+id/textViewTotal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Total: $0"
        android:textSize="20sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp" />
    
    <!-- Método de Pago -->
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Método de Pago"
        android:textSize="16sp"
        android:textStyle="bold"
        android:layout_marginBottom="8dp" />
    
    <RadioGroup
        android:id="@+id/radioGroupMetodoPago"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp">
        
        <RadioButton
            android:id="@+id/radioBtnTarjeta"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Tarjeta de Crédito/Débito" />
        
        <RadioButton
            android:id="@+id/radioBtnTransferencia"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Transferencia Bancaria" />
    </RadioGroup>
    
    <!-- Progress Bar -->
    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:visibility="gone"
        android:layout_marginBottom="16dp" />
    
    <!-- Botones -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:spacing="8dp">
        
        <Button
            android:id="@+id/btnComprar"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="Comprar"
            android:background="@color/green" />
        
        <Button
            android:id="@+id/btnCancelar"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="Cancelar"
            android:background="@color/gray" />
    </LinearLayout>
</LinearLayout>
```

---

## ✅ Flujo Final Completo

```
Usuario en Carrito
    ↓
Click en "Proceder al Pago"
    ↓
CheckoutFragment se abre
    ↓
Muestra resumen de productos
    ↓
Usuario selecciona método de pago
    ↓
Click en "Comprar"
    ↓
CheckoutViewModel.procesarCompra()
    ↓
CarritoRepository.procesarCompra()
    ↓
Se convierte ItemCarrito → DetalleVentaRequest
    ↓
POST /ventas/crear
    ↓
Backend crea Venta + DetalleVenta
    ↓
Retorna VentaResponse
    ↓
Se limpia el carrito
    ↓
Navega a ConfirmacionFragment
    ↓
Muestra ID de compra y detalles
```

---

## 🎯 Resumen

Con esta implementación tienes:

✅ Carrito funcional  
✅ Checkout integrado  
✅ Conexión con backend de ventas  
✅ Validaciones  
✅ Manejo de errores  
✅ Interfaz de usuario completa  
✅ Limpieza automática del carrito tras la compra  

**¡Tu aplicación ya permite a los usuarios comprar! 🛍️**

