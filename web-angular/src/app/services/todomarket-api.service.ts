import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto, ProductoInput, Usuario, UsuarioInput } from '../models/todomarket.models';

declare global {
  interface Window {
    TODOMARKET_CONFIG?: { apiBaseUrl: string };
  }
}

@Injectable({ providedIn: 'root' })
export class TodoMarketApiService {
  private readonly http = inject(HttpClient);
  private readonly base = (window.TODOMARKET_CONFIG?.apiBaseUrl || 'http://localhost:8184').replace(/\/+$/, '');
  private readonly productosUrl = this.base + '/api/productos';
  private readonly usuariosUrl = this.base + '/api/usuarios';

  listarProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.productosUrl);
  }

  buscarProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.productosUrl}/${id}`);
  }

  crearProducto(producto: ProductoInput): Observable<Producto> {
    return this.http.post<Producto>(this.productosUrl, { ...producto, fechaVencimiento: producto.fechaVencimiento || null });
  }

  actualizarProducto(id: number, producto: ProductoInput): Observable<Producto> {
    return this.http.put<Producto>(`${this.productosUrl}/${id}`, { ...producto, idProducto: id, fechaVencimiento: producto.fechaVencimiento || null });
  }

  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.productosUrl}/${id}`);
  }

  listarUsuarios(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.usuariosUrl);
  }

  buscarUsuario(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.usuariosUrl}/${id}`);
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
