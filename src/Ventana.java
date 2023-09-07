import javax.swing.JFrame;
public class Ventana extends JFrame{

    //Ventana que hereda de JFrame
    //Constructor
    public Ventana(int x, int y, int ancho, int alto, String titulo){
        setBounds(x, y, ancho, alto);
        setTitle(titulo);
        //añadir el panel a la ventana
        add(new Panel(this));
    }

}