import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Panel extends JPanel{

    //barra de herramientas
    private JToolBar barra_heramientas;
    private URL url;

    //menu emergente
    private JPopupMenu menu_emergente;


    //para estilos
    private String text_color = "blanco";

    //Número de paneles creados
    private int cont_paneles=0;
    //Si un panel existe
    private boolean existe=false;

    //Si se muestra numero de linea
    private boolean numerar=true;
    //Contenedor
    private JTabbedPane texto_contenedor;
    //ventana
    private JPanel ventana;
    //Lista de Areas donde está el texto
    private ArrayList <JTextPane> text_list;
    //Lista de archivos
    private ArrayList <File> file_list;
    //Lista de scroll
    private ArrayList <JScrollPane> scroll_list;
    //Para guardar los cambios
    private ArrayList <UndoManager> historial;
    //jmenubar menú
    private JMenuBar menu;
    //Opciones del menú
    private JMenu archivo, editar, seleccion, ver, ejecutar, apariencia;
    //item generico
    private JMenuItem menu_item;
    //barra derecha
    private JPanel barra_der;
    //boton fijar
    private JLabel lbl_fijar;
    //si se fija la pestaña
    private boolean estado_fijacion = false;
    //Tamaño de la letra
    private JSlider tamano_letra;
    //array de items para controlar excepciones
    private JMenuItem items [];

    //barra inferior
    public JPanel barra_inferior;


    //Espacio para mostrar los resultados
    public JPanel contenedor_resultados;
    private JLabel lbl_resultado;
    private JScrollPane scrol_resultados;
    private JTextArea resultado;

    /**
     * 
     */
    public Panel(JFrame vent){

        //Organizar objetos con border layout
        setBackground(new Color (23,25,27));
        setLayout(new BorderLayout());

        //MENU
        //contenedor del menú
        JPanel contenedor_menu = new JPanel();
        contenedor_menu.setLayout(new BorderLayout());
        contenedor_menu.setBorder(BorderFactory.createEmptyBorder());

        items = new JMenuItem [9];

        //Instanciar opciones del menú
        menu = new JMenuBar();
        menu.setBackground(new Color (23,25,27));
        menu.setBorder(BorderFactory.createEmptyBorder());

        archivo=new JMenu("Archivo");
        archivo.setForeground(Color.WHITE);
        archivo.setBorder(BorderFactory.createEmptyBorder());
        editar=new JMenu("Editar");
        editar.setForeground(Color.WHITE);
        editar.setBorder(BorderFactory.createEmptyBorder());
        seleccion=new JMenu("Selección");
        seleccion.setForeground(Color.WHITE);
        seleccion.setBorder(BorderFactory.createEmptyBorder());
        ver=new JMenu("Ver");
        ver.setForeground(Color.WHITE);
        ver.setBorder(BorderFactory.createEmptyBorder());
        ejecutar=new JMenu("Ejecutar");
        ejecutar.setForeground(Color.WHITE);
        ejecutar.setBorder(BorderFactory.createEmptyBorder());
        apariencia=new JMenu("Apariencia");
        apariencia.setForeground(Color.WHITE);
        apariencia.setBorder(BorderFactory.createEmptyBorder());

        //Añadir opciones al menú
        menu.add(archivo);
        menu.add(editar);
        menu.add(seleccion);
        menu.add(ver);
        menu.add(ejecutar);
        menu.add(apariencia);

        //añadir los items a los menú
        //Archivo
        crearItemMenu("Nuevo Archivo","archivo","nuevo");
        crearItemMenu("Abrir Archivo","archivo","abrir");
        archivo.addSeparator();
        crearItemMenu("Guardar","archivo","guardar");
        crearItemMenu("Guardar Como","archivo","guardar_como");

        //Editar
        crearItemMenu("Deshacer","editar","deshacer");
        crearItemMenu("Rehacer","editar","rehacer");
        editar.addSeparator();
        crearItemMenu("Cortar","editar","cortar");
        crearItemMenu("Copiar","editar","copiar");
        crearItemMenu("Pegar","editar","pegar");
        
        //Seleccion
        crearItemMenu("Seleccionar Todo","seleccion","sl_todo");

        //Ver
        crearItemMenu("Numeración","ver","numerar");
        
        //Ejecutar
        crearItemMenu("Ejecutar Codigo","ejecutar","ejecutar");
        
        //Apatiencia
        crearItemMenu("Normal","apariencia","normal");
        crearItemMenu("Modo Oscuro","apariencia","oscuro");

        //Añadir el menú a su respectivo contenedor
        contenedor_menu.add(menu);
        contenedor_menu.setBorder(BorderFactory.createEmptyBorder());
         //añadir Contenedor menú
         setBorder(BorderFactory.createEmptyBorder());
         add(contenedor_menu, BorderLayout.NORTH);
        //Instancia del contenedor
        texto_contenedor = new JTabbedPane();
        //instancia de los array
        file_list = new ArrayList<File>();
        text_list = new ArrayList<JTextPane>();
        scroll_list = new ArrayList<JScrollPane>();
        historial = new ArrayList<UndoManager>();

        //desactivar items que no se pueden usar
        OpItems.desactivarItems(items);

        //herramientas
        barra_heramientas = new JToolBar(JToolBar.VERTICAL);
        barra_heramientas.setBackground(new Color (23,25,27));
        url = Panel.class.getResource("img/cerrar.jpg");
        BarraHerramientas.añadirHerramienta("Cerrar Archivo", url, barra_heramientas).addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                
                int selected = texto_contenedor.getSelectedIndex();
                if(selected != -1){

                    scroll_list.get(texto_contenedor.getSelectedIndex()).setRowHeader(null);
                    texto_contenedor.remove(selected);
                    text_list.remove(selected);
                    scroll_list.remove(selected);
                    historial.remove(selected);
                    file_list.remove(selected);
                    cont_paneles=cont_paneles-1;
                    if(texto_contenedor.getSelectedIndex() == -1){
                        existe=false;
                        OpItems.desactivarItems(items);
                    }
                }
                
            }

        });

        url = Panel.class.getResource("img/nuevo.jpg");
        BarraHerramientas.añadirHerramienta("Nuevo Archivo", url, barra_heramientas).addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                crearContenedor();
                if(existe){
                    OpItems.activarItems(items);
                }
            }
        });

        //barra inferior
        barra_inferior = new JPanel();
        barra_inferior.setLayout(new BorderLayout());

        JPanel fijar = new JPanel();
        fijar.setBorder(new MatteBorder(150,10,10,15, new Color (23,25,27)));
        lbl_fijar = new JLabel();
        url = Panel.class.getResource("img/fijar.png");
        lbl_fijar.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        lbl_fijar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(estado_fijacion){
                    url = Panel.class.getResource("img/fijar.png");
                    lbl_fijar.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }else{
                    url = Panel.class.getResource("img/fijado.png");
                    lbl_fijar.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if(estado_fijacion){
                    url = Panel.class.getResource("img/fijado.png");
                    lbl_fijar.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }else{
                    url = Panel.class.getResource("img/fijar.png");
                    lbl_fijar.setIcon(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if(estado_fijacion){
                    estado_fijacion=false;
                    
                }else{
                    estado_fijacion = true;
                }
                vent.setAlwaysOnTop(estado_fijacion);
            }
        });
        fijar.add(lbl_fijar);
        fijar.setBackground(new Color (23,25,27));

        tamano_letra = new JSlider(JSlider.VERTICAL,8, 38, 14);
        tamano_letra.setMajorTickSpacing(10);
        tamano_letra.setMinorTickSpacing(2);
        tamano_letra.setPaintTicks(true);
        tamano_letra.setBackground(new Color (23,25,27));
        tamano_letra.setPaintLabels(true);

        tamano_letra.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {Fondo.cambiarTamanoTexto(tamano_letra.getValue(), text_list);}
        });

        //**************************************Barra Inferior************************************** */

        contenedor_resultados= new JPanel(new BorderLayout());
        contenedor_resultados.setBorder(new MatteBorder(10,0,30,50,new Color (23,25,27)));
        contenedor_resultados.setBackground(Color.RED);
        //label resultados
        lbl_resultado = new JLabel("Resultados:");
        lbl_resultado.setForeground(Color.WHITE);
        contenedor_resultados.add(lbl_resultado, BorderLayout.NORTH);
        //area de texto con los resultados
        resultado = new JTextArea(10,20);
        resultado.setMaximumSize(resultado.getPreferredSize());
        resultado.setEditable(false);
        //J scroll de los resultados
        scrol_resultados = new JScrollPane(resultado);
        contenedor_resultados.add(scrol_resultados, BorderLayout.CENTER);
        contenedor_resultados.setBackground(new Color (23,25,27));
        barra_inferior.add(fijar, BorderLayout.WEST);
        barra_inferior.add(contenedor_resultados, BorderLayout.CENTER);
        barra_inferior.setBackground(new Color (23,25,27));


        //**************************************************************************** */

         //barra lateral derecha
         barra_der = new JPanel();
         barra_der.setLayout(new BorderLayout());
         JPanel esp = new JPanel();
         esp.add(tamano_letra);
         esp.setBackground(new Color (23,25,27));
 
         barra_der.add(esp, BorderLayout.CENTER);
        
        //añadir contenedor a la ventana
        add(texto_contenedor, BorderLayout.CENTER);
        add(barra_heramientas, BorderLayout.WEST);
        add(barra_der, BorderLayout.EAST);
        add(barra_inferior, BorderLayout.SOUTH);

        //menu emergente
        menu_emergente = new JPopupMenu();
        JMenuItem cortar = new JMenuItem("Cortar");
        cortar.addActionListener(new DefaultEditorKit.CutAction());
        cortar.setBackground(Color.BLACK);
        cortar.setForeground(Color.WHITE);
        JMenuItem copiar = new JMenuItem("Copiar");
        copiar.addActionListener(new DefaultEditorKit.CopyAction());
        copiar.setBackground(Color.BLACK);
        copiar.setForeground(Color.WHITE);
        JMenuItem pegar = new JMenuItem("Pegar");
        pegar.setBackground(Color.BLACK);
        pegar.setForeground(Color.WHITE);
        pegar.addActionListener(new DefaultEditorKit.PasteAction());
        JMenuItem deshacer = new JMenuItem("Deshacer");
        deshacer.addActionListener(new ActionListener () {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(historial.get(texto_contenedor.getSelectedIndex()).canUndo()){
                    historial.get(texto_contenedor.getSelectedIndex()).undo();
                }
            }

        });
        deshacer.setBackground(Color.BLACK);
        deshacer.setForeground(Color.WHITE);
        JMenuItem reahacer = new JMenuItem("Reahacer");
        reahacer.addActionListener(new ActionListener () {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(historial.get(texto_contenedor.getSelectedIndex()).canRedo()){
                    historial.get(texto_contenedor.getSelectedIndex()).redo();
                }
            }

        });
        reahacer.setBackground(Color.BLACK);
        reahacer.setForeground(Color.WHITE);
        //agregar los items
        menu_emergente.add(deshacer);
        menu_emergente.add(reahacer);
        menu_emergente.add(cortar);
        menu_emergente.add(copiar);
        menu_emergente.add(pegar);
    }

    public void crearContenedor(){
        //instanciar contenedor ventana
        ventana = new JPanel();
        ventana.setBackground(new Color (23,25,27));
        ventana.setLayout(new BorderLayout());

        //crear archivo nuevo y guardar en el array
        file_list.add(new File(""));
        // crear el area de texto y guardar en el array
        text_list.add(new JTextPane());
        //crear scrol y añador a la lista añadiendo la area de texto que corresponda
        scroll_list.add(new JScrollPane(text_list.get(cont_paneles)));

        //crear la lista de historiales
        historial.add(new UndoManager());
        //establecer la relacion del undomanager con los cambios del area de texto
        text_list.get(cont_paneles).getDocument().addUndoableEditListener(historial.get(cont_paneles));

        //agregar menu emergente
        text_list.get(cont_paneles).setComponentPopupMenu(menu_emergente);

        //añadir el area de texto a la ventana
        ventana.add(scroll_list.get(cont_paneles) ,BorderLayout.CENTER);

        //colcoar la numeracion
        Numeracion.mostrarNumeracion(numerar, text_list.get(cont_paneles), scroll_list.get(cont_paneles));
        //Añadir el cuadro de texto al contenedor
        texto_contenedor.addTab("titulo", ventana);
        texto_contenedor.setSelectedIndex(cont_paneles);
        Fondo.cambiarFondo(tamano_letra.getValue(), text_color, text_list);
        cont_paneles=cont_paneles+1;
        existe=true;
    }

    public void crearItemMenu(String texto, String pertence_menu, String accion){
        //instanciar item con el texto entregado
        menu_item = new JMenuItem(texto);

        //si el item pertenece a archivo
        if(pertence_menu.equalsIgnoreCase("archivo")){
            if(accion.equalsIgnoreCase("nuevo")){
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        crearContenedor();
                        if(existe){
                            OpItems.activarItems(items);
                        }
                    }

                });
            }else if(accion.equalsIgnoreCase("abrir")){
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Crear el selector de archivos
                        JFileChooser selector_archivos = new JFileChooser();
                        //crear el contenedor de texto
                        crearContenedor();
                        //Que maneje archivos y directorios
                        selector_archivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        //Vlaor de retorno si seleciona open o cancel
                        int accion = selector_archivos.showOpenDialog(text_list.get(texto_contenedor.getSelectedIndex()));

                        if(accion == JFileChooser.APPROVE_OPTION){
                            if(existe){OpItems.activarItems(items);}
                            try {
                                boolean abierto=false;
                                File archivo_tmp = new File("");
                                //posicion
                                int pos = 0;
                                //tomar el archivo selecionado
                                archivo_tmp = selector_archivos.getSelectedFile();
                                //validar que el archivo aun no ha sido abierto
                                for(int i = 0; i<texto_contenedor.getTabCount(); i++){
                                    if(file_list.get(i).getPath().equalsIgnoreCase(archivo_tmp.getPath())){
                                        abierto=true;
                                        pos=i;
                                    }
                                }

                                if(!abierto){

                                    //agregar el archivo a la lista
                                    file_list.add(archivo_tmp);
                                    file_list.set(texto_contenedor.getSelectedIndex(), archivo_tmp);

                                    try {
                                        //leer el archivo
                                        FileReader archivo = new FileReader(file_list.get(texto_contenedor.getSelectedIndex()).getPath());
                                        BufferedReader bf1 = new BufferedReader(archivo);

                                        String linea = "";
                                        String contenido= "";
                                        String titulo = file_list.get(texto_contenedor.getSelectedIndex()).getName();
                                       //Colocar titulo al tab
                                        texto_contenedor.setTitleAt(texto_contenedor.getSelectedIndex(), titulo);
                                        while(linea != null){

                                            //leer el archivo linea por linea y la guarda en esta variable
                                            linea = bf1.readLine();
                                            if(linea!=null){
                                                contenido = contenido+linea+"\n";
                                            }

                                        }
                                        archivo.close();
                                        if(contenido!=null){
                                            text_list.get(texto_contenedor.getSelectedIndex()).setText(contenido);;
                                        }

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    Fondo.cambiarFondo(tamano_letra.getValue(),text_color, text_list);


                                }else{
                                    //si el archivo ya está abierto, recorre los paneles y ubica al usuario donde está abierto
                                    //ese archivo
                                    texto_contenedor.setSelectedIndex(pos);

                                    text_list.remove(texto_contenedor.getTabCount()-1);
                                    scroll_list.remove(texto_contenedor.getTabCount()-1);
                                    file_list.remove(texto_contenedor.getTabCount()-1);
                                    texto_contenedor.remove(texto_contenedor.getTabCount()-1);
                                    cont_paneles=cont_paneles-1;
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }else{
                            if(texto_contenedor.getSelectedIndex() != -1){
                                text_list.remove(texto_contenedor.getTabCount()-1);
                                scroll_list.remove(texto_contenedor.getTabCount()-1);
                                file_list.remove(texto_contenedor.getTabCount()-1);
                                texto_contenedor.remove(texto_contenedor.getTabCount()-1);
                                cont_paneles=cont_paneles-1;
                            }
                        }

                    }

                });
            }else if(accion.equalsIgnoreCase("guardar")){
                items[0] = menu_item;
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(file_list.get(texto_contenedor.getSelectedIndex()).getPath().equalsIgnoreCase("")){
                            JFileChooser selector_archivos = new JFileChooser();
                            int opcion= selector_archivos.showSaveDialog(null);

                            if(opcion==JFileChooser.APPROVE_OPTION){
                                File archivo_tmp = selector_archivos.getSelectedFile();
                                file_list.set(texto_contenedor.getSelectedIndex(), archivo_tmp);
                                texto_contenedor.setTitleAt(texto_contenedor.getSelectedIndex(),archivo_tmp.getName());

                                //Pasar el texto al text area
                               try {
                                FileWriter escritor = new FileWriter(archivo_tmp.getPath());
                                String texto = text_list.get(texto_contenedor.getSelectedIndex()).getText();

                                for(int i=0; i<texto.length(); i++){
                                    escritor.write(texto.charAt(i));
                                }

                                escritor.close();

                               } catch (Exception ex) {
                                ex.printStackTrace();
                               }

                            }
                        }else{
                               try {
                                FileWriter escritor = new FileWriter(file_list.get(texto_contenedor.getSelectedIndex()).getPath());
                                String texto = text_list.get(texto_contenedor.getSelectedIndex()).getText();

                                for(int i=0; i<texto.length(); i++){
                                    escritor.write(texto.charAt(i));
                                }

                                escritor.close();

                               } catch (Exception ex) {
                                ex.printStackTrace();
                               }

                        }
                    }

                });
            }else if(accion.equalsIgnoreCase("guardar_como")){
                items[1] = menu_item;
                menu_item.addActionListener(new ActionListener () {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser selector_archivos = new JFileChooser();
                        int opcion= selector_archivos.showSaveDialog(null);
        
                        if(opcion==JFileChooser.APPROVE_OPTION){
                            File archivo_tmp = selector_archivos.getSelectedFile();
                            file_list.set(texto_contenedor.getSelectedIndex(), archivo_tmp);
                            texto_contenedor.setTitleAt(texto_contenedor.getSelectedIndex(),archivo_tmp.getName());
        
                            //Pasar el texto al text area
                           try {
                            FileWriter escritor = new FileWriter(archivo_tmp.getPath());
                            String texto1 = text_list.get(texto_contenedor.getSelectedIndex()).getText();
        
                            for(int i=0; i<texto1.length(); i++){
                                escritor.write(texto1.charAt(i));
                            }
        
                            escritor.close();
        
                           } catch (Exception ex) {
                            ex.printStackTrace();
                           }
        
                        }
                    }

                });
            }
            menu_item.setBorder(BorderFactory.createEmptyBorder());
            menu_item.setBackground(new Color (23,25,27));
            menu_item.setForeground(Color.WHITE);
            archivo.add(menu_item);
        }else if(pertence_menu.equalsIgnoreCase("editar")){

            if(accion.equalsIgnoreCase("deshacer")){
                items[2] = menu_item;
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(historial.get(texto_contenedor.getSelectedIndex()).canUndo()){
                            historial.get(texto_contenedor.getSelectedIndex()).undo();
                        }
                    }

                });
            }else if(accion.equalsIgnoreCase("rehacer")){
                items[3] = menu_item;
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(historial.get(texto_contenedor.getSelectedIndex()).canRedo()){
                            historial.get(texto_contenedor.getSelectedIndex()).redo();
                        }
                    }

                });
            }else if(accion.equalsIgnoreCase("cortar")){
                items[4] = menu_item;
                menu_item.addActionListener(new DefaultEditorKit.CutAction());
            }else if(accion.equalsIgnoreCase("copiar")){
                items[5] = menu_item;
                menu_item.addActionListener(new DefaultEditorKit.CopyAction());
            }else if(accion.equalsIgnoreCase("pegar")){
                items[6] = menu_item;
                menu_item.addActionListener(new DefaultEditorKit.PasteAction());
            }
            menu_item.setBorder(BorderFactory.createEmptyBorder());
            menu_item.setBackground(new Color (23,25,27));
            menu_item.setForeground(Color.WHITE);
            editar.add(menu_item);
        }else if(pertence_menu.equalsIgnoreCase("seleccion")){
            if(accion.equalsIgnoreCase("sl_todo")){
                items[7] = menu_item;
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        text_list.get(texto_contenedor.getSelectedIndex()).selectAll();
                    }

                });
            }
            menu_item.setBorder(BorderFactory.createEmptyBorder());
            menu_item.setBackground(new Color (23,25,27));
            menu_item.setForeground(Color.WHITE);
            seleccion.add(menu_item);
        }else if(pertence_menu.equalsIgnoreCase("ver")){
            if(accion.equalsIgnoreCase("numerar")){
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(numerar){
                            numerar=false;
                        }else{
                            numerar=true;
                        }
                        try {
                            for(int i=0; i<texto_contenedor.getTabCount(); i++){
                                Numeracion.mostrarNumeracion(numerar, text_list.get(i), scroll_list.get(i));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                });
            }
            menu_item.setBorder(BorderFactory.createEmptyBorder());
            menu_item.setBackground(new Color (23,25,27));
            menu_item.setForeground(Color.WHITE);
            ver.add(menu_item);
        }else if(pertence_menu.equalsIgnoreCase("ejecutar")){

            if(accion.equalsIgnoreCase("ejecutar")){

                menu_item.addActionListener(new ActionListener () {
                    boolean guardado = false;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(cont_paneles>0){
                            if(file_list.get(texto_contenedor.getSelectedIndex()).getPath().equalsIgnoreCase("")){
                                JFileChooser selector_archivos = new JFileChooser();
                                int opcion= selector_archivos.showSaveDialog(null);
    
                                if(opcion==JFileChooser.APPROVE_OPTION){
                                    File archivo_tmp = selector_archivos.getSelectedFile();
                                    file_list.set(texto_contenedor.getSelectedIndex(), archivo_tmp);
                                    texto_contenedor.setTitleAt(texto_contenedor.getSelectedIndex(),archivo_tmp.getName());
    
                                    //Pasar el texto al text area
                                   try {
                                    FileWriter escritor = new FileWriter(archivo_tmp.getPath());
                                    String texto = text_list.get(texto_contenedor.getSelectedIndex()).getText();
    
                                    for(int i=0; i<texto.length(); i++){
                                        escritor.write(texto.charAt(i));
                                    }
    
                                    escritor.close();
    
                                   } catch (Exception ex) {
                                    ex.printStackTrace();
                                   }
                                   guardado = true;
                                }else{guardado=false;}
                            }else{
                                   try {
                                    FileWriter escritor = new FileWriter(file_list.get(texto_contenedor.getSelectedIndex()).getPath());
                                    String texto = text_list.get(texto_contenedor.getSelectedIndex()).getText();
    
                                    for(int i=0; i<texto.length(); i++){
                                        escritor.write(texto.charAt(i));
                                    }
    
                                    escritor.close();
    
                                   } catch (Exception ex) {
                                    ex.printStackTrace();
                                   }
                                guardado = true;
                            }

                            if(guardado){
                                //******************* INICIO COMPILADOR ******************** */

                                String codigo =""+text_list.get(texto_contenedor.getSelectedIndex()).getText();
                                resultado.setText(resultado.getText()+"\nEjecutando: "+file_list.get(texto_contenedor.getSelectedIndex()).getAbsolutePath()+"\n"
                                +new AnalizadorLexico(codigo).Analizar());

                                //***************************** FIN COMPILADOR ******************************/

                            }
                        }
                    }

                });
            }

            items[8] = menu_item;
            menu_item.setBorder(BorderFactory.createEmptyBorder());
            menu_item.setBackground(new Color (23,25,27));
            menu_item.setForeground(Color.WHITE);
            ejecutar.add(menu_item);
        }else if(pertence_menu.equalsIgnoreCase("apariencia")){
            if(accion.equalsIgnoreCase("normal")){
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        text_color = "blanco";
                        if(cont_paneles>0){
                            Fondo.cambiarFondo(tamano_letra.getValue(), text_color, text_list);
                        }
                    }

                });
            }else if(accion.equalsIgnoreCase("oscuro")){
                menu_item.addActionListener(new ActionListener () {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        text_color = "oscuro";
                        if(cont_paneles>0){
                            Fondo.cambiarFondo(tamano_letra.getValue(), text_color, text_list);
                        }
                    }

                });
            }
            menu_item.setBorder(BorderFactory.createEmptyBorder());
            menu_item.setBackground(new Color (23,25,27));
            menu_item.setForeground(Color.WHITE);
            apariencia.add(menu_item);
        }
    }
    
}