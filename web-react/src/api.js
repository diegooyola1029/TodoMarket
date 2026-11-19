const base = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8184').replace(/\/+$/, '');

async function solicitud(seccion, { id, method = 'GET', datos } = {}) {
  const url = base + '/api/' + seccion + (id === undefined ? '' : '/' + id);
  let respuesta;
  try {
    respuesta = await fetch(url, {
      method,
      signal: AbortSignal.timeout(10_000),
      ...(datos && { headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(datos) })
    });
  } catch {
    throw new Error('El servicio no responde. Comprueba la conexión y vuelve a intentarlo.');
  }
  if (!respuesta.ok) {
    const error = await respuesta.json().catch(() => null);
    throw new Error(error?.message || 'No se pudo completar la solicitud (HTTP ' + respuesta.status + ').');
  }
  if (respuesta.status !== 204) return respuesta.json();
}

export const api = {
  listar: (seccion) => solicitud(seccion),
  buscar: (seccion, id) => solicitud(seccion, { id }),
  guardar: (seccion, id, datos) => solicitud(seccion, {
    id, datos, method: id === undefined ? 'POST' : 'PUT'
  }),
  eliminar: (seccion, id) => solicitud(seccion, { id, method: 'DELETE' })
};
