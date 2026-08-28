# Sistema de Gestión para un Minimercado

Aplicación universitaria de escritorio para administrar el inventario de un único minimercado. Su objetivo es demostrar POO, CRUD, MVC, SOLID, Repository y Strategy sin base de datos ni frameworks.

## Tecnologías y ejecución

Java (JDK 17 o superior), Swing, `ArrayList` y `LocalDate`. Desde la raíz del proyecto:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName)
java -cp out minimercado.Main
```

## Arquitectura

- `modelo`: entidades del dominio. `Usuario` es padre de `Administrador`, `Empleado` y `Cliente`.
- `repositorio`: `ProductoRepository` es la abstracción y `MemoriaProductoRepository` guarda en un `ArrayList`.
- `servicio`: reglas de negocio, validaciones, alertas y CRUD de productos.
- `controlador`: coordina la vista con el servicio.
- `vista`: componentes Swing, sin reglas de negocio.

El CRUD de Productos permite crear, listar, seleccionar, actualizar y eliminar con confirmación. Incluye productos de prueba, detección de agotados, stock bajo y vencimiento dentro de 30 días.

## SOLID y patrones

SRP se aprecia en la separación modelo/repositorio/servicio/controlador. DIP: `ProductoService` recibe `ProductoRepository`, no una implementación concreta. LSP: las subclases se pueden usar como `Usuario`. ISP: las interfaces son pequeñas. OCP se demuestra en `MetodoDePago`: para añadir, por ejemplo, PagoNequi, se crea otra implementación sin modificar `Pago`.

`PagoEfectivo`, `PagoTarjeta` y `PagoTransferencia` implementan la estrategia `MetodoDePago`. `Factura` compone detalles y un pago; una venta aprobada disminuye stock y no permite superar el disponible. `Compra` compone sus detalles y al registrarse aumenta stock.

## Limitaciones y mejoras

Los datos se pierden al cerrar la aplicación: no hay base de datos, autenticación ni facturación electrónica. Posibles mejoras: persistencia, proveedores, autenticación, sucursales y reportes avanzados.
