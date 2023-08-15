
package paquete;

import java.io.File;
import java.util.Stack;

public class ManejoVolverAvanzar {

    public Stack<File> pila_atras;
    public Stack<File> pila_adelante;
    
    public ManejoVolverAvanzar()
    {
        pila_atras = new Stack<>();
        pila_adelante = new Stack<>();
    }
    
    public void Agregar(File carpeta)
    {
        if(pila_atras.isEmpty() || (pila_atras.peek() != carpeta)){
            pila_atras.add(carpeta);
        }
    }
    public File Volver(File actual)
    {
        pila_adelante.add(actual);
        return pila_atras.pop();
    }
    public File Avanzar()
    {
        return pila_adelante.pop();
    }

            
}
