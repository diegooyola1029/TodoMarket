FROM node:22-bookworm-slim AS angular-build
WORKDIR /src
COPY web-angular/package*.json ./
RUN npm ci
COPY web-angular ./
RUN npm run build -- --base-href=/angular/ \
    && printf '%s\n' 'window.TODOMARKET_CONFIG = { apiBaseUrl: window.location.origin };' \
       > dist/todomarket-angular/browser/config.js

FROM node:22-bookworm-slim AS react-build
WORKDIR /src
COPY web-react/package*.json ./
RUN npm ci
COPY web-react ./
RUN VITE_API_BASE_URL=/ npm run build -- --base=/react/

FROM node:22-bookworm-slim
WORKDIR /app
COPY --from=angular-build /src/dist/todomarket-angular/browser/ runtime/angular/
COPY --from=react-build /src/dist/ runtime/react/
COPY deploy/servidor.mjs deploy/portada.html deploy/
USER 10001:10001
EXPOSE 10000
ENTRYPOINT ["node", "deploy/servidor.mjs"]
