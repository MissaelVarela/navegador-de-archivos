
package paquete;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class Navegador extends javax.swing.JFrame {

    private ifrmCarpeta ifrmCarpetaActual;
    private ManejoVolverAvanzar manejoVA;
    private boolean banderaVolver = false;
    private boolean banderaAvanzar = false;
    
    public File copiado;
    public boolean cortado;
    
    public Navegador() { 
        ifrmCarpetaActual = new ifrmCarpeta();
        initComponents();
        initMyComponents();
        ocultarAccesosRapidos();
    }
    
    public void AbrirCarpeta(File carpeta)
    {
        if(!manejoVA.pila_adelante.isEmpty() && !banderaVolver && !banderaAvanzar /*&& (carpeta != manejoVA.pila_adelante.peek())*/)
            manejoVA.pila_adelante.clear();
        if(ifrmCarpetaActual.carpetaLocal != null && !banderaVolver) manejoVA.Agregar(ifrmCarpetaActual.carpetaLocal);
        banderaVolver = false; banderaAvanzar = false;
        
        ActualizarBotonesVA();
        
        ifrmCarpetaActual.dispose();
        ifrmCarpetaActual = new ifrmCarpeta(carpeta, this);
        AgregarListeners();
        dpanCarpeta.add(ifrmCarpetaActual);
        ifrmCarpetaActual.show();
        ifrmCarpetaActual.setLocation(0, 0);
        AjustarSizeTabla();
       
        txtRuta.setText(carpeta.getPath());
    }
    public void AbrirArchivo(File archivo)
    {
        try
        {
            Desktop.getDesktop().open(archivo);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo.", "Aviso", JOptionPane.PLAIN_MESSAGE);
        }
        
    }
    public void Actualizar()
    {
        AbrirCarpeta(ifrmCarpetaActual.carpetaLocal);
        manejoVA.pila_atras.pop();
    }
    private void ocultarAccesosRapidos()
    {
        btnAccesoRapido.setVisible(false);
        lstAccesoRapido.setVisible(false);
    }
    private void ActualizarBotonesVA()
    {
        if(manejoVA.pila_atras.isEmpty()) btnAtras.setEnabled(false);
        else btnAtras.setEnabled(true);
        if(manejoVA.pila_adelante.isEmpty()) btnAdelante.setEnabled(false); 
        else btnAdelante.setEnabled(true);
    }
    private void Actualizar_cbxArchivoAcciones(boolean esArchivoSeleccionado)
    {
        JComboBox cb = ifrmCarpetaActual.getComboBoxArchivoAcciones();
        cb.removeAllItems();
        String[] acciones;
        if(esArchivoSeleccionado) 
        {
            acciones = ControlDeArchivos.accionesArchivo;
            ifrmCarpetaActual.estadoDeSeleccion = true;
        }
        else 
        {
            acciones = ControlDeArchivos.accionesCarpetaLocal;
            ifrmCarpetaActual.estadoDeSeleccion = false;
        }
        for(String item : acciones)
        {
            cb.addItem(item);
        }   
        if(!cb.isEnabled()) cb.setEnabled(true);
    }
    private void CambiarEstadoJList(JScrollPane JSPane, JList<String> Jlist)
    {
        if(JSPane.getSize().height == 0)
        {
            int c = Jlist.getModel().getSize();
            JSPane.setSize(JSPane.getSize().width, c * 20);
            spanListAccesoRapido.setAlignmentY(2);
            spanListAccesoRapido.setAlignmentY(0);
        }
        else
        {
            JSPane.setSize(JSPane.getSize().width, 0);
        }
    }
    private void CargarJList(JList<String> Jlist, String path)
    {
        File file = new File(path);
        if(file.exists())
        {
            File[] files = file.listFiles();
            Jlist.removeAll();
            DefaultListModel modeloJlist = new DefaultListModel();
            for(File elemento : files)
            {
                if(!elemento.isHidden() && elemento.isDirectory())
                modeloJlist.addElement(elemento.getName());
            }
            Jlist.setModel(modeloJlist);
        }
        
            
    }
    private void AjustarSizeTabla()
    {
        ifrmCarpetaActual.setSize(dpanCarpeta.getSize());
    }
    private void QuitarSeleccionTabla()
    {
        javax.swing.JTable tabla = ifrmCarpetaActual.getTablaArchivos();
        tabla.getSelectionModel().removeSelectionInterval(0, tabla.getRowCount());
    }
    @Override
    public Image getIconImage() 
    {
        Image retValue = Toolkit.getDefaultToolkit().
        getImage(getClass().getResource("/recursos/acciones/logo.png"));

        return retValue;
    }
    
    private void AgregarListeners()
    {
        ifrmCarpetaActual.getTablaArchivos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                archivoMousePressed(evt);
            }           
        });
        ifrmCarpetaActual.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                carpetaLocalMousePressed(evt);
            } 
        });
        ifrmCarpetaActual.getScrollPaneTabla().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                carpetaLocalMousePressed(evt);
            } 
        });
    } 

    private void initMyComponents()
    {
        manejoVA = new ManejoVolverAvanzar();
        
        AgregarListeners();
        
        btnAtras.setIcon(new ImageIcon(getClass().getResource("/recursos/acciones/flecha_atras.png")));
        btnAdelante.setIcon(new ImageIcon(getClass().getResource("/recursos/acciones/flecha_adelante.png")));
        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/recursos/acciones/buscar.png")));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panNavegacion = new javax.swing.JPanel();
        txtRuta = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnAdelante = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        panCarpeta = new javax.swing.JPanel();
        dpanCarpeta = new javax.swing.JDesktopPane();
        spanAccesos = new javax.swing.JScrollPane();
        panAccesos = new javax.swing.JPanel();
        btnAccesoRapido = new javax.swing.JButton();
        spanListAccesoRapido = new javax.swing.JScrollPane();
        lstAccesoRapido = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Explorador de Archivos");
        setIconImage(getIconImage());
        setMinimumSize(new java.awt.Dimension(400, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panNavegacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtRuta.setEditable(false);
        txtRuta.setBackground(java.awt.SystemColor.text);
        txtRuta.setMinimumSize(new java.awt.Dimension(300, 20));

        btnBuscar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnAdelante.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAdelante.setPreferredSize(new java.awt.Dimension(36, 28));
        btnAdelante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdelanteActionPerformed(evt);
            }
        });

        btnAtras.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAtras.setPreferredSize(new java.awt.Dimension(36, 28));
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Buscar ruta:");

        javax.swing.GroupLayout panNavegacionLayout = new javax.swing.GroupLayout(panNavegacion);
        panNavegacion.setLayout(panNavegacionLayout);
        panNavegacionLayout.setHorizontalGroup(
            panNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panNavegacionLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(btnAdelante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRuta, javax.swing.GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(panNavegacionLayout.createSequentialGroup()
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );
        panNavegacionLayout.setVerticalGroup(
            panNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panNavegacionLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdelante, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panNavegacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        panCarpeta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCarpeta.setMinimumSize(new java.awt.Dimension(200, 200));
        panCarpeta.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                panCarpetaAncestorResized(evt);
            }
        });

        javax.swing.GroupLayout dpanCarpetaLayout = new javax.swing.GroupLayout(dpanCarpeta);
        dpanCarpeta.setLayout(dpanCarpetaLayout);
        dpanCarpetaLayout.setHorizontalGroup(
            dpanCarpetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        dpanCarpetaLayout.setVerticalGroup(
            dpanCarpetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panCarpetaLayout = new javax.swing.GroupLayout(panCarpeta);
        panCarpeta.setLayout(panCarpetaLayout);
        panCarpetaLayout.setHorizontalGroup(
            panCarpetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dpanCarpeta)
        );
        panCarpetaLayout.setVerticalGroup(
            panCarpetaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dpanCarpeta)
        );

        btnAccesoRapido.setText("Acceso");
        btnAccesoRapido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccesoRapidoActionPerformed(evt);
            }
        });

        spanListAccesoRapido.setViewportView(lstAccesoRapido);

        javax.swing.GroupLayout panAccesosLayout = new javax.swing.GroupLayout(panAccesos);
        panAccesos.setLayout(panAccesosLayout);
        panAccesosLayout.setHorizontalGroup(
            panAccesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAccesosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panAccesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spanListAccesoRapido, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnAccesoRapido, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        panAccesosLayout.setVerticalGroup(
            panAccesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAccesosLayout.createSequentialGroup()
                .addComponent(btnAccesoRapido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spanListAccesoRapido, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 391, Short.MAX_VALUE))
        );

        spanAccesos.setViewportView(panAccesos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panNavegacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(spanAccesos, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panCarpeta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panNavegacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panCarpeta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spanAccesos, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        btnAtras.setEnabled(false);
        btnAdelante.setEnabled(false);
        CargarJList(lstAccesoRapido, "C:\\Users\\DELL\\Pictures");
        AbrirCarpeta(new File("C:\\"));
        
    }//GEN-LAST:event_formWindowOpened

    private void panCarpetaAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_panCarpetaAncestorResized
        AjustarSizeTabla();
    }//GEN-LAST:event_panCarpetaAncestorResized

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        String entrada = "";
        entrada = txtBuscar.getText();
        if(entrada.equals("")) return;
        File archivo = new File(entrada);
        if(archivo.exists())
        AbrirCarpeta(archivo);
        else JOptionPane.showMessageDialog(this, "Ruta no valida.", "Aviso", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnAdelanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdelanteActionPerformed
        File carpeta = manejoVA.Avanzar();
        banderaAvanzar = true;
        AbrirCarpeta(carpeta);
    }//GEN-LAST:event_btnAdelanteActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        File carpeta = manejoVA.Volver(ifrmCarpetaActual.carpetaLocal);
        banderaVolver = true;
        AbrirCarpeta(carpeta);
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void btnAccesoRapidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccesoRapidoActionPerformed
        CambiarEstadoJList(spanListAccesoRapido, lstAccesoRapido);
    }//GEN-LAST:event_btnAccesoRapidoActionPerformed

    private void archivoMousePressed(java.awt.event.MouseEvent evt)
    {
        if ((evt.getModifiers() & 4) != 0)// Click derecho
        {
            JTable source = (JTable)evt.getSource();
            int row = source.rowAtPoint( evt.getPoint() );
            int column = source.columnAtPoint( evt.getPoint() );

            if (! source.isRowSelected(row))
                source.changeSelection(row, column, false, false);

            ifrmCarpetaActual.getPopupMenuArchivos().show(evt.getComponent(), evt.getX(), evt.getY());
        }
        
        String ruta = ifrmCarpetaActual.getRutaSeleccionada();
        File archivo = new File(ruta);
        
        if(archivo.exists())
        {
            //Actualizar_cbxArchivoAcciones(true);
            ifrmCarpetaActual.estadoDeSeleccion = true;
            
            JLabel label = ifrmCarpetaActual.getLabelSeleccionado();
            label.setText("Archivo " + archivo.getName());
            label.setForeground(java.awt.SystemColor.textHighlight);       
            
            if (evt.getClickCount() == 2 && (evt.getButton() == 1)) // Doble click izquierdo
            {
                if(archivo.isDirectory())
                    AbrirCarpeta(archivo);
                else
                    AbrirArchivo(archivo);
            } 
        }  
    }
    private void carpetaLocalMousePressed(java.awt.event.MouseEvent evt)
    {
       // Actualizar_cbxArchivoAcciones(false);
        ifrmCarpetaActual.estadoDeSeleccion = false;
        
        JLabel label = ifrmCarpetaActual.getLabelSeleccionado();
        label.setText("Carpeta local " + ifrmCarpetaActual.carpetaLocal.getName());
        label.setForeground(Color.BLUE);
        
        QuitarSeleccionTabla();
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Navegador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Navegador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Navegador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Navegador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

  
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Navegador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccesoRapido;
    private javax.swing.JButton btnAdelante;
    private javax.swing.JButton btnAtras;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JDesktopPane dpanCarpeta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> lstAccesoRapido;
    private javax.swing.JPanel panAccesos;
    private javax.swing.JPanel panCarpeta;
    private javax.swing.JPanel panNavegacion;
    private javax.swing.JScrollPane spanAccesos;
    private javax.swing.JScrollPane spanListAccesoRapido;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtRuta;
    // End of variables declaration//GEN-END:variables

    
}



