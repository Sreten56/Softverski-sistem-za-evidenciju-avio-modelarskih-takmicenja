/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.*;

/**
 *
 * @author Srecko
 */
public class Kategorija implements Apstraktni_Domenski_Objekat{
    
    private int idKategorija;
    private String nazivKategorija;
    private String opisKategorija;

    public Kategorija() {
    }

    public Kategorija(int idKategorija, String nazivKategorija, String opisKategorija) {
        this.idKategorija = idKategorija;
        this.nazivKategorija = nazivKategorija;
        this.opisKategorija = opisKategorija;
    }

    public int getIdKategorija() {
        return idKategorija;
    }

    public void setIdKategorija(int idKategorija) {
        this.idKategorija = idKategorija;
    }

    public String getNazivKategorija() {
        return nazivKategorija;
    }

    public void setNazivKategorija(String nazivKategorija) {
        this.nazivKategorija = nazivKategorija;
    }

    public String getOpisKategorija() {
        return opisKategorija;
    }

    public void setOpisKategorija(String opisKategorija) {
        this.opisKategorija = opisKategorija;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public String toString() {
        return "Kategorija{" + "idKategorija=" + idKategorija + ", nazivKategorija=" + nazivKategorija + ", opisKategorija=" + opisKategorija + '}';
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
        final Kategorija other = (Kategorija) obj;
        if (this.idKategorija != other.idKategorija) {
            return false;
        }
        return Objects.equals(this.nazivKategorija, other.nazivKategorija);
    }
    
    
        @Override
    public String vratiNazivTabele() {
        return "kategorija";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(nazivKategorija, opisKategorija)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        return "'" + nazivKategorija + "', '" + opisKategorija + "'";
    }

    @Override
    public String vratiUslov() {
        return "idKategorija = " + idKategorija;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            Kategorija kat = new Kategorija(
                rs.getInt("idKategorija"),
                rs.getString("nazivKategorija"),
                rs.getString("opisKategorija")
            );
            lista.add(kat);
        }
        return lista;
    }
    
    
    
    @Override
    public String vratiVrednostiZaUpdate() {
        return "nazivKategorija = '" + nazivKategorija + "', "
                + "opisKategorija = '" + opisKategorija + "'";
    }


}
