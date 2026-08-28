package minimercado.modelo;
/** Usuario que representa al personal que registra ventas y consulta inventario. */
public class Empleado extends Usuario {
    private int idEmpleado; private String cargo;
    public Empleado(int id, String nombre, String clave, String doc, String tel, String correo, int idEmpleado, String cargo) { super(id,nombre,clave,doc,tel,correo); this.idEmpleado=idEmpleado; this.cargo=cargo; }
    public void registrarVenta() { } public void consultarInventario() { }
    public int getIdEmpleado(){return idEmpleado;} public String getCargo(){return cargo;}
}
