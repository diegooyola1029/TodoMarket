# Publicación de los tres repositorios

Cada ZIP corresponde a una raíz de repositorio. Usa una rama de entrega en cada repositorio y deja en esa rama el contenido de su ZIP. Conserva el historial del segundo corte en las ramas que ya tienen. Cada raíz contiene su propio `Dockerfile` y su propio `render.yaml`. La URL que se comparte con el profesor es la que Render asigna a **TodoMarket**.

1. Crea o selecciona una base de evaluación `todomarket` en MongoDB Atlas. Usa datos ficticios. Autoriza en Atlas los rangos de salida de Productos y Usuarios que muestre Render. [Integración de Render y Atlas](https://render.com/docs/connect-to-mongodb-atlas)
2. Publica Productos desde su repositorio y configura `MONGODB_URI` y un `SERVICE_TOKEN` privado. Anota el origen HTTPS del servicio.
3. Publica Usuarios desde su repositorio. Configura `MONGODB_URI`, su propio `SERVICE_TOKEN`, e `INVENTORY_URL` con el origen de Productos. Asigna `INVENTORY_TOKEN` al mismo valor de `SERVICE_TOKEN` de Productos.
4. Publica TodoMarket desde su repositorio. Configura `USUARIOS_URL` con el origen HTTPS de Usuarios y `USUARIOS_TOKEN` con su credencial de servicio. Guarda las URI y credenciales de servicio en Render. [Variables de Render](https://render.com/docs/configure-environment-variables)
5. Abre la URL de TodoMarket y las rutas `/angular/`, `/react/` y `/swagger-ui/index.html`. Puedes probar el CRUD directamente desde la URL pública con registros ficticios de evaluación.

| Salto | Protocolo | Verificación |
| --- | --- | --- |
| Navegador → TodoMarket | HTTPS | La portada y las dos interfaces cargan. |
| TodoMarket → Usuarios | HTTPS | Usuarios responde a la credencial interna. |
| Usuarios → Productos | HTTPS | Inventario acepta la credencial de Usuarios y devuelve el producto. |
| Productos y Usuarios → Atlas | MongoDB sobre TLS | Se consultan los registros persistidos. |

Para verificar la URL publicada, define `TODOMARKET_URL` en el entorno y ejecuta `node deploy/verificar.mjs` dentro de TodoMarket. El verificador comprueba la salud, las interfaces, Swagger y el CRUD en ambos servicios. Borra los registros de prueba al terminar. Las instancias gratuitas pueden tardar en reactivarse si estuvieron inactivas. [Plan gratuito de Render](https://render.com/docs/free)
