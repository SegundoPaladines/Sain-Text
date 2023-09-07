import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BarraHerramientas {
    public static JButton añadirHerramienta(String desc, URL url, Object contenedor){
        JButton boton = new JButton(new ImageIcon( new ImageIcon(url).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        boton.setToolTipText(desc);
        boton.setBackground(new Color (23,25,27));
        boton.setMargin(new Insets(5, 5, 5, 5));
        boton.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        ((Container) contenedor).add(boton);
        return boton;
    }
}
