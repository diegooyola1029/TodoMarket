# TodoMarket

Aplicación de gestión con dos microservicios Spring Boot y una interfaz web Angular. La interfaz de escritorio Swing existente se conserva.

## Módulos

- `inventory-service`: microservicio REST de productos, existencias y alertas (puerto 8183).
- `todomarket-backend`: microservicio REST de usuarios (puerto 8184); también conserva el proxy de productos usado por Swing.
- `web-angular`: interfaz web para gestionar productos y usuarios.
- `todomarket-desktop`: aplicación Swing existente.

## Ejecutar los servicios

Desde esta carpeta, inicia cada módulo en una terminal distinta:

```powershell
cd inventory-service
mvn spring-boot:run
```

```powershell
cd todomarket-backend
mvn spring-boot:run
```

## Ejecutar Angular

Con ambos servicios activos, inicia el frontend:

```powershell
cd web-angular
npm install
npm start
```

Abre `http://localhost:4200`. Angular consume usuarios desde `http://localhost:8184/api/usuarios` y productos desde `http://localhost:8183/api/productos`.

## Funcionalidades

- Crear, listar, editar, buscar y eliminar productos y usuarios.
- Los IDs se asignan automáticamente en cada servicio; no se solicitan en los formularios de creación.
- Los productos incluyen categorías, precios, stock, lote y fecha de vencimiento.
- Los usuarios incluyen documento, contacto, rol y estado.

Los repositorios actuales mantienen los datos en memoria. Al reiniciar cada servicio, se vuelven a cargar los datos de ejemplo y se reinicia la secuencia de IDs. La configuración CORS permite el origen local de Angular (`http://localhost:4200`).

## Ejecutar la aplicación Swing en IntelliJ IDEA

Abre esta carpeta, selecciona un JDK 17 o superior y usa las configuraciones en `.run` en este orden:

1. `1 Inventario 8183`
2. `2 Backend 8184`
3. `3 TodoMarket escritorio`

Mantén activos los dos servicios mientras usas la aplicación.
