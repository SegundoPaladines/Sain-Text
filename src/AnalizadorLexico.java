import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorLexico{
    private String codigo;
    private ArrayList <Lexema> lexemas;

     //**********************************************EXPRESIONES REGULARES********************************************************************************/
     
     //***********************************************TIPOS DE DATO******************************************************************************************* */
     private Pattern entero = Pattern.compile("^(-|\\+|)[0-9]+$");
     private Pattern real = Pattern.compile("^(-|\\+|)([0-9]+)|([0-9]+)(\\.)([0-9]+)$");
     private Pattern cadena = Pattern.compile("^['][\s]*([A-Za-z0-9][\s]*)+[']$");
     private Pattern fecha = Pattern.compile("^['](\\d{2})/(\\d{2})/(\\d{4})[']$");
     private Pattern caracter = Pattern.compile("^[']([A-Za-z0-9][\s]*){1}[']$");

     //**********************************************FIN TIPOS DE DATO**************************************************** */

     //**************************************************VARIABLES********************************************************************/
     private Pattern vacio = Pattern.compile("^(\\t|\s)*$");
     private Pattern variable = Pattern.compile("^(\\t|\s)*(entero|cadena|real|caracter|fecha|boleano)(\s)+[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*[;](\\t|\s)*$");
     private Pattern asignacion_variable_veriable = Pattern.compile("^(\\t|\s)*(entero(\s)+|cadena(\s)+|real(\s)+|caracter(\s)+|fecha(\s)+|boleano(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*(-|\\+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*[;](\\t|\s)*$");
     private Pattern asignacion_variable_entero = Pattern.compile("^(\\t|\s)*(entero(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*(-|\\+|)[0-9]+[;](\\t|\s)*$");
     private Pattern asignacion_variable_real = Pattern.compile("^(\\t|\s)*(real(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*(-|\\+|)(([0-9]+)|([0-9]+)(\\.)([0-9]+))[;](\\t|\s)*$");
     private Pattern asignacion_variable_cadena = Pattern.compile("^(\\t|\s)*(cadena(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*['][\s]*([A-Za-z0-9][\s]*)+['][;](\\t|\s)*$");
     private Pattern asignacion_variable_caracter = Pattern.compile("^(\\t|\s)*(caracter(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*[']([A-Za-z0-9][\s]*){1}['][;](\\t|\s)*$");
     private Pattern asignacion_variable_fecha = Pattern.compile("^(\\t|\s)*(fecha(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*['](\\d{2})/(\\d{2})/(\\d{4})['][;](\\t|\s)*$");
     private Pattern asignacion_variable_boleano = Pattern.compile("^(\\t|\s)*(boleano(\s)+|)[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*(verdadero|falso)[;](\\t|\s)*$");
     //****************************************************FIN VARIABLES***************************************************************************/

     //************************************************CICLOS ITERATIVOS*************************************************/

     //**Cliclo Para */

     private Pattern inicio_ciclo_para = Pattern.compile("^(\\t|\s)*(Para)(\s)+(entero)(\s)+[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*(\\=)(\s)*[0-9]+(\s)*(Hasta)(\s)*[0-9]+(\s)*(Paso)(\s)+[0-9]+(\s)+(=>)(\\t|\s)*$");
     private Pattern fin_ciclo_para = Pattern.compile("^(\\t|\s)*(FinPara)(\\t|\s)*$");

     //** */

     //***Ciclo mientras***************************** */
     private Pattern inicio_ciclo_mientras = Pattern.compile("^(\\t|\s)*(Mientras(\s)*((\\()(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*((\\!)(\\=)|(\\=)(\\=)|(\\<)|(\\>)|((\\<)(\\=))|((\\>)(=)))(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|((-|\\+|)[0-9]+)|((-|\\+|)([0-9]+)|([0-9]+)(\\.)([0-9]+))|(['][\s]*([A-Za-z0-9][\s]*)+['])|([']([A-Za-z0-9][\s]*){1}['])|(['](\\d{2})/(\\d{2})/(\\d{4})[']))))(\s)*(\\))))(\\t|\s)*$");
     private Pattern fin_ciclo_mientras = Pattern.compile("^(\\t|\s)*(FinMientras)(\\t|\s)*$");
     //******* */

     //***************************************************FIN CILOS ITERATIVOS *****************************************************************************************************************************************/

     //*******************************************************SECUENCIAS IF*************************************************************************************/
     private Pattern inicio_si = Pattern.compile("^(\\t|\s)*(Si(\s)*((\\()(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*((\\!)(\\=)|(\\=)(\\=)|(\\<)|(\\>)|((\\<)(\\=))|((\\>)(=)))(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|((-|\\+|)[0-9]+)|((-|\\+|)([0-9]+)|([0-9]+)(\\.)([0-9]+))|(['][\s]*([A-Za-z0-9][\s]*)+['])|([']([A-Za-z0-9][\s]*){1}['])|(['](\\d{2})/(\\d{2})/(\\d{4})[']))))(\s)*(\\)))(\s)+(Entonces))(\\t|\s)*$");
     private Pattern inicio_sino_si = Pattern.compile("^(\\t|\s)*((SiNo)(\s)+(Si)(\s)*((\\()(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*(\s)*((\\!)(\\=)|(\\=)(\\=)|(\\<)|(\\>)|((\\<)(\\=))|((\\>)(=)))(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|((-|\\+|)[0-9]+)|((-|\\+|)([0-9]+)|([0-9]+)(\\.)([0-9]+))|(['][\s]*([A-Za-z0-9][\s]*)+['])|([']([A-Za-z0-9][\s]*){1}['])|(['](\\d{2})/(\\d{2})/(\\d{4})[']))))(\s)*(\\)))(\s)+(Entonces))(\\t|\s)*$");
     private Pattern inicio_sino = Pattern.compile("^(\\t|\s)*(SiNo(\s)+(Entonces))(\\t|\s)*$");
     private Pattern fin_si = Pattern.compile("^(\\t|\s)*(FinSi)(\\t|\s)*$");
     //********************************************************FIN SECUENCIAS IF****************************************************************** */
     
     //***************************************************EXPRESIONES ARITMETICAS*********************************************/
     private Pattern expresion_aritmetica = Pattern.compile("^(\\t|\s)*((entero(\s)+|real(\s)+|)(\s)*[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)(\s)*=(\s)*(\\[|\\(|\\{)*(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|((-|\\+|)[0-9]+)|((-|\\+|)([0-9]+)|([0-9]+)(\\.)([0-9]+)))(\s)*(\\]|\\)|\\})*(\s)*((\s)*((\\+)|(\\-)|(\\*)|(\\/)|(\\^))(\s)*(\\[|\\(|\\{)*(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|((-|\\+|)[0-9]+)|((-|\\+|)([0-9]+)|([0-9]+)(\\.)([0-9]+)))(\s)*(\\]|\\)|\\})*(\s)*)*(\\;)(\\t|\s)*$");
    
     //**Expresion concatenar*/
     private Pattern concatenar = Pattern.compile("^(\\t|\s)*(cadena(\s)+|)([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)(\s)*(\\=)(\s)*([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*|['][\s]*([A-Za-z0-9][\s]*)*['])(\\s)*((\s)*(\\+)(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|(['][\s]*([A-Za-z0-9][\s]*)*['])))*[;](\\t|\s)*$");

     //**************************************************IMPRIMIR */
     private Pattern imprimir = Pattern.compile("^(\\t|\s)*(Imprimir)(\s)*(\\()(\s)*([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*|['][\s]*([A-Za-z0-9][\s]*)*['])(\\s)*((\s)*(\\+)(\s)*(([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*)|(['][\s]*([A-Za-z0-9][\s]*)*['])))*(\s)*(\\))[;](\\t|\s)*$");


     //****************************************************FIN EXPRESIONES ARITMETICAS*************************************************** */

     //****************************************************FUNCIONES**********************************************************************/
     private Pattern inicio_funcion = Pattern.compile("^(\\t|\s)*(Funcion)(\s)+(boleano|entero|real|cadena|caracter|fecha|nulo)(\s)+([A-Za-z])*(\s)*(\\()(\s)*(|(boleano|entero|real|cadena|caracter|fecha)(\s)+[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*((\\,)(\s)*(boleano|entero|real|cadena|caracter|fecha)(\s)+([A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*))*)(\s)*(\\))(\\t|\s)*$");
     private Pattern retorno_funcion = Pattern.compile("^(\\t|\s)*(Devolver)(\s)+[A-Za-z]+(\\_[A-Za-z0-9]+)*([0-9]+)*[;](\\t|\s)*$");
     private Pattern fin_funcion = Pattern.compile("^(\\t|\s)*(FinFuncion)(\\t|\s)*$");
     /******************************************************************FIN RE PARA FUNCIONES**************************************************** */

     //****************************************************Fin RE************************************************************/

    public AnalizadorLexico(String codigo){
        this.codigo=codigo;
    }
    public String Analizar() {
        String resultados = "";
        this.lexemas = Lexema.ExtraerLexemas(codigo);
        if(lexemas.size()>0){
            resultados=ValidarLexemas();
        }else{
            resultados = "No hay codigo legible";
        }
        return resultados;
    }
    private String ValidarLexemas(){
        String resultado = "";
        Lexema abierto = lexemas.get(0).getPadre();
        for(int i = 0; i<lexemas.size(); i++){
            Matcher es_vacio = vacio.matcher(lexemas.get(i).getTexto());

            //** inicializar variable */
            Matcher es_variable = variable.matcher(lexemas.get(i).getTexto());

            //**asignacion o inicializacion con asignacion */
            Matcher es_asigacion_variable_con_variable = asignacion_variable_veriable.matcher(lexemas.get(i).getTexto());
            Matcher es_asigacion_variable_con_entero = asignacion_variable_entero.matcher(lexemas.get(i).getTexto());
            Matcher es_asigacion_variable_con_real = asignacion_variable_real.matcher(lexemas.get(i).getTexto());
            Matcher es_asigacion_variable_con_caracter = asignacion_variable_caracter.matcher(lexemas.get(i).getTexto());
            Matcher es_asigacion_variable_con_fecha = asignacion_variable_fecha.matcher(lexemas.get(i).getTexto());
            Matcher es_asigacion_variable_con_cadena = asignacion_variable_cadena.matcher(lexemas.get(i).getTexto());
            Matcher es_asigacion_variable_con_boleano = asignacion_variable_boleano.matcher(lexemas.get(i).getTexto());

            //**expresion aritmetica */
            Matcher es_expresion_aritmetica= expresion_aritmetica.matcher(lexemas.get(i).getTexto());

            //*** ciclo para */
            Matcher es_inicio_para = inicio_ciclo_para.matcher(lexemas.get(i).getTexto());
            Matcher es_fin_para = fin_ciclo_para.matcher(lexemas.get(i).getTexto());

            //*** ciclo mientras */
            Matcher es_inicio_mientras = inicio_ciclo_mientras.matcher(lexemas.get(i).getTexto());
            Matcher es_fin_mientras = fin_ciclo_mientras.matcher(lexemas.get(i).getTexto());

            //** secuencia si */
            Matcher es_inicio_si = inicio_si.matcher(lexemas.get(i).getTexto());
            Matcher es_inicio_sino_si = inicio_sino_si.matcher(lexemas.get(i).getTexto());
            Matcher es_inicio_sino = inicio_sino.matcher(lexemas.get(i).getTexto());
            Matcher es_fin_si = fin_si.matcher(lexemas.get(i).getTexto());

            //****funcion***** */
            Matcher es_inicio_funcion = inicio_funcion.matcher(lexemas.get(i).getTexto());
            Matcher es_retorno_funcion = retorno_funcion.matcher(lexemas.get(i).getTexto());
            Matcher es_fin_funcion = fin_funcion.matcher(lexemas.get(i).getTexto());

            //****Concatenacion***/
            Matcher es_concatenacion = concatenar.matcher(lexemas.get(i).getTexto());

            //****imprimir***/
            Matcher es_imprimir = imprimir.matcher(lexemas.get(i).getTexto());

            //**PARA VALIDAR Lexemas**/
            if(es_variable.find()){
                
                //variable
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una variable";
                lexemas.get(i).setTipoLexema("variable");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }
            
            }else if(es_asigacion_variable_con_boleano.find()){

                //**asignacion variable - boleano */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = boleano";
                lexemas.get(i).setTipoLexema("asignacion-variable=boleano");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_asigacion_variable_con_variable.find()){

                //**asignacion variable - variable */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = variable";
                lexemas.get(i).setTipoLexema("asignacion-variable=variable");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_asigacion_variable_con_entero.find()){

                //**asignacion variable - entero */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = entero";
                lexemas.get(i).setTipoLexema("asignacion-variable=entero");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_asigacion_variable_con_real.find()){

                //**asignacion variable - real */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = real";
                lexemas.get(i).setTipoLexema("asignacion-variable=real");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_asigacion_variable_con_caracter.find()){

               //**asignacion variable - caracter */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = caracter";
                lexemas.get(i).setTipoLexema("asignacion-variable=caracter");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_asigacion_variable_con_fecha.find()){

                //**asignacion variable - fecha */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = fecha";
                lexemas.get(i).setTipoLexema("asignacion-variable=fecha");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_asigacion_variable_con_cadena.find()){

                //**asignacion variable - cadena */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es es una asignacion: variable = cadena";
                lexemas.get(i).setTipoLexema("asignacion-variable=cadena");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_expresion_aritmetica.find()){

                //**expresion aritmetica*/
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es una expresion aritmetica";
                lexemas.get(i).setTipoLexema("expresion-aritmetica");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_concatenacion.find()){

                //**expresion concatenacion*/
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es una concatenacion";
                lexemas.get(i).setTipoLexema("concatenacion");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_imprimir.find()){

                //**imprimir*/
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Es imprimir";
                lexemas.get(i).setTipoLexema("imprimir");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }else if(es_inicio_si.find()){

                //**inicio secuencia if */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" inicio secuencia si";
                lexemas.get(i).setTipoLexema("inicio-si");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }
                abierto = lexemas.get(i);
                
            }else if(es_inicio_sino_si.find()){

                //**inicio sino si */
                String res = "";
                if(abierto != null){
                    if(abierto.getTipoLexema().equalsIgnoreCase("inicio-si")){
                        res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Inicio SiNo Si";
                        lexemas.get(i).setTipoLexema("inicio-sino-si");
                        lexemas.get(i).setPadre(abierto);
                        res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                        abierto = lexemas.get(i);
                    }else if(abierto.getTipoLexema().equalsIgnoreCase("inicio-sino-si")){
                        Lexema aux = abierto.getPadre();
                        while((aux!= null)&&(!aux.getTipoLexema().equalsIgnoreCase("inicio-si"))){
                            aux = aux.getPadre();
                        }
                        if(aux != null){
                            res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Inicio SiNo Si";
                            lexemas.get(i).setTipoLexema("inicio-sino-si");
                            lexemas.get(i).setPadre(aux);
                            res = res + " Pertenece al lexema abierto en linea "+aux.getNumeroLinea();
                            abierto = lexemas.get(i);
                        }else{
                            /**lexema invalido */
                            res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                            lexemas.get(i).setTipoLexema("invalido");
                        }
                    }else{
                        /**lexema invalido */
                        res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                        lexemas.get(i).setTipoLexema("invalido");
                    }
                }else{
                    /**lexema invalido */
                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                    lexemas.get(i).setTipoLexema("invalido");
                }

                resultado = resultado + res;
                
            }else if(es_inicio_sino.find()){

                 //**inicio sino*/
                 String res = "";
                 if(abierto != null){
 
                     if(abierto.getTipoLexema().equalsIgnoreCase("inicio-si")){
                         res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Inicio SiNo";
                         lexemas.get(i).setTipoLexema("inicio-sino");
                         lexemas.get(i).setPadre(abierto);
                         res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                         abierto = lexemas.get(i);
                     }else if(abierto.getTipoLexema().equalsIgnoreCase("inicio-sino-si")){
                         Lexema aux = abierto.getPadre();
                         while((aux!= null)&&(!aux.getTipoLexema().equalsIgnoreCase("inicio-si"))){
                             aux = aux.getPadre();
                         }
                         if(aux != null){
                             res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Inicio SiNo Si";
                             lexemas.get(i).setTipoLexema("inicio-sino");
                             lexemas.get(i).setPadre(aux);
                             res = res + " Pertenece al lexema abierto en linea "+aux.getNumeroLinea();
                             abierto = lexemas.get(i);
                         }else{
                             /**lexema invalido */
                             res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                             lexemas.get(i).setTipoLexema("invalido");
                         }
                     }else{
                         /**lexema invalido */
                         res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                         lexemas.get(i).setTipoLexema("invalido");
                     }
                 }else{
                     /**lexema invalido */
                     res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                     lexemas.get(i).setTipoLexema("invalido");
                 }
 
                 resultado = resultado + res;
                
            }else if(es_fin_si.find()){

                 //**Fin Secuencia if*/
                 String res = "";
                 if(abierto != null){

                     if(abierto.getTipoLexema().equalsIgnoreCase("inicio-si")){
                         res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Fin si";
                         lexemas.get(i).setTipoLexema("fin-si");
                         lexemas.get(i).setPadre(abierto);
                         res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                         abierto = abierto.getPadre();
                     }else if(abierto.getTipoLexema().equalsIgnoreCase("inicio-sino-si")|abierto.getTipoLexema().equalsIgnoreCase("inicio-sino")){
                         Lexema aux = abierto.getPadre();
                         while((aux!= null)&&(!aux.getTipoLexema().equalsIgnoreCase("inicio-si"))){
                             aux = aux.getPadre();
                         }
                         if(aux != null){
                             res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" FinSi";
                             lexemas.get(i).setTipoLexema("fin-si");
                             lexemas.get(i).setPadre(aux);
                             res = res + " Pertenece al lexema abierto en linea "+aux.getNumeroLinea();
                             abierto = aux.getPadre();
                         }else{
                             /**lexema invalido */
                             res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                             lexemas.get(i).setTipoLexema("invalido");
                         }
                     }else{
                         /**lexema invalido */
                         res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                         lexemas.get(i).setTipoLexema("invalido");
                     }
                 }else{
                     /**lexema invalido */
                     res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Secuencia Si para este lexema";
                     lexemas.get(i).setTipoLexema("invalido");
                 }
 
                 resultado = resultado + res;
                
            }else if(es_inicio_para.find()){

                //**inicio ciclo para */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" inicio para";
                lexemas.get(i).setTipoLexema("inicio-para");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }
                abierto = lexemas.get(i);
                
            }else if(es_fin_para.find()){

                //**fin ciclo para */
                String res = "";
                if((abierto != null)&&(abierto.getTipoLexema().equalsIgnoreCase("inicio-para"))){

                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" fin para";
                    lexemas.get(i).setTipoLexema("fin-para");
                    lexemas.get(i).setPadre(abierto);
                    res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                    abierto = abierto.getPadre();
                }else{
                    res= "";
                    /**lexema invalido */
                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Para para este lexema";
                    lexemas.get(i).setTipoLexema("invalido");
                }

                resultado = resultado + res;

            }else if(es_inicio_mientras.find()){

                //**inicio ciclo mientras */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" inicio mientras";
                lexemas.get(i).setTipoLexema("inicio-mientras");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }
                abierto = lexemas.get(i);
                
            }else if(es_fin_mientras.find()){

                //**fin ciclo mientras */
                String res = "";
                if((abierto != null)&&(abierto.getTipoLexema().equalsIgnoreCase("inicio-mientras"))){

                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" fin mientras";
                    lexemas.get(i).setTipoLexema("fin-mientras");
                    lexemas.get(i).setPadre(abierto);
                    res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                    abierto = abierto.getPadre();
                }else{
                    res= "";
                    /**lexema invalido */
                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio Mientras para este lexema";
                    lexemas.get(i).setTipoLexema("invalido");
                }

                resultado = resultado + res;

            }else if(es_inicio_funcion.find()){

                //**inicio funcion */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" inicio funcion";
                lexemas.get(i).setTipoLexema("inicio-funcion");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }
                abierto = lexemas.get(i);
                
            }else if(es_retorno_funcion.find()){

                //**retorno de la funcion */
                String res = "";
                if(abierto != null){
                    if(abierto.getTipoLexema().equalsIgnoreCase("inicio-funcion")){
                        res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Retorno de la funcion";
                        lexemas.get(i).setTipoLexema("retorno-funcion");
                        lexemas.get(i).setPadre(abierto);
                        res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                    }else{
                        /**lexema invalido */
                        res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio funcion para este lexema";
                        lexemas.get(i).setTipoLexema("invalido");
                    }
                }else{
                    /**lexema invalido */
                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay iniciofuncion para este lexema";
                    lexemas.get(i).setTipoLexema("invalido");
                }

                resultado = resultado + res;
                
            }else if(es_fin_funcion.find()){

                //**fin de la funcion */
                String res = "";
                if(abierto != null){
                    if(abierto.getTipoLexema().equalsIgnoreCase("inicio-funcion")){
                        res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Fin de la funcion";
                        lexemas.get(i).setTipoLexema("fin-funcion");
                        lexemas.get(i).setPadre(abierto);
                        res = res + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                        abierto = abierto.getPadre();
                    }else{
                        /**lexema invalido */
                        res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay inicio funcion para este lexema";
                        lexemas.get(i).setTipoLexema("invalido");
                    }
                }else{
                    /**lexema invalido */
                    res = "\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" No hay iniciofuncion para este lexema";
                    lexemas.get(i).setTipoLexema("invalido");
                }

                resultado = resultado + res;
                
            }else if(es_vacio.find()){

                //**lexema vacio */
                //resultado = resultado+"\n"+"Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" linea vacia";
                lexemas.get(i).setTipoLexema("vacio");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + " Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }
            else{

                //**lexema invalido */
                resultado = resultado+"\n Lexema "+lexemas.get(i).getTexto()+" de la linea "+lexemas.get(i).getNumeroLinea()+" Tiene un error Lexico";
                lexemas.get(i).setTipoLexema("invalido");
                if(abierto != null){
                    lexemas.get(i).setPadre(abierto);
                    //resultado = resultado + "Pertenece al lexema abierto en linea "+abierto.getNumeroLinea();
                }

            }
            //**FIN VALIDACION Lexemas* */
        }

        int contador_errores=0;
        for(int i=0; i<lexemas.size(); i++){
            if(lexemas.get(i).getTipoLexema().equalsIgnoreCase("invalido")){
                contador_errores++;        
            }
        }
        if(contador_errores==0){
            Compilador compilador = new Compilador();
            resultado = resultado + compilador.Compilar(resultado, lexemas);
        }
        return resultado;
    }



}