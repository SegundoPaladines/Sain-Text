import java.util.ArrayList;

public class Lexema {
    private int numero_linea;
    private String texto;
    private String tipo_lexema;
    private Lexema padre;

    public Lexema(int numero_linea, String texto){
        this.numero_linea=numero_linea;
        this.texto = texto;
    }
    public int getNumeroLinea(){
        return this.numero_linea;
    }
    public String getTexto(){
        return this.texto;
    }
    public String getTipoLexema(){
        return this.tipo_lexema;
    }
    public Lexema getPadre(){
        return this.padre;
    }
    public void setTipoLexema(String tipo){
        this.tipo_lexema=tipo;
    }
    public void setPadre(Lexema padre){
        this.padre = padre;
    }

    public static ArrayList <Lexema> ExtraerLexemas(String codigo){ 
        ArrayList <Lexema> lexemas = new ArrayList<Lexema>();
        int contador=0;
        int i=0;
        while(contador<codigo.length()){
            String linea ="";
            char letra=codigo.charAt(contador);
            while(letra != '\n'){
                if(letra != '\n'){
                    linea = linea +letra;
                }
                contador++;
                if(contador<codigo.length()){
                    letra=codigo.charAt(contador);
                }else{letra='\n';}
            }
            contador++;
            i++;
            lexemas.add(new Lexema(i, linea));
        }
        return lexemas;
    }
}
