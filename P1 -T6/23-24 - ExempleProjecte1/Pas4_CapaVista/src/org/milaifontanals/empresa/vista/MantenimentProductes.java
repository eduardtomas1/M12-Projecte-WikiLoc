/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.empresa.vista;

import org.milaifontanals.empresa.persistencia.GestorBDEmpresaException;
import org.milaifontanals.empresa.persistencia.IGestorBDEmpresa;
import org.milaifontanals.empresa.model.Producte;
import info.infomila.utils.swing.JTextFieldLimit;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Professor
 */
public class MantenimentProductes {

    private IGestorBDEmpresa gBD = null;
    static private String nomClassePersistencia = null;
    private JFrame f;   // Pantalla principal
    private JTable jTable;
    private DefaultTableModel tProductes;
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
        MantenimentProductes mp = new MantenimentProductes();
        mp.go();
    }

    private void go() {
        // Disseny de la interfície
        JFrame f = new JFrame("Manteniment de Productes");

        // Títol
        Titol tit = new Titol();
        tit.setPreferredSize(new Dimension(600, 60));
        f.add(tit, BorderLayout.NORTH);

        // Graella per gestionar els productes
        tProductes = new DefaultTableModel();
        jTable = new JTable(tProductes) {
            @Override
            public boolean isCellEditable(int row, int column) // Per aconseguir que la columna 2 sigui editable
            {
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                Class clazz = String.class;
                switch (column) {
                    case 0:
                        clazz = Integer.class;
                        break;
                    case 2:
                        clazz = Boolean.class;
                        break;
                }
                return clazz;
            }

        };
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tProductes.addColumn("Codi");
        tProductes.addColumn("Descripció");
        tProductes.addColumn("Eliminar?");
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

    private class GestioBotons implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Cerca")) {
                tProductes.setRowCount(0);
                try {
                    List<Producte> llProd = gBD.obtenirLlistaProducte();
                    for (Producte p : llProd) {
                        tProductes.addRow(new Object[]{p.getProdNum(), p.getDescripcio(), false});
                    }
                    txtInfo.setText("Cerca de productes efectuada.");
                    return;
                } catch (GestorBDEmpresaException ex) {
                    txtInfo.setText(infoError(ex));
                }
            }
            if (e.getActionCommand().equals("Alta")) {
                if (form == null) {
                    formulari();    // Es crea la 1a. vegada que es necessita
                } else {
                    netejaForm();   // Les altres vegades cal fer "neteja" dels seus camps
                }
                txtInfo.setText("");
                form.setTitle("Alta de Producte");
                esAlta = true;
                codi.setEditable(true);
                codi.requestFocus();
                form.setVisible(true);
                return;
            }
            if (e.getActionCommand().equals("Modifica")) {
                filaModificada = jTable.getSelectedRow();
                if (filaModificada == -1) {
                    txtInfo.setText("No hi ha cap producte seleccionat");
                    return;
                }
                if (form == null) {
                    formulari();    // Es crea la 1a. vegada que es necessita
                } else {
                    netejaForm();   // Les altres vegades cal fer "neteja" dels seus camps
                }
                txtInfo.setText("");
                form.setTitle("Modificació de Producte");
                esAlta = false;
                codi.setEditable(false);
                codi.setText(((Integer) tProductes.getValueAt(filaModificada, 0)).toString());
                desc.setText((String) tProductes.getValueAt(filaModificada, 1));
                desc.requestFocus();
                form.setVisible(true);
                return;
            }
            if (e.getActionCommand().equals("Elimina")) {
                int nRows = tProductes.getRowCount();
                List<Integer> productesAEliminar = new ArrayList<Integer>();
                for (int i = 0; i < nRows; i++) {
                    if ((Boolean) tProductes.getValueAt(i, 2)) {
                        productesAEliminar.add((Integer) tProductes.getValueAt(i, 0));
                    }
                }
                if (productesAEliminar.isEmpty()) {
                    txtInfo.setText("No hi ha cap producte seleccionat");
                    return;
                }
                try {
                    // Eliminem de la BD
                    gBD.eliminarLlistaProducte(productesAEliminar);
                    // Eliminem de la taula
                    for (int i = 0; i < nRows; i++) {
                        if ((Boolean) tProductes.getValueAt(i, 2)) {
                            tProductes.removeRow(i);
                            nRows--;
                            i--;
                        }
                    }
                    txtInfo.setText("Eliminació efectuada.");
                    gBD.confirmarCanvis();
                } catch (GestorBDEmpresaException ex) {
                    txtInfo.setText(infoError(ex));
                    try {
                        gBD.desferCanvis();
                    } catch (GestorBDEmpresaException ex1) {
                    }
                }
                return;
            }
            if (e.getActionCommand().equals("Neteja")) {
                tProductes.setRowCount(0);
                txtInfo.setText("");
                return;
            }
            // Botons del formulari per fer les ALTES i les MODIFICACIONS
            if (e.getActionCommand().equals("Enregistra")) {
                Integer prod_num = Integer.parseInt(codi.getText());
                String descripcio = desc.getText();
                try {
                    if (esAlta) {
                        gBD.afegirProducte(new Producte(prod_num, descripcio));
                        txtInfo.setText("Alta efectuada.");
                        tProductes.addRow(new Object[]{prod_num, descripcio, false});
                    } else {
                        gBD.actualitzarProducte(new Producte(prod_num, descripcio));
                        txtInfo.setText("Modificació efectuada.");
                        tProductes.setValueAt(descripcio, filaModificada, 1);
                    }
                    gBD.confirmarCanvis();
                } catch (GestorBDEmpresaException ex) {
                    txtInfo.setText(infoError(ex));
                }
                form.setVisible(false);
            }
            if (e.getActionCommand().equals("Cancel·la")) {
                form.setVisible(false);
                if (esAlta) {
                    txtInfo.setText("Alta avortada per l'usuari");
                } else {
                    txtInfo.setText("Modificació avortada per l'usuari");
                }
                return;
            }
        }

    }

    class Titol extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String s = "MANTENIMENT DE PRODUCTES";

            int sWidth = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            int lWidth = (int) g.getClipBounds().getWidth();

            int sHeight = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
            int lHeight = (int) g.getClipBounds().getHeight();

            g.drawString(s, lWidth / 2 - sWidth / 2, lHeight / 2 - sHeight / 2);
        }
    }

    private void formulari() {
        form = new JDialog(f, true);

        codi = new JTextField();
        codi.setDocument(new JTextFieldLimit(6));   // No es podrà introduir més de 6 caràcters
        JLabel lCodi = new JLabel("Codi");

        desc = new JTextField();
        desc.setDocument(new JTextFieldLimit(30));  // No es podrà introduir més de 30 caràcters
        JLabel lDesc = new JLabel("Descripció");

//        desc.setColumns(20);    // Provoca la grandària gràfica "més o menys" adequada per al camp
        JButton bSave = new JButton("Enregistra");
        JButton bCancel = new JButton("Cancel·la");

        Box bCodi = Box.createHorizontalBox();
        bCodi.add(lCodi);
        bCodi.add(Box.createHorizontalStrut(10));
        bCodi.add(codi);
        Box bDesc = Box.createHorizontalBox();
        bDesc.add(lDesc);
        bDesc.add(Box.createHorizontalStrut(10));
        bDesc.add(desc);
        Box bButtons = Box.createHorizontalBox();
        bButtons.add(bSave);
        bButtons.add(Box.createHorizontalStrut(20));
        bButtons.add(bCancel);

        Box b = Box.createVerticalBox();
        b.add(bCodi);
        b.add(Box.createVerticalStrut(10));
        b.add(bDesc);
        b.add(Box.createVerticalStrut(10));
        b.add(bButtons);

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(400, 100));
        p.add(b);

        form.add(p);
        form.pack();
        form.setResizable(false);
        // Per centrar la finestra a la pantalla
        form.setLocationRelativeTo(f);
        form.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Donem funcionalitat als buttons "save" i "cancel"
        GestioBotons gb = new GestioBotons();
        bSave.addActionListener(gb);
        bCancel.addActionListener(gb);

        // Controlem que el camp codi només pugui admetre dígits 
        codi.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (((caracter < '0') || (caracter > '9'))
                        && (caracter != '\b')) {
                    e.consume();
                }
            }
        });

    }

    private void netejaForm() {
        codi.setText("");
        desc.setText("");
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
