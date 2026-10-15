import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto, ProductoInput, Usuario, UsuarioInput } from '../models/todomarket.models';

@Injectable({ providedIn: 'root' })
export class TodoMarketApiService {
  private readonly http = inject(HttpClient);
  private readonly productosUrl = 'http://localhost:8183/api/productos';
  private readonly usuariosUrl = 'http://localhost:8184/api/usuarios';

  listarProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.productosUrl);
  }

  crearProducto(producto: ProductoInput): Observable<Producto> {
    return this.http.post<Producto>(this.productosUrl, producto);
  }

  actualizarProducto(id: number, producto: ProductoInput): Observable<Producto> {
    return this.http.put<Producto>(`${this.productosUrl}/${id}`, { ...producto, idProducto: id });
  }

  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.productosUrl}/${id}`);
  }

  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.usuariosUrl);
  }

  crearUsuario(usuario: UsuarioInput): Observable<Usuario> {
    return this.http.post<Usuario>(this.usuariosUrl, usuario);
  }

  actualizarUsuario(id: number, usuario: UsuarioInput): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.usuariosUrl}/${id}`, { ...usuario, idPersona: id });
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.usuariosUrl}/${id}`);
  }
}
