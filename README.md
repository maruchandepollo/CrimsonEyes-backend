# CrimsonEyes – Backend

API REST para la gestión de recetas, productos y compras con QR

## Desarrolladores

Proyecto desarrollado por:

* **Sergio Puebla (maruchandepollo)** — [GitHub](https://github.com/maruchandepollo)
* **Matías Bórquez (Anideout)** — [GitHub](https://github.com/Anideout)

---

## Descripción

El **backend de CrimsonEyes** es una API REST creada para servir como motor principal de la aplicación móvil *CrimsonEyes-App*, una plataforma desarrollada para una óptica.

Este backend se encarga de manejar:

* Gestión de recetas ópticas
* Registro y autenticación de usuarios
* Inventario de productos
* Procesos de compra y pedidos
* Generación de datos para códigos QR (pagos o validación)
* Comunicación con la app Android mediante JSON

El objetivo es ofrecer una base robusta, segura y escalable para todas las funcionalidades del ecosistema CrimsonEyes.

---

## Tecnologías utilizadas

El backend está construido con:

* **Java / Kotlin (dependiendo del repo)**
* **Spring Boot** — Framework principal
* **Spring Web** — Controladores REST
* **Spring Data JPA** — Gestión de entidades y repositorios
* **Hibernate** — ORM
* **MySQL / PostgreSQL** — Base de datos (dependiendo de configuración)
* **Maven / Gradle** — Sistema de construcción
* **Lombok** — Simplificación de modelos (si aplica)
* **JWT o Tokens** — Para autenticación (si lo implementan)

---

## Funcionalidades principales del backend

### Gestión de recetas ópticas

* CRUD de recetas
* Asociación receta–paciente
* Estructuras de dioptrías, eje, cilindro, prisma, etc.

### Catálogo de productos

* Lista de productos disponibles
* Prendas, lentes, gafas, accesorios
* Manejo de stock disponible

### Procesos de compra

* Creación de órdenes
* Validación de productos seleccionados
* Cálculo final del pedido

### Generación y validación de QR

* Generación de datos para el QR al finalizar una compra
* Validación del QR desde el backend para confirmar pedidos

*(Esta funcionalidad está alineada con la app Android, donde se muestra y/o escanea un código QR por compra.)*

### Gestión de usuarios

* Registro e inicio de sesión
* Perfiles de usuario
* Control de permisos (si aplica)

---

## Estructura del proyecto

Una estructura típica del backend podría verse así:

```
src/
 ├── main/java/com/crimsoneyes/
 │      ├── controller/       # Controladores REST
 │      ├── service/          # Lógica de negocio
 │      ├── repository/       # Acceso a base de datos
 │      ├── model/            # Entidades JPA
 │      ├── security/         # Autenticación/Autorización (si aplica)
 │      └── CrimsonEyesApp    # Clase principal
 └── main/resources/
        ├── application.properties (o .yml) # Configuración
        ├── static/
        └── templates/
```

---

## Configuración del proyecto

Asegúrate de configurar tu archivo:

```
src/main/resources/application.properties
```

Ejemplo estándar:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crimsoneyes
spring.datasource.username=root
spring.datasource.password=tuClave

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Cómo ejecutar el backend localmente

### 1. Clonar el repositorio

```bash
git clone https://github.com/maruchandepollo/CrimsonEyes-backend.git
cd CrimsonEyes-backend
```

### 2. Configurar base de datos

* Crear una base de datos MySQL o PostgreSQL
* Ajustar credenciales en `application.properties`

### 3. Ejecutar el proyecto

Puedes iniciarlo desde tu IDE (IntelliJ / Eclipse) o por consola:

```bash
mvn spring-boot:run
```

o

```bash
./gradlew bootRun
```

### 4. Acceder a la API

Por defecto la API estará disponible en:

```
http://localhost:8080
```

---

## Conexión con la App Android

La app **CrimsonEyes-App** consume los endpoints de este backend mediante JSON.

Algunas funciones clave utilizadas por la app:

* Obtener recetas
* Crear compras
* Recuperar productos
* Generar información para QR
* Validar pedidos mediante QR

---
