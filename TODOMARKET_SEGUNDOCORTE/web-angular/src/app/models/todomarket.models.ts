export interface Producto {
  idProducto: number;
  nombre: string;
  descripcion: string;
  precioCompra: number;
  precioVenta: number;
  stock: number;
  stockMinimo: number;
  lote: string;
  fechaVencimiento: string | null;
  categorias: string[];
  estado: boolean;
}

export interface ProductoInput {
  nombre: string;
  descripcion: string;
  precioCompra: number;
  precioVenta: number;
  stock: number;
  stockMinimo: number;
  lote: string;
  fechaVencimiento: string | null;
  categorias: string[];
  estado: boolean;
}

export interface Usuario {
  idPersona: number;
  nombreUsuario: string;
  documento: string;
  telefono: string;
  correo: string;
  rol: Rol;
  estado: boolean;
  fechaCreacion: string;
}

export interface Rol {
  idRol: number;
  nombreRol: string;
  descripcion: string;
}

export interface UsuarioInput {
  nombreUsuario: string;
  contrasena: string;
  documento: string;
  telefono: string;
  correo: string;
  rol: Rol;
  estado: boolean;
}
