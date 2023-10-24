/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.milaifontanals.empresa.model;

import java.util.Objects;

/**
 * Exemple de classe per gestionar les dades de la taula PRODUCTE de l'esquema EMPRESA.
 * @author Professor
 */
public class Producte {
    private Integer prodNum;
    private String descripcio;

    public Producte(Integer prodNum, String descripcio) {
        this.setProdNum(prodNum);
        this.setDescripcio(descripcio);
    }

    public Integer getProdNum() {
        return prodNum;
    }

    /* Codi de producte no modificable */
    private void setProdNum(Integer prodNum) {
        if (prodNum==null || prodNum<=0 || prodNum>999999) {
            throw new RuntimeException("Codi de producte obligatori entre 1 i 999999");
        }
        this.prodNum = prodNum;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        if (descripcio==null || descripcio.length()==0 || descripcio.length()>30) {
            throw new RuntimeException("Descripció de producte obligatòria i amb contingut i de 30 caràcters com a màxim");
        }
        this.descripcio = descripcio;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.prodNum);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Producte other = (Producte) obj;
        return Objects.equals(this.prodNum, other.prodNum);
    }


    @Override
    public String toString() {
        return "Producte{" + "prodNum=" + prodNum + ", descripcio=" + descripcio + '}';
    }
  
}
