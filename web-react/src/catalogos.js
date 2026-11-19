const categorias = ['Granos', 'Lácteos', 'Bebidas', 'Aseo', 'Otros'];
const roles = [
  { idRol: 1, nombreRol: 'Administrador', descripcion: 'Gerente' },
  { idRol: 2, nombreRol: 'Empleado', descripcion: 'Cajero' },
  { idRol: 3, nombreRol: 'Cliente', descripcion: 'Cliente del minimercado' }
];
const cop = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP' });
const conValor = (valor) => valor || '—';

export const catalogos = {
  productos: {
    singular: 'producto',
    titulo: 'Productos',
    descripcion: 'Gestiona precios, existencias y lotes del supermercado.',
    id: 'idProducto',
    nombre: 'nombre',
    columnas: [
      ['Categorías', (item) => item.categorias.join(', ')],
      ['Precio de venta', (item) => cop.format(item.precioVenta)],
      ['Stock', (item) => item.stock],
      ['Lote', (item) => item.lote]
    ],
    detalles: [
      ['Descripción', (item) => conValor(item.descripcion)],
      ['Precio de compra', (item) => cop.format(item.precioCompra)],
      ['Stock mínimo', (item) => item.stockMinimo],
      ['Fecha de vencimiento', (item) => conValor(item.fechaVencimiento)]
    ],
    campos: [
      { clave: 'nombre', etiqueta: 'Nombre', required: true, maxLength: 100, ancho: true },
      { clave: 'descripcion', etiqueta: 'Descripción', ancho: true },
      { clave: 'precioCompra', etiqueta: 'Precio de compra', type: 'number', min: 0, step: '0.01', required: true },
      { clave: 'precioVenta', etiqueta: 'Precio de venta', type: 'number', min: 0, step: '0.01', required: true },
      { clave: 'stock', etiqueta: 'Stock', type: 'number', min: 0, required: true },
      { clave: 'stockMinimo', etiqueta: 'Stock mínimo', type: 'number', min: 0, required: true },
      { clave: 'lote', etiqueta: 'Lote', required: true },
      { clave: 'fechaVencimiento', etiqueta: 'Fecha de vencimiento', type: 'date' },
      { clave: 'categorias', etiqueta: 'Categorías', categorias, ancho: true }
    ],
    inicial: {
      nombre: '', descripcion: '', precioCompra: 0, precioVenta: 0, stock: 0,
      stockMinimo: 0, lote: '', fechaVencimiento: '', categorias: ['Otros'], estado: true
    }
  },
  usuarios: {
    singular: 'usuario',
    titulo: 'Usuarios',
    descripcion: 'Gestiona contactos, roles y estado de las cuentas.',
    id: 'idPersona',
    nombre: 'nombreUsuario',
    columnas: [
      ['Documento', (item) => item.documento],
      ['Correo', (item) => conValor(item.correo)],
      ['Teléfono', (item) => conValor(item.telefono)],
      ['Rol', (item) => item.rol.nombreRol]
    ],
    detalles: [['Fecha de creación', (item) => item.fechaCreacion]],
    campos: [
      { clave: 'nombreUsuario', etiqueta: 'Nombre de usuario', required: true, maxLength: 50, ancho: true },
      { clave: 'documento', etiqueta: 'Documento', required: true },
      { clave: 'telefono', etiqueta: 'Teléfono', type: 'tel' },
      { clave: 'correo', etiqueta: 'Correo', type: 'email', ancho: true },
      { clave: 'contrasena', etiqueta: 'Contraseña', type: 'password', minLength: 4, maxLength: 72 },
      { clave: 'rol', etiqueta: 'Rol', roles }
    ],
    inicial: { nombreUsuario: '', contrasena: '', documento: '', telefono: '', correo: '', rol: roles[2], estado: true }
  }
};
