/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.empresa.jdbc;

import org.milaifontanals.empresa.persistencia.GestorBDEmpresaException;
import org.milaifontanals.empresa.persistencia.IGestorBDEmpresa;
import org.milaifontanals.empresa.model.Producte;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Professor
 */
public class GestorBDEmpresaJdbc implements IGestorBDEmpresa {

    /*
     * Aquest objecte és el que ha de mantenir la connexió amb el SGBD Es crea
     * en el constructor d'aquesta classe i manté la connexió fins que el
     * programador decideix tancar la connexió amb el mètode tancarCapa
     */
    private Connection conn;

    /**
     * Sentències preparades que es crearan només 1 vegada i s'utilitzaran
     * tantes vegades com siguin necessàries. En aquest programa es creen la
     * primera vegada que es necessiten i encara no han estat creades. També es
     * podrien crear al principi del programa, una vegada establerta la
     * connexió.
     */
    private PreparedStatement psDelListProduct;
    private PreparedStatement psUpdateProduct;
    private PreparedStatement psInsertProduct;

    /**
     * Estableix la connexió amb la BD.Les dades que necessita (url, usuari i
     * contrasenya) es llegiran d'un fitxer de configuració anomenat
     * empresaJDBC.xml" que cercarà a l'arrel de l'aplicació i que ha de
     * contenir les següents claus: url, user, password
     *
     * En cas que l'aplicació s'executés en Java anterior a 1.6, caldria també
     * passar el nom de la classe JDBC que permet la connexió amb el SGBDR.
     *
     * @throws org.milaifontanals.empresa.persistencia.GestorBDEmpresaException
     */
    public GestorBDEmpresaJdbc() throws GestorBDEmpresaException {
        String nomFitxer = "empresaJDBC.xml";
        try {
            Properties props = new Properties();
            props.loadFromXML(new FileInputStream(nomFitxer));
            String[] claus = {"url", "user", "password"};
            String[] valors = new String[3];
            for (int i = 0; i < claus.length; i++) {
                valors[i] = props.getProperty(claus[i]);
                if (valors[i] == null || valors[i].isEmpty()) {
                    throw new GestorBDEmpresaException("L'arxiu " + nomFitxer + " no troba la clau " + claus[i]);
                }
            }
            conn = DriverManager.getConnection(valors[0], valors[1], valors[2]);
            conn.setAutoCommit(false);
        } catch (IOException ex) {
            throw new GestorBDEmpresaException("Problemes en recuperar l'arxiu de configuració " + nomFitxer, ex);
        } catch (SQLException ex) {
            throw new GestorBDEmpresaException("No es pot establir la connexió.", ex);
        }
    }

