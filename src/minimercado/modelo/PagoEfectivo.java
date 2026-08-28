package minimercado.modelo;
/** Estrategia para pagos en efectivo. */
public class PagoEfectivo implements MetodoDePago { public boolean procesarPago(double monto){return monto>0;} public String getNombre(){return "Efectivo";} }
