# Aplicación de Simulación de Cobro en Supermercado

## 📋 Validación de Requisitos

### ✅ Requisito 1: Múltiples cajeras y clientes simultáneos
**Estado:** CUMPLIDO
- Clase `Cajera` extiende `Thread`, permitiendo ejecución concurrente
- La clase `Cliente` encapsula lista de productos
- En la simulación se crean 3 cajeras ejecutándose en paralelo
- Uso de `thread.start()` y `thread.join()` para sincronización

### ✅ Requisito 2: Simulación del proceso de cobro con tiempo
**Estado:** CUMPLIDO
- Clase `Producto` define `tiempoProcesamientoSegundos`
- `Cajera.run()` usa `Thread.sleep()` para simular tiempo real de procesamiento
- Cada producto tiene su tiempo independiente
- Se acumula el tiempo por cliente

### ✅ Requisito 3: Mostrar tiempo total de cobro
**Estado:** CUMPLIDO
- Usa `System.currentTimeMillis()` para medir tiempo global
- Calcula y muestra: tiempo total de cobro (reloj real)
- Muestra tiempo por cajera/cliente
- Muestra tiempo acumulado por producto

### ✅ Requisito 4: Productos, costos y tiempos de procesamiento
**Estado:** CUMPLIDO
- Muestra nombre del producto
- Muestra precio de cada producto
- Muestra tiempo de procesamiento de cada producto
- Calcula total por cliente
- Detalla proceso paso a paso

---

## 🏗️ Arquitectura del Sistema

### Diagrama de Clases
```
┌─────────────────┐
│   Producto      │
├─────────────────┤
│ - nombre        │
│ - precio        │
│ - tiempo        │
└─────────────────┘
         △
         │
         └─ usado por
              │
         ┌────────────┐
         │  Cliente   │
         ├────────────┤
         │ - nombre   │
         │ - productos│
         └────────────┘
                △
                │
         ┌──────────────────┐
         │ Cajera (Thread)  │
         ├──────────────────┤
         │ - nombre         │
         │ - cliente        │
         │ - startTime      │
         │ - run()          │
         └──────────────────┘
```

### Clases del Sistema

#### 1. **Producto**
- **Responsabilidad:** Representar un artículo comprado
- **Atributos:**
  - `nombre`: Nombre del producto
  - `precio`: Costo unitario
  - `tiempoProcesamientoSegundos`: Tiempo de escaneo/cobro

#### 2. **Cliente**
- **Responsabilidad:** Encapsular datos del cliente y sus compras
- **Atributos:**
  - `nombre`: Identificador del cliente
  - `productos`: Lista de artículos comprados

#### 3. **Cajera** (extends Thread)
- **Responsabilidad:** Simular el proceso de cobro en paralelo
- **Atributos:**
  - `nombre`: Identificador de la cajera
  - `cliente`: Cliente a atender
  - `startTime`: Tiempo inicial para cálculos relativos
- **Proceso:**
  1. Inicia atención del cliente
  2. Itera sobre productos
  3. Simula tiempo de procesamiento con `Thread.sleep()`
  4. Acumula total y tiempo
  5. Reporta resultado final

#### 4. **SupermercadoSimulacion** (main)
- **Responsabilidad:** Orquestar la simulación
- **Proceso:**
  1. Crea productos
  2. Agrupa productos en compras (clientes)
  3. Asigna clientes a cajeras
  4. Inicia threads en paralelo
  5. Espera finalización con `join()`
  6. Reporta estadísticas globales

---

## 🔄 Flujo de Ejecución

```
INICIO
  │
  ├─→ Crear 6 productos (Arroz, Leche, Pan, Huevos, Queso, Manzanas)
  │
  ├─→ Crear 3 clientes con sus compras
  │
  ├─→ Crear 3 cajeras
  │
  ├─→ Iniciar ejecución paralela (start)
  │   │
  │   ├─ CAJERA 1 → Cliente 1 (2 productos)
  │   │ (tiempo: 3s)
  │   │
  │   ├─ CAJERA 2 → Cliente 2 (2 productos)
  │   │ (tiempo: 4s)
  │   │
  │   └─ CAJERA 3 → Cliente 3 (2 productos)
  │   (tiempo: 3s)
  │
  ├─→ Esperar a que todas terminen (join)
  │
  ├─→ Calcular tiempo total (máximo de los tiempos individuales)
  │
  └─→ FIN (Tiempo total: 4 segundos)
```

---

## 📊 Ejemplo de Salida Actual

```
📌 Cajera María comienza a atender a Cliente 3 en el tiempo 0s
📌 Cajera Luis comienza a atender a Cliente 2 en el tiempo 0s
📌 Cajera Ana comienza a atender a Cliente 1 en el tiempo 0s
  → Cajera Luis procesa Pan - Precio: $0.8
  → Cajera Ana procesa Arroz - Precio: $2.5
  → Cajera María procesa Queso - Precio: $4.5
    ✔ Pan procesado en 1s (acumulado: 1s)
  → Cajera Luis procesa Huevos - Precio: $3.0
    ✔ Queso procesado en 2s (acumulado: 2s)
    ✔ Arroz procesado en 2s (acumulado: 2s)
  → Cajera María procesa Manzanas - Precio: $2.0
  → Cajera Ana procesa Leche - Precio: $1.2
    ✔ Manzanas procesado en 1s (acumulado: 3s)
    ✔ Leche procesado en 1s (acumulado: 3s)
✅ Cajera María terminó con Cliente 3 - Total: $6.5 - Tiempo total: 3s
✅ Cajera Ana terminó con Cliente 1 - Total: $3.7 - Tiempo total: 3s
    ✔ Huevos procesado en 3s (acumulado: 4s)
✅ Cajera Luis terminó con Cliente 2 - Total: $3.8 - Tiempo total: 4s

===================================
🏁 SIMULACIÓN FINALIZADA
⏱️ Tiempo total de cobro (reloj real): 4 segundos
===================================
```

---

## 🎯 Ventajas del Diseño

1. **Concurrencia eficiente:** Múltiples cajeras procesan simultáneamente
2. **Modularidad:** Cada clase tiene responsabilidad única
3. **Extensibilidad:** Fácil agregar más cajeras/clientes
4. **Claridad:** Código legible y bien estructurado
5. **Realismo:** Simula correctamente el paralelismo real

---

## 💡 Posibles Mejoras Futuras

1. **Estadísticas avanzadas:**
   - Total recaudado en la simulación
   - Promedio de tiempo por cliente
   - Cajera más/menos eficiente

2. **Configuración dinámica:**
   - Leer número de cajeras y clientes desde entrada
   - Permitir diferentes productos y cantidades

3. **Cola de espera:**
   - Implementar cola FIFO de clientes
   - Asignar dinámicamente clientes a cajeras libres

4. **Persistencia:**
   - Guardar resultados en archivo
   - Base de datos para análisis histórico

5. **Interfaz gráfica:**
   - Visualizar cajeras atendiendo clientes
   - Gráficos de tiempo real

---

## ✨ Conclusión

La aplicación **cumple completamente con todos los requisitos** solicitados:
- ✅ Múltiples cajeras y clientes simultáneos
- ✅ Simulación del proceso de cobro con tiempos
- ✅ Tiempo total de cobro mostrado
- ✅ Detalles de productos, costos y tiempos

**La simulación está lista para usar.**
