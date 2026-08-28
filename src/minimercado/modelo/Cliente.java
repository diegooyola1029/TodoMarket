package minimercado.modelo;
/** Usuario comprador. Se asocia a una factura para saber quién realizó la compra. */
public class Cliente extends Usuario {
    private int idCliente;
    public Cliente(int id, String nombre, String clave, String doc, String tel, String correo, int idCliente) { super(id,nombre,clave,doc,tel,correo); this.idCliente=idCliente; }
    public void verHistorialCompras() { } public int getIdCliente(){return idCliente;} @Override public String toString(){return getNombreUsuario();}
}
