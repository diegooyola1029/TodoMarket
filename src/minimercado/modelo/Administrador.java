package minimercado.modelo;
/** Usuario con responsabilidades de administración del sistema. */
public class Administrador extends Usuario {
    private int idAdmin; private String cargo;
    public Administrador(int id, String nombre, String clave, String doc, String tel, String correo, int idAdmin, String cargo) { super(id,nombre,clave,doc,tel,correo); this.idAdmin=idAdmin; this.cargo=cargo; }
    public void gestionarSistema() { }
    public int getIdAdmin(){return idAdmin;} public String getCargo(){return cargo;}
}
