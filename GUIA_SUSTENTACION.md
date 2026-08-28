# Guía de sustentación

**¿Por qué se utilizó herencia?** Porque Administrador, Empleado y Cliente comparten los datos de una persona usuaria, pero cada uno tiene responsabilidades propias.

**¿Por qué Usuario es la clase padre?** Centraliza atributos comunes como documento, correo y contraseña, evitando repetición.

**¿Qué SOLID usa ProductoService?** Principalmente SRP porque concentra reglas de producto, y DIP porque depende de `ProductoRepository`.

**¿Dónde está Dependency Inversion?** En el constructor de `ProductoService`: recibe la interfaz `ProductoRepository`, no `MemoriaProductoRepository`.

**¿Por qué Strategy?** Permite que Pago delegue el cobro a `MetodoDePago`. Se puede agregar otro medio sin cambiar Pago.

**¿Qué es MVC?** Modelo son entidades; Vista es Swing; Controlador conecta las acciones de la vista con el servicio.

**¿Responsabilidad del Controller?** Coordinar solicitudes de la vista. **¿Del Service?** Aplicar reglas del negocio. **¿Del Repository?** Guardar y consultar datos.

**¿Por qué no hay base de datos?** Es el alcance del primer corte: usar almacenamiento temporal en memoria para concentrarse en diseño y CRUD.

**¿Cómo funciona el CRUD?** La vista crea un Producto con el formulario, el controlador llama al servicio y este usa el repositorio. Después se refresca la tabla.

**¿Cómo cambia el stock?** `Compra.registrarCompra()` suma cada detalle; `Factura.generarFactura()` resta cada detalle después de aprobar el pago.

**¿Qué ocurre si se vende más del stock?** `DetalleFactura` valida la cantidad y muestra un error de stock insuficiente.

**¿Factura vs. DetalleFactura?** Factura representa la venta completa; DetalleFactura representa cada producto y cantidad vendidos. Es composición porque los detalles pertenecen a esa factura.

**¿Cómo escalarlo?** Implementando otro `ProductoRepository` para una base de datos, nuevos medios de pago Strategy y módulos completos sin cambiar el CRUD actual.
