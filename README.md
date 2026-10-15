# TodoMarket

TodoMarket incluye dos microservicios REST en Spring Boot, una interfaz web Angular y la aplicación Swing existente.

## Aplicación web

El código de la aplicación está en [`TODOMARKET_SEGUNDOCORTE/web-angular`](./TODOMARKET_SEGUNDOCORTE/web-angular).

1. Inicia `inventory-service` en el puerto `8183`.
2. Inicia `todomarket-backend` en el puerto `8184`.
3. En otra terminal, ejecuta:

   ```powershell
   cd TODOMARKET_SEGUNDOCORTE/web-angular
   npm install
   npm start
   ```

4. Abre <http://localhost:4200>.

La interfaz permite crear, consultar, editar y eliminar productos y usuarios. Los IDs se generan en los microservicios. Para más detalles de módulos, endpoints y ejecución, consulta [`TODOMARKET_SEGUNDOCORTE/README.md`](./TODOMARKET_SEGUNDOCORTE/README.md).
