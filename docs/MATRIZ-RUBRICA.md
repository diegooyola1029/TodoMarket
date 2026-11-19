# Matriz de los nueve criterios

La evidencia de integración utiliza tres procesos independientes y MongoDB. Los pesos corresponden a la hoja **Rúbrica** del archivo de evaluación entregado. Las llamadas se verifican de extremo a extremo y directamente contra cada API donde corresponde.

| Criterio | Peso | Prueba y resultado esperado | Evidencia |
| --- | ---: | --- | --- |
| 1. Crear registros | 10 % | Crear un producto y un usuario. Las dos altas devuelven 201. | Resultado de CRUD |
| 2. Listar y consultar | 10 % | Consultar listas y registros por ID. Se observa el mismo producto vía TodoMarket y vía Productos. | CRUD e integración |
| 3. Actualizar registros | 10 % | Modificar stock y estado. Las dos solicitudes devuelven 200 y los cambios se conservan. | CRUD y persistencia |
| 4. Eliminar registros | 10 % | Borrar los registros de prueba. Las bajas devuelven 204 y la consulta posterior 404. | Resultado de CRUD |
| 5. Implementación de MongoDB | 20 % | Leer el stock y el rol embebido en MongoDB después de reiniciar los procesos Java. | Persistencia y PIDs de reinicio |
| 6. Primera interfaz | 10 % | Abrir Angular y consultar un producto editado desde React. | Captura de Angular y resultado del navegador |
| 7. Segunda interfaz | 10 % | Abrir React, editar el producto y comprobar que Angular presenta su nuevo stock. | Captura de React y resultado del navegador |
| 8. Swagger | 10 % | Abrir Swagger UI de ambos servicios y el acceso de TodoMarket. Consultar las especificaciones OpenAPI. | Resultado de Swagger |
| 9. Comunicación entre microservicios | 10 % | Consultar el mismo producto por ambas APIs. Detener Productos y observar 503 en inventario y 200 en usuarios. Reiniciar Productos y confirmar recuperación. | Resultado de integración |

[Resultado de la prueba de tres repositorios](evidencias/integracion-tres-repos.json) · [Angular](evidencias/angular.png) · [React](evidencias/react.png)

La captura de Angular muestra el stock inicial de 14 y la de React muestra el mismo producto tras actualizarlo a 18. El JSON registra la consulta posterior en Angular con el valor 18.

La inspección en Compass de las colecciones y el rol embebido se realizó sobre la base de demostración de la etapa anterior. La prueba en tres repositorios comprobó de nuevo los documentos directamente en MongoDB. [Registro de Compass](evidencias/compass.md)
