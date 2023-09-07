import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.Color;

public class Fondo{

    public static void cambiarFondo(int tamano, String color, ArrayList <JTextPane> lista_textarea){

            Color text_color = Color.BLACK;
            Color fondo = Color.WHITE;
            if(color.equals("blanco")){

                text_color = Color.BLACK;
                fondo = Color.WHITE;
                
            }else if(color.equals("oscuro")){

                text_color = new Color(0,143,57);
                fondo = new Color (23,25,27);
                
            }

            for(int i=0; i<lista_textarea.size(); i++){
                StyleContext estilo = StyleContext.getDefaultStyleContext();

                lista_textarea.get(i).selectAll();
                //color de texto
                AttributeSet atri_texto = estilo.addAttribute(lista_textarea.get(i).getCharacterAttributes(), StyleConstants.Foreground, text_color);
                //tipo de texto
                atri_texto = estilo.addAttribute(atri_texto, StyleConstants.FontFamily, "Arial");
                atri_texto = estilo.addAttribute(atri_texto, StyleConstants.FontSize, tamano);

                lista_textarea.get(i).setCharacterAttributes(atri_texto, true);
                lista_textarea.get(i).setBackground(fondo);
            }
    }

    public static void cambiarTamanoTexto(int tamano, ArrayList<JTextPane> text_list){
        for(int i=0; i<text_list.size(); i++){
            text_list.get(i).selectAll();
            StyleContext estilo = StyleContext.getDefaultStyleContext();
            AttributeSet atri_texto = estilo.addAttribute(text_list.get(i).getCharacterAttributes(), StyleConstants.FontSize, tamano);
            text_list.get(i).setCharacterAttributes(atri_texto,true);
        }
    }

}