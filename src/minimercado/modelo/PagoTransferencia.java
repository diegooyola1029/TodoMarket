package minimercado.modelo;
/** Estrategia para transferencias; su implementación real puede añadirse después. */
public class PagoTransferencia implements MetodoDePago { public boolean procesarPago(double monto){return monto>0;} public String getNombre(){return "Transferencia";} }
