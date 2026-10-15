import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { finalize } from 'rxjs';
import { Producto, ProductoInput, Rol, Usuario, UsuarioInput } from './models/todomarket.models';
import { TodoMarketApiService } from './services/todomarket-api.service';

type Seccion = 'productos' | 'usuarios';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private readonly api = inject(TodoMarketApiService);

  readonly categorias = ['Granos', 'Lácteos', 'Bebidas', 'Aseo', 'Otros'];
  readonly roles: Rol[] = [
    { idRol: 1, nombreRol: 'Administrador', descripcion: 'Gerente' },
    { idRol: 2, nombreRol: 'Empleado', descripcion: 'Cajero' },
    { idRol: 3, nombreRol: 'Cliente', descripcion: 'Cliente del minimercado' }
  ];

  seccion: Seccion = 'productos';
  productos: Producto[] = [];
  usuarios: Usuario[] = [];
  filtro = '';
  mensaje = '';
  tipoMensaje: 'exito' | 'error' = 'exito';
  cargandoProductos = true;
  cargandoUsuarios = true;
  guardando = false;
  errorProductos = false;
  errorUsuarios = false;
  productoEditando: number | null = null;
  usuarioEditando: number | null = null;

  productoForm: ProductoInput = this.nuevoProducto();
  usuarioForm: UsuarioInput = this.nuevoUsuario();

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarUsuarios();
  }

  get productosFiltrados(): Producto[] {
    const texto = this.filtro.trim().toLocaleLowerCase();
    if (!texto) return this.productos;
    return this.productos.filter(producto =>
      [producto.nombre, producto.descripcion, producto.lote, ...producto.categorias]
        .join(' ')
        .toLocaleLowerCase()
        .includes(texto)
    );
  }

  get usuariosFiltrados(): Usuario[] {
    const texto = this.filtro.trim().toLocaleLowerCase();
    if (!texto) return this.usuarios;
    return this.usuarios.filter(usuario =>
      [usuario.nombreUsuario, usuario.documento, usuario.correo, usuario.telefono, usuario.rol.nombreRol]
        .join(' ')
        .toLocaleLowerCase()
        .includes(texto)
    );
  }

  get productosConStockBajo(): number {
    return this.productos.filter(producto => producto.stock > 0 && producto.stock <= producto.stockMinimo).length;
  }

  cambiarSeccion(seccion: Seccion): void {
    this.seccion = seccion;
    this.filtro = '';
    this.limpiarMensaje();
  }

  cargarProductos(): void {
    this.cargandoProductos = true;
    this.api.listarProductos()
      .pipe(finalize(() => this.cargandoProductos = false))
      .subscribe({
        next: productos => {
          this.productos = productos;
          this.errorProductos = false;
        },
        error: error => {
          this.errorProductos = true;
          this.notificarError('No se pudo conectar con el microservicio de productos.', error);
        }
      });
  }

  cargarUsuarios(): void {
    this.cargandoUsuarios = true;
    this.api.listarUsuarios()
      .pipe(finalize(() => this.cargandoUsuarios = false))
      .subscribe({
        next: usuarios => {
          this.usuarios = usuarios;
          this.errorUsuarios = false;
        },
        error: error => {
          this.errorUsuarios = true;
          this.notificarError('No se pudo conectar con el microservicio de usuarios.', error);
        }
      });
  }

  guardarProducto(form: NgForm): void {
    if (form.invalid || this.guardando) return;
    this.guardando = true;
    const operacion = this.productoEditando === null
      ? this.api.crearProducto(this.productoForm)
      : this.api.actualizarProducto(this.productoEditando, this.productoForm);

    operacion.pipe(finalize(() => this.guardando = false)).subscribe({
      next: producto => {
        this.cargarProductos();
        this.notificarExito(this.productoEditando === null
          ? `Producto creado con el ID ${producto.idProducto}.`
          : 'Producto actualizado.');
        this.nuevoProductoForm(form);
      },
      error: error => this.notificarError('No se pudo guardar el producto.', error)
    });
  }

  editarProducto(producto: Producto): void {
    this.productoEditando = producto.idProducto;
    this.productoForm = {
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      precioCompra: producto.precioCompra,
      precioVenta: producto.precioVenta,
      stock: producto.stock,
      stockMinimo: producto.stockMinimo,
      lote: producto.lote,
      fechaVencimiento: producto.fechaVencimiento,
      categorias: [...producto.categorias],
      estado: producto.estado
    };
    this.limpiarMensaje();
  }

  eliminarProducto(producto: Producto): void {
    if (!confirm(`¿Eliminar el producto "${producto.nombre}"?`)) return;
    this.api.eliminarProducto(producto.idProducto).subscribe({
      next: () => {
        this.productos = this.productos.filter(item => item.idProducto !== producto.idProducto);
        this.notificarExito('Producto eliminado.');
        if (this.productoEditando === producto.idProducto) this.cancelarProducto();
      },
      error: error => this.notificarError('No se pudo eliminar el producto.', error)
    });
  }

  cancelarProducto(): void {
    this.productoEditando = null;
    this.productoForm = this.nuevoProducto();
  }

  guardarUsuario(form: NgForm): void {
    if (form.invalid || this.guardando) return;
    this.guardando = true;
    const operacion = this.usuarioEditando === null
      ? this.api.crearUsuario(this.usuarioForm)
      : this.api.actualizarUsuario(this.usuarioEditando, this.usuarioForm);

    operacion.pipe(finalize(() => this.guardando = false)).subscribe({
      next: usuario => {
        this.cargarUsuarios();
        this.notificarExito(this.usuarioEditando === null
          ? `Usuario creado con el ID ${usuario.idPersona}.`
          : 'Usuario actualizado.');
        this.nuevoUsuarioForm(form);
      },
      error: error => this.notificarError('No se pudo guardar el usuario.', error)
    });
  }

  editarUsuario(usuario: Usuario): void {
    this.usuarioEditando = usuario.idPersona;
    this.usuarioForm = {
      nombreUsuario: usuario.nombreUsuario,
      contrasena: '',
      documento: usuario.documento,
      telefono: usuario.telefono,
      correo: usuario.correo,
      rol: { ...usuario.rol },
      estado: usuario.estado
    };
    this.limpiarMensaje();
  }

  eliminarUsuario(usuario: Usuario): void {
    if (!confirm(`¿Eliminar el usuario "${usuario.nombreUsuario}"?`)) return;
    this.api.eliminarUsuario(usuario.idPersona).subscribe({
      next: () => {
        this.usuarios = this.usuarios.filter(item => item.idPersona !== usuario.idPersona);
        this.notificarExito('Usuario eliminado.');
        if (this.usuarioEditando === usuario.idPersona) this.cancelarUsuario();
      },
      error: error => this.notificarError('No se pudo eliminar el usuario.', error)
    });
  }

  cancelarUsuario(): void {
    this.usuarioEditando = null;
    this.usuarioForm = this.nuevoUsuario();
  }

  cambiarRol(idRol: number): void {
    const rol = this.roles.find(opcion => opcion.idRol === idRol);
    if (rol) this.usuarioForm.rol = { ...rol };
  }

  private nuevoProducto(): ProductoInput {
    return {
      nombre: '',
      descripcion: '',
      precioCompra: 0,
      precioVenta: 0,
      stock: 0,
      stockMinimo: 0,
      lote: '',
      fechaVencimiento: null,
      categorias: ['Otros'],
      estado: true
    };
  }

  private nuevoUsuario(): UsuarioInput {
    return {
      nombreUsuario: '',
      contrasena: '',
      documento: '',
      telefono: '',
      correo: '',
      rol: { ...this.roles[2] },
      estado: true
    };
  }

  private nuevoProductoForm(form: NgForm): void {
    this.cancelarProducto();
    form.resetForm();
    this.productoForm = this.nuevoProducto();
  }

  private nuevoUsuarioForm(form: NgForm): void {
    this.cancelarUsuario();
    form.resetForm();
    this.usuarioForm = this.nuevoUsuario();
  }

  private notificarExito(texto: string): void {
    this.mensaje = texto;
    this.tipoMensaje = 'exito';
  }

  private notificarError(contexto: string, error: unknown): void {
    const detalle = error instanceof HttpErrorResponse
      ? error.error?.message ?? error.message
      : 'Error inesperado.';
    this.mensaje = `${contexto} ${detalle}`;
    this.tipoMensaje = 'error';
  }

  private limpiarMensaje(): void {
    this.mensaje = '';
  }
}
