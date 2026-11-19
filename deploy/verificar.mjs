import assert from 'node:assert/strict';
import { randomUUID } from 'node:crypto';
import { setTimeout as esperar } from 'node:timers/promises';

const { TODOMARKET_URL } = process.env;
if (!TODOMARKET_URL) {
  throw new Error('Configura TODOMARKET_URL antes de ejecutar esta prueba.');
}
const base = new URL(TODOMARKET_URL).origin;
async function pedir(ruta, status, opciones) {
  const res = await fetch(base + ruta, { signal: AbortSignal.timeout(15_000), ...opciones });
  assert.equal(res.status, status, (opciones?.method ?? 'GET') + ' ' + ruta);
  return res;
}
async function api(ruta, status, method = 'GET', data) {
  return pedir(ruta, status, { method, ...(data && {
    headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data)
  }) });
}

let listo = false;
for (let intento = 0; intento < 60 && !listo; intento++) {
  const res = await fetch(base + '/salud', { signal: AbortSignal.timeout(8_000) }).catch(() => null);
  if (res?.ok) listo = (await res.json().catch(() => null))?.listo === true;
  if (!listo) await esperar(2000);
}
assert(listo, 'No están disponibles ambos servicios y MongoDB.');
const portada = await (await pedir('/', 200)).text();
assert.doesNotMatch(portada, /href="\/acceso"/);
await pedir('/acceso', 404);
for (const interfaz of ['angular','react']) {
  assert(portada.includes('href="/' + interfaz + '/"'));
  const pagina = await (await pedir('/' + interfaz + '/', 200)).text();
  const script = pagina.match(/<script[^>]+type="module"[^>]+src="([^"]+)"|<script[^>]+src="([^"]+)"[^>]+type="module"/);
  assert(script, 'Falta el script de aplicación de ' + interfaz);
  const path = new URL(script[1] || script[2], base + '/' + interfaz + '/').pathname;
  assert(path.startsWith('/' + interfaz + '/'));
  assert.match((await pedir(path, 200)).headers.get('Content-Type'), /javascript/);
}
await pedir('/swagger-ui/index.html', 200);
const spec = await (await pedir('/v3/api-docs', 200)).json();
assert.equal(spec.servers[0].url, base);
assert(spec.paths['/api/productos'] && spec.paths['/api/usuarios']);
for (const recurso of ['productos','usuarios']) {
  assert(Array.isArray(await (await pedir('/api/' + recurso, 200)).json()));
}
const marca = randomUUID();
const producto = { nombre: 'Verificación ' + marca, stock: 4, lote: 'QA-' + marca, estado: true };
const usuario = { nombreUsuario: 'qa.' + marca, documento: 'QA-' + marca, estado: true,
  rol: { idRol: 3, nombreRol: 'Cliente', descripcion: 'Verificación' } };
const rutas = [];
try {
  const p = await (await api('/api/productos', 201, 'POST', producto)).json();
  rutas.push('/api/productos/' + p.idProducto);
  const u = await (await api('/api/usuarios', 201, 'POST', usuario)).json();
  rutas.push('/api/usuarios/' + u.idPersona);
  await pedir(rutas[0], 200);
  await pedir(rutas[1], 200);
  const p2 = await (await api(rutas[0], 200, 'PUT', { ...producto, stock: 14 })).json();
  assert.equal(p2.stock, 14);
  const u2 = await (await api(rutas[1], 200, 'PUT', { ...usuario, estado: false })).json();
  assert.equal(u2.estado, false);
  while (rutas.length) {
    const ruta = rutas.at(-1);
    await api(ruta, 204, 'DELETE');
    rutas.pop();
    await pedir(ruta, 404);
  }
} finally {
  for (const ruta of rutas) await api(ruta, 204, 'DELETE').catch(() => {});
}
console.log(JSON.stringify({ url: base, salud: 200, angular: 200, react: 200, swagger: 200,
  crudProductos: true, crudUsuarios: true }, null, 2));
