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
public class VrstaCVM implements Apstraktni_Domenski_Objekat {

    private int idVrstaCVM;
    private String naziv;
    private String kratkiOpis;

    public VrstaCVM() {
    }

    public VrstaCVM(int idVrstaCVM, String naziv, String kratkiOpis) {
        this.idVrstaCVM = idVrstaCVM;
        this.naziv = naziv;
        this.kratkiOpis = kratkiOpis;
    }

    public int getIdVrstaCVM() {
        return idVrstaCVM;
    }

    public void setIdVrstaCVM(int idVrstaCVM) {
        this.idVrstaCVM = idVrstaCVM;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKratkiOpis() {
        return kratkiOpis;
    }

    public void setKratkiOpis(String kratkiOpis) {
        this.kratkiOpis = kratkiOpis;
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVrstaCVM, naziv);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final VrstaCVM other = (VrstaCVM) obj;
        return this.idVrstaCVM == other.idVrstaCVM
                && Objects.equals(this.naziv, other.naziv);
    }

    // ========================
    // Implementacija interfejsa
    // ========================
    @Override
    public String vratiNazivTabele() {
        return "vrsta_cvm";
    }

    @Override
    public String vratiKoloneZaInsert() {
        return "(naziv, kratkiOpis)";
    }

    @Override
    public String vratiVrednostiZaInsert() {
        return "'" + naziv + "', '" + kratkiOpis + "'";
    }

    @Override
    public String vratiUslov() {
        return "idVrstaCVM = " + idVrstaCVM;
    }

    @Override
    public List<Apstraktni_Domenski_Objekat> vratiListu(ResultSet rs) throws SQLException {
        List<Apstraktni_Domenski_Objekat> lista = new ArrayList<>();
        while (rs.next()) {
            VrstaCVM vrsta = new VrstaCVM(
                    rs.getInt("idVrstaCVM"),
                    rs.getString("naziv"),
                    rs.getString("kratkiOpis")
            );
            lista.add(vrsta);
        }
        return lista;
    }

    @Override
    public String vratiVrednostiZaUpdate() {
        return "naziv = '" + naziv + "', kratkiOpis = '" + kratkiOpis + "'";
    }

}
