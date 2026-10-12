# TodoMarket - Segundo corte

Aplicación Java 17 organizada en tres módulos Maven. La interfaz de escritorio administra usuarios y productos; los servicios Spring Boot reciben las operaciones por API REST.

## Módulos

- `todomarket-desktop`: aplicación Swing y cliente REST.
- `todomarket-backend`: API general para usuarios y productos.
- `inventory-service`: catálogo, existencias y alertas de productos.

## Abrir en IntelliJ IDEA

1. Abre esta carpeta con **File → Open**.
2. Espera a que IntelliJ importe el `pom.xml` raíz y selecciona un JDK 17.
3. Ejecuta las configuraciones compartidas en este orden:
   - `1 Inventario 8183`
   - `2 Backend 8184`
   - `3 TodoMarket escritorio`
4. Deja activos los dos servicios mientras utilizas la aplicación.

Las configuraciones están guardadas en `.run`. Si aparece un error de conexión, revisa que ambos servicios hayan iniciado correctamente.

## Direcciones y rutas

- API general: `http://localhost:8184`
- Servicio de inventario: `http://localhost:8183`
- Usuarios: `/api/usuarios`
- Productos: `/api/productos`
- Consultas de inventario: `/api/productos/stock-bajo`, `/agotados`, `/vencidos`, `/proximos-a-vencer` y `/alertas`

El backend general recibe las solicitudes de productos y se comunica con el servicio de inventario por HTTP.

## Datos de ejemplo

Al iniciar, se cargan tres usuarios y cinco productos. Los repositorios actuales guardan los datos en memoria; al detener un servicio, sus cambios se pierden y vuelven a cargarse los datos de ejemplo.
