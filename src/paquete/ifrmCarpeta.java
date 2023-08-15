
package paquete;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ifrmCarpeta extends javax.swing.JInternalFrame {

    public Navegador navPadre;
    public File carpetaLocal;
    public ArrayList<File> listaArchivosLocales;
    public ControlDeArchivos ctrlArchivos;
    public boolean estadoDeSeleccion;
    private final ImageIcon[] iconosTipos = new ImageIcon[8];
    private final ImageIcon[] iconosAccionesArchivos = new ImageIcon[ControlDeArchivos.accionesArchivo.length];
    private final ImageIcon[] iconosAccionesCarpetaL = new ImageIcon[ControlDeArchivos.accionesCarpetaLocal.length];
    
    public ifrmCarpeta(File archivo, Navegador padre) {
        this.navPadre = padre;
        carpetaLocal = archivo;
        initComponents();
        initMyComponets();
        
        //Si la carpeta esta vacia, o no existe.
        if(archivo.listFiles() == null || archivo.listFiles().length <= 0) 
        {
            remove(spanTabla);
            lblCarpetaVacia.setSize(300,9);
            lblCarpetaVacia.setText("Esta carpeta esta vacia.");
        }
        //Si la carpeta no esta vacia.
        else
        {
            listaArchivosLocales = Ordenar(this.carpetaLocal.listFiles());
            Listar();
        }
        //cbxArchivosAcciones.setEnabled(false);
    }
    public ifrmCarpeta() {
        initComponents();     
    }
    
    private void Listar()
    {
        if(carpetaLocal != null)
        {
            int i = 0;
            String pattern = "dd/MM/yyyy hh:mm aa";
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            for(File elemento : listaArchivosLocales)
            {
                String[] archivoNomTipo = getNombre_Y_Tipo(elemento);

		Date lastModifiedDate = new Date(elemento.lastModified());
                   
                DefaultTableModel modelo = (DefaultTableModel) tblArchivos.getModel(); 
                modelo.addRow(new Object[7]);
                tblArchivos.setModel(modelo);
                
                
                JLabel label = new JLabel();
                label = asignarIcono(label, elemento);
                
                tblArchivos.setValueAt(label, i, 0);
                tblArchivos.setValueAt(archivoNomTipo[0], i, 1);
                tblArchivos.setValueAt(simpleDateFormat.format( lastModifiedDate ), i, 2);
                tblArchivos.setValueAt(archivoNomTipo[1], i, 3);
                tblArchivos.setValueAt(getTamaño(elemento), i, 4);
                tblArchivos.setValueAt(elemento.isHidden(), i, 5); 
                tblArchivos.setValueAt(elemento.canRead() && !elemento.canWrite(), i, 6);
                i++;
            }
        }

    }
    private void agregarIconos()
    {
        try
        {
            iconosTipos[0] = new ImageIcon(getClass().getResource("/recursos/tipos/default.png"));
            iconosTipos[1] = new ImageIcon(getClass().getResource("/recursos/tipos/carpeta.png"));
            iconosTipos[2] = new ImageIcon(getClass().getResource("/recursos/tipos/doc.png"));
            iconosTipos[3] = new ImageIcon(getClass().getResource("/recursos/tipos/pdf.png"));
            iconosTipos[4] = new ImageIcon(getClass().getResource("/recursos/tipos/zip.png"));
            iconosTipos[5] = new ImageIcon(getClass().getResource("/recursos/tipos/jpg.png"));
            iconosTipos[6] = new ImageIcon(getClass().getResource("/recursos/tipos/png.png"));
            iconosTipos[7] = new ImageIcon(getClass().getResource("/recursos/tipos/txt.png"));
        
            iconosAccionesArchivos[0] = new ImageIcon(getClass().getResource("/recursos/acciones/abrir.png"));
            iconosAccionesArchivos[1] = new ImageIcon(getClass().getResource("/recursos/acciones/cambiar_nombre.png"));
            iconosAccionesArchivos[2] = new ImageIcon(getClass().getResource("/recursos/acciones/copiar.png"));
            iconosAccionesArchivos[3] = new ImageIcon(getClass().getResource("/recursos/acciones/cortar.png"));
            iconosAccionesArchivos[4] = new ImageIcon(getClass().getResource("/recursos/acciones/hacer_copia.png"));
            iconosAccionesArchivos[5] = new ImageIcon(getClass().getResource("/recursos/acciones/borrar.png"));
            iconosAccionesArchivos[6] = new ImageIcon(getClass().getResource("/recursos/acciones/contenido.png"));
        
            iconosAccionesCarpetaL[0] = new ImageIcon(getClass().getResource("/recursos/acciones/crear_archivo.png"));
            iconosAccionesCarpetaL[1] = new ImageIcon(getClass().getResource("/recursos/acciones/crear_carpeta.png"));
            iconosAccionesCarpetaL[2] = new ImageIcon(getClass().getResource("/recursos/acciones/pegar.png"));
        }
        catch(Exception e)
        {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo crear la nueva carpeta. ",
                    "Mensaje", javax.swing.JOptionPane.PLAIN_MESSAGE);
        }
                
        
    }
    private void inicializarPopUpMenu()
    {
        JMenuItem[] menuItemsArchivos = new JMenuItem[ControlDeArchivos.accionesArchivo.length];
        JMenuItem[] menuItemsCarpeta = new JMenuItem[ControlDeArchivos.accionesCarpetaLocal.length];
        
        int i = 0;
        for(JMenuItem elemento : menuItemsArchivos)
        {
            elemento = new JMenuItem(ControlDeArchivos.accionesArchivo[i]);        
            if(iconosAccionesArchivos[i] != null) elemento.setIcon(iconosAccionesArchivos[i]);
            else elemento.setIcon(iconosAccionesArchivos[0]);
            
            elemento.addActionListener(this::menuitemActionPerformed);
           
            popmArchivos.add(elemento);
            i++;
        }
        
        i = 0;
        for(JMenuItem elemento : menuItemsCarpeta)
        {
            elemento = new JMenuItem(ControlDeArchivos.accionesCarpetaLocal[i]);
            if(iconosAccionesCarpetaL[i] != null) elemento.setIcon(iconosAccionesCarpetaL[i]);
            else elemento.setIcon(iconosAccionesCarpetaL[0]);
            
            elemento.addActionListener(this::menuitemActionPerformed);
            
            popmCarpetaLocal.add(elemento);
            i++;
        }
        tblArchivos.setComponentPopupMenu(popmArchivos);
        this.setComponentPopupMenu(popmCarpetaLocal);
        spanTabla.setComponentPopupMenu(popmCarpetaLocal);
    }
    private JLabel asignarIcono(JLabel label, File archivo)
    {
        String tipo = getNombre_Y_Tipo(archivo)[1];
        label.setIcon(iconosTipos[0]);
        if(archivo.isDirectory())
            label.setIcon(iconosTipos[1]);
        else if(tipo.equals("docx"))
            label.setIcon(iconosTipos[2]);
        else if(tipo.equals("pdf"))
            label.setIcon(iconosTipos[3]);
        else if(tipo.equals("zip"))
            label.setIcon(iconosTipos[4]);
        else if(tipo.equals("jpg"))
            label.setIcon(iconosTipos[5]);
        else if(tipo.equals("png"))
            label.setIcon(iconosTipos[6]);
        else if(tipo.equals("txt"))
            label.setIcon(iconosTipos[7]);
            
        return label;
    }
    public static String[] getNombre_Y_Tipo(File archivo)
    {
        String nombre = archivo.getName();
        String tipo = "", nom = "";
        if(archivo.isDirectory()) { tipo = "Carpeta de archivos"; nom = nombre;}
            else                    
            {
                int j = nombre.lastIndexOf('.');
                if (j > 0) 
                { 
                    tipo = nombre.substring(j+1);
                    nom = nombre.substring(0, j);
                }     
            }
        return new String[]{nom,tipo};
    }
    private String getTamaño(File archivo)
    {
	DecimalFormat df = new DecimalFormat("#.00");
	float longitud = archivo.length();
        String tamaño = "";
        String[] b = {"Gb","Mb","Kb","b"};
        int i = 0;

        if(!archivo.isDirectory())
        {
            if(longitud>1024000000)
            {
                tamaño = df.format(longitud/1024000000);
            }      
	    else if(longitud>1024000)
            {
                tamaño = df.format(longitud/1024000);
                i = 1;
            }
	    else if(longitud>1024)
            {
                tamaño = df.format(longitud/1024);
                i = 2;
            }    
	    else
            {
                tamaño = df.format(longitud);
                i = 3;
            }	        
        }
        if(tamaño.endsWith(".00")) tamaño = tamaño.substring(0, tamaño.length() - 3);
        if(!tamaño.equals("")) tamaño = tamaño + " " + b[i]; 
        return tamaño;
    }
    private ArrayList<File> Ordenar(File[] original)
    {
        ArrayList<File> nuevo = new ArrayList<>();
        for(File elemento : original)
            if(elemento.isDirectory()) nuevo.add(elemento);
        for(File elemento : original)
            if(!elemento.isDirectory()) nuevo.add(elemento);
        return nuevo;
    }
    public String getRutaSeleccionada()
    {
        int i = tblArchivos.getSelectedRow();
        if(i>=0)
        return listaArchivosLocales.get(i).getAbsolutePath();
        return "";
    }
    public JTable getTablaArchivos()
    {
        return tblArchivos;
    }
    public JComboBox getComboBoxArchivoAcciones()
    {
        return cbxArchivosAcciones;
    }
    public JLabel getLabelSeleccionado()
    {
        return lblSeleccionado;
    }
    public JPopupMenu getPopupMenuArchivos()
    {
        return popmArchivos;
    }
    public JScrollPane getScrollPaneTabla()
    {
        return spanTabla;
    }
    public void lblCopiado_setText(String copiado, String cortado)
    {
        lblCopiado.setText(copiado);
        lblCortado.setText(cortado);
    }
    private void accionArchivo(int valorSeleccionado, boolean esArchivoSeleccionado)
    {
        String ruta = getRutaSeleccionada();
        File archivo = new File(ruta);
        if(esArchivoSeleccionado)
        {
            switch(valorSeleccionado)
           {
                case 0: 
                    ctrlArchivos.abrir(archivo);
                    break;
                case 1: 
                    ctrlArchivos.cambiarNombre(archivo);
                    break;
                case 2: 
                    ctrlArchivos.copiar(archivo);
                    break;
                case 3:
                    ctrlArchivos.cortar(archivo);
                    break;
                case 4: 
                    ctrlArchivos.hacerCopia(archivo);
                    break;
                case 5: 
                    ctrlArchivos.borrar(archivo);
                    break;
                case 6:
                    ctrlArchivos.mostrarContenido(archivo);
           }
        }
        else
        {
           switch(valorSeleccionado)
            {
                case 0:
                    ctrlArchivos.crearArchivo();
                    break;
                case 1:
                    ctrlArchivos.crearCarpeta();
                    break;
                case 2:
                    ctrlArchivos.pegar();
                    break;
            }
        }
        
        
    }
    private void ocultarSeleccionadorAcciones()
    {
        cbxArchivosAcciones.setVisible(false);
        btnArchivoAccion.setVisible(false);
    }
    
    
    private void initMyComponets()
    {
        ctrlArchivos = new ControlDeArchivos(this);
        ctrlArchivos.actualizarJLabelsCopiado();
        
        ocultarSeleccionadorAcciones();
        agregarIconos();
        inicializarPopUpMenu();
        tblArchivos.setDefaultRenderer(Object.class, new ColumnRender());
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popmArchivos = new javax.swing.JPopupMenu();
        popmCarpetaLocal = new javax.swing.JPopupMenu();
        spanTabla = new javax.swing.JScrollPane();
        tblArchivos = new javax.swing.JTable();
        lblCarpetaVacia = new javax.swing.JLabel();
        cbxArchivosAcciones = new javax.swing.JComboBox<>();
        btnArchivoAccion = new javax.swing.JButton();
        lblTituloSeleccionado = new javax.swing.JLabel();
        lblSeleccionado = new javax.swing.JLabel();
        lblTituloCopiado = new javax.swing.JLabel();
        lblCopiado = new javax.swing.JLabel();
        lblTituloCortado = new javax.swing.JLabel();
        lblCortado = new javax.swing.JLabel();

        setBorder(null);
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                formAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        spanTabla.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tblArchivos.setBackground(java.awt.SystemColor.control);
        tblArchivos.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        tblArchivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Nombre", "Fecha de Modificación", "Tipo", "Tamaño", "Oculto", "Solo lectura"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblArchivos.setGridColor(java.awt.SystemColor.control);
        tblArchivos.setRowHeight(24);
        spanTabla.setViewportView(tblArchivos);
        if (tblArchivos.getColumnModel().getColumnCount() > 0) {
            tblArchivos.getColumnModel().getColumn(0).setResizable(false);
            tblArchivos.getColumnModel().getColumn(0).setPreferredWidth(16);
            tblArchivos.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblArchivos.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblArchivos.getColumnModel().getColumn(3).setPreferredWidth(120);
            tblArchivos.getColumnModel().getColumn(5).setPreferredWidth(50);
            tblArchivos.getColumnModel().getColumn(6).setPreferredWidth(60);
        }

        lblCarpetaVacia.setForeground(new java.awt.Color(153, 153, 153));
        lblCarpetaVacia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnArchivoAccion.setText("Acción");
        btnArchivoAccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArchivoAccionActionPerformed(evt);
            }
        });

        lblTituloSeleccionado.setForeground(java.awt.SystemColor.controlDkShadow);
        lblTituloSeleccionado.setText("Seleccionado: ");

        lblSeleccionado.setForeground(java.awt.SystemColor.textHighlight);

        lblTituloCopiado.setForeground(java.awt.SystemColor.controlDkShadow);
        lblTituloCopiado.setText("Archivo copiado:");

        lblCopiado.setForeground(new java.awt.Color(153, 0, 0));
        lblCopiado.setText(" ");

        lblTituloCortado.setForeground(java.awt.SystemColor.controlDkShadow);
        lblTituloCortado.setText("Cortado:");

        lblCortado.setForeground(new java.awt.Color(0, 153, 0));
        lblCortado.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(spanTabla, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTituloCopiado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCopiado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cbxArchivosAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnArchivoAccion))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblTituloSeleccionado)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblSeleccionado, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)))
                                .addGap(7, 7, 7))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblTituloCortado)
                                .addGap(13, 13, 13)
                                .addComponent(lblCortado, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                .addGap(4, 4, 4)))
                        .addGap(14, 14, 14))
                    .addComponent(lblCarpetaVacia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spanTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCarpetaVacia, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxArchivosAcciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnArchivoAccion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTituloSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(lblCopiado))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTituloCopiado)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCortado)
                    .addComponent(lblTituloCortado))
                .addContainerGap(259, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorMoved
        setLocation(0,0);
    }//GEN-LAST:event_formAncestorMoved

    private void btnArchivoAccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArchivoAccionActionPerformed
        accionArchivo(cbxArchivosAcciones.getSelectedIndex(), estadoDeSeleccion);
    }//GEN-LAST:event_btnArchivoAccionActionPerformed

    private void menuitemActionPerformed(java.awt.event.ActionEvent evt)      
    {
        JMenuItem menuitemFuente = (JMenuItem)evt.getSource();
        String accion = menuitemFuente.getText();
        int indexSelected = -1;
        for (int i = 0; i < ControlDeArchivos.accionesArchivo.length; i++) {
            if(accion.equals(ControlDeArchivos.accionesArchivo[i]))
                indexSelected = i;
        }
        for (int i = 0; i < ControlDeArchivos.accionesCarpetaLocal.length; i++) {
            if(accion.equals(ControlDeArchivos.accionesCarpetaLocal[i]))
                indexSelected = i;
        }
        
        accionArchivo(indexSelected, estadoDeSeleccion);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnArchivoAccion;
    private javax.swing.JComboBox<String> cbxArchivosAcciones;
    private javax.swing.JLabel lblCarpetaVacia;
    private javax.swing.JLabel lblCopiado;
    private javax.swing.JLabel lblCortado;
    private javax.swing.JLabel lblSeleccionado;
    private javax.swing.JLabel lblTituloCopiado;
    private javax.swing.JLabel lblTituloCortado;
    private javax.swing.JLabel lblTituloSeleccionado;
    private javax.swing.JPopupMenu popmArchivos;
    private javax.swing.JPopupMenu popmCarpetaLocal;
    private javax.swing.JScrollPane spanTabla;
    private javax.swing.JTable tblArchivos;
    // End of variables declaration//GEN-END:variables
}
