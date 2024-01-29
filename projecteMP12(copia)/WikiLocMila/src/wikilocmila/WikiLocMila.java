/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package wikilocmila;
import wikilocmilabd.WikiLocMilaBD;
import wikilocmilamodel.WikiLocMilaModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eduar
 */
public class WikiLocMila {

    private WikiLocMilaBD gBD = null;
    static private String nomClassePersistencia = null;
    private JFrame f;   // Pantalla principal
    private JTable jTable;
    private DefaultTableModel tWikiLoc;
    private TextArea txtInfo;
    private JDialog form;    // Formulari "modal" per altes i modificacions
    private JTextField codi;
    private JTextField desc;
    private Boolean esAlta;   // Per distingir, en el formulari "modal", si estem fent una alta o una modificació
    private int filaModificada;     // Per saber quina fila s'està modificant i refrescar la taula en finalitzar
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Cal passar el nom de la classe que dona la persistència com a primer argument");
            System.exit(0);
        }
        nomClassePersistencia = args[0];
        WikiLocMila mp = new WikiLocMila();
        mp.run();
    }
    
    private void run() {
        JFrame f = new JFrame("WikiLoc Mila");
        
        Titol tit = new Titol();
        tit.setPreferredSize(new Dimension(500, 50));
        f.add(tit, BorderLayout.NORTH);
        
        tWikiLoc = new DefaultTableModel();
        jTable = new JTable(tWikiLoc) {
            
        };
        
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tWikiLoc.addColumn("Codi");
        tWikiLoc.addColumn("Descripció");
        tWikiLoc.addColumn("Eliminar?");
        JScrollPane scrollPane = new JScrollPane(jTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JPanel pCentre = new JPanel();
        pCentre.add(scrollPane);
        f.add(pCentre);

        // Zona informativa
        JLabel labInfo = new JLabel("Zona de missatges");
        txtInfo = new TextArea(5, 140);
        txtInfo.setEditable(false);     // Per a que l'usuari no hi pugui escriure
//        txtInfo.setEnabled(false);    // Amb això, en cas que hi hagi molt de text, l'usuari no podria fer scroll amunt i avall
        Box pInf = Box.createVerticalBox();
        pInf.add(labInfo);
        pInf.add(txtInfo);
        f.add(pInf, BorderLayout.SOUTH);

        // Buttons 
        JButton bCercar = new JButton("Cerca");
        bCercar.setToolTipText("Mostra tots els productes");
        bCercar.setMaximumSize(new Dimension(200,500));
        JButton bAlta = new JButton("Alta");
        bAlta.setToolTipText("Permet crear un nou producte");
        bAlta.setMaximumSize(new Dimension(200,500));
        JButton bModifica = new JButton("Modifica");
        bModifica.setToolTipText("Permet modificar el producte seleccionat");
        bModifica.setMaximumSize(new Dimension(200,500));
        JButton bEliminar = new JButton("Elimina");
        bEliminar.setToolTipText("Elimina els registres seleccionats");
        bEliminar.setMaximumSize(new Dimension(200,500));
        JButton bNetejar = new JButton("Neteja");
        bNetejar.setToolTipText("Neteja la graella");
        bNetejar.setMaximumSize(new Dimension(200,500));

        Box boxButtons = Box.createVerticalBox();
        boxButtons.add(bCercar);
        boxButtons.add(Box.createVerticalGlue());
        boxButtons.add(bAlta);
        boxButtons.add(Box.createVerticalGlue());
        boxButtons.add(bModifica);
        boxButtons.add(Box.createVerticalGlue());
        boxButtons.add(bEliminar);
        boxButtons.add(Box.createVerticalGlue());
        boxButtons.add(bNetejar);
        bCercar.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAlta.setAlignmentX(Component.CENTER_ALIGNMENT);
        bModifica.setAlignmentX(Component.CENTER_ALIGNMENT);
        bEliminar.setAlignmentX(Component.CENTER_ALIGNMENT);
        bNetejar.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxButtons.setPreferredSize(new Dimension(100, 140));
        JPanel pDreta = new JPanel();
        pDreta.add(boxButtons);
        f.add(pDreta, BorderLayout.EAST);

        // En començar, bEliminar desactivat
//        bEliminar.setEnabled(false);
        GestioBotons gb = new GestioBotons();
        bCercar.addActionListener(gb);
        bAlta.addActionListener(gb);
        bModifica.addActionListener(gb);
        bEliminar.addActionListener(gb);
        bNetejar.addActionListener(gb);

        // En tancar la finestra, tancar la connexió
        f.addWindowListener(
                new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (gBD != null) {      // Per si no s'havia establert connexió
                    try {
                        gBD.tancarCapa();
                    } catch (GestorBDEmpresaException ex) {
                        txtInfo.setText("Error en tancar la connexió.\n\nMotiu:\n\n" + ex.getMessage());
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex1) {
                        }
                    }
                }
                System.exit(0);

            }
        });

        f.pack();
        // Per centrar la finestra a la pantalla
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        try {
            // Intent de crear objecte per gestionar la connexió amb la BD
            txtInfo.setText("Intentant establir connexió...");
            gBD = (IGestorBDEmpresa) Class.forName(nomClassePersistencia).newInstance();
            txtInfo.setText("Connexió establerta");
        } catch (Exception ex) {
            txtInfo.setText(infoError(ex) + "Finalitzi l'aplicació...");
        }
    }
    
    class Titol extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String s = "WikiLoc Mila";

            int sWidth = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            int lWidth = (int) g.getClipBounds().getWidth();

            int sHeight = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
            int lHeight = (int) g.getClipBounds().getHeight();

            g.drawString(s, lWidth / 2 - sWidth / 2, lHeight / 2 - sHeight / 2);
        }
    }
    
    private String infoError(Throwable ex) {
        String aux;
        String info = ex.getMessage();
        if (info != null) {
            info += "\n";
        }
        while (ex.getCause() != null) {
            aux = ex.getCause().getMessage();
            if (aux != null) {
                aux += "\n";
            }
            info = info + aux;
            ex = ex.getCause();
        }
        return info;
    }
}