    /**
     * Tanca la connexió
     *
     * @throws org.milaifontanals.empresa.persistencia.GestorBDEmpresaException
     */
    @Override
    public void tancarCapa() throws GestorBDEmpresaException {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new GestorBDEmpresaException("Error en fer rollback final.", ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new GestorBDEmpresaException("Error en tancar la connexió.\n", ex);
            }
        }
    }

    /**
     * Obtenir tots els productes de la BD
     *
     * @return Llista de productes
     * @throws org.milaifontanals.empresa.persistencia.GestorBDEmpresaException
     */
    @Override
    public List<Producte> obtenirLlistaProducte() throws GestorBDEmpresaException {
        List<Producte> llProd = new ArrayList<Producte>();
        Statement q = null;
        try {
            q = conn.createStatement();
            ResultSet rs = q.executeQuery("SELECT prod_num, descripcio FROM producte");
            while (rs.next()) {
                Integer codi = rs.getInt("prod_num");
                String desc = rs.getString("descripcio");
                Producte p = new Producte(codi, desc);
                llProd.add(p);
//                llProd.add(new Producte(rs.getInt("prod_num"), rs.getString("descripcio")));
            }
            rs.close();
        } catch (SQLException ex) {
            throw new GestorBDEmpresaException("Error en intentar recuperar la llista de productes.", ex);
        } finally {
            if (q != null) {
                try {
                    q.close();
                } catch (SQLException ex) {
                    throw new GestorBDEmpresaException("Error en intentar tancar la sentència que ha recuperat la llista de productes.", ex);
                }
            }
        }
        return llProd;
    }

    /**
     * Eliminar un conjunt de productes de la BD, a partir dels seus codis
     *
     * @param ll Llista de codis de productes a eliminar
     */
    @Override
    public void eliminarLlistaProducte(List<Integer> ll) throws GestorBDEmpresaException {
        // ALERTA: El lògic és disposar d'un PreparedStatement amb un paràmetre que sigui la
        // llista de valors a eliminar i usar el mètode setArray definit a JDBC per emplenar-lo.
        // La teoria diu que hauria de ser, suposant PreparedStatement psDelListProduct:
        // psDelListProduct = conn.prepareStatement("DELETE FROM PRODUCTE WHERE prod_num IN (?)");
        // psDelListProduct.setArray(1, conn.createArrayOf("INTEGER", ll.toArray()));
        // PERÒ no tots els SGBD faciliten aquesta funcionalitat (Oracle - MySQL8)
        // o la faciliten amb sintaxis pròpia
        // Per tant, NO usarem PreparedStatement per eliminar d'una sola vegada tots els productes
        // indicats, sinó que ho farem 1 a 1, per garantir funcionalitat en tots els SGBD
        if (psDelListProduct == null) {
            try {
                psDelListProduct = conn.prepareStatement("DELETE FROM producte WHERE prod_num = ?");
            } catch (SQLException ex) {
                throw new GestorBDEmpresaException("Error en preparar sentència psDelListProduct", ex);
            }
        }
        Savepoint sp = null;
        try {
            sp = conn.setSavepoint();
            int q = 0;
            for (Integer codi : ll) {
                psDelListProduct.setInt(1, codi);
                q = q + psDelListProduct.executeUpdate();
            }
            if (q < ll.size()) {
                conn.rollback(sp);
                throw new GestorBDEmpresaException("No s'ha efectuat l'eliminació per què "
                        + "no s'han trobat tots el productes que es demanava d'eliminar");
            }
        } catch (SQLException ex) {
            if (sp != null) {
                try {
                    conn.rollback(sp);
                } catch (SQLException ex1) {
                }
            }
            throw new GestorBDEmpresaException("Error en eliminar els productes de codi indicat", ex);
        }
    }

    @Override
    public void afegirProducte(Producte p) throws GestorBDEmpresaException {
        if (psInsertProduct == null) {
            try {
                psInsertProduct = conn.prepareStatement("INSERT INTO producte VALUES (?,?)");
            } catch (SQLException ex) {
                throw new GestorBDEmpresaException("Error en preparar sentència psInsertProduct", ex);
            }
        }
        try {
            psInsertProduct.setInt(1, p.getProdNum());
            psInsertProduct.setString(2, p.getDescripcio());
            psInsertProduct.executeUpdate();
        } catch (SQLException ex) {
            throw new GestorBDEmpresaException("Error en intentar inserir el producte " + p.getProdNum(), ex);
        }
    }

    @Override
    public void actualitzarProducte(Producte p) throws GestorBDEmpresaException {
        if (psUpdateProduct == null) {
            try {
                psUpdateProduct = conn.prepareStatement("UPDATE producte SET descripcio=? WHERE prod_num=?");
            } catch (SQLException ex) {
                throw new GestorBDEmpresaException("Error en preparar sentència psUpdateProduct", ex);
            }
        }
        try {
            psUpdateProduct.setString(1, p.getDescripcio());
            psUpdateProduct.setInt(2, p.getProdNum());
            int q = psUpdateProduct.executeUpdate();
            if (q == 0) {
                throw new GestorBDEmpresaException("Article no modificat per no existir");
            }
        } catch (SQLException ex) {
            throw new GestorBDEmpresaException("Error en intentar modificar el producte " + p.getProdNum(), ex);
        }
    }

    @Override
    public void confirmarCanvis() throws GestorBDEmpresaException {
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new GestorBDEmpresaException("Error en confirmar canvis", ex);
        }
    }

    @Override
    public void desferCanvis() throws GestorBDEmpresaException {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            throw new GestorBDEmpresaException("Error en desfer canvis", ex);
        }
    }
}
