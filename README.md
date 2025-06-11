# 🧪 Prueba Técnica Backend - Sistema de Pedidos con Microservicios y RabbitMQ

Este proyecto implementa una solución distribuida para la recepción, validación de stock y despacho de pedidos en una tienda online. Se compone de tres microservicios independientes que se comunican de forma asíncrona mediante **RabbitMQ**.

---

## 📦 Microservicios

### 1. `order-service`
- Recibe y crea pedidos.
- Envía las órdenes al `inventory-service` mediante `order.queue`.
- Escucha respuestas del inventario en `inventory.response.queue`.
- Escucha confirmaciones de despacho en `delivery.response.queue`.
- Permite consultar todos los pedidos o uno por ID.

### 2. `inventory-service`
- Escucha pedidos desde `order.queue`.
- Simula validación aleatoria del stock (aceptado o rechazado).
- Responde al `order-service` mediante `inventory.response.queue`.
- Si el inventario aprueba, reenvía el pedido a `delivery-service` mediante `delivery.request.queue`.

### 3. `delivery-service`
- Escucha pedidos desde `delivery.request.queue`.
- Simula creación de despacho.
- Responde al `order-service` mediante `delivery.response.queue`.

---

## ⚙️ Requisitos Técnicos

- Java 17
- Spring Boot 3
- Maven
- RabbitMQ (localhost:5672, guest/guest)
- H2 Database (en `order-service`)

---

## 🚀 Instrucciones de Ejecución

### 1. Clona el repositorio

```bash
git clone https://github.com/tu-usuario/test-backend.git
cd test-backend
```

---

### 2. Levanta RabbitMQ con Docker

```bash
docker run -d --hostname rabbitmq --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Accede al panel en: [http://localhost:15672](http://localhost:15672)  
**Usuario**: `guest`  
**Contraseña**: `guest`

---

### 3. Ejecuta los Microservicios

En tres terminales separadas, ejecuta:

```bash
# Terminal 1
cd order-service
mvn spring-boot:run
```

```bash
# Terminal 2
cd inventory-service
mvn spring-boot:run
```

```bash
# Terminal 3
cd delivery-service
mvn spring-boot:run
```

---

## 🧪 Prueba del Flujo

### Crear un Pedido

Realiza una petición:

```http
POST http://localhost:8081/orders
```

**Body:**

```json
{
  "product": "Laptop",
  "quantity": 2
}
```

---

### Resultado del Proceso

1. El pedido es creado por `order-service`.  
2. Se envía a `inventory-service` para validar stock.  
3. Si hay stock, se envía a `delivery-service`.  
4. Finalmente, se actualiza el estado del pedido en `order-service`.

**Posibles Estados del Pedido:**
- `REJECTED` → Si el inventario lo rechaza.  
- `DELIVERY_CREATED` → Si se despacha exitosamente.

---

## 📥 Endpoints Disponibles (`order-service`)

| Método | Endpoint         | Descripción               |
|--------|------------------|---------------------------|
| POST   | `/orders`        | Crea un nuevo pedido      |
| GET    | `/orders`        | Consulta todos los pedidos|
| GET    | `/orders/{id}`   | Consulta un pedido por ID |

---

## 📁 Estructura del Proyecto

```
/test-backend
│
├── delivery-service      # Servicio de despacho
├── inventory-service     # Servicio de inventario
├── order-service         # Servicio de pedidos
├── docker-compose        # Archivos auxiliares
└── README.md             # Documentación del proyecto
```

---

## 👤 Autor

**Michael Pachón**  
GitHub: [@mspachon112](https://github.com/mspachon112)