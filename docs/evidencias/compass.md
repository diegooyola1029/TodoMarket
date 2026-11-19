# Inspección en MongoDB Compass

Comprobación visual del 18 de noviembre de 2026 en MongoDB Compass 1.53.2. La conexión usada para la prueba aislada fue `mongodb://127.0.0.1:27029/todomarket_demo`. El lanzador de la entrega inicia su propia instancia en el puerto 27028.

- En el panel de la base se observaron las colecciones `productos`, `secuencias` y `usuarios`.
- En `productos`, la vista **Documents** mostró los registros **Arroz blanco 500 g** (`TM-DEMO-01`), **Leche entera 1 L** (`TM-DEMO-02`) y **Jabón de tocador** (`TM-DEMO-03`). La vista mostró los campos `stock`, `stockMinimo`, `lote` y el arreglo `categorias`.
- En `usuarios`, la vista **Documents** mostró `operadora.demo` con documento `TM-DEMO-U01` y `cliente.demo` con documento `TM-DEMO-U02`. Al expandir el objeto `rol` de `operadora.demo`, Compass mostró `idRol: 2`, `nombreRol: Empleado` y `descripcion: Cajero`.

La inspección se hizo sobre datos ficticios. [Valores consultados directamente en MongoDB](mongodb.json)
