package minimercado.modelo;
/** Estrategia para pagos con tarjeta; en esta fase solo valida el monto. */
public class PagoTarjeta implements MetodoDePago { public boolean procesarPago(double monto){return monto>0;} public String getNombre(){return "Tarjeta";} }
