import javax.swing.JMenuItem;

public class OpItems {

    //activar items
    public static void activarItems(JMenuItem lista_items []){
        for (JMenuItem item: lista_items) {item.setEnabled(true);}
    }
    //desactivar items
    public static void desactivarItems(JMenuItem lista_items []){
        for (JMenuItem item: lista_items) {item.setEnabled(false);}
    }
    
}
