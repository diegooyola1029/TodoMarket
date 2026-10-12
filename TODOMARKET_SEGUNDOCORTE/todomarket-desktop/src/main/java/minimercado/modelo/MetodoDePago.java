package minimercado.modelo;
/** Estrategia de pago: nuevas formas de cobro se agregan implementando esta interfaz. */
public interface MetodoDePago { boolean procesarPago(double monto); String getNombre(); }
