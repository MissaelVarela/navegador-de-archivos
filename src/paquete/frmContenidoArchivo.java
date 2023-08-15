
package paquete;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class frmContenidoArchivo extends javax.swing.JFrame {

    public frmContenidoArchivo() {
        initComponents();
    }
    public frmContenidoArchivo(File archivo){
        initComponents();
        abrir_archivo(archivo);
    }
    private boolean abrir_archivo(File archivo)
    {
        lblNombreArchivo.setText("Contenido del archivo: " + archivo.getName());
        String tipo = ifrmCarpeta.getNombre_Y_Tipo(archivo)[1];
        boolean es_txt = tipo.equals("txt");
        try 
        {
            String cadena;
            FileReader reader = new FileReader(archivo);
            BufferedReader buf = new BufferedReader(reader);
            String contenido = "";
            while((cadena = buf.readLine())!= null) {
                if(!es_txt) cadena = toHexadecimal(cadena);
                contenido += "\n" + cadena;
            }
            txtSalidaContenido.setText(contenido);    
            buf.close();
        } 
        catch (IOException ioe) 
        {
            return false;
        }
        return true;
    }
    private String toHexadecimal(String cadena)
    {
        String salida = "";
        char[] caracteres = cadena.toCharArray();
        for (int i = 0; i < caracteres.length; i++) {
            salida += decToHex(caracteres[i]) + " ";
        }
        return salida;
    }  
    public static String decToHex(int dec) 
    { 
        int sizeOfIntInHalfBytes = 8; 
        int numberOfBitsInAHalfByte = 4; 
        int halfByte = 0x0F; 
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', 
                             '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'  }; 
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes); 
        hexBuilder.setLength(sizeOfIntInHalfBytes); 
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i) 
        { 
            int j = dec & halfByte; 
            hexBuilder.setCharAt(i, hexDigits[j]); 
            dec >>= numberOfBitsInAHalfByte; 
        } 
        return hexBuilder.toString(); 
    }     
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombreArchivo = new javax.swing.JLabel();
        spanSalidaContenido = new javax.swing.JScrollPane();
        txtSalidaContenido = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblNombreArchivo.setText("Contenido del archivo:");

        txtSalidaContenido.setColumns(20);
        txtSalidaContenido.setRows(5);
        spanSalidaContenido.setViewportView(txtSalidaContenido);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spanSalidaContenido, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                    .addComponent(lblNombreArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNombreArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spanSalidaContenido, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {
        
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
            java.util.logging.Logger.getLogger(frmContenidoArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmContenidoArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmContenidoArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmContenidoArchivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmContenidoArchivo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblNombreArchivo;
    private javax.swing.JScrollPane spanSalidaContenido;
    private javax.swing.JTextArea txtSalidaContenido;
    // End of variables declaration//GEN-END:variables
}
