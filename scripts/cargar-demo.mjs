const base = (process.env.TODOMARKET_API_BASE_URL || 'http://localhost:8184').replace(/\/+$/, '');
const vencimiento = (dias) => {
  const fecha = new Date();
  fecha.setDate(fecha.getDate() + dias);
  return fecha.toISOString().slice(0, 10);
};
const productos = [
  { nombre: 'Arroz blanco 500 g', descripcion: 'Arroz de grano largo', precioCompra: 2800,
    precioVenta: 4000, stock: 20, stockMinimo: 5, lote: 'TM-DEMO-01', categorias: ['Granos'], estado: true },
  { nombre: 'Leche entera 1 L', descripcion: 'Leche entera pasteurizada', precioCompra: 2300,
    precioVenta: 3500, stock: 3, stockMinimo: 5, lote: 'TM-DEMO-02', categorias: ['Lácteos'],
    fechaVencimiento: vencimiento(20), estado: true },
  { nombre: 'Jabón de tocador', descripcion: 'Barra de 100 g', precioCompra: 1500,
    precioVenta: 2500, stock: 0, stockMinimo: 5, lote: 'TM-DEMO-03', categorias: ['Aseo'], estado: true }
];
const usuarios = [
  { nombreUsuario: 'operadora.demo', documento: 'TM-DEMO-U01', correo: 'operadora@example.test',
    rol: { idRol: 2, nombreRol: 'Empleado', descripcion: 'Cajero' }, estado: true },
  { nombreUsuario: 'cliente.demo', documento: 'TM-DEMO-U02', correo: 'cliente@example.test',
    rol: { idRol: 3, nombreRol: 'Cliente', descripcion: 'Cliente del minimercado' }, estado: true }
];

for (const [coleccion, registros] of Object.entries({ productos, usuarios })) {
  for (const registro of registros) {
    const respuesta = await fetch(base + '/api/' + coleccion, {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(registro), signal: AbortSignal.timeout(10_000)
    });
    const datos = await respuesta.json();
    const nombre = registro.nombre ?? registro.nombreUsuario;
    if (respuesta.status === 409) {
      console.log(nombre + ' · omitido por datos duplicados');
      continue;
    }
    if (respuesta.status !== 201) throw new Error(nombre + ' · HTTP ' + respuesta.status + ' · ' + datos.message);
    console.log(nombre + ' · ID ' + (datos.idProducto ?? datos.idPersona));
  }
}
