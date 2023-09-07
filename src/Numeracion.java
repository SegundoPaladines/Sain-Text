import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Numeracion {

    //metodo para numerar
    public static void mostrarNumeracion(boolean numerar, JTextPane text_area, JScrollPane scroll){
        if(numerar == true){
            try {
                scroll.setRowHeaderView(new TextLineNumber(text_area));
            } catch (Exception e) {}
        }else{
            try {
                scroll.setRowHeaderView(null);
            } catch (Exception e) {}
        }
    }
    
}
