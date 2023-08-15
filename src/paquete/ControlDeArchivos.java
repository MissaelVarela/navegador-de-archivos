
package paquete;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ControlDeArchivos {
    
    private final ifrmCarpeta ifrmPadre;
    static String[] accionesArchivo = new String[]{"Abrir", "Cambiar nombre", "Copiar", "Cortar", "Hacer copia", "Borrar", "Mostrar contenido"};
    static String[] accionesCarpetaLocal = new String[]{"Crear archivo", "Crear carpeta", "Pegar"};

    public ControlDeArchivos(ifrmCarpeta ifrmCarpeta) {
        this.ifrmPadre = ifrmCarpeta;
    }
    
    public void abrir(File archivo)
    {
        try
        {
            if(archivo.isDirectory())
                ifrmPadre.navPadre.AbrirCarpeta(archivo);
            else
            {
                Desktop.getDesktop().open(archivo);
            }   
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(ifrmPadre, "No se pudo abrir el archivo.", "Aviso", JOptionPane.PLAIN_MESSAGE);
        }          
    }
    public void cambiarNombre(File archivo)
    {
        String nuevoNombre = JOptionPane.showInputDialog(ifrmPadre, "Nuevo nombre:", "Cambiar nombre", JOptionPane.PLAIN_MESSAGE);
        if(nuevoNombre == null || nuevoNombre.trim().equals(""))
        {
            JOptionPane.showMessageDialog(ifrmPadre, "No se pudo cambiar el nombre. Campo vacio. ", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        String path = copiarPathFile(archivo, nuevoNombre);
        
        File nueva = new File(path);
        boolean success = archivo.renameTo(nueva);
        if(success)
        {
            ifrmPadre.navPadre.Actualizar();
        }       
        else
        {
            JOptionPane.showMessageDialog(ifrmPadre, "Ya existe un archivo con el mismo nombre y el mismo tipo. ", "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }
    public void copiar(File archivo)
    {
        ifrmPadre.navPadre.cortado = false;
        ifrmPadre.navPadre.copiado = archivo;
        actualizarJLabelsCopiado();
    }
    public void cortar(File archivo)
    {
        ifrmPadre.navPadre.cortado = true;
        ifrmPadre.navPadre.copiado = archivo;
        actualizarJLabelsCopiado();
    }
    public void hacerCopia(File original)
    {
        String nombre = JOptionPane.showInputDialog(ifrmPadre, "Agregar nombre:", "Copiar archivo", JOptionPane.PLAIN_MESSAGE);
        if(nombre == null || nombre.trim().equals(""))
        {
            JOptionPane.showMessageDialog(ifrmPadre, "No se pudo copiar. Campo vacio.", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        String path = copiarPathFile(original, nombre);
        
        File copia = new File(path);
        if(copia.exists()) 
        {
            JOptionPane.showMessageDialog(ifrmPadre, "Ya existe un archivo con ese nombre y tipo. ", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        boolean success;
        if(original.isDirectory()) 
            success = copiarDirectory(original, copia);
        else
            success = copiarFile(original, copia);
        
        if(!success)
        {
            JOptionPane.showMessageDialog(ifrmPadre, "No se pudo copiar. ", "Error", JOptionPane.PLAIN_MESSAGE);
        }
        ifrmPadre.navPadre.Actualizar();
    }
    public void borrar(File archivo)
    {
        int i = JOptionPane.showConfirmDialog(ifrmPadre, "¿Estas seguro de borrar el archivo " + archivo.getName() 
                + "? Se borrara para siempre.", "Confirmar", JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
        if(i == 0)
        {
            if(archivo.isDirectory())
            {
                boolean success = borrarDirectory(archivo);
                if(!success) JOptionPane.showMessageDialog(ifrmPadre, "No se pudo borrar por completo la carpeta. ", "Error", JOptionPane.PLAIN_MESSAGE);
            }    
            else
                archivo.delete();
            ifrmPadre.navPadre.Actualizar();
        }
        if(i == 2)
        {
            //cancel
        }
                
    }
    public void mostrarContenido(File archivo)
    {
        if(!archivo.isDirectory())
        {
            frmContenidoArchivo contenido = new frmContenidoArchivo(archivo);
            contenido.setVisible(true);
        }
        else
        {
            JOptionPane.showMessageDialog(ifrmPadre, "Solo se puede mostrar el contenido de un solo archivo en especifico. No carpetas. ", "Mensaje", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    public void crearArchivo()
    {
        String nombre = JOptionPane.showInputDialog(ifrmPadre, "Agregar nombre:", "Crear nuevo archivo", JOptionPane.PLAIN_MESSAGE);
        String tipo = JOptionPane.showInputDialog(ifrmPadre, "Agregar tipo (.txt, .docx)", "Crear nuevo archivo", JOptionPane.PLAIN_MESSAGE);
        if(nombre == null || tipo == null || nombre.trim().equals("") || tipo.trim().equals("")) 
        {
            JOptionPane.showMessageDialog(ifrmPadre, "No se pudo crear el nuevo archivo. ", "Mensaje", JOptionPane.PLAIN_MESSAGE);
            return;
        } 
        if(!tipo.startsWith(".")) tipo = "." + tipo;
        crearFile(nombre, tipo, false);
    }
    public void crearCarpeta()
    {
        String nombre = JOptionPane.showInputDialog(ifrmPadre, "Agregar nombre:", "Crear nueva carpeta", JOptionPane.PLAIN_MESSAGE);
        if(nombre == null || nombre.trim().equals("")) 
        {
            JOptionPane.showMessageDialog(ifrmPadre, "No se pudo crear la nueva carpeta. ", "Mensaje", JOptionPane.PLAIN_MESSAGE);
            return;
        } 
        crearFile(nombre, "", true);
    }
    public boolean pegar()
    {
        if(ifrmPadre.navPadre.copiado != null)
        {
            File copiado = ifrmPadre.navPadre.copiado;
            File copia;
            String[] nom_tip = ifrmCarpeta.getNombre_Y_Tipo(copiado);
            if(ifrmPadre.navPadre.cortado)
            {
                String path = ifrmPadre.carpetaLocal.getAbsolutePath() + "\\" + copiado.getName();
                File nuevo = new File(path); 
                
                if(nuevo.exists()) 
                {
                    limpiarCopiado();
                    JOptionPane.showMessageDialog(ifrmPadre, "Ya existe un archivo con ese nombre y tipo. ", "Error", JOptionPane.PLAIN_MESSAGE);
                    return false;
                } 
            } 
            if(copiado.isDirectory())
            {
                if(esSubCarpeta(ifrmPadre.carpetaLocal, copiado))
                {
                    limpiarCopiado();
                    JOptionPane.showMessageDialog(ifrmPadre, "La carpeta destino es una subcarpeta de la carpeta origen. ", "Error", JOptionPane.PLAIN_MESSAGE);
                    return false;
                }
            }
            if(copiado.isDirectory())
            {
                copia = getDirectoryConNombreValido(nom_tip[0], ifrmPadre.carpetaLocal);
            }
            else
            {
                copia = getFileConNombreValido(nom_tip[0], nom_tip[1], ifrmPadre.carpetaLocal);
            }   
            
            boolean success;
            if(copiado.isDirectory())
                success = copiarDirectory(copiado, copia);
            else
                success = copiarFile(copiado, copia);
            if(!success)
            {
                JOptionPane.showMessageDialog(ifrmPadre, "No se pudo pegar. ", "Error", JOptionPane.PLAIN_MESSAGE);
                limpiarCopiado();
                return false;
            }
            else
            {
                if(ifrmPadre.navPadre.cortado) //Si se pegó un elemento que fue cortado, borrar el orginal despues de pegar la copia.
                {
                    if(copiado.isDirectory())
                        borrarDirectory(copiado);
                    else
                        copiado.delete();
                    limpiarCopiado();
                }
                ifrmPadre.navPadre.Actualizar();
                return true;        
            } 
        }
        return false;
    }
    
    
    private boolean copiarFile(File original, File copia) 
    {
        if (original.exists()) 
        {
            try 
            {
                InputStream in = new FileInputStream(original);
                OutputStream out = new FileOutputStream(copia);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                copia.createNewFile();
                return true;
            } 
            catch (IOException ioe) 
            {
                return false;
            }
        } 
        else 
        {
            return false;
        }
    }
    private boolean copiarDirectory(File original_carpeta, File copia_carpeta)
    {
        if(!copia_carpeta.exists())
        {
            copia_carpeta.mkdir();
            if(original_carpeta.listFiles().length != 0)
            {
                for (File elemento : original_carpeta.listFiles()) 
                {
                    String path = copia_carpeta.getAbsolutePath() + "\\" + elemento.getName();
                    File nueva = new File(path);
                    if(elemento.isDirectory())
                        copiarDirectory(elemento, nueva);
                    else
                        copiarFile(elemento, nueva);
                }
            }
            return true;
        }
        return false;
    }
    private String copiarPathFile(File original, String nuevoNombre)
    {
        String archivo_tipo = ifrmCarpeta.getNombre_Y_Tipo(original)[1];
        String localPath = ifrmPadre.carpetaLocal.getAbsolutePath();
        String path = localPath + "\\" + nuevoNombre;
        if(!original.isDirectory())
            path += "." + archivo_tipo;
        return path;
    }
    private File getFileConNombreValido(String nombre, String tipo, File carpetaLocal) 
    {
        String path = carpetaLocal.getAbsolutePath() + "\\" + nombre + "." + tipo;
        File nuevo = new File(path);
        if(nuevo.exists())
        {
            return getFileConNombreValido( nombre + " - copia", tipo, carpetaLocal);          
        }
        return nuevo;
    }
    private File getDirectoryConNombreValido(String nombre, File carpetaLocal) 
    {
        String path = carpetaLocal.getAbsolutePath() + "\\" + nombre;
        File nuevo = new File(path);
        if(nuevo.exists())
        {
            return getDirectoryConNombreValido( nombre + " - copia", carpetaLocal);          
        }
        return nuevo;
    }
    private void crearFile(String nombre, String tipo, boolean isDirectory)
    {
        String path = ifrmPadre.carpetaLocal.getAbsolutePath() + "\\" + nombre + tipo;
        File nuevo = new File(path);
        if(!nuevo.exists())
        {
            try 
            { 
                int i = JOptionPane.showConfirmDialog(ifrmPadre, "Se creará el nuevo archivo", "Confirmar", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(i == 0)
                {
                    if(isDirectory)
                        nuevo.mkdir();
                    else
                        nuevo.createNewFile();
                    ifrmPadre.navPadre.Actualizar();
                }
            }
            catch(IOException e){ JOptionPane.showMessageDialog(ifrmPadre, "No se pudo crear archivo. ", "Error", JOptionPane.PLAIN_MESSAGE); }  
        }
        else
            JOptionPane.showMessageDialog(ifrmPadre, "Ya existe un archivo con este nombre y tipo. ", "Error", JOptionPane.PLAIN_MESSAGE);
        
    }
    private void limpiarCopiado()
    {
        ifrmPadre.navPadre.cortado = false;
        ifrmPadre.navPadre.copiado = null;
        actualizarJLabelsCopiado();
    }
    private boolean borrarDirectory(File carpeta)
    {
        if(carpeta.listFiles().length != 0)
        {
            File[] archivos = carpeta.listFiles();
            for (File elemento : archivos)
            {
                if(elemento.isDirectory())
                    borrarDirectory(elemento);
                else
                    elemento.delete();
            }
        }
        if(carpeta.listFiles().length == 0)
        {
            carpeta.delete();
            return true;
        }
        else 
        {
            return false;
        }     
    }
    private boolean esSubCarpeta(File destino, File origen)
    {
        File current = destino;
        while(current != null)
        {
            if(current.equals(origen))
                return true;
            current = current.getParentFile();
        }
        return false;
    }
    public void actualizarJLabelsCopiado()
    {
        String cortado = (ifrmPadre.navPadre.cortado)? "true" : "";
        String archivo = (ifrmPadre.navPadre.copiado != null)? ifrmPadre.navPadre.copiado.getName() : "";
        ifrmPadre.lblCopiado_setText(archivo, cortado); 
    }
}
