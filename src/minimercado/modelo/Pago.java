package minimercado.modelo;
import java.time.LocalDate;
/** Contexto del patrón Strategy: delega el cobro al método de pago elegido. */
public class Pago { private int idPago; private double monto; private LocalDate fecha; private String referencia, estado; private MetodoDePago metodoPago;
    public Pago(int id,double monto,String ref,MetodoDePago metodo){this.idPago=id;this.monto=monto;this.referencia=ref;this.metodoPago=metodo;this.fecha=LocalDate.now();this.estado="PENDIENTE";} public boolean validarPago(){return monto>0&&metodoPago!=null;} public boolean procesarPago(){boolean ok=validarPago()&&metodoPago.procesarPago(monto);estado=ok?"APROBADO":"RECHAZADO";return ok;} public double getMonto(){return monto;} public String getEstado(){return estado;} public MetodoDePago getMetodoPago(){return metodoPago;} }
