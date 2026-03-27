/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Objects;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Srecko
 */
public class Mesto implements Apstraktni_Domenski_Objekat{
    
    private int idMesto;
    private String nazivMesto;
    private String opstina;

    public Mesto() {
    }

    public Mesto(int idMesto, String nazivMesto, String opstina) {
        this.idMesto = idMesto;
        this.nazivMesto = nazivMesto;
        this.opstina = opstina;
    }

    public int getIdMesto() {
        return idMesto;
    }

    public void setIdMesto(int idMesto) {
        this.idMesto = idMesto;
    }

    public String getNazivMesto() {
        return nazivMesto;
    }

    public void setNazivMesto(String nazivMesto) {
        this.nazivMesto = nazivMesto;
    }

    public String getOpstina() {
        return opstina;
    }

    public void setOpstina(String opstina) {
        this.opstina = opstina;
    }

    @Override
    public String toString() {
        return "Mesto{" + "idMesto=" + idMesto + ", nazivMesto=" + nazivMesto + ", opstina=" + opstina + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Mesto other = (Mesto) obj;
        if (this.idMesto != other.idMesto) {
            return false;
        }
        if (!Objects.equals(this.nazivMesto, other.nazivMesto)) {
            return false;
        }
        return Objects.equals(this.opstina, other.opstina);
    }
    
    
    
    
    
    
        @Override
    public String vratiNazivTabele() {
        return "mesto";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(nazivMesto, opstina)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        return "'" + nazivMesto + "', '" + opstina + "'";
    }

    @Override
    public String vratiUslov() {
        return "idMesto = " + idMesto;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            Mesto mesto = new Mesto(
                rs.getInt("idMesto"),
                rs.getString("nazivMesto"),
                rs.getString("opstina")
            );
            lista.add(mesto);
        }
        return lista;
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        return "nazivMesto = '" + nazivMesto + "', "
                + "opstina = '" + opstina + "'";
    }

}
