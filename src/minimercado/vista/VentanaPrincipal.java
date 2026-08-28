package minimercado.vista;

import java.awt.*;
import javax.swing.*;
import minimercado.controlador.ProductoController;
import minimercado.controlador.VentaController;

/** Ventana principal: organiza los módulos y actualiza sus datos al cambiar de pestaña. */
public class VentanaPrincipal extends JFrame {
    private final ProductoController productos; private final VentaController ventas;
    public VentanaPrincipal(ProductoController productos, VentaController ventas) { this.productos=productos;this.ventas=ventas;setTitle("SISTEMA DE GESTIÓN - MINIMERCADO");setDefaultCloseOperation(EXIT_ON_CLOSE);setMinimumSize(new Dimension(1050,700));setSize(1180,760);setLocationRelativeTo(null);construir(); }
    private void construir(){JTabbedPane pestañas=new JTabbedPane();pestañas.setFont(new Font("Segoe UI",Font.BOLD,14));ProductoPanel productosPanel=new ProductoPanel(productos);VentaPanel ventasPanel=new VentaPanel(ventas,productos);pestañas.addTab("Productos",productosPanel);pestañas.addTab("Ventas",ventasPanel);pestañas.addTab("Compras",moduloPendiente("Compras"));pestañas.addTab("Clientes",moduloPendiente("Clientes"));pestañas.addTab("Reportes",reportes());pestañas.addChangeListener(e->{if(pestañas.getSelectedComponent()==productosPanel)productosPanel.refrescar();if(pestañas.getSelectedComponent()==ventasPanel)ventasPanel.refrescar();});add(pestañas);}
    private JPanel moduloPendiente(String nombre){JPanel p=new JPanel();p.add(new JLabel("Módulo de "+nombre+" disponible en una siguiente fase."));return p;}
    private JPanel reportes(){JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,20,25));JButton ver=new JButton("Ver resumen de inventario");ver.addActionListener(e->JOptionPane.showMessageDialog(this,"Total de productos: "+productos.listar().size()+"\nStock bajo: "+productos.stockBajo().size()+"\nAgotados: "+productos.agotados().size()+"\nPróximos a vencer: "+productos.proximosAVencer().size(),"Reportes",JOptionPane.INFORMATION_MESSAGE));p.add(ver);return p;}
}
