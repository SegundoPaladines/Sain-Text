public class Variable {

    private String nombre;
    private String tipo;
    private String valor;
    private int linea;
    Lexema padre;

    public Variable(String nombre, String tipo, int linea){
        this.nombre = nombre;
        this.tipo = tipo;
        this.linea = linea;
    }

    public Variable(String nombre, String tipo, String valor){
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
    }

    public void setValor(String valor){
        this.valor = valor;
    }
    public String getNombre(){
        return this.nombre;
    }
    public String getTipo(){
        return this.tipo;
    }
    public String getValor(){
        return this.valor;
    }
    public int getLinea(){
        return this.linea;
    }
    public void setPadre(Lexema padre){
        this.padre=padre;
    }
    public Lexema getPadre(){
        return this.padre;
    }
}
