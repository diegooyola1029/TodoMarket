import React, { useEffect, useRef, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { api } from './api';
import { catalogos } from './catalogos';
import './styles.css';

function Dialogo({ titulo, cerrar, children }) {
  const dialogo = useRef(null);
  useEffect(() => { dialogo.current.showModal(); }, []);
  return (
    <dialog ref={dialogo} aria-labelledby="titulo-dialogo" onCancel={(evento) => {
      evento.preventDefault();
      cerrar();
    }}>
      <div className="dialog-title">
        <h2 id="titulo-dialogo">{titulo}</h2>
        <button type="button" className="text-action" aria-label="Cerrar diálogo" onClick={cerrar}>Cerrar</button>
      </div>
      {children}
    </dialog>
  );
}

function Formulario({ config, registro, guardar, cerrar, ocupado, error }) {
  const [datos, setDatos] = useState(() => ({ ...config.inicial, ...registro }));
  const cambiar = (clave, valor) => setDatos((actual) => ({ ...actual, [clave]: valor }));
  const enviar = (evento) => {
    evento.preventDefault();
    const cuerpo = Object.fromEntries(Object.keys(config.inicial).map((clave) => [clave, datos[clave]]));
    if ('fechaVencimiento' in cuerpo) cuerpo.fechaVencimiento ||= null;
    guardar(cuerpo);
  };

  return (
    <form onSubmit={enviar}>
      {error && <p className="notice error" role="alert">{error}</p>}
      <fieldset className="fields" disabled={ocupado}>
        {config.campos.map(({ clave, etiqueta, ancho, categorias, roles, ...atributos }) => {
          if (categorias) {
            const opciones = [...new Set([...categorias, ...datos[clave]])];
            return (
              <div key={clave} className="field wide">
                <span id="categorias-label">{etiqueta}</span>
                <div className="choices" role="group" aria-labelledby="categorias-label">
                  {opciones.map((categoria) => <label key={categoria}>
                    <input type="checkbox" checked={datos[clave].includes(categoria)} onChange={(e) => {
                      cambiar(clave, e.target.checked
                        ? [...datos[clave], categoria] : datos[clave].filter((actual) => actual !== categoria));
                    }} /> {categoria}
                  </label>)}
                </div>
              </div>
            );
          }
          if (roles) {
            const listas = roles.some(({ idRol }) => idRol === datos.rol.idRol) ? roles : [...roles, datos.rol];
            return <label className="field" key={clave}>{etiqueta}
              <select value={datos.rol.idRol} onChange={(e) => cambiar(clave, listas.find((rol) => rol.idRol === Number(e.target.value)))}>
                {listas.map((rol) => <option key={rol.idRol} value={rol.idRol}>{rol.nombreRol}</option>)}
              </select>
            </label>;
          }
          return <label className={ancho ? 'field wide' : 'field'} key={clave}>{etiqueta}
            <input {...atributos} value={datos[clave] ?? ''}
              autoComplete={clave === 'contrasena' ? 'new-password' : undefined}
              required={atributos.required || (clave === 'contrasena' && !registro)}
              placeholder={clave === 'contrasena' && registro ? 'Vacía para conservarla' : undefined}
              onChange={(e) => cambiar(clave, atributos.type === 'number'
                ? (e.target.value === '' ? '' : e.target.valueAsNumber) : e.target.value)} />
          </label>;
        })}
        <label className="check wide"><input type="checkbox" checked={datos.estado}
          onChange={(e) => cambiar('estado', e.target.checked)} /> Registro activo</label>
      </fieldset>
      <footer className="dialog-actions">
        <button type="button" className="button" onClick={cerrar} disabled={ocupado}>Cancelar</button>
        <button className="button primary" disabled={ocupado}>{ocupado ? 'Guardando…' : 'Guardar ' + config.singular}</button>
      </footer>
    </form>
  );
}

function Gestion({ seccion }) {
  const config = catalogos[seccion];
  const [registros, setRegistros] = useState([]);
  const [filtro, setFiltro] = useState('');
  const [cargando, setCargando] = useState(true);
  const [ocupado, setOcupado] = useState(false);
  const [aviso, setAviso] = useState(null);
  const [errorDialogo, setErrorDialogo] = useState('');
  const [dialogo, setDialogo] = useState(null);
  const carga = useRef(0);

  useEffect(() => {
    let vigente = true;
    async function actualizar() {
      const peticion = ++carga.current;
      setCargando(true);
      try {
        const datos = await api.listar(seccion);
        if (vigente && peticion === carga.current) setRegistros(datos);
      } catch (error) {
        if (vigente && peticion === carga.current) setAviso({ error: true, texto: error.message });
      } finally {
        if (vigente && peticion === carga.current) setCargando(false);
      }
    }
    actualizar();
    window.addEventListener('focus', actualizar);
    return () => {
      vigente = false;
      window.removeEventListener('focus', actualizar);
    };
  }, [seccion]);

  async function recargar() {
    const peticion = ++carga.current;
    setCargando(true);
    setAviso(null);
    try {
      const datos = await api.listar(seccion);
      if (peticion === carga.current) setRegistros(datos);
    } catch (error) {
      if (peticion === carga.current) setAviso({ error: true, texto: error.message });
    } finally {
      if (peticion === carga.current) setCargando(false);
    }
  }

  async function abrir(tipo, registro) {
    setAviso(null);
    setErrorDialogo('');
    if (tipo === 'crear' || tipo === 'eliminar') {
      setDialogo({ tipo, registro });
      return;
    }
    setOcupado(true);
    try {
      setDialogo({ tipo, registro: await api.buscar(seccion, registro[config.id]) });
    } catch (error) {
      setAviso({ error: true, texto: error.message });
    } finally {
      setOcupado(false);
    }
  }

  function cerrar() {
    if (!ocupado) setDialogo(null);
  }

  async function guardar(datos) {
    const id = dialogo.registro?.[config.id];
    setOcupado(true);
    setErrorDialogo('');
    try {
      const guardado = await api.guardar(seccion, id, datos);
      ++carga.current;
      setCargando(false);
      setRegistros((lista) => [...lista.filter((item) => item[config.id] !== guardado[config.id]), guardado]
        .sort((a, b) => a[config.id] - b[config.id]));
      setDialogo(null);
      setAviso({ texto: id === undefined ? 'Registro creado. ID ' + guardado[config.id] + '.' : 'Registro actualizado.' });
    } catch (error) {
      setErrorDialogo(error.message);
    } finally {
      setOcupado(false);
    }
  }

  async function confirmarEliminar() {
    const id = dialogo.registro[config.id];
    setOcupado(true);
    setErrorDialogo('');
    try {
      await api.eliminar(seccion, id);
      ++carga.current;
      setCargando(false);
      setRegistros((lista) => lista.filter((item) => item[config.id] !== id));
      setDialogo(null);
      setAviso({ texto: 'Registro eliminado.' });
    } catch (error) {
      setErrorDialogo(error.message);
    } finally {
      setOcupado(false);
    }
  }

  const buscar = filtro.trim().toLocaleLowerCase('es-CO');
  const visibles = registros.filter((item) =>
    [item[config.id], item[config.nombre], ...config.columnas.map(([, valor]) => valor(item))]
      .join(' ').toLocaleLowerCase('es-CO').includes(buscar));
  const registro = dialogo?.registro;

  return (
    <main id="contenido">
      <header className="page-title">
        <div><h1>{config.titulo}</h1><p>{config.descripcion}</p></div>
        <button className="button primary" disabled={ocupado} onClick={() => abrir('crear')}>＋ Nuevo {config.singular}</button>
      </header>
      {aviso && <p className={'notice ' + (aviso.error ? 'error' : 'success')} role={aviso.error ? 'alert' : 'status'}>{aviso.texto}</p>}
      <section className="panel" aria-label={'Listado de ' + seccion}>
        <div className="toolbar">
          <label className="search"><span>Buscar {seccion}</span>
            <input type="search" value={filtro} onChange={(evento) => setFiltro(evento.target.value)}
              placeholder={seccion === 'productos' ? 'Nombre, ID, categoría o lote' : 'Nombre, ID, documento o correo'} />
          </label>
          <button className="button" onClick={recargar} disabled={cargando}>Actualizar lista</button>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr>
              <th scope="col">ID</th><th scope="col">Nombre</th>
              {config.columnas.map(([titulo]) => <th key={titulo} scope="col">{titulo}</th>)}
              <th scope="col">Estado</th><th scope="col">Acciones</th>
            </tr></thead>
            <tbody>
              {visibles.map((item) => <tr key={item[config.id]}>
                <td>{item[config.id]}</td><th scope="row">{item[config.nombre]}</th>
                {config.columnas.map(([titulo, valor]) => <td key={titulo}>{valor(item)}</td>)}
                <td><span className={'state' + (item.estado ? ' active' : '')}>{item.estado ? 'Activo' : 'Inactivo'}</span></td>
                <td><div className="row-actions">
                  <button disabled={ocupado} onClick={() => abrir('ver', item)}>Ver</button>
                  <button disabled={ocupado} onClick={() => abrir('editar', item)}>Editar</button>
                  <button disabled={ocupado} className="danger-text" onClick={() => abrir('eliminar', item)}>Eliminar</button>
                </div></td>
              </tr>)}
            </tbody>
          </table>
          {visibles.length === 0 && <p className="empty" role="status">{cargando ? 'Cargando…'
            : aviso?.error ? 'No se pudo cargar el listado.'
            : buscar ? 'No hay resultados para la búsqueda.' : 'No hay registros. Crea el primero con el botón superior.'}</p>}
        </div>
        <p className="count">{visibles.length} de {registros.length} registros</p>
      </section>

      {dialogo && <Dialogo cerrar={cerrar} titulo={
        dialogo.tipo === 'crear' ? 'Nuevo ' + config.singular
          : dialogo.tipo === 'editar' ? 'Editar ' + config.singular
          : dialogo.tipo === 'eliminar' ? 'Eliminar ' + config.singular : registro[config.nombre]}>
        {(dialogo.tipo === 'crear' || dialogo.tipo === 'editar') &&
          <Formulario config={config} registro={registro} guardar={guardar} cerrar={cerrar}
            ocupado={ocupado} error={errorDialogo} />}
        {dialogo.tipo === 'ver' && <dl className="details">
          <div><dt>ID</dt><dd>{registro[config.id]}</dd></div>
          <div><dt>Estado</dt><dd>{registro.estado ? 'Activo' : 'Inactivo'}</dd></div>
          {[...config.columnas, ...config.detalles].map(([titulo, valor]) =>
            <div key={titulo}><dt>{titulo}</dt><dd>{valor(registro)}</dd></div>)}
        </dl>}
        {dialogo.tipo === 'eliminar' && <div>
          <p>¿Eliminar «{registro[config.nombre]}»?</p>
          {errorDialogo && <p className="notice error" role="alert">{errorDialogo}</p>}
          <footer className="dialog-actions">
            <button className="button" onClick={cerrar} disabled={ocupado}>Cancelar</button>
            <button className="button danger" onClick={confirmarEliminar} disabled={ocupado}>Confirmar eliminación</button>
          </footer>
        </div>}
      </Dialogo>}
    </main>
  );
}

function App() {
  const [seccion, setSeccion] = useState('productos');
  return (
    <div className="layout">
      <aside className="sidebar">
        <a className="skip" href="#contenido">Saltar al contenido</a>
        <div className="brand"><span className="brand-mark" aria-hidden="true">TM</span>TodoMarket</div>
        <nav aria-label="Gestión">
          {Object.entries(catalogos).map(([clave, config]) =>
            <button key={clave} type="button" aria-current={clave === seccion ? 'page' : undefined}
              onClick={() => setSeccion(clave)}>{config.titulo}</button>)}
        </nav>
      </aside>
      <Gestion key={seccion} seccion={seccion} />
    </div>
  );
}

createRoot(document.getElementById('root')).render(<App />);
