package org.milaifontanals.cfs.projecte1;

import java.util.Arrays;
import java.util.List;
import org.milaifontanals.empresa.jdbc.GestorBDEmpresaJdbc;
import org.milaifontanals.empresa.model.Producte;
import org.milaifontanals.empresa.persistencia.GestorBDEmpresaException;

/**
 *
 * @author Usuari
 */
public class PropostaProvesCapaPersistenciaJDBC {

    private static GestorBDEmpresaJdbc gBD;

    public static void main(String[] args) {

        Producte p = null;

        // 1. Crear gBD invocant el constructor.
        try {
            System.out.println("Intent de creació de la capa...");
            gBD = new GestorBDEmpresaJdbc();
            System.out.println("Capa de persistència creada");
            System.out.println("");
        } catch (GestorBDEmpresaException ex) {
            ex.printStackTrace();
            System.out.println("Problema en crear capa de persistència:");
            System.out.println(ex.getMessage());
            System.out.println("Avortem programa");
            return;
        }

        // 2. Prova de mètode obtenirLlistaProductes
        mostrarProductes();     // Aquest mètode recull tots els productes
                                // Està programat al final...
                                // L'utilitzem després de diverses proves
                                // per comprovar els productes existents a la BD
        System.out.println("");
        
        // ATENCIÓ: No estem fent commit després de les instruccions d'actualització.
        // Per tant, si comproveu contingut de BD des d'altres sessions (SQLPlus o
        // SQLDeveloper) no veureu els canvis.

        // 3. Proves de mètode afegirProducte
        try {
            p = new Producte(100860, "Producte repetit");
            System.out.println("Intent d'afegir producte 100860 de codi existent");
            gBD.afegirProducte(p);
            System.out.println("Producte afegit");
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }
        System.out.println("");
        try {
            p = new Producte(999999, "Producte nou");
            System.out.println("Intent d'afegir producte 999999 de codi inexistent");
            gBD.afegirProducte(p);
            System.out.println("Producte afegit");
            mostrarProductes();
        } catch (GestorBDEmpresaException ex) {
            ex.printStackTrace();
            System.out.println("\tError: " + ex.getMessage());
        }
        System.out.println("");

        // 4. Proves de mètode modificarProducte
        try {
            // p és el darrer producte afegit. Modifiquem la descripció.
            p.setDescripcio("Nova descripció");
            System.out.println("Intent de modificar producte 999999 existent");
            gBD.actualitzarProducte(p);
            System.out.println("Producte modificat");
            mostrarProductes();
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }
        System.out.println("");
        try {
            Producte pp = new Producte(888888, "Producte inexistent");
            System.out.println("Intent de modificar producte 888888 inexistent");
            gBD.actualitzarProducte(pp);
            System.out.println("Producte modificat");
            mostrarProductes();
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }
        System.out.println("");

        // 5. Proves de mètode eliminarLlistaProductes
        try {
            List<Integer> codis = Arrays.asList(new Integer[]{999999, 888888, 100860});
            System.out.println("Intent d'eliminar els productes de codis: " + codis);
            gBD.eliminarLlistaProducte(codis);
            System.out.println("Productes eliminats");
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }
        mostrarProductes();
        System.out.println("");
        try {
            List<Integer> codis = Arrays.asList(new Integer[]{999999, 888888});
            System.out.println("Intent d'eliminar els productes de codis: " + codis);
            gBD.eliminarLlistaProducte(codis);
            System.out.println("Productes eliminats");
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }
        mostrarProductes();
        System.out.println("");
        try {
            List<Integer> codis = Arrays.asList(new Integer[]{999999,});
            System.out.println("Intent d'eliminar els productes de codis: " + codis);
            gBD.eliminarLlistaProducte(codis);
            System.out.println("Productes eliminats");
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }
        mostrarProductes();
        System.out.println("");

        try {
            System.out.println("Tancament de la capa");
            gBD.tancarCapa();
            System.out.println("Capa tancada");
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }

    }

    private static void mostrarProductes() {
        try {
            System.out.println("Recuperació de productes");
            List<Producte> ll = gBD.obtenirLlistaProducte();
            if (ll.isEmpty()) {
                System.out.println("No hi ha cap producte");
            } else {
                System.out.println("Llistat de productes:");
                for (Producte p : ll) {
                    System.out.println("\t" + p);
                }
            }
        } catch (GestorBDEmpresaException ex) {
            System.out.println("\tError: " + ex.getMessage());
        }

    }
}
