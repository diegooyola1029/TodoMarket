import { createReadStream } from 'node:fs';
import { stat } from 'node:fs/promises';
import { createServer, request as solicitarHttp } from 'node:http';
import { request as solicitarHttps } from 'node:https';
import { extname, isAbsolute, join, relative, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const dirDeploy = fileURLToPath(new URL('.', import.meta.url));
const runtime = resolve(process.env.TODOMARKET_RUNTIME_DIR || join(dirDeploy, '..', 'runtime'));
const { USUARIOS_TOKEN } = process.env;
const puerto = Number(process.env.PORT || 10000);
const hostPublico = process.env.BIND_HOST || '0.0.0.0';
const hostInterno = '127.0.0.1';
const origen = new URL(process.env.TODOMARKET_PUBLIC_URL || process.env.RENDER_EXTERNAL_URL ||
  'http://' + hostInterno + ':' + puerto);
const usuarios = new URL(process.env.USUARIOS_URL || 'http://127.0.0.1:8184');
if (!['http:', 'https:'].includes(usuarios.protocol) ||
    usuarios.username || usuarios.password || usuarios.pathname !== '/' || usuarios.search || usuarios.hash) {
  throw new Error('USUARIOS_URL debe ser un origen HTTP o HTTPS.');
}
const solicitar = usuarios.protocol === 'https:' ? solicitarHttps : solicitarHttp;
const servicioHeaders = USUARIOS_TOKEN ? { 'X-TodoMarket-Token': USUARIOS_TOKEN } : {};

const mime = {
  '.html': 'text/html; charset=utf-8', '.css': 'text/css; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8', '.ico': 'image/x-icon',
  '.json': 'application/json', '.png': 'image/png', '.svg': 'image/svg+xml', '.woff2': 'font/woff2'
};

function json(res, status, objeto) {
  if (!res.headersSent) res.writeHead(status, { 'Content-Type': 'application/json; charset=utf-8' });
  res.end(JSON.stringify(objeto));
}

async function archivo(req, res, base, ruta) {
  const visible = ruta === '/' || ruta.endsWith('/') ? ruta + 'index.html' : ruta;
  const local = resolve(base, '.' + visible);
  const rel = relative(base, local);
  if (!rel || rel.startsWith('..') || isAbsolute(rel)) {
    json(res, 404, { message: 'Ruta no encontrada.' });
    return;
  }
  let info;
  try { info = await stat(local); } catch {}
  if (!info?.isFile()) {
    json(res, 404, { message: 'Archivo no encontrado.' });
    return;
  }
  res.writeHead(200, {
    'Content-Type': mime[extname(local)] || 'application/octet-stream',
    'Content-Length': info.size, 'Cache-Control': 'no-cache'
  });
  if (req.method === 'HEAD') res.end();
  else createReadStream(local).on('error', () => res.destroy()).pipe(res);
}

function alBackend(req, res, url) {
  const headers = {
    ...servicioHeaders,
    'X-Forwarded-Host': origen.host,
    'X-Forwarded-Proto': origen.protocol.slice(0, -1),
    'X-Forwarded-Port': origen.port || (origen.protocol === 'https:' ? '443' : '80')
  };
  for (const nombre of ['accept', 'content-type', 'content-length']) {
    if (req.headers[nombre]) headers[nombre] = req.headers[nombre];
  }
  if (Number(headers['content-length']) > 65536) {
    json(res, 413, { message: 'El cuerpo supera 64 KiB.' });
    return;
  }
  const destino = new URL(usuarios);
  destino.pathname = url.pathname;
  destino.search = url.search;
  const request = solicitar(destino, { method: req.method, headers, timeout: 12000 }, (response) => {
    for (const nombre of ['content-type', 'content-length', 'location', 'cache-control', 'etag', 'last-modified']) {
      if (response.headers[nombre]) res.setHeader(nombre, response.headers[nombre]);
    }
    res.writeHead(response.statusCode);
    response.on('error', () => res.destroy()).pipe(res);
  });
  request.on('timeout', () => request.destroy());
  request.on('error', () => {
    if (res.headersSent) res.destroy();
    else json(res, 503, { message: 'No se pudo conectar con la API de Usuarios.' });
  });
  req.on('aborted', () => request.destroy()).pipe(request);
}

async function saludable(res) {
  const consultas = await Promise.all(['/api/productos', '/api/usuarios'].map((ruta) =>
    fetch(new URL(ruta, usuarios), {
      method: 'HEAD', headers: servicioHeaders, signal: AbortSignal.timeout(2600)
    }).catch(() => null)
  ));
  const listo = consultas.every((r) => r?.ok);
  json(res, listo ? 200 : 503, { listo });
}

const servidor = createServer((req, res) => {
  res.setHeader('X-Content-Type-Options', 'nosniff');
  const leer = ['GET', 'HEAD'].includes(req.method);
  async function manejar() {
    const url = new URL(req.url, origen);
    const ruta = decodeURIComponent(url.pathname);
    if (ruta === '/salud' && leer) return saludable(res);
    if (ruta.startsWith('/api/')) return alBackend(req, res, url);
    if (!leer) return json(res, 405, { message: 'Método no permitido.' });
    if (ruta === '/swagger-ui.html' || ruta.startsWith('/swagger-ui/') ||
        ruta === '/v3/api-docs' || ruta.startsWith('/v3/api-docs/')) {
      return alBackend(req, res, url);
    }
    if (ruta === '/angular' || ruta === '/react') {
      res.writeHead(308, { Location: ruta + '/' }).end();
      return;
    }
    if (ruta === '/') return archivo(req, res, dirDeploy, '/portada.html');
    const interfaz = ruta.startsWith('/angular/') ? 'angular' : ruta.startsWith('/react/') ? 'react' : null;
    if (interfaz) {
      return archivo(req, res, join(runtime, interfaz), ruta.slice(interfaz.length + 1));
    }
    return json(res, 404, { message: 'Ruta no encontrada.' });
  }
  manejar().catch(() => {
    if (res.headersSent) res.destroy();
    else json(res, 400, { message: 'Solicitud inválida.' });
  });
});

function apagar() {
  servidor.close();
  servidor.closeAllConnections();
}
process.on('SIGTERM', apagar);
process.on('SIGINT', apagar);
servidor.on('error', (error) => {
  console.error(error.message);
  process.exitCode = 1;
});
servidor.listen(puerto, hostPublico, () => console.log('TodoMarket · puerto ' + puerto));
