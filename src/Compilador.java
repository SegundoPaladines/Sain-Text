import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compilador {

    //**************Palabras Reservadas****************** */
    private String [] p_reservadas;
    //tipos de dato
    private String [] t_dato;
    //tipos operadpres
    private String [] operadores;
    //************** FIn Palabras Reservadas****************** */

    private Pattern variable = Pattern.compile("^[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*$");
    
    private String resultado;
    private ArrayList<Variable> varibales_globales;

    public Compilador(){
        resultado = "";
        varibales_globales = new ArrayList<Variable>();

        String [] palabras_reservadas = {"Para",
                                        "FinPara",
                                        "Hasta",
                                        "Paso",
                                        "Si",
                                        "Entonces",
                                        "SiNo",
                                        "SiNo Si",
                                        "Funcion",
                                        "verdadero",
                                        "falso"

        };

        p_reservadas = palabras_reservadas;

        String [] tipos_datos = {"entero",
                                "cadena",
                                "real",
                                "caracter",
                                "fecha",
                                "boleano"
        };
                
        t_dato = tipos_datos;

        String [] op = {"+",
                        "-",
                        "*",
                        "/"
        };

        operadores = op;
    }

    public String Compilar(String resultado_completo, ArrayList<Lexema> lexemas){

        resultado = resultado_completo;

        boolean es_valido = AnalizarLogica(lexemas);
        if(es_valido){
            //**********segmento para traducir archivo a java */
            resultado = resultado +"\n traducioendo resaultado a java";
        }else{
            resultado = "\n El Codigo Contiene Errores Logicos:"+"\n"+resultado;
        }        
        return resultado;
    }

    public boolean AnalizarLogica(ArrayList<Lexema> lexemas){
        boolean es_valido =  obtenerVariables(lexemas,null,varibales_globales);
        if(es_valido){
            int k = 0;
            while((k!=-1)&&(k<lexemas.size())){

                int suma = ejecutarLexema(lexemas.get(k), k, null, lexemas, null);

                k=suma;
            }
            if(k==-1){
                es_valido=false;
            }

        }
        return es_valido;
    }

    private int ejecutarLexema(Lexema lexema, int suma, Lexema padre, ArrayList<Lexema> lexemas, ArrayList<Variable>variables_locales) {

        String tipo = lexema.getTipoLexema();
            switch (tipo) {
                /***Cuando es una variable y se le asigna un boleano* */
                case "asignacion-variable=boleano":

                    String nombre = obtenerNombreVariable(lexema.getTexto());
                    String tipo_variable = obtenerTipoVariable(lexema.getTexto());

                    if(tipo_variable==""){
                        if((lexema.getTexto().indexOf("verdadero")!=-1)||((lexema.getTexto().indexOf("falso")!=-1))){
                            tipo_variable="boleano";
                        }else{
                            tipo_variable = "invalida";
                        }
                    }

                    if(lexema.getPadre()==padre){
                        int pos = -1;
                        for(int i= 0; i<varibales_globales.size(); i++) {
                            if(nombre.equals(varibales_globales.get(i).getNombre())){
                                pos = i;
                            }
                        }
                        if(pos!=-1){

                            if(tipo_variable.equals(varibales_globales.get(pos).getTipo())){
                                varibales_globales.get(pos).setValor(obtenerValorVariable(lexema.getTexto(), tipo_variable));
                                suma++;
                            }else{
                                resultado = resultado +"\n Error en la linea:" +lexema.getNumeroLinea()+" la variable "+nombre+" no coincide con el tipo de dato que se le está asignando";
                                suma = -1;
                            }

                        }else{
                            if(variables_locales!=null){
                                for(int i= 0; i<variables_locales.size(); i++) {
                                    if(nombre.equals(variables_locales.get(i).getNombre())){
                                        pos = i;
                                    }
                                }
                            }else{pos=-1;}
                            if(pos!=-1){
                                if(tipo_variable.equals(variables_locales.get(pos).getTipo())){
                                    variables_locales.get(pos).setValor(obtenerValorVariable(lexema.getTexto(), tipo_variable));
                                    suma++;
                                }else{
                                    resultado = resultado +"\n Error en la linea:" +lexema.getNumeroLinea()+" la variable "+nombre+" no coincide con el tipo de dato que se le está asignando";
                                    suma = -1;
                                }
                            }else{
                                resultado = resultado +"\n Error en la linea:" +lexema.getNumeroLinea()+" la variable "+nombre+" no fue inicializada";
                                suma = -1;
                            }
                        }
                    }else{
                        suma++;
                    }

                    break;

                /***Cuando es una variable y se le otra variable* */
                case "asignacion-variable=variable":
                        suma++;
                    break;
                
                 /***Cuando es una variable y se le asigna un entero* */
                case "asignacion-variable=entero":
                    suma++;
                    break;

                /***Cuando es una variable y se le asigna un real* */
                case "asignacion-variable=real":
                    suma++;
                    break;

                /***Cuando es una variable y se le asigna un caracter* */
                case "asignacion-variable=caracter":
                    suma++;
                    break;

                /***Cuando es una variable y se le asigna una fecha* */
                case "asignacion-variable=fecha":
                    suma++;
                    break;
                
                /***Cuando es una variable y se le asigna una cadena* */
                case "asignacion-variable=cadena":
                    suma++;
                    break;
                
                /***Cuando es una variable y se le asigna una expresion aritmetica* */
                case "expresion-aritmetica":
                    suma++;
                    break;

                /***Cuando es una variable y se le asigna una concatenacion* */
                case "concatenacion":
                    suma++;
                    break;
                
                /***imprimir* 
                 * @param lexemas*/
                case "imprimir":
                    suma++;
                    break;

                case "variable":
                    suma++;
                    break;

                default: suma=ejecutarSeccion(suma, lexemas);
                    break;
            }
        return suma;
    }

    private int ejecutarSeccion(int suma, ArrayList<Lexema> lexemas) {

        Lexema padre = lexemas.get(suma);
        ArrayList<Variable>varibales_locales = obtenerVariablesLocales(padre, lexemas);
        String tipo = lexemas.get(suma).getTipoLexema();
        switch(tipo){
             /***inicio secuencia si* */
            case "inicio-si":

                int fin_secuencia = ObtenerFinSecuencia(lexemas, padre);
                String texto = padre.getTexto();
                String variable1 = "";
                if((texto.indexOf("=")!=-1)|(texto.indexOf("<")!=-1)|(texto.indexOf(">")!=-1)){
                    String variable2 ="";
                    texto = texto.substring(texto.indexOf("("), texto.indexOf(")"));
                    texto = texto.replaceAll("==", "-");
                    texto = texto.replaceAll("!=", "-");
                    texto = texto.replaceAll("<=", "-");
                    texto = texto.replaceAll(">=", "-");
                    texto = texto.replaceAll("<", "-");
                    texto = texto.replaceAll(">", "-");
                    texto = texto.replaceAll("\\(", "");
                    texto = texto.replaceAll("\\)", "");
                    texto = texto.replaceAll("\s", "");

                    variable1 = texto.substring(0, texto.indexOf("-"));
                    variable2 = texto.substring(texto.indexOf("-"), texto.length());
                    variable1 = variable1.replaceAll("-", "");
                    variable2 = variable2.replaceAll("-", "");

                    boolean existe1 = existeVariable(variable1, obtenerVariablesLocales(padre.getPadre(), lexemas));
                    boolean existe2 = existeVariable(variable2, obtenerVariablesLocales(padre.getPadre(), lexemas));
                    if(existe1){
                        if(existe2){

                            String valor1 = obtenerValorAsigado(variable1, varibales_locales);
                            String valor2 = obtenerValorAsigado(variable2, varibales_locales);
                            
                            if(valor1.equals("")){
                                resultado = resultado + "\n Errror en la linea" + padre.getNumeroLinea()+" "+variable1+" no tiene valor asignado";
                            }else{
                                if(valor2.equals("")){
                                    resultado = resultado + "\n Errror en la linea" + padre.getNumeroLinea()+" "+variable2+" no tiene valor asignado";
                                }else{

                                    int inicio = suma+1;
                                    while(inicio<fin_secuencia){
                                        ejecutarLexema(lexemas.get(inicio), inicio, padre, lexemas, obtenerVariablesLocales(padre, lexemas));
                                        inicio++;
                                    }
                                }
                            }

                        }else{
                            resultado = resultado + "\n Errror en la linea " + padre.getNumeroLinea()+" "+variable2+" no ha sido definida";
                        }
                    }else{
                        resultado = resultado + "\n Errror en la linea " + padre.getNumeroLinea()+" "+variable1+" no ha sido definida";
                    }
                }else{
                    
                    variable1 = texto.substring(texto.indexOf("("), texto.indexOf(")"));
                    variable1 = variable1.replaceAll("\\(", "");
                    variable1 = variable1.replaceAll("\\)", "");
                    variable1 = variable1.replaceAll("\s", "");

                    boolean existe = existeVariable(variable1, varibales_locales);
                    if(existe){
                        String valor = obtenerValorAsigado(variable1, varibales_locales);
                        if(valor.equals("")){
                            resultado = resultado + "\n Errror en la linea" + padre.getNumeroLinea()+" "+variable1+" no tiene valor asignado";
                        }else{
                            int inicio = suma+1;
                            while(inicio<fin_secuencia){
                                ejecutarLexema(lexemas.get(inicio), inicio, padre, lexemas, obtenerVariablesLocales(padre, lexemas));
                                inicio++;
                            }
                        }
                    }else{
                        resultado = resultado + "\n Errror en la linea " + padre.getNumeroLinea()+" "+variable1+" no ha sido definida";
                    }

                }
                suma=fin_secuencia;
            break;

            /***inicio para* */
            case "inicio-para":
                suma++;
            break;

            /***inicio mientras* */
            case "inicio-mientras":
                suma++;
            break;

            /***inicio funcion* 
             * @param lexemas*/
            case "inicio-funcion":
                suma++;
            break;
            
            default:suma++; break;
        }
        return suma;
    }

    private int ObtenerFinSecuencia(ArrayList<Lexema> lexemas, Lexema padre) {
        int fin_secuencia = padre.getNumeroLinea()+1;
        while(!((lexemas.get(fin_secuencia).getTipoLexema().equals("fin-si"))&&(lexemas.get(fin_secuencia).getPadre()==padre))&&fin_secuencia<lexemas.size()){
            fin_secuencia++;
        }
        return fin_secuencia+2;
    }

    private String obtenerValorAsigado(String nombre, ArrayList<Variable> varibales_locales) {
        String valor = "";
        for (Variable variable : varibales_locales) {
            if(variable.getNombre().equals(nombre)){
                valor = variable.getValor();
            }
        }
        for (Variable variable : varibales_globales) {
            if(variable.getNombre().equals(nombre)){
                valor = variable.getValor();
            }
        }

        return valor;
    }

    private boolean existeVariable(String nombre, ArrayList<Variable> varibales_locales) {
        boolean existe = false;
        for (Variable variable : varibales_locales) {
            if(variable.getNombre().equals(nombre)){
                existe=true;
            }
        }
        for (Variable variable : varibales_globales) {
            if(variable.getNombre().equals(nombre)){
                existe=true;
            }
        }
        return existe;
    }

    private ArrayList<Variable> obtenerVariablesLocales(Lexema padre, ArrayList<Lexema> lexemas) {

        ArrayList<Variable> v_locales = new ArrayList<Variable>();
        Lexema aux = padre;
        boolean x = true;
        while(aux!=null && x==true){
            x = obtenerVariables(lexemas, aux, v_locales);
            aux=aux.getPadre();
        }
        return v_locales;
    }
    
    private String obtenerValorVariable(String lexema, String tipo) {
        String valor = "";
        if(tipo.equals("cadena")){
            int k = lexema.indexOf("'");
            int contador = k;
            while(lexema.charAt(contador)!=';'){
                contador++;
            }
            valor = lexema.substring(k, contador-1);
            valor = valor.replaceAll("(\s|\\t)*", "");
        }else{
            int k = lexema.indexOf("=");
            if(k!=-1){
                int contador = k;
                while(lexema.charAt(contador)!=';'){
                    contador++;
                }
                valor = lexema.substring(k, contador);
                valor = valor.replaceAll("(\s|\\t|'|=)*", "");
            }else{valor="";}
        }

        return valor;
    }

    private String obtenerTipoVariable(String lexema) {

        String type="";
        if((lexema.length()>0)&&(!tienePalabrasReservadas(lexema))){
        
            String tipo = "";
            int k=0;
            if(lexema.indexOf("entero")!=-1){

                tipo="entero";
                k=lexema.indexOf("entero")+6;

            }else if(lexema.indexOf("real")!=-1){

                tipo="real";
                k=lexema.indexOf("real")+4;

            }else if(lexema.indexOf("fecha")!=-1){

                tipo="fecha";
                k=lexema.indexOf("fecha")+5;

            }else if(lexema.indexOf("boleano")!=-1){

                tipo="boleano";
                k=lexema.indexOf("boleano")+7;
                
            }else if(lexema.indexOf("cadena")!=-1){

                tipo="cadena";
                k=lexema.indexOf("cadena")+6;
                
            }else if(lexema.indexOf("caracter")!=-1){

                tipo="caracter";
                k=lexema.indexOf("caracter")+8;
                
            }
            type = tipo;
        }
        return type;
    }

    private String obtenerNombreVariable(String lexema) {

        String name = "";

        if((lexema.length()>0)&&(!tienePalabrasReservadas(lexema))){
        
                String tipo = "";
                int k=0;
                if(lexema.indexOf("entero")!=-1){

                    tipo="entero";
                    k=lexema.indexOf("entero")+6;

                }else if(lexema.indexOf("real")!=-1){

                    tipo="real";
                    k=lexema.indexOf("real")+4;

                }else if(lexema.indexOf("fecha")!=-1){

                    tipo="fecha";
                    k=lexema.indexOf("fecha")+5;

                }else if(lexema.indexOf("boleano")!=-1){

                    tipo="boleano";
                    k=lexema.indexOf("boleano")+7;
                    
                }else if(lexema.indexOf("cadena")!=-1){

                    tipo="cadena";
                    k=lexema.indexOf("cadena")+6;
                    
                }else if(lexema.indexOf("caracter")!=-1){

                    tipo="caracter";
                    k=lexema.indexOf("caracter")+8;
                    
                }

                String nombre = "";
                char letra = lexema.charAt(k);
                while((letra!=';')&&(letra!='=')){
                    letra = lexema.charAt(k);
                    nombre=nombre+letra;
                    k++;
                }
                nombre = nombre.replaceAll("(\\t|\s|;|=)", "");
                Matcher es_variable = variable.matcher(nombre);
                if(es_variable.find()){
                    name = nombre;
                }
            }
        return name;
    }

    public boolean obtenerVariables(ArrayList<Lexema> lexemas, Lexema padre, ArrayList <Variable> variables){
        boolean v_validas=true;
        for(int i =0; i<lexemas.size(); i++){
            if((lexemas.get(i).getPadre()==padre)){

                String lexema = lexemas.get(i).getTexto();
                if((lexema.length()>0)&&(!tienePalabrasReservadas(lexema))){
                    String tipo = "";
                    int k=0;
                    if(lexema.indexOf("entero")!=-1){

                        tipo="entero";
                        k=lexema.indexOf("entero")+6;

                    }else if(lexema.indexOf("real")!=-1){

                        tipo="real";
                        k=lexema.indexOf("real")+4;

                    }else if(lexema.indexOf("fecha")!=-1){

                        tipo="fecha";
                        k=lexema.indexOf("fecha")+5;

                    }else if(lexema.indexOf("boleano")!=-1){

                        tipo="boleano";
                        k=lexema.indexOf("boleano")+7;
                        
                    }else if(lexema.indexOf("cadena")!=-1){

                        tipo="cadena";
                        k=lexema.indexOf("cadena")+6;
                        
                    }else if(lexema.indexOf("caracter")!=-1){

                        tipo="caracter";
                        k=lexema.indexOf("caracter")+8;
                        
                    }

                    if(tipo != ""){
                        String nombre = "";
                        char letra = lexema.charAt(k);
                        while((letra!=';')&&(letra!='=')){
                            letra = lexema.charAt(k);
                            nombre=nombre+letra;
                            k++;
                        }
                        nombre = nombre.replaceAll("(\\t|\s|;|=)", "");
                        Matcher es_variable = variable.matcher(nombre);
                        if(es_variable.find()){
                            boolean existe_variable=false;
                            int linea = 0;
                            for (Variable variable : variables) {
                                if(variable.getNombre().equals(nombre)){
                                    existe_variable=true;
                                    linea = variable.getLinea();
                                }
                            }
                            for (Variable variable : varibales_globales) {
                                if(variable.getNombre().equals(nombre)){
                                    existe_variable=true;
                                    linea = variable.getLinea();
                                }
                            }
                            if(!existe_variable){
                                Variable v = new Variable(nombre, tipo, lexemas.get(i).getNumeroLinea());
                                v.setValor(obtenerValorVariable(lexema, tipo));
                                variables.add(v);
                                v_validas = true;
                            }else{
                               String error = "";
                               if(linea<lexemas.get(i).getNumeroLinea()){
                                    error="\n Error en la linea: "+lexemas.get(i).getNumeroLinea()+" Variable con nombre duplicado, nombre usado en la linea "+linea;
                               }else{
                                    error="\n Error en la linea: "+linea+" Variable con nombre duplicado, nombre usado en la linea "+lexemas.get(i).getNumeroLinea();
                               }
                               if(resultado.indexOf(error)==-1){
                                    resultado=resultado+error;
                               } 
                               v_validas= false;
                            }
                        }
                    }
                }
            }
        }
        return v_validas;
    }

    private boolean tienePalabrasReservadas(String texto) {
        boolean tiene_palabra_reservada=false;

        for (String palabra : p_reservadas) {
            texto = texto.replaceAll("(\\t|\s|;|=)", "");
            int pos=texto.indexOf(palabra);
            if(pos==0){
                tiene_palabra_reservada=true;
            }
        }

        return tiene_palabra_reservada;
    }
    
}
