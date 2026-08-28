package minimercado.vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import minimercado.controlador.ProductoController;
import minimercado.controlador.VentaController;
import minimercado.modelo.Cliente;
import minimercado.modelo.Factura;
import minimercado.modelo.MetodoDePago;
import minimercado.modelo.PagoEfectivo;
import minimercado.modelo.PagoTarjeta;
import minimercado.modelo.PagoTransferencia;
import minimercado.modelo.Producto;

/**
 * Vista Swing para que un cliente compre un producto. Delega la venta al
 * controlador y presenta la factura resultante sin modificar stock directamente.
 */
public class VentaPanel extends JPanel {
    private final VentaController ventas; private final ProductoController productos;
    private final JComboBox<Producto> producto=new JComboBox<>(); private final JComboBox<Cliente> cliente=new JComboBox<>(); private final JComboBox<String> metodo=new JComboBox<>(new String[]{"Efectivo","Tarjeta","Transferencia"}); private final JTextField cantidad=new JTextField(); private final JLabel disponibles=new JLabel("Seleccione un producto");
    private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"Factura","Fecha","Cliente","Total","Pago","Estado"},0){public boolean isCellEditable(int r,int c){return false;}};
    public VentaPanel(VentaController ventas,ProductoController productos){this.ventas=ventas;this.productos=productos;construir();refrescar();}
    private void construir(){setLayout(new BorderLayout(14,14));setBorder(BorderFactory.createEmptyBorder(22,22,22,22));setBackground(new Color(245,247,250));JPanel cab=new JPanel(new GridLayout(2,1));cab.setOpaque(false);JLabel titulo=new JLabel("Registrar venta");titulo.setFont(new Font("Segoe UI",Font.BOLD,24));titulo.setForeground(new Color(31,78,121));cab.add(titulo);JLabel sub=new JLabel("Selecciona el cliente, producto, cantidad y medio de pago.");sub.setForeground(new Color(92,101,112));cab.add(sub);add(cab,BorderLayout.NORTH);JPanel centro=new JPanel(new BorderLayout(0,14));centro.setOpaque(false);centro.add(formulario(),BorderLayout.NORTH);JTable tabla=new JTable(modelo);tabla.setRowHeight(27);tabla.getTableHeader().setReorderingAllowed(false);centro.add(new JScrollPane(tabla),BorderLayout.CENTER);add(centro,BorderLayout.CENTER);}
    private JPanel formulario(){JPanel p=new JPanel(new GridBagLayout());p.setBackground(Color.WHITE);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220,225,231)),BorderFactory.createEmptyBorder(16,16,16,16)));GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(4,4,10,14);c.fill=GridBagConstraints.HORIZONTAL;c.weightx=1;campo(p,c,0,0,"Cliente",cliente);campo(p,c,1,0,"Producto",producto);campo(p,c,2,0,"Cantidad",cantidad);campo(p,c,0,1,"Método de pago",metodo);c.gridx=1;c.gridy=1;c.gridwidth=2;disponibles.setForeground(new Color(43,126,91));p.add(disponibles,c);c.gridwidth=1;JButton vender=new JButton("Confirmar compra");vender.setBackground(new Color(31,78,121));vender.setForeground(Color.WHITE);vender.setFocusPainted(false);c.gridx=2;c.gridy=2;c.anchor=GridBagConstraints.EAST;p.add(vender,c);producto.addActionListener(e->stockSeleccionado());vender.addActionListener(e->registrar());return p;}
    private void campo(JPanel p,GridBagConstraints c,int x,int y,String texto,JComponent componente){JPanel g=new JPanel(new BorderLayout(0,4));g.setOpaque(false);JLabel l=new JLabel(texto);l.setFont(new Font("Segoe UI",Font.BOLD,12));g.add(l,BorderLayout.NORTH);g.add(componente,BorderLayout.CENTER);c.gridx=x;c.gridy=y;p.add(g,c);}
    private void registrar(){try{Producto p=(Producto)producto.getSelectedItem();int unidades=Integer.parseInt(cantidad.getText().trim());Factura factura=ventas.registrar(p.getIdProducto(),unidades,(Cliente)cliente.getSelectedItem(),estrategia());refrescar();cantidad.setText("");stockSeleccionado();JOptionPane.showMessageDialog(this,"Compra registrada correctamente.\nFactura: "+factura.getNumero()+"\nTotal: $ "+String.format("%,.0f",factura.getTotal()),"Venta realizada",JOptionPane.INFORMATION_MESSAGE);}catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"La cantidad debe ser un número válido.","Validación",JOptionPane.WARNING_MESSAGE);}catch(IllegalArgumentException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Validación",JOptionPane.WARNING_MESSAGE);}}
    /** Convierte la opción visual en una estrategia de pago concreta. */
    private MetodoDePago estrategia(){return switch((String)metodo.getSelectedItem()){case "Tarjeta" -> new PagoTarjeta();case "Transferencia" -> new PagoTransferencia();default -> new PagoEfectivo();};}
    private void stockSeleccionado(){Producto p=(Producto)producto.getSelectedItem();disponibles.setText(p==null?"No hay productos disponibles":"Stock disponible: "+p.getStock()+" unidades");}
    public void refrescar(){producto.removeAllItems();for(Producto p:productos.listar())producto.addItem(p);cliente.removeAllItems();for(Cliente c:ventas.listarClientes())cliente.addItem(c);modelo.setRowCount(0);for(Factura f:ventas.listar())modelo.addRow(new Object[]{f.getNumero(),f.getFecha(),f.getCliente(),"$ "+String.format("%,.0f",f.getTotal()),f.getPago().getMetodoPago().getNombre(),f.isEstado()?"PAGADA":"PENDIENTE"});stockSeleccionado();}
}
