public class Nodo
{
    public Nodo siguiente;
    public String codigo;

    
    public Nodo()
    {
        this.siguiente=null;
    }
    
    public Nodo getSiguiente(){
        return siguiente;
    }
        
    public void setSiguiente(Nodo siguiente){
        this.siguiente = siguiente;
    }
}