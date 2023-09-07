public class Pila
{
    Nodo primero;
    public boolean estado;

    public Pila(){
        this.primero=null;
        this.estado=true;
    } 
    public void apilar()
    {
        Nodo nuevo = new Nodo();
        if (this.primero == null){
            this.primero = nuevo;
        }
        else{
            nuevo.siguiente=this.primero;
            this.primero = nuevo;
        }        
    }
    public void desApilar(){
        if(this.primero==null){

            this.estado=false;

        }else{
            this.primero=this.primero.siguiente;
        }
        
    }
    public boolean validarPila(){

        if(this.primero!=null){
            this.estado=false;
        }
        return this.estado;

    }
    public void LimpiarPila() {
        if(this.primero!=null){this.primero=null;}
    }
}