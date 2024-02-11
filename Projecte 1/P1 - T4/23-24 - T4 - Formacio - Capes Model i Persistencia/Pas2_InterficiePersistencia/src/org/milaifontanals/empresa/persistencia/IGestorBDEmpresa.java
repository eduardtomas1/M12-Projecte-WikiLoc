/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.milaifontanals.empresa.persistencia;

import org.milaifontanals.empresa.model.Producte;
import java.util.List;

/**
 *
 * @author Professor
 */
public interface IGestorBDEmpresa {

    /**
     * Insereix producte a la BD
     * @param p Producte a inserir
     * @throws GestorBDEmpresaException 
     */
    void afegirProducte(Producte p) throws GestorBDEmpresaException;

    /**
     * Tanca la connexió
     * @throws org.milaifontanals.empresa.persistencia.GestorBDEmpresaException
     */
    void tancarCapa() throws GestorBDEmpresaException;

    /**
     * Eliminar un conjunt de productes de la BD, a partir dels seus codis
     * @param ll Llista de codis a eliminar
     * @throws org.milaifontanals.empresa.persistencia.GestorBDEmpresaException
     */
    void eliminarLlistaProducte(List<Integer> ll) throws GestorBDEmpresaException;

    /**
     * Obtenir tots els productes de la BD
     * @return Llista de productes recuperats
     * @throws org.milaifontanals.empresa.persistencia.GestorBDEmpresaException
     */
    List<Producte> obtenirLlistaProducte() throws GestorBDEmpresaException;

    /**
     * Actualitzar un producte
     * @param p Producte a actualitzar
     * @throws GestorBDEmpresaException 
     */
    void actualitzarProducte(Producte p) throws GestorBDEmpresaException;
    
    /**
     * Confirma els canvis efectuats a la BD
     * @throws GestorBDEmpresaException 
     */
    void confirmarCanvis() throws GestorBDEmpresaException;
    
    /**
     * Eliminar les transaccions no confirmades
     * @throws GestorBDEmpresaException 
     */
    void desferCanvis() throws GestorBDEmpresaException;
}
