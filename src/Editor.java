import javax.swing.JFrame;

public class Editor {
    public static void main(String[] args) throws Exception {
        //Creacion e instancia de la clase ventana
        Ventana ventana = new Ventana(250, 40, 1000, 650, "Sain Text Editor");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}
