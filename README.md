# TodoMarket · Tercer corte

Repositorio de las interfaces de la entrega. Contiene Angular, React y el cliente Swing existente. Las interfaces consumen las operaciones de Usuarios y Productos por HTTP. La API de Usuarios reenvía las solicitudes de inventario al microservicio de Productos.

| Repositorio | Responsabilidad | Dirección local |
| --- | --- | --- |
| [todomarket-productos](https://github.com/diegooyola1029/todomarket-productos) | CRUD de productos, alertas, persistencia y Swagger | `http://localhost:8183` |
| [todomarket-usuarios](https://github.com/diegooyola1029/todomarket-usuarios) | CRUD de usuarios, persistencia, Swagger y cliente HTTP de Productos | `http://localhost:8184` |
| [TodoMarket](https://github.com/diegooyola1029/TodoMarket) | Interfaces Angular, React y Swing | Angular `4200` y React `5173` |

## Demostración local

1. Extrae los tres ZIP en carpetas hermanas.
2. Inicia MongoDB en el puerto 27017. Los dos servicios usan por defecto la base `todomarket`.
3. Abre Productos y Usuarios como proyectos Maven independientes en IntelliJ, o ejecuta `mvn spring-boot:run` dentro de cada repositorio. Inicia primero Productos.
4. En `web-angular` y `web-react` abre terminales separadas y ejecuta `npm ci` y `npm start` en cada una. Usa una versión de Node compatible con Angular 20, como Node 22.12 o posterior en la rama 22.
5. Visita [Angular](http://localhost:4200), [React](http://localhost:5173) y [Swagger de Usuarios](http://localhost:8184/swagger-ui/index.html). Para cargar datos ficticios ejecuta `node scripts/cargar-demo.mjs` desde TodoMarket. Para ver el inventario directo abre [Swagger de Productos](http://localhost:8183/swagger-ui/index.html).
6. En MongoDB Compass conecta `mongodb://127.0.0.1:27017/todomarket` para ver las colecciones de la demostración.

El cliente Swing se abre como proyecto Maven independiente desde `todomarket-desktop/pom.xml`. Ejecuta la clase `minimercado.Main` con la API de Usuarios disponible en el puerto 8184.

La configuración de publicación de TodoMarket sirve las dos interfaces y reenvía solicitudes HTTPS a Usuarios. El despliegue se prepara por repositorio y cada componente conserva su propio build. [Publicación en Render](docs/PUBLICACION.md) · [Matriz de evaluación](docs/MATRIZ-RUBRICA.md)
